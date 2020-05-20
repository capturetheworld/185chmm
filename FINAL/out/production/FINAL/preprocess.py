import os
import itertools

first_time = True

def frequency(opcode_file, family_count):
    f = open(opcode_file, 'r')
    lines = f.readlines()
    op_codes = family_count
    for l in lines:
        if l.strip() not in op_codes.keys():
            op_codes[l.strip()] = 1
        else:
            op_codes[l.strip()] += 1
    return op_codes

def join_together(opcode_file):
    opened_file = open(opcode_file, 'r')
    # writing_file = open('output/USELESS.txt', 'a')
    write_symbols = open('output/CLUSTERnewavr.txt', 'a')
    lines = opened_file.read().splitlines()
    for line in lines:
        # writing_file.write(str(line)+'\n')
        #print(symbol_list)
        try:
            #print(line)
            write_symbols.write(str(symbol_list.index(line))+'\n')
        except ValueError:
            write_symbols.write("30\n")
    # writing_file.close()
    write_symbols.close()



def print_out():
    f = open('output/CLUSTERnewavr.csv', 'w+')
    for opcode in truncated_opcode_family:
        f.write(str(opcode) + ',')
    f.write('\n')
    for symbol in symbolized_opcode:
        f.write(str(symbol) + ',')
    f.write('\n')
    for opcode in truncated_opcode_family:
        f.write(str(truncated_opcode_family[opcode]) + ',')
    f.close()


parent_dir = 'malicia/CLUSTERnewavr'
for (root, dirs, files) in os.walk(parent_dir, topdown=False):
    family_count = {}
    print ('===================================================================')
    print ('ANALYZING FAMILY:', root)
    print ('. . . . . . . . . . . . . . . . .')
    for name in files:
        full_path = os.path.join(root, name)

        # print(full_path)

        if '.txt' in name:
            family_count = frequency(full_path, family_count)

    print (len(files), 'FILE(S) ANALYZED IN THIS FAMILY')
    print ('. . . . . . . . . . . . . . . . .')

    # THE FINAL DICTIONARY COUNT FOR EACH FAMILY

    sorted_final_family = {}
    for k in sorted(family_count, key=family_count.get, reverse=True):
        sorted_final_family[k] = family_count[k]

    print(sorted_final_family)

    # TRUNCATE

    truncated_opcode_family = \
        dict(itertools.islice(sorted_final_family.items(), 30))

   # print(truncated_opcode_family)

    discarded_opcode_family = \
        dict(itertools.islice(sorted_final_family.items(), 30, None))
    total = 0
    for (opcode, occurrence) in discarded_opcode_family.items():
        total = total + occurrence

    # print(total)

    truncated_opcode_family['discarded'] = total  # add the discarded sum to the truncated list

   # print("length is correct: ", len(truncated_opcode_family) is 31)

    print (truncated_opcode_family)

    # print(discarded_opcode_family)

    # create a list for the first time
    # if first_time:
    #     symbol_list = list(truncated_opcode_family.keys())
    #     first_time = False
    #
    # print(symbol_list)

    symbolized_opcode = {}

    iter = 0
    symbol_list = list(truncated_opcode_family.keys())
    for opcode in truncated_opcode_family:

        # if opcode in symbol_list:
        #   symbolized_opcode[symbol_list.index(opcode)+1] = truncated_opcode_family[opcode]
        #   # print(symbolized_opcode)
        # else:

        symbolized_opcode[iter] = truncated_opcode_family[opcode]
        iter += 1

        # print(symbolized_opcode)

    print(symbol_list)
    print(symbolized_opcode)

    for name in files:
        full_path = os.path.join(root, name)

        # print(full_path)
        if '.txt' in name:
            join_together(full_path)

    print_out()


