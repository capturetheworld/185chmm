import json
import os
import itertools

observation_symbols=['mov','push','add','call','cmp','jmp','xor','pop','jz','jnz','lea','sub','test','retn','or','and','inc','nop','dec','shr','movzx','jb','sbb','adc','shl','leave','imul','jnb','jbe','discarded']

# THIS FILE IS FOR PRODUCING THE OBSERVATION MATRIX
B={}
ob_data=open("opcode_rates.csv","r")
data=ob_data.readlines()
ob_data.close()

for line in range(1,len(data)):
  x=data[line].split(",")
  state=x[0]
  print(state)
  B[state]={}
  
  # Put the rest of the line's data into JSON entry
  for c in range(1,len(x)):
    print(observation_symbols[c-1])
    B[state][observation_symbols[c-1]]=round((float(x[c].strip()[0:-1])/100),3)

with open('observation_matrix', 'w') as outfile:
    json.dump(B, outfile)
