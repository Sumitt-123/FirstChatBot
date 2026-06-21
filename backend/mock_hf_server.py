#!/usr/bin/env python3
from http.server import BaseHTTPRequestHandler, HTTPServer
import json

class Handler(BaseHTTPRequestHandler):
    def _set_headers(self):
        self.send_response(200)
        self.send_header('Content-Type', 'application/json')
        self.end_headers()

    def do_POST(self):
        # Expect path like /models/gpt2
        content_length = int(self.headers.get('Content-Length', 0))
        body = self.rfile.read(content_length).decode('utf-8') if content_length > 0 else ''
        try:
            j = json.loads(body) if body else {}
        except Exception:
            j = {}
        # Hugging Face API uses 'inputs' field for prompt
        prompt = None
        if isinstance(j, dict):
            prompt = j.get('inputs') or j.get('input') or j.get('text')
        if not prompt:
            # try to extract simple message
            prompt = body
        if not prompt:
            prompt = 'Hello from mock'
        # Build fake response: array with object containing generated_text
        reply_text = str(prompt) + ' (mock reply)'
        response = [ { 'generated_text': reply_text } ]
        self._set_headers()
        self.wfile.write(json.dumps(response).encode('utf-8'))

    def log_message(self, format, *args):
        # Silence logging to stdout
        return

if __name__ == '__main__':
    server_address = ('', 9000)
    httpd = HTTPServer(server_address, Handler)
    print('Mock HuggingFace server running on port 9000')
    httpd.serve_forever()

