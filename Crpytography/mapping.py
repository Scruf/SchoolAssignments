from pprint import pprint


xor = lambda a,b: int(a)^int(b)
permute = lambda a: list(a[1]+a[0]+a[3]+a[2])

def get_binary_box (key,list1):
	second_row_arr = []
	for number in list1:
		second_row_number = " "
		for val1, key1 in zip(permute(list("{0:04b}".format(number))), key):
			second_row_number = second_row_number + str(xor(val1,key1))
			second_row_arr.append(second_row_number[::-1])
			second_row_number = " "
	second_row_numb = []
	pprint(second_row_arr)
	for number in second_row_arr:
		result = 0
		for i in range(0,len(list(number.strip()))):
			result = result + ((2**i)*int(number[i]))
		
	
	

		

thir_row = [10, 6 ,9 ,0 ,12 ,11 ,7 ,13 ,15 ,1 ,3 ,14 ,5 ,2 ,8, 4]
first_row = [7,13,14,3,0,6,9,10,1,2,0,5,11,12,4,15]
fourth_row =[3 ,15 ,0 ,6 ,10 ,1 ,13 ,8 ,9 ,4 ,5 ,11 ,12 ,7 ,2 ,14]
third_row=[10 ,6 ,9 ,0 ,12 ,11 ,7 ,13 ,15 ,1 ,3 ,14 ,5 ,2 ,8 ,4]
key = ["0","1","1","0"]

get_binary_box(key,first_row)
for i in range(0,16):
	possible_key = list("{0:04b}".format(i))




# for item in map(lambda num,index: num**index, enumerate(second_row_arr)):
# 	print(item)
"""
Output Sample
[13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 6, 12, 1, 10, 14, 9]
"""