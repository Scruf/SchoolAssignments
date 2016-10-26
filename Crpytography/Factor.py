#part 2c find the smallest value of B0 such that p-1 divides n!
#Its true to say that p-1 will divide B0! only when B0 is >= p-1 and there is no remainder
#first lets calculate the B0 factorial which should be  grater or equals to p-1

from math import factorial
""""
	@method retun_BO will return the smallest value of B0 such that p-1 divides n! 
"""
def retrun_B0():
	#initializing p - 1
	p = 250387200 - 1
	#baisc counter which we will use to check whether p -1 divides B0
	i = 0
	#initialize B0 to one
	B0 = 1
	#iterate from o to p to check wheter we cand find the smallest number	
	for i in range(0,p):
		#if factorial of i divides p without the remainder than
			if factorial(i)%p==0:
				#assign this i to B)
				B0 = i
				#break the loop
				break
	#print the number		
	print("\n\n\n\n\n\n"+str(B0))

	

#print the B0
retrun_B0()
#resulting smallest B0 is 24
		




