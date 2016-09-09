import pymssql
class DataSet():
	def __init__(self):
		self.DATABASE = {
			"Host":"23.24.85.82",
			"Db":"CGB",
			"Name":"tmpusr",
			"Pass":"aYA84u9vEjuySFgapyLjHfQaSy3eLCEcdCaYy24r"
		}

	def get_data(self):
		conn = pymssql.connect(self.DATABASE['Host'],self.DATABASE['Name'],self.DATABASE['Pass'],self.DATABASE['Db'])
		cursor = conn.cursor()
		cursor.execute("""EXEC [uspTempPL]""")
		data_set =  cursor.fetchall()
		data_set_array = []
		for data in data_set:
			temp_obj  = {
				"Channel":              str(data[0]),
				"OrderID":              str(data[1]),
				"TimeOfOrder":          str(data[2]),
				"Qty":                  str(data[3]),
				"ProductID":            str(data[4]),
				"ProductName":          str(data[5]),
				"PhysicalQty":          str(data[6]),
				"TotalReceived":        str(data[7]),
				"ItemCost":             str(data[8]),
				"IsSalesManCost":       str(data[9]),
				"CollectTax":           str(data[10]),
				"CollectedShippingFee": str(data[11]),
				"eBaySubsidy":          str(data[12]),
				"ChanngelFees":         str(data[13]),
				"ProductCount":         str(data[14]),
				"NetProffit_":          str(data[15]),
				"NetProffit":           str(data[16]),
				"CostType":             str(data[17])
			}
			data_set_array.append(temp_obj)
		return data_set_array
