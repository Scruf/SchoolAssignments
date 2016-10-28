import math
from random import randint
"""
	@method square_multiply will compute modular exponentiation,
	@param base  is the base
	@param exponent is ebaseponent
	@param n is mod
"""
def square_multiply(base,exponent,n):
	#get the binary form of exponent
	exponent = '{0:b}'.format(exponent)
	#temporarry value where the result will be stored
	temp = 1
	#iterate over binary representation of exponent
	for i in range(0, len(exponent)):
		#if the number in exponent is not 1 than square and mod
		temp = (math.pow(z, 2)) % n
		#otherwise sqaure and than mods
		if (exponent[i] == "1"):
			temp = (z*base) % n
	#return the result
	return temp
"""
	@method isPrime will determine wheter the number is prime or not
	@param a is number which will be checked
"""
def isPrime(a):
    return not ( a < 2 or any(a % i == 0 for i in range(2, int(a ** 0.5) + 1)))
def miller_rabin(n):
	#sainty checks to filter out all even numbers
	if n == 2:
		return True
	if n%2 == 0:
		return False
	#lets choose random number
	a = randint(1,n-1)
	#b
	b = square_multiply(a,m,n)
	for i in range (k-1):
		if (1-b)%n==0:
			return n
		else:
			b = b**2%n
	return n



