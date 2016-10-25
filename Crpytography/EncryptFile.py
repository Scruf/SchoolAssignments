from ReadIni import IniFileReader
from Encrypt import AESCipher
from pprint import pprint

#initialize IniFileReader 
inifile_reader = IniFileReader()

#dumo ini file into dictionary
dictionary_to_encrypt = inifile_reader.read_config_file('rp_credentials.ini')
key = '468d947f-b79f-4e83-aeed-2947b68b'
#initialize encryption
encryption = AESCipher(key)


for dictionary_key in dictionary_to_encrypt:
	for key,values in dictionary_to_encrypt[dictionary_key].items():
		dictionary_to_encrypt[dictionary_key][key]=encryption.encrypt(values)



pprint(inifile_reader.write_config_file('rp_credentials_test.ini',dictionary_to_encrypt))