from pprint import pprint
import sys

file_name = input('DFA specification file name:')
if len(file_name) == 0:
	print('File name cannot be left empty')
	sys.exit(0)
