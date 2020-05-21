import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.lang.Math;
import java.io.File;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;
import java.io.File;  // Import the File class





public class HMMScaled {
    private static int N = 40; //number of states
    private static int M = 31; //number of observation symbols (count of possible observations to chose from)
    private final static int OPCODE_COUNT = 100000;
    private static double[][] A = new double[N][N]; //OUTPUT transition matrix
    private static double[][] B = new double[N][M]; //OUTPUT observation matrix
    private static double[] pi = new double[N]; //OUTPUT initial state distribution
    private static double[][] alpha_pass;
    private static double[][] beta_pass;
    private static int iters = 0;
    private static int maxiters = 100;
    private static int oldLogProb = -1000000;
    private static double[] logProbList = new double[maxiters+1];



    public static void main(String[] args) throws IOException {
        PrintStream o = new PrintStream(new File("src/output/HMM100-40-100000-smarthdd.txt"));

        System.out.println("Working Directory = " + System.getProperty("user.dir"));


        Path filePath = new File("src/output/smarthdd.txt").toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);
        String[] stringArray = stringList.toArray(new String[]{});

        int[] O = new int[OPCODE_COUNT]; //observation sequence


        System.out.println(stringArray.length);

        for(int i = 0; i<OPCODE_COUNT;i++){
            O[i] = Integer.parseInt(stringArray[i]);
        }



        System.out.println("\n ************************ INIT ************************");
        HMM hmm = new HMM(N,M,O);



        while(iters == 0||(iters<maxiters && hmm.getlogProb()>oldLogProb)){


            System.out.println("\n ------------------ALPHA PASS----------------------");
//            hmm.printA();
//             hmm.printB();
            alpha_pass = hmm.alpha_pass(O, hmm.getUpdatedA(), hmm.getUpdatedB(), hmm.getUpdatedpi());

//            System.out.println("ALPHA ARRAY:\n" + Arrays.deepToString(alpha_pass).replace("], ", "]\n"));

//            if(iters == 0) {
                System.out.println("\n --------------BETA PASS-----------------------");
                beta_pass = hmm.beta_pass(O, hmm.getUpdatedA(), hmm.getUpdatedB());
//              System.out.println("BETA ARRAY:\n" + Arrays.deepToString(beta_pass).replace("], ", "]\n"));

//            }

            System.out.println("\n -----------GAMMA PASS & RE-ESTIMATING--------------");
            hmm.gammas(hmm.getUpdatedA(), hmm.getUpdatedB(), alpha_pass, beta_pass, O);
            hmm.gamma_reestimate(O);

            System.out.println("\n ---------------AFTER GAMMA PASS------------------");
            hmm.updatelogProb(); //recalculate log prob

//            hmm.printA();
//            hmm.printB();
//            hmm.printPI();

            iters++;
            logProbList[iters] = hmm.getlogProb();
           // System.out.println(iters<maxiters);
           // System.out.println(hmm.getlogProb()>oldLogProb);

        }

        A = hmm.getUpdatedA(); B = hmm.getUpdatedB(); pi = hmm.getUpdatedpi();

        PrintStream console = System.out;
        System.setOut(o);
        print("PI"); print("A"); print("B");print("LOG");
        System.setOut(console);
        print("PI"); print("A"); print("B");print("LOG");
        /////////////////////////////////////////////////////////////////////////////////////

        //SCORE - USE THE REMAINING DATA

        for(int i = OPCODE_COUNT; i<OPCODE_COUNT*2;i++){
            O[i-(OPCODE_COUNT)] = Integer.parseInt(stringArray[i]);
        }


        System.out.println("\n ------------------SCORING OBSERVATION 1 (FROM LATER OPCODES EXISTING FAMILY) ----------------------");
        alpha_pass = hmm.alpha_pass(O, hmm.getUpdatedA(), hmm.getUpdatedB(), hmm.getUpdatedpi());
        hmm.updatelogProb();
        System.out.println(hmm.getlogProb());
        console = System.out;
        System.setOut(o);
        System.out.println("\n ------------------SCORING OBSERVATION 1 (FROM LATER OPCODES EXISTING FAMILY) ----------------------");
        System.out.println(hmm.getlogProb());
        System.setOut(console);

        //SCORE - RANDOM FAMILY 2

         filePath = new File("src/testing/cleaman.txt").toPath();
         charset = Charset.defaultCharset();
         stringList = Files.readAllLines(filePath, charset);
         stringArray = stringList.toArray(new String[]{});

        int[] O2 = new int[67041]; //observation sequence
        hmm.setT(O2.length);

        //System.out.println("O2 length" + O2.length);

        for(int j = 0; j<67041-1;j++){
           // System.out.println(j);
            O2[j] = Integer.parseInt(stringArray[j]);
        }


        System.out.println("\n ------------------SCORING OBSERVATION 2 DIFFERENT FAMILY ----------------------");
        hmm.alpha_pass(O2, hmm.getUpdatedA(), hmm.getUpdatedB(), hmm.getUpdatedpi());
        hmm.updatelogProb();
        System.out.println(hmm.getlogProb());
        console = System.out;
        System.setOut(o);
        System.out.println("\n ------------------SCORING OBSERVATION 2 DIFFERENT FAMILY ----------------------");
        System.out.println(hmm.getlogProb());
        System.setOut(console);


        //SCORE - RANDOM FAMILY 3
        filePath = new File("src/testing/CLUSTERnewavr.txt").toPath();
        charset = Charset.defaultCharset();
        stringList = Files.readAllLines(filePath, charset);
        stringArray = stringList.toArray(new String[]{});

        int[] O3 = new int[156741]; //observation sequence

        for(int i = 0; i<156741;i++){
            O3[i] = Integer.parseInt(stringArray[i]);
        }
        hmm.setT(O3.length);

        System.out.println("\n ------------------SCORING OBSERVATION 3 DIFFERENT FAMILY ----------------------");
        hmm.alpha_pass(O3, hmm.getUpdatedA(), hmm.getUpdatedB(), hmm.getUpdatedpi());
        hmm.updatelogProb();
        System.out.println(hmm.getlogProb());
        console = System.out;
        System.setOut(o);
        System.out.println("\n ------------------SCORING OBSERVATION 3 DIFFERENT FAMILY ----------------------");
        System.out.println(hmm.getlogProb());
        System.setOut(console);



        //SCORE - RANDOM FAMILY 4
        filePath = new File("src/testing/CLUSTERpositivtkninua.txt").toPath();
        charset = Charset.defaultCharset();
        stringList = Files.readAllLines(filePath, charset);
        stringArray = stringList.toArray(new String[]{});

        int[] O4 = new int[1000]; //observation sequence

        for(int i = 2000; i<3000;i++){
            O4[i-2000] = Integer.parseInt(stringArray[i]);
        }
        hmm.setT(O4.length);
        System.out.println("\n ------------------SCORING OBSERVATION 4 DIFFERENT FAMILY ----------------------");
        hmm.alpha_pass(O4, hmm.getUpdatedA(), hmm.getUpdatedB(), hmm.getUpdatedpi());
        hmm.updatelogProb();
        System.out.println(hmm.getlogProb());
        console = System.out;
        System.setOut(o);
        System.out.println("\n ------------------SCORING OBSERVATION 4 DIFFERENT FAMILY ----------------------");
        System.out.println(hmm.getlogProb());
        System.setOut(console);


        //SCORE - RANDOM FAMILY 5
        //SCORE - RANDOM FAMILY 4
        filePath = new File("src/testing/cridex.txt").toPath();
        charset = Charset.defaultCharset();
        stringList = Files.readAllLines(filePath, charset);
        stringArray = stringList.toArray(new String[]{});

        int[] O5 = new int[10000]; //observation sequence

        for(int i = 32000; i<33000;i++){
            O5[i-32000] = Integer.parseInt(stringArray[i]);
        }
        hmm.setT(O5.length);

        System.out.println("\n ------------------SCORING OBSERVATION 5 DIFFERENT FAMILY ----------------------");
        hmm.alpha_pass(O5, hmm.getUpdatedA(), hmm.getUpdatedB(), hmm.getUpdatedpi());
        hmm.updatelogProb();
        System.out.println(hmm.getlogProb());
        console = System.out;
        System.setOut(o);
        System.out.println("\n ------------------SCORING OBSERVATION 5 DIFFERENT FAMILY ----------------------");
        System.out.println(hmm.getlogProb());
        System.setOut(console);


    }

    public static void print(String match){
        switch (match) {
            case "PI":
                System.out.print(">>>>>>>>>>FINAL PI IS>>>>>>\n");
                for (double v : pi) System.out.print("[" + v + " " + "]");
                System.out.println(" ");
                break;
            case "A":
                System.out.println(" ");
                System.out.println(">>>>>>>>>>FINAL A IS>>>>>>");
                System.out.println(Arrays.deepToString(A).replace("], ", "]\n"));
                break;
            case "B":
                System.out.println(" ");
                System.out.println(">>>>>>>>>>FINAL B IS>>>>>>");
                System.out.println(Arrays.deepToString(B).replace("], ", "]\n"));
                break;
            case "LOG":
                System.out.println(" ");
                System.out.println(">>>>>>>>>>FINAL LOG IS>>>>>>");
                System.out.println("[");
                for (double v : logProbList) System.out.println("" + v + " " + ",");
                System.out.println("]");
                break;
        }

    }









}

class HMM {
    int N = 0;
    int M = 0;

    private double[][] updatedA; //transition matrix
    private double[][] updatedB; //observation matrix
    private double[] updatedpi; //initial state distribution
    private double[] c; //initialize c - scaling factor
    private double[][] gamma ;
    private double[][][] digamma;
    double[][] alpha_array;
    double[][] beta_array;
    private double logProb = 0;
    int T;




    public HMM(int N, int M, int[] O){ //ctor
        this.N = N;
        this.M = M;
        this.updatedA = new double[N][N]; //transition matrix
        this.updatedB = new double[N][M]; //observation matrix
        this.updatedpi = new double[N]; //initial state distribution
        this.T = O.length;
        c = new double[T];
        this.initalize(); //run the INIT RANDOMIZATION PROCESS
        gamma = new double[T][N];
        digamma = new double[T][N][N];
        alpha_array = new double[T][N]; //initialize alpha
        beta_array = new double[T][N]; //initialize beta


    }

    public void setT(int toSet){this.T = toSet;alpha_array = new double[T][N];c = new double[T];}; //reset T for future observations and reset alpha array
    public double[][] getUpdatedA(){ return updatedA; }
    public double[][] getUpdatedB(){ return updatedB; }
    public double[] getUpdatedpi(){ return updatedpi; }
    public double getlogProb(){
        return logProb;
    }
    public double[][] getgamma(){ return gamma; }
    public double[][][] getdigamma(){
        return digamma;
    }
    public double getLogProb(){
        return logProb;
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
        System.out.println("WITHIN ALPHA" + alpha_array.length +" WHAAT "+alpha_array[0].length);

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
        //printPI(); printA(); printB();
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
                    //System.out.println("OBSERVATION " + O[t]);
                    //System.out.println("K IS " + k);
                    if (O[t] == k) {
                        numer = numer + this.gamma[t][j];
                       //System.out.println("numer2 " + numer);
                    }
                    denom = denom + this.gamma[t][j];
                    //System.out.println("denom2 " + denom);
                }
                //System.out.println("B BEFORE " + this.updatedB[j][k]);
                this.updatedB[j][k] = numer/denom;
                //System.out.println("B AFTER " + this.updatedB[j][k]);
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