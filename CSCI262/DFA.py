import sys
import re

file_name = input ("DFA specification file name:")

if len(file_name) == 0:
    print("File name cannot be left empty")
    sys.exit(0)
try:
    file = open(file_name,'r')
except Exception as ex:
    print(str(ex))
    sys.exit(0)

file_content = file.read().splitlines()

_file_content = []

for content in file_content:
    if '#' not in content:
        _file_content.append(content)

#state any alpha-numeric character also , `-` also `@` length must be 1
state_regex = re.compile(r'[a-zA-Z1-9@-]')
#alphabet state any alpha-numeric character of length 1
alphabet_regex = re.compile(r'[a-zA-Z1-9')
#chaeck whether state falls under boundaries
state_match = lambda state: state_regex.match(state)
#check whether alphabet character falls under defined boundaries
alpa_state = lambda alphabet: alphabet_regex.match(alphabet)


