
from pprint import pprint
from fractions import gcd




n = 618240007109027021


i = 2
while i * i < n:
     while n % i == 0:
         n = n / i
     i = i + 1

print (n)