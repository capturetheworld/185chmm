#Python HMM Project 
import os
import itertools

import numpy as np

class alphabeta:
    def beta_pass(self, o, a, b, pi):
        number_of_states = len(a)

        t = len(o)
        # print(t)
        # print(number_of_states)


        beta = np.zeros((number_of_states, t))
        # print(beta)

        for row in range(number_of_states):
            beta[row][t-1] = 1
        print("TEST: " , beta)

        for new_t in range(t-2,0,-1):
            for row in range(number_of_states):
                for column in range(number_of_states):
                    beta[row][new_t] += (a[row][column] *
                        beta[column][new_t+1]* b[column][o[new_t+1]])
        return beta









def main():

    pi = np.zeros((1, 2))
    a = np.zeros((2, 2))
    b = np.zeros((2, 3))

    a[0][0] = 0.7
    a[1][0] = 0.4
    a[0][1] = 0.3
    a[1][1] = 0.6

    b[0][0] = 0.1
    b[1][0] = 0.7
    b[0][1] = 0.4
    b[1][1] = 0.2
    b[0][2] = 0.5
    b[1][2] = 0.1

    pi[0][0] = 0.6
    pi[0][1] = 0.4

    observation = [0, 1, 0, 2]

    hmm = alphabeta()
    print(hmm.beta_pass(observation, a, b, pi))

    #
    #
    #
    # print(pi)
    # print(a)
    # print(a[0][1])
    # a[0][1] = 4
    # print(a[0][1])
    # print(b)


if __name__ == "__main__":
    main()
