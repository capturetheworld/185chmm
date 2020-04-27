import os

def frequency(opcode_file, family_count):
  f=open(opcode_file, "r")
  lines=f.readlines()
  op_codes=family_count
  for l in lines:
    if l.strip() not in op_codes.keys():
      op_codes[l.strip()]=1
    else:
      op_codes[l.strip()]+=1
  return op_codes

parent_dir="malicia/"
for root, dirs, files in os.walk(parent_dir, topdown=False):
   family_count={}
   print("===================================================================")
   print("ANALYZING FAMILY:", root)
   print(". . . . . . . . . . . . . . . . .")
   for name in files:
      full_path=os.path.join(root, name)
      #print(full_path)
      if ".txt" in name:
        family_count=frequency(full_path, family_count)

   print(len(files), " FILES ANALYZED IN THIS FAMILY")
   print(". . . . . . . . . . . . . . . . .")

   # THE FINAL DICTIONARY COUNT FOR EACH FAMILY
   sorted_final_family={}
   for k in sorted(family_count,key=family_count.get, reverse=True):
     sorted_final_family[k]=family_count[k]
   print(sorted_final_family)
