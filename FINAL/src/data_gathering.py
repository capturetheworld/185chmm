import os
import itertools

first_time = True

symbol_list = ['mov', 'push', 'add', 'call', 'cmp', 'jmp', 'xor', 'pop', 'jz', 'jnz', 'lea', 'sub', 'test', 'retn', 'or', 'and', 'inc', 'nop', 'dec', 'shr', 'movzx', 'jb', 'sbb', 'adc', 'shl', 'leave', 'imul', 'jnb', 'jbe', 'discarded']

def frequency(opcode_file, family_count):
    f = open(opcode_file, "r")
    lines = f.readlines()
    op_codes = family_count
    for l in lines:
        if l.strip() not in op_codes.keys():
            op_codes[l.strip()] = 1
        else:
            op_codes[l.strip()] += 1
    return op_codes


parent_dir = "malicia/"
csv=[]
f=open("opcode_rates.csv","w+")

for root, dirs, files in os.walk(parent_dir, topdown=False):
    family_count = {}
    print(
        "===================================================================")
    print("ANALYZING FAMILY:", root)
    print(". . . . . . . . . . . . . . . . .")
    for name in files:
        full_path = os.path.join(root, name)
        # print(full_path)
        if ".txt" in name:
            family_count = frequency(full_path, family_count)

    print(len(files), " FILES ANALYZED IN THIS FAMILY")
    print(". . . . . . . . . . . . . . . . .")

    # THE FINAL DICTIONARY COUNT FOR EACH FAMILY
    sorted_final_family = {}
    total_sum=0
    for k in sorted(family_count, key=family_count.get, reverse=True):
        sorted_final_family[k] = family_count[k]
        total_sum+=family_count[k]

    print(sorted_final_family)
    print("\n")
    # generate the averages
    averages = {}
    for k in sorted(family_count, key=family_count.get, reverse=True):
        averages[k] = str(round(family_count[k]/total_sum*100,2)) + "%"

    print(averages)

    scale=0.0
    row=""
    # LET'S GRAB THE DATA OF THE SYMBOL LIST ONLY
    for k in symbol_list:
       # discarded will be all k added up, then subtracted out of 100.00
       # because it represents all the leftover

       if k in averages.keys():
           row+=(averages[k]+",")
           scale+=float(averages[k][:-1])
       else:
         if k=="discarded":
           row+=(str(round(100.00-scale,2)) + "%,")
         else:
           #print(k+" is not in this family")
           row+="0.0%,"
    print(row[:-1])
    csv.append(root+","+row[:-1]+"\n")

for row in csv:
  f.write(row)
f.close()
