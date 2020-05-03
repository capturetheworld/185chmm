import os
import itertools

first_time = True

symbol_list = ['mov', 'push', 'add', 'call', 'cmp', 'jmp', 'xor',
               'pop', 'jz', 'jnz', 'lea', 'sub', 'test', 'retn', 'or',
               'and', 'inc', 'nop', 'dec', 'shr', 'movzx', 'jb', 'sbb', 'adc',
               'shl', 'leave', 'imul', 'jnb', 'jbe', 'discarded']

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
    for k in sorted(family_count, key=family_count.get, reverse=True):
        sorted_final_family[k] = family_count[k]
    print(sorted_final_family)

    # TRUNCATE
    truncated_opcode_family = dict(
        itertools.islice(sorted_final_family.items(), 29))

    print(truncated_opcode_family)

    """discarded_opcode_family = dict(
        itertools.islice(sorted_final_family.items(), 29, None))
    total = 0
    for opcode, occurrence in discarded_opcode_family.items():
        total = total + occurrence

    # print(total)
    truncated_opcode_family[
        'discarded'] = total  # add the discarded sum to the truncated list
    print("length is correct: ", len(truncated_opcode_family) is 30)

    print(truncated_opcode_family)
    # print(discarded_opcode_family)

    # create a list for the first time
    # if first_time:
    #     symbol_list = list(truncated_opcode_family.keys())
    #     first_time = False
    #
    # print(symbol_list)

    symbolized_opcode = {}

    for opcode in truncated_opcode_family:
      if opcode in symbol_list:
        symbolized_opcode[symbol_list.index(opcode)+1] = truncated_opcode_family[opcode]
        # print(symbolized_opcode)
      else:
        symbolized_opcode[opcode] = truncated_opcode_family[opcode]
        # print(symbolized_opcode)
    print(symbolized_opcode)
"""
