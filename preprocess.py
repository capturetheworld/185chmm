import os

def frequency(opcode_file):
  f=open(opcode_file, "r")
  lines=f.readlines()
  op_codes={}
  for l in lines:
    if l.strip() not in op_codes.keys():
      op_codes[l.strip()]=1
    else:
      op_codes[l.strip()]+=1
  print(op_codes)


parent_dir="malicia/"
for root, dirs, files in os.walk(parent_dir, topdown=False):
   print("=================================")
   print("ANALYZING FAMILY:", root)
   print("=================================")
   for name in files:
      full_path=os.path.join(root, name)
      print(full_path)
      if ".txt" in name:
        frequency(full_path)

