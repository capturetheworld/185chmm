import java.util.Arrays;

public class HmmAlphaBeta {

    //alpha
    //we return the alpha result we get at the end
    public double[][] alphaPass(int[] O, double[][] a, double[][] b, double[] pi)
    {
        int numberOfStates = a.length; //determine this now since it's used a lot

        int T = O.length; //determine T

        //create the blank alpha
        double[][] alpha = new double[numberOfStates][T];

        //first for loop like in pseudocode
        for(int i = 0; i < numberOfStates; i++)
        {
            alpha[i][0] = pi[i] * b[i][O[0]];
        }

        for (int t = 1; t < T; t++)
        {
            for(int i = 0; i < numberOfStates; i++)
            {
                //in the big sigma parenthesis in a for loop
                for(int j = 0; j < numberOfStates; j++)
                {
                    alpha[i][t] += (alpha[j][t-1] * a[j][i]);
                }

                //do the rest after sigma end
                alpha[i][t] *= b[i][O[t]];
            }
        }

        //result after all for loops end
        return alpha;
    }



    ///beta
    //we return the alpha result we get at the end
    public double[][] betaPass(int[] O, double[][] a, double[][] b, double[] pi)
    {
        int numberOfStates = a.length; //determine this now since it's used a lot

        int T = O.length; //determine T

        //create the blank alpha
        double[][] beta = new double[numberOfStates][T];

        //first for loop like in pseudocode
        for(int i = 0; i < numberOfStates; i++)
        {
            beta[i][T-1] = 1;
        }

        for (int t = T-2; t >= 0; t--)
        {
            for(int i = 0; i < numberOfStates; i++)
            {
                //in the big sigma parenthesis in a for loop
                for(int j = 0; j < numberOfStates; j++)
                {
                    beta[i][t] += ( a[i][j] * beta[j][t+1] * b[j][O[t+1]] );
                }
            }
        }

        //result after all for loops end
        return beta;
    }





    public static void main(String[] args) {

        //problem2 example

        double[] pi = new double[2];
        double[][] a = new double[2][2];
        double[][] b = new double[2][3];

        a[0][0] = 0.7;
        a[1][0] = 0.4;
        a[0][1] = 0.3;
        a[1][1] = 0.6;

        b[0][0] = 0.1;
        b[1][0] = 0.7;
        b[0][1] = 0.4;
        b[1][1] = 0.2;
        b[0][2] = 0.5;
        b[1][2] = 0.1;

        pi[0] = 0.6;
        pi[1] = 0.4;

        int[] observation = new int[4];
        observation[0] = 0;
        observation[1] = 1;
        observation[2] = 0;
        observation[3] = 2;

        HmmAlphaBeta hmm = new HmmAlphaBeta();
        System.out.println( Arrays.deepToString( hmm.alphaPass(observation, a, b, pi) ) );

        System.out.println( Arrays.deepToString( hmm.betaPass(observation, a, b, pi) ) );
    }

}