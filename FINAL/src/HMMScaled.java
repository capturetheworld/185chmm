import java.io.IOException;
import java.util.Arrays;
import java.lang.Math;



public class HMMScaled {
    private static int N = 2; //number of states
    private static int M = 3; //number of observation symbols (count of possible observations to chose from)
    private static double[][] A = new double[N][N]; //OUTPUT transition matrix
    private static double[][] B = new double[N][M]; //OUTPUT observation matrix
    private static double[] pi = new double[N]; //OUTPUT initial state distribution
    private static double[][] alpha_pass;
    private static double[][] beta_pass;
    public static int iters = 0;
    public static int maxiters = 3;
    public static int oldLogProb = -1000000;

    public static void printPI(){
        System.out.print(">>>>>>>>>>FINAL PI IS>>>>>>\n");
        for (double v : pi) System.out.print("[" + v + " " + "]");
        System.out.println(" ");
    }
    public static void printA(){
        System.out.println(" ");
        System.out.println(">>>>>>>>>>FINAL A IS>>>>>>");
        System.out.println(Arrays.deepToString(A).replace("], ", "]\n"));
    }
    public static void printB(){
        System.out.println(" ");
        System.out.println(">>>>>>>>>>FINAL B IS>>>>>>");
        System.out.println(Arrays.deepToString(B).replace("], ", "]\n"));
    }






    public static void main(String[] args) throws IOException {



        int[] O = new int[]{0, 1, 0, 2}; //observation sequence


        System.out.println("\n ************************ INIT ************************");
        HMM hmm = new HMM(N,M,O);



        while(iters == 0||(iters<maxiters && hmm.getlogProb()>oldLogProb)){


            System.out.println("\n ------------------ALPHA PASS----------------------");
           // hmm.printA();
            // hmm.printB();
            alpha_pass = hmm.alpha_pass(O, hmm.getUpdatedA(), hmm.getUpdatedB(), hmm.getUpdatedpi());

            System.out.println("ALPHA ARRAY:\n" + Arrays.deepToString(alpha_pass).replace("], ", "]\n"));

            if(iters == 0) {
                System.out.println("\n --------------BETA PASS-----------------------");
                beta_pass = hmm.beta_pass(O, hmm.getUpdatedA(), hmm.getUpdatedB());
              System.out.println("BETA ARRAY:\n" + Arrays.deepToString(beta_pass).replace("], ", "]\n"));

            }

            System.out.println("\n -----------GAMMA PASS & RE-ESTIMATING--------------");
            hmm.gammas(hmm.getUpdatedA(), hmm.getUpdatedB(), alpha_pass, beta_pass, O);
            hmm.gamma_reestimate(O);

            System.out.println("\n ---------------AFTER GAMMA PASS------------------");
            hmm.updatelogProb(); //recalculate log prob

            hmm.printA();
            hmm.printB();
            hmm.printPI();

            iters++;
           // System.out.println(iters<maxiters);
           // System.out.println(hmm.getlogProb()>oldLogProb);

        }

        A = hmm.getUpdatedA(); B = hmm.getUpdatedB(); pi = hmm.getUpdatedpi();
        printPI(); printA(); printB();


    }

}

class HMM {
    int N = 0;
    int M = 0;

    private double[][] updatedA; //transition matrix
    private double[][] updatedB; //observation matrix
    private double[] updatedpi; //initial state distribution
    private double[] c; //initialize c - scaling factor
    private double logProb = 0;
    int T;
    private double[][] gamma ;
    private double[][][] digamma;
    double[][] alpha_array;
    double[][] beta_array;



    public HMM(int N, int M, int[] O){ //ctor
        this.N = N;
        this.M = M;
        this.updatedA = new double[N][N]; //transition matrix
        this.updatedB = new double[N][M]; //observation matrix
        this.updatedpi = new double[N]; //initial state distribution
        this.T = O.length;
        c = new double[T];
        this.initalize();
        gamma = new double[T][N];
        digamma = new double[T][N][N];
        alpha_array = new double[T][N]; //initialize alpha
        beta_array = new double[T][N]; //initialize beta


    }

    public double[][] getUpdatedA(){
        return updatedA;
    }
    public double[][] getUpdatedB(){
        return updatedB;
    }
    public double[] getUpdatedpi(){
        return updatedpi;
    }
    public double getlogProb(){
        return logProb;
    }
    public double[][] getgamma(){
        return gamma;
    }
    public double[][][] getdigamma(){
        return digamma;
    }

    public  void initalize(){
        double summationPI=0.0;
        double summationA=0.0;
        double summationB=0.0;


        for(int i = 0; i<=N-2; i++){
            this.updatedpi[i] = (Math.random() * ((1.0/N+0.001)-(1.0/N-0.001))) + (1.0/N-0.001);
            summationPI+=this.updatedpi[i];
        }
        this.updatedpi[N-1]= 1-summationPI;

        for(int i = 0; i<N; i++){
            for(int j = 0; j<=N-2; j++){
                this.updatedA[i][j] = (Math.random() * ((1.0/N+0.001)-(1.0/N-0.001))) + (1.0/N-0.001);
                summationA= summationA + this.updatedA[i][j];
            }
            //System.out.println(summationA);
            this.updatedA[i][N-1] = 1-summationA;
            summationA=0;
        }
        for(int j = 0; j<N; j++){
            for(int k = 0; k<=M-2; k++){
                this.updatedB[j][k] = (Math.random() * ((1.0/M+0.001)-(1.0/M-0.001))) + (1.0/M-0.001);
                summationB= summationB + this.updatedB[j][k];
            }
            //System.out.println(summationA);
            this.updatedB[j][M-1] = 1-summationB;
            summationB=0;
        }



////        System.out.println(1.0/N-0.001);
////        System.out.println(1.0/N+0.001);
//
//
//
        //printPi(); printA(); printB();

    }






    /**
     *         Alpha Pass
     *         int[] O = new int[]{.......}; //observation sequence
     *         double[][] A = new double[N][N]; //transition matrix
     *         double[][] B = new double[N][M]; //observation matrix
     *         double[] pi = new double[N]; //initial state distribution
     *         int T = O.length; //number of observations in observation sequence
     *         int N;  //number of states
     */
    public double[][] alpha_pass(int[] O, double[][] a, double[][] b, double[] pi) {
        //System.out.println("WITHIN ALPHA");

        //System.out.println(Arrays.deepToString(a).replace("], ", "]\n"));
        //System.out.println(Arrays.deepToString(b).replace("], ", "]\n"));

        c[0] = 0;

        //compute alpha_0(i)
        for (int i = 0; i < N; i++) {
            alpha_array[0][i] = pi[i] * b[i][O[0]];
            c[0] = c[0] + alpha_array[0][i];
        }

        //scale the alpha_0(i)
        c[0] = 1/c[0];
        for (int i = 0; i<N; i++){
            alpha_array[0][i] = c[0]*alpha_array[0][i];
        }

        //compute alpha_t(i)
        for (int t = 1; t < T; t++) {
            c[t] = 0;
            for (int i = 0; i < N; i++) {
                alpha_array[t][i] = 0;
                for (int j = 0; j < N; j++) {
                    alpha_array[t][i] += (alpha_array[t - 1][j] * a[j][i]);
                    //System.out.println("TEST 1:" + alpha_array[t][i]);
                }
                alpha_array[t][i] = alpha_array[t][i] * b[i][O[t]];
                c[t] = c[t] + alpha_array[t][i];
                //System.out.println("TEST 2:" + alpha_array[t][i]);
                //System.out.println(alpha_array[t][i]);
            }

            //scale alpha_t(i)
            c[t] = 1/c[t];
            for(int i = 0; i<N;i++){
                alpha_array[t][i] = c[t]*alpha_array[t][i];
                //System.out.println("TEST 3:" + alpha_array[t][i]);
            }
        }



        return alpha_array;
    }



    /**
     *         Beta Pass
     *         int N ; //number of states
     *         int M ; //number of observation symbols (count of possible observations to chose from)
     *         double[][] A = new double[N][N]; //transition matrix
     *         double[][] B = new double[N][M]; //observation matrix
     *         double[] pi = new double[N]; //initial state distribution
     *         int[] O = new int[]{.......}; //observation sequence
     *         int T = O.length; //number of observations in observation sequence
     */
    public double[][] beta_pass(int[] O, double[][] a, double[][] b) {

     // double[][] beta_array = new double[T][N]; //initialize beta

        //Let Beta_T-1(i) = 1 scaled by c_(T-1)
        for (int i = 0; i < N; i++){
            beta_array[T-1][i] = c[T-1];
        }


        //Beta-pass
        for (int t = T-2; t >= 0; t--) {
            for (int i = 0; i < N; i++) {
                beta_array[t][i] = 0;
                for (int j = 0; j < N; j++) {
                    beta_array[t][i] = beta_array[t][i] + (a[i][j] * b[j][O[t+1]] * beta_array[t+1][j]);
                }
                //scale Beta_t(i) w/ alpha_t(i)
                beta_array[t][i] = c[t]*beta_array[t][i];
            }
        }

        //printPI(); printA(); printB();
        return beta_array;


    }

    public void gamma_reestimate(int[] O) {
        System.out.println("REESTIMATE");
        printPI(); printA(); printB();
        //re-estimate pi
        for (int i = 0; i < N; i++) {
            this.updatedpi[i] = this.gamma[0][i];
        }

        //re-estimate A
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                double numer = 0;
                double denom = 0;
                for (int t = 0; t <=T-2; t++) {
                    numer = numer + this.digamma[t][i][j];
                    denom = denom + this.gamma[t][i];
                }
                this.updatedA[i][j] = numer/denom;
            }
        }

        //re-estimate B
        for (int j = 0; j < N; j++) {
            for (int k = 0; k < M; k++) {
                double numer = 0;
                double denom = 0;
                for (int t = 0; t <= T-1; t++) {
                    System.out.println("OBSERVATION " + O[t]);
                    System.out.println("K IS " + k);
                    if (O[t] == k) {
                        numer = numer + this.gamma[t][j];
                       System.out.println("numer2 " + numer);
                    }
                    denom = denom + this.gamma[t][j];
                    //System.out.println("denom2 " + denom);
                }
                //System.out.println("B BEFORE " + this.updatedB[j][k]);
                this.updatedB[j][k] = numer/denom;
                System.out.println("B AFTER " + this.updatedB[j][k]);
            }
        }
        //printPI(); printA(); printB();

    }

    public void gammas(double[][] a, double[][] b, double[][] alpha, double[][] beta, int[] O){
       // printPI(); printA(); printB();
        for(int t=0;t<T-1;t++){
            double denom = 0;
            for(int i=0;i<N;i++){
                for(int j=0;j<N;j++){
                    denom = denom + alpha[t][i] * a[i][j]*b[j][O[t+1]]*beta[t+1][j];
                }
            }
           // System.out.println("HERE");
            for(int i=0;i<N;i++){
                this.gamma[t][i] = 0;
                for(int j=0;j<N;j++){
                    this.digamma[t][i][j] = (alpha[t][i] * a[i][j] * b[j][O[t+1]]*beta[t+1][j])/denom;
                    this.gamma[t][i] = gamma[t][i] + digamma[t][i][j];
                }
            }
        }

        //Special case for gamma[T-1][i]
        double denom = 0;
        for(int i=0;i<N;i++){
            denom = denom + alpha[T-1][i];
        }
        for(int i=0;i<N;i++){
           // System.out.println(gamma[T-1].length);
            this.gamma[T-1][i] = alpha[T-1][i]/denom;
        }
       // printPI(); printA(); printB();
    }




    public void updatelogProb() {
        this.logProb=0;
       for(int i = 0;i< this.T;i++){
           this.logProb = this.logProb + Math.log(this.c[i]);
       }
       this.logProb = -this.logProb;
    }

    public void printPI(){
        System.out.print(">>>>>>>>>>HMM PI IS>>>>>>\n");
        for (double v : this.updatedpi) System.out.print("[" + v + " " + "]");
        System.out.println(" ");
    }
    public void printA(){
        System.out.println(" ");
        System.out.println(">>>>>>>>>>HMM A IS>>>>>>");
        System.out.println(Arrays.deepToString(this.updatedA).replace("], ", "]\n"));
    }
    public void printB(){
        System.out.println(" ");
        System.out.println(">>>>>>>>>>HMM B IS>>>>>>");
        System.out.println(Arrays.deepToString(this.updatedB).replace("], ", "]\n"));
    }







   // public double[][][] filldigamma(double[][] a, double[][] b, int T, double probability, double[][] alpha, double[][] beta, int N, int[] O) {
//        double digamma[][][] = new double[T - 1][N][N];
//        for (int t = 0; t <= T - 2; t++) {
//            for (int i = 0; i < N; i++) {
//                for (int j = 0; j < N; j++) {
//                    digamma[t][i][j] = (alpha[t][i] * a[i][j] * b[j][O[t + 1]] * beta[t + 1][j]) / probability;
//
//                }
//
//            }
//        }
//        return digamma;
//
//    }
//    public double[][] fillgamma(int N, int T, double[][][] digamma) {
//        double[][] gamma = new double[T - 1][N];
//        for (int t = 0; t <= T - 2; t++) {
//            for (int i = 0; i < N; i++) {
//                for (int j = 0; j < N; j++) {
//                    gamma[t][i] += digamma[t][i][j];
//
//                }
//
//            }
//        }
//        return gamma;
//
//    }

}