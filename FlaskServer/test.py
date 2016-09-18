from flask import Flask
from flask import request
from flask import send_from_directory, after_this_request
from dataset import DataSet
import json
import pandas
import os
import uuid
from pprint import pprint
app = Flask(__name__)

@app.route('/')
def hello_world():
	pprint('Hello')
	dataset = DataSet()
	json_to_exel = pandas.read_json(json.dumps(dataset.get_data()))
	file_name = str(uuid.uuid4())+'.xlsx'
	file_to_delete = json_to_exel.to_excel(file_name,index=False)
	
	@after_this_request
	def cleanup(response):
	
		os.remove(file_name)
		return response

	return send_from_directory(".", file_name, as_attachment=True)