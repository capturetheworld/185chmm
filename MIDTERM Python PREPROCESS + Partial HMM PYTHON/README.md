# 185chmm
Midterm 2 CS 185C

PREPROCESSING

In order to get our symbol list, we analyzed every file, in every family, and did a count of the frequency of each opcode. To see this in action, run python symbol_list.py to see the output. We get a grand total across all files, then only take the top 29 most occuring opcode. The 30th opcode is called "discarded" which is what all of the less common opcodes are mapped to.

['mov', 'push', 'add', 'call', 'cmp', 'jmp', 'xor', 'pop', 'jz', 'jnz', 'lea', 'sub', 'test', 'retn', 'or', 'and', 'inc', 'nop', 'dec', 'shr', 'movzx', 'jb', 'sbb', 'adc', 'shl', 'leave', 'imul', 'jnb', 'jbe', 'discarded'] 

TODO: write about how the common opcodes are mapped to symbols
