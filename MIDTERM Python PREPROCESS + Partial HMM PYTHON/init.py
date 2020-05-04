import os
import itertools
parent_dir = "malicia/"

# START ANALYZING DATA TO POPULATE HMM VARIABLES
N=0 # Number of States in the Model
Q=[] # Distinct States of the Markov Process
V=[] # Possible Observations

# MODEL: INITIAL STATE DISTRIBUTION
initial_state={}

# INITIAL STATE DISTRIBUTION: At the beginning, here's the likelihood that the sequence is from any of these family, based off the number of files representing each family.
# It is row stochastic, all states (malware families) in the initial state distribution add up to 1
total=0
for root, dirs, files in os.walk(parent_dir, topdown=False):
  family=root.split(parent_dir)[1]
  if(family!="" and len(files)!=0):
    total+=len(files)
    N+=1
    Q.append(root.split(parent_dir)[1])
    
for root, dirs, files in os.walk(parent_dir, topdown=False):
  family=root.split(parent_dir)[1]
  if(family!="" and len(files)!=0):
    percentage=(len(files)/total)
    #print("FAMILY",root.split(parent_dir)[1],"HAS",len(files),"FILES",percentage*100,"% OF THE DATASET")
    # We will populate the initial state distribution here
    initial_state[root.split(parent_dir)[1]]=percentage



# FINISH ALL HMM VARIABLES
values={}
values["N"]=N
values["Q"]=Q
values["V"]=V

# We can handle T=30, and observation sequences in main.py instead of here
# To get M, number of observation symbols. Ranked distinct top 29 opcodes across dataset. 30th one is 'discard' for all the uncommon ones). Those final observation symbols will go into M.

observation_symbols=['mov','push','add','call','cmp','jmp','xor','pop','jz','jnz','lea','sub','test','retn','or','and','inc','nop','dec','shr','movzx','jb','sbb','adc','shl','leave','imul','jnb','jbe','discarded']
values["M"]=len(observation_symbols)

# The observation symbols have to be from 0, M-1. E.g 0=Push, 1=MOV, 2=XOR, etc.)"
# For V, possible observations, just make a list of 0,1,2,..9,A,...Z
letter='A'
for x in range(len(observation_symbols)):
  if x<10:
    values["V"].append(str(x))
  else:
    values["V"].append(letter)
    letter=chr(ord(letter)+1)



