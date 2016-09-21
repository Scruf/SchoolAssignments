from pprint import pprint

xor = lambda a,b: int(a)^int(b)
permute = lambda a: list(a[1]+a[0]+a[3]+a[2])

first_row = [7,13,14,3,0,6,9,10,1,2,0,5,11,12,4,15]
key = ["0","1","1","0"]
second_row_arr = []
for number in first_row:
	second_row_number = " "
	for val1, key1 in zip(permute(list("{0:04b}".format(number))), key):
		second_row_number = second_row_number + str(xor(val1,key1))
	second_row_arr.append(second_row_number[::-1])
	second_row_number = " "

for number in second_row_arr:
	result = 0
	for i in range(0,len(list(number.strip()))):
		result = result + ((2**i)*int(number[i]))
	pprint(result)

# for item in map(lambda num,index: num**index, enumerate(second_row_arr)):
# 	print(item)