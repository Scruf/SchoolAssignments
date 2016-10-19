import math
#z=x^c mod n

print ("\nFormat is z=x^c mod n")
x = raw_input("\nEnter x: ")
c = raw_input("Enter c: ")
n = raw_input("Enter n: ")

x = int(x)
c = int(c)
n = int(n)

c = '{0:b}'.format(c)

z = 1
l = len(c)

for i in range(0, l):
	z = (math.pow(z, 2)) % n
	print("%d\n"%z)
	if (c[i] == "1"):
		z = (z*x) % n
		print("%d\n"%z)
