import pymssql
from pprint import pprint
import itertools
import datetime


class DataSet():
	def __init__(self):
		self.DATABASE = {
			"Host":"23.24.85.82",
			"Db":"CGB",
			"Name":"tmpusr",
			"Pass":"aYA84u9vEjuySFgapyLjHfQaSy3eLCEcdCaYy24r"
		}

	def find_decimal(self,data):
		return type(data) is not int \
						 and type(data) is not str \
						 and type(data) is not float \
						 and not isinstance(data, datetime.datetime)

	def get_data(self):
		conn = pymssql.connect(self.DATABASE['Host'],self.DATABASE['Name'],self.DATABASE['Pass'],self.DATABASE['Db'])
		cursor = conn.cursor()
		cursor.execute("""EXEC [uspTempPL]""")
		cursor_dataset = cursor.fetchall()
		end = len(cursor_dataset)-1
		temp_list = []
		for i in range(0,end):
			tupple_list = list(cursor_dataset[i])
			for x in range(0, len(tupple_list)-1):
				if isinstance(tupple_list[x], datetime.datetime):
					tupple_list[x] = str(tupple_list[x])
				if self.find_decimal(tupple_list[x]):
					tupple_list[x] = float(tupple_list[x])
			temp_list.append(tuple(tupple_list))

			colum_names = [col[0] for col in cursor.description]
		data = [dict(itertools.zip_longest(colum_names, row))  
        				for row in temp_list]

		return data
