const http = require('http');

const server = http.createServer((req, res) => {
  if (req.method === 'POST') {
    let body = '';
    req.on('data', chunk => body += chunk);
    req.on('end', () => {
      let prompt = null;
      try {
        const j = JSON.parse(body || '{}');
        prompt = j.inputs || j.input || j.text || null;
      } catch (e) {
        prompt = body;
      }
      if (!prompt) prompt = 'Hello from mock';
      const reply = [{ generated_text: String(prompt) + ' (mock reply)' }];
      res.writeHead(200, { 'Content-Type': 'application/json' });
      res.end(JSON.stringify(reply));
    });
  } else {
    res.writeHead(200, { 'Content-Type': 'text/plain' });
    res.end('Mock HF server');
  }
});

server.listen(9000, () => {
  console.log('Mock HuggingFace server listening on port 9000');
});

