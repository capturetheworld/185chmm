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

def main():
  # States of Markov Process
  Q={"state1","state2"}
  N=len(Q)

  # Observation Symbols
  # TODO: Change these symbols the the ones for our opcodes
  V={0,1,2}
  M=len(V)

  # Initial State Distribution
  # TODO: Dynamically populate this with random values stochastically
  pi=[None]*N
  pi[0]=0.6
  pi[1]=0.4
  
  # Transition Matrix
  # TODO: Dynamically populate this with random values stochastically
  a=[[None]*N]*N
  a[0][0] = 0.7
  a[1][0] = 0.4
  a[0][1] = 0.3
  a[1][1] = 0.6

  # Observation Matrix
  # TODO: Find values from these based on dataset
  b=[[None]*M]*N
  b[0][0] = 0.1
  b[1][0] = 0.7
  b[0][1] = 0.4
  b[1][1] = 0.2
  b[0][2] = 0.5
  b[1][2] = 0.1

  # Observation Sequence
  observation=[0,1,0,2]

  # Hidden Markov Model
  hmm=hmmAlphaBeta()
  print(hmm.alphaPass(observation, a, b, pi))
  #hmm.betaPass(observation, a, b, pi)

main()
