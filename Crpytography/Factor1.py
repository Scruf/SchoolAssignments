#part 2c find the smallest value of B0 such that p-1 divides n!
#Its true to say that p-1 will divide B0! only when B0 is >= p-1 and there is no remainder
#first lets calculate the B0 factorial which should be  grater or equals to p-1

from math import factorial

def retrun_B0():
	
	p = 2469135820 
	i = 0
	B0 = 1

	
	
	for i in range(0,p):
		print(i)
		if factorial(i)>=p:
			print(i) 
			if p%factorial(i)==0:
				B0 = i
				break
			
	print("\n\n\n\n\n\n"+str(B0))
	


retrun_B0()
		




