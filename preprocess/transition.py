import numpy as np
import json
import os
import itertools

# THIS FILE IS FOR PRODUCING THE TRANSITION MATRIX
A={}
Q = ['CLUSTERclarkclark', 'ufasoftbitcoin', 'CLUSTERgdata', 'CLUSTERblackandwhite', 'CLUSTERfadedtext', 'fakeavrena', 'CLUSTERin7cy', 'zeroaccess', 'CLUSTERchapterleomemorykomboeu', 'winwebsec', 'ramnit', 'CLUSTERcolorballs', 'CLUSTERhalfmoon', 'CLUSTERm9swachube', 'CLUSTERpaydayloatnsstcru', 'dprn', 'CLUSTERforeign', 'CLUSTERdzony3777su', 'CLUSTERnewavr', 'CLUSTERazonpowzanadinoarcom', 'CLUSTERmergenew', 'WinRescue', 'CLUSTER46105131121', 'cutwail', 'securityshield', 'CLUSTERwhatismyipcom', 'zbot', 'CLUSTERpositivtkninua', 'CLUSTERautoitplus', 'cridex', 'CLUSTERbundlemonkeycom', 'CLUSTERonlinepolicecom', 'russkill', 'CLUSTERpricedtdnsnet', 'unknown', 'CLUSTER859317123', 'spyeyeep', 'smarthdd', 'CLUSTERmycomputer', 'fakeavwebprotection', 'cleaman', 'harebot', 'ransomNoaouy', 'CLUSTER912343210', 'CLUSTERup2xcom', 'CLUSTERmindamuracom', 'CLUSTERjustontime12com', 'CLUSTERfloppies']
N = len(Q)

result = np.identity(N)
result = result + np.random.uniform(low=0., high=.25, size=(N, N))
result = result / result.sum(axis=1, keepdims=1)
print(type(result))
np.savetxt('transition_matrix', result)

#with open('transition_matrix', 'w') as outfile:
#    json.dump(A, outfile)
