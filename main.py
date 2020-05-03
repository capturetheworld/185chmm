import hmm

class hmmAlphaBeta:
  def alphaPass(self, observation, a, b, pi):
    N=len(a)
    T=len(observation)

    alpha=[[0]*T]*N

    for i in range(N):
      alpha[i][0]=pi[i] * b[i][0]

    # t = observation
    for t in range(0,T-1):
      # i = states or row of alpha
      for i in range(0,N):
        # summation
        for j in range(0,N):
          alpha[i][t]+=(alpha[j][t-1]*a[j][i])

        alpha[i][t]*=b[i][t]
    return alpha

# TODO: Translate betaPass next
  def betaPass(self, observation, a, b, pi):
    N=len(a)
    T=len(observation)

    beta=[N][T]
    return beta

def main():
  # States of Markov Process
  Q=hmm.Q
  N=hmm.N

  # Observation Symbols
  V=hmm.V
  M=hmm.M

  # Initial State Distribution
  pi=hmm.pi

  # Transition Matrix
  A=hmm.A

  # Observation Matrix
  B=hmm.B

  # Observation Sequence
  observation=['A','B','3','2','T','R','8','4','J','W','1','5','X']

  # Hidden Markov Model
  model=hmmAlphaBeta()
  print(model.alphaPass(observation, A, B, pi))
  #hmm.betaPass(observation, a, b, pi)

main()
