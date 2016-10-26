from math import *
from fractions import gcd




"""
	@method pollard will find the smallest non trivial factor of n
	@param n is the number for which nontrivial factor of n will be found
	@param x is B0 smothing 
"""
def pollard(n,x):

	#assign x to a B0
	B = x
	#start from 
	b = 2 
	#itterate from 2 until smothing
	for j in range(2,B):
		#calcuulate the b
		b = b**j%n
	#fuse gcd of b-1 to find the d 
	d = gcd(b-1,n)
	#if the d is greater than one and less than n than we found our number
	if d>1 and d < n:
		return d
	#otherwise return failure 
	else:
		return 'failure'
# B = 25
# while  pollard(618240007109027021,100) !='failure':
# 	B = B + 1
print(pollard(618240007109027021,24))
#23
