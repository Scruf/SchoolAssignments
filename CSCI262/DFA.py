import sys
import re
"""
	@param transition is a list of transition 
	@param alphabet is a alphabet is a st of accepted characters
"""
def transition_check(transiton,alphabet):
	#check middle element for alphabet characters
	pass

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
state_regex = re.compile(r'[a-zA-Z0-9@-]')
#alphabet state any alpha-numeric character of length 1
alphabet_regex = re.compile(r'[a-zA-Z0-9]')
#chaeck whether state falls under boundaries

#the set of states
states = _file_content[0].split(' ')
#alphabet
alphabet = _file_content[1].split(' ')

#start state
q0 = _file_content[2]

states = list(filter (lambda state: state_regex.match(state), states))
#is in the states
alphabet = list(filter (lambda alphabet: alphabet_regex.match(alphabet),alphabet))

if None in states:
	print("Invalid states")
	sys.exit(0)
print(states)

if q0 not in states:
	print("q0 state is not in the set of states")
	sys.exit(0)
#set of accepted states
set_of_accepted_states =  _file_content[3].split(' ')
#check whether accepted state is in the set of states

for _set in set_of_accepted_states:
	if _set not in states:
		print("accepeted stated is not in the set of states")
		sys.exit(0)

#transition function
delta_function = _file_content[4:]
#check delta function

	
#display file content
content = """
			Q = {{{}}}
			Sigma = {{{}}}
			q0 = {}
			F = {{{}}}
		  """.format(', '.join(map(lambda x: "'" + x + "'", states)), \
		  			', '.join(map(lambda x: "'" + x + "'", alphabet)), \
		  			q0,''.join(set_of_accepted_states))
print(content)