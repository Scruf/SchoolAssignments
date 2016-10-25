import requests



payload = {
	"some":"Value"
}

session = requests.session()

url = "http://127.0.0.1:5000/test"


req = requests.post(url,data=payload)

print req.json()
