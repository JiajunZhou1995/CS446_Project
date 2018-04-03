from flask import Flask, request, render_template
import io
from contextlib import redirect_stdout
app = Flask(__name__)
app.debug = True

@app.route('/')
def my_form():
    return "Welcome to codeTrip API"

@app.route('/', methods=['POST'])
def my_form_post():
    text = request.get_json()["text"]

    print(text)

    with io.StringIO() as buf, redirect_stdout(buf):
        try:
          result = exec(text, None, None)
          output = buf.getvalue()
        except Exception as e:
          output = repr(e)

    print ("output from exec: ", output)
    print (output)
    return (output)

if __name__ == '__main__':
      app.run(host='0.0.0.0', port=5000)
