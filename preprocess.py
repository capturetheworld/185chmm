import os

for i in os.walk('malicia/'):
    print(i[0])
    for k in os.walk(i[0]):
      print(k[2])
#directory_name="malicia/zeroaccess/fd6b61857f59334e95aa12c9336425834cd404e8.asm.txt"

#parent_dir="malicia/"


#f=open(directory_name, "r")
#lines=f.readlines()

#op_codes={}

#for l in lines:
  #if l.strip() not in op_codes.keys():
    #op_codes[l.strip()]=1
  #else:
    #op_codes[l.strip()]+=1

#print(op_codes)

