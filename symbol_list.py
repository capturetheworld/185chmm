import os
import itertools

first_time = True
symbol_count={}

# Observation Symbols, 30th one is "discarded"
M=29

"""symbol_list = ['mov', 'add', 'sub', 'adc', 'push', 'jnz', 'inc', 'cmp', 'jz',
               'jmp', 'call', 'lea', 'or', 'jnb', 'outsd', 'retn', 'pop', 'jb',
               'xor', 'dec', 'ja', 'xchg', 'sti', 'fild', 'sbb', 'rcl',
               'fisttp',
               'pusha', 'shl', 'discarded']  # use index as symbol
"""



def symbol(opcode_file):
    f = open(opcode_file, "r")
    lines = f.readlines()
    for l in lines:
        if l.strip() not in symbol_count.keys():
            symbol_count[l.strip()] = 1
        else:
            symbol_count[l.strip()] += 1


parent_dir = "malicia/"
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
            symbol(full_path)

    print(len(files), " FILES ANALYZED IN THIS FAMILY")
    print(". . . . . . . . . . . . . . . . .")

# THIS IS THE GRAND COUNT FOR THE FREQUENCY OF OPCODES ACROSS ALL FILES
final_symbol={}
for k in sorted(symbol_count, key=symbol_count.get, reverse=True)[:M]:
  final_symbol[k] = symbol_count[k]

print("===================================================================")
print("TOP ", M, " SYMBOLS")
print(final_symbol)
print("===================================================================")
print("SYMBOL LIST")
print(final_symbol.keys())


