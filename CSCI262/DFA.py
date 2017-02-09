import sys
import re
"""
	Checks whether state in the right form
"""
def state_match(state,reg):
	for s in state:
		if reg.match(s) is None:
			return False 
	return True
"""
	Checks whether alphabet in the right form
"""
def alphabet_check(alphabet,reg):
	if len(alphabet) > 1:
		return False

	for alp in alphabet:
		if reg.match(alp) is None:
			return False
	return True
"""
	Check whether transitions are valid
"""
def transition_check(transitions,alphabet,state):
	for transition in transitions:
		if len(transition.split(' ')) > 3:
			return False
		if not transition.split(' ')[0] in state \
			and transition.split(' ')[2] in state:
			return False
		if not transition.split(' ')[1] in alphabet:
			return False
	return True




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

for state in states:
	if not state_match(state,state_regex):
		print("Invalid state %s"%state)
		sys.exit(0)
#alphabet
alphabet = _file_content[1].split(' ')
for alp in alphabet:
	if not alphabet_check(alp,alphabet_regex):
		print("Invalid alphabet")
		sys.exit(0)


#start state
q0 = _file_content[2]
if q0 not in states:
	print("Start state not in states")
	sys.exit(0)


#set of accepted states
set_of_accepted_states =  _file_content[3].split(' ')
for state in set_of_accepted_states:
	if state not in states:
		print("Accepted state not in set of states")
		sys.exit(0)


#check whether accepted state is in the set of states


#transition function
delta_function = _file_content[4:]
delta_function = list(filter(None, delta_function))

#check delta function
if not transition_check(delta_function,alphabet,states):
	print("Invalid transition")
	sys.exit(0)
	
if len(delta_function) > len(alphabet)*len(states):
	print("Invalid number of states")
	sys.exit(0)
#display file content
content = """
Q = {{{}}}
Sigma = {{{}}}
q0 = {}
F = {{{}}}
		  """.format(', '.join(map(lambda x: "'" + x + "'", states)), \
					', '.join(map(lambda x: "'" + x + "'", alphabet)), \
					q0,''.join(set_of_accepted_states))

transition_string = ""

for delta in delta_function:
	transition_string = transition_string + """transition: ({}) -> {}\n""".format(
		', '.join(delta.split(' ')[:2]), delta.split(' ')[2]
	)

print(content)
print(transition_string)