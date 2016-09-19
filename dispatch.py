from pprint import pprint
from datetime import datetime



while True:
	if datetime.now().hour in range(0,23):
		pprint("Every Hour I am partying")
	elif datetime.now().minute in range(0,59):
		pprint("I am sexy and I know it")
	else:
		pprint("It should have not happened")
	