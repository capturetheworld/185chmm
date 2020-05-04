import java.io.IOException;
import java.util.Arrays;


public class HMMAlphaBetaGammaPass {
    private static int N = 2; //number of states
    private static int M = 3; //number of observation symbols (count of possible observations to chose from)

    static double[][] updatedA = new double[N][N]; //transition matrix
    static double[][] updatedB = new double[N][M]; //observation matrix
    static double[] updatedpi = new double[N]; //initial state distribution

    public static void main(String[] args) throws IOException {




        double[][] A = new double[N][N]; //transition matrix
        double[][] B = new double[N][M]; //observation matrix
        double[] pi = new double[N]; //initial state distribution
        int[] O = new int[] {
                0,
                1,
                0,
                2
        }; //observation sequence
        int T = O.length; //number of observations in observation sequence

        // fill out a data
        A[0][0] = 0.7; A[0][1] = 0.3;
        A[1][0] = 0.4; A[1][1] = 0.6;



        // fill out b data
        B[0][0] = 0.1; B[0][1] = 0.4; B[0][2] = 0.5;
        B[1][0] = 0.7; B[1][1] = 0.2; B[1][2] = 0.1;

        // fill out pi data
        pi[0] = 0.6; pi[1] = 0.4;


        HMMAlphaBetaGammaPass hmm = new HMMAlphaBetaGammaPass();
        double[][] alpha_pass = hmm.alpha_pass(O, A, B, pi, T, N);
        System.out.println("\n =======================Alpha PASS============================ \n");

        System.out.println(Arrays.deepToString(alpha_pass).replace("], ", "]\n"));

        System.out.println("P(O|λ) is = " + scoring(alpha_pass));



        System.out.println("\n =======================BETA PASS============================");

        // Loop through all rows
        //        for (int i = 0; i < alpha_pass.length; i++) {
        ////             Loop through all elements of current row
        //            for (int j = 0; j < alpha_pass[i].length; j++)
        //                System.out.println(alpha_pass[i][j] + " ");
        //            System.out.println
        //        }
        System.out.println(" Beta pass is: \n ");
        double[][] beta_pass = hmm.beta_pass(O, A, B, pi, T, N);
        System.out.println(Arrays.deepToString(beta_pass).replace("], ", "]\n"));

        System.out.println("\n =========================RUNNING GAMMA PASS==========================");
        double[][][] digamma = filldigamma(A, B, T, scoring(alpha_pass), alpha_pass, beta_pass, N, O);
        double[][] gamma = fillgamma(N, T, digamma);
        hmm.gamma_pass(N, M, T, gamma, digamma, O);

        System.out.println("\n =========================AFTER GAMMA PASS==========================");
        System.out.println(" A  is: \n ");
        System.out.println(Arrays.deepToString(updatedA).replace("], ", "]\n"));
        System.out.println(" B  is: \n ");
        System.out.println(Arrays.deepToString(updatedB).replace("], ", "]\n"));
        System.out.println(" PI pass is: \n ");
        // Loop through all rows
        //        for (int i = 0; i < alpha_pass.length; i++) {
        ////             Loop through all elements of current row
                    for (int j = 0; j < updatedpi.length; j++)
                        System.out.print("["+updatedpi[j] + " "+"]");
        //            System.out.println
        //        }






    }



    /**
     *         Alpha Pass
     *         int N ; //number of states
     *         int M ; //number of observation symbols (count of possible observations to chose from)
     *         double[][] A = new double[N][N]; //transition matrix
     *         double[][] B = new double[N][M]; //observation matrix
     *         double[] pi = new double[N]; //initial state distribution
     *         int[] O = new int[]{.......}; //observation sequence
     *         int T = O.length; //number of observations in observation sequence
     */
    public double[][] alpha_pass(int[] O, double[][] a, double[][] b, double[] pi, int T, int N) {

        double[][] alpha_array = new double[T][N]; //initialize alpha

        for (int i = 0; i < N; i++) {
            alpha_array[0][i] = pi[i] * b[i][O[0]];
        }
        /// System.out.println(alpha_array[0][0]);
        /// System.out.println(alpha_array[0][1]);

        for (int t = 1; t < T; t++) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    alpha_array[t][i] += (alpha_array[t - 1][j] * a[j][i]);
                }
                alpha_array[t][i] *= b[i][O[t]];
                /// System.out.println(alpha_array[t][i]);
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
    public double[][] beta_pass(int[] O, double[][] a, double[][] b, double[] pi, int T, int N) {

        double[][] beta_array = new double[T][N]; //initialize beta



        for (int i = 0; i < N; i++) {
            beta_array[T - 1][i] = 1;
        }
        //
        //        // Loop through all rows
        //        for (int i = 0; i < beta.length; i++)
        ////             Loop through all elements of current row
        //            for (int j = 0; j < beta[i].length; j++)
        //                System.out.print(beta[i][j] + " ");

        for (int t = T - 2; t >= 0; t--) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    beta_array[t][i] += (a[i][j] * b[j][O[t + 1]] * beta_array[t + 1][j]);
                }
            }
        }


        return beta_array;
    }

    public static void gamma_pass(int N, int M, int T, double[][] gamma, double[][][] digamma, int[] O) {



        for (int i = 0; i <= N - 1; i++) {
            updatedpi[i] = gamma[0][i];
        }
        for (int i = 0; i <= N - 1; i++) {
            for (int j = 0; j <= N - 1; j++) {
                double todivide = 0;
                for (int t = 0; t <= T - 2; t++) {
                    updatedA[i][j] += digamma[t][i][j];
                    todivide += gamma[t][i];
                }
                updatedA[i][j] = updatedA[i][j] / todivide;
            }
        }

        for (int j = 0; j <= N - 1; j++) {
            for (int k = 0; k <= M - 1; k++) {
                double todivide = 0;
                for (int t = 0; t <= T - 2; t++) {
                    if (O[t] == k)
                        updatedB[j][k] += gamma[t][j];
                    todivide += gamma[t][j];
                }
                updatedB[j][k] = updatedB[j][k] / todivide;
            }
        }

    }

    public static double[][][] filldigamma(double[][] a, double[][] b, int T, double probability, double[][] alpha, double[][] beta, int N, int[] O) {
        double digamma[][][] = new double[T - 1][N][N];
        for (int t = 0; t <= T - 2; t++) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    digamma[t][i][j] = (alpha[t][i] * a[i][j] * b[j][O[t + 1]] * beta[t + 1][j]) / probability;

                }

            }
        }
        return digamma;

    }
    public static double[][] fillgamma(int N, int T, double[][][] digamma) {
        double[][] gamma = new double[T - 1][N];
        for (int t = 0; t <= T - 2; t++) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    gamma[t][i] += digamma[t][i][j];

                }

            }
        }
        return gamma;

    }


    public static double scoring(double[][] inputArray) {
        int N = inputArray[0].length;
        int T = inputArray.length;
        double probability = 0;

        for (int i = 0; i < N; i++) {
            probability += inputArray[T - 1][i];
        }
        return probability;

    }

}