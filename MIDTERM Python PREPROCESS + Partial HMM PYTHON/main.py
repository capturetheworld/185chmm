import hmm
import numpy as np


class hmmAlphaBeta:
    def alpha_pass(self, observation, a, b, pi):

        N = len(a)
        T = len(observation)

        alpha = [[0] * T] * N

        for i in range(N):
            alpha[i][0] = pi[i] * b[i][0]

        # t = observation
        for t in range(0, T - 1):
            # i = states or row of alpha
            for i in range(0, N):
                # summation
                for j in range(0, N):
                    alpha[i][t] += (alpha[j][t - 1] * a[j][i])

                alpha[i][t] *= b[i][t]
        return alpha

    # Betapass
    def beta_pass(self, o, a, b, pi):
        print("B IS", b)
        print(b.keys())
        number_of_states = len(a)

        t = len(o)
        # print(t)
        # print(number_of_states)

        # print("O IS", o)
        # print("A IS", a)
        # print("B IS", b)

        beta = np.zeros((number_of_states, t))
        # print(beta)

        for row in range(number_of_states):
            beta[row][t - 1] = 1
        print("TEST: ", beta)

        for new_t in range(t - 2, 0, -1):
            for row in range(number_of_states):
                for column in range(number_of_states):
                    beta[row][new_t] += (a[row][column] *
                      beta[column][new_t + 1] * b[column][o[new_t + 1]])
        return beta


def main():
    # States of Markov Process
    o = hmm.Q
    n = hmm.N

    # Observation Symbols
    v = hmm.V
    m = hmm.M

    # Initial State Distribution
    pi = hmm.pi

    # Transition Matrix
    a = hmm.A

    # Observation Matrix
    b = hmm.B

    # Observation Sequence
    observation = ['A', 'B', '3', '2', 'T', 'R', '8', '4', 'J', 'W', '1', '5',
                   'X']
    t = len(observation)

    # Hidden Markov Model
    model = hmmAlphaBeta()
    # print(model.alphaPass(observation, A, B, pi))
    model.beta_pass(observation, a, b, pi)

    # Hidden Markov Model
    model = hmmAlphaBeta()
    print(model.alphaPass(observation, a, b, pi))
    print(model.betaPass(observation, a, b, pi))

main()
