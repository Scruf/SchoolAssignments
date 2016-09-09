from flask import Flask
from flask import request
from flask import send_from_directory
from dataset import DataSet
import json
import pandas
app = Flask(__name__)

@app.route('/')
def hello_world():
	dataset = DataSet()
	#save data to json file with a name data.json
	with open('data.json','w') as outfile:
		#dump list of dicionaries into .json file
		json.dump(dataset.get_data(),outfile,indent=4)
	#read json file into xlsx
	pandas.read_json('data.json').to_excel('output.xlsx')
	#download the file whe redirecting to the url
	#first @param is direcctory
	#second @param is the name of the file which will downloaded
	return send_from_directory(".", 'output.xlsx',as_attachment=True)


