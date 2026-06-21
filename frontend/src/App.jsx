import { useState } from "react";

const initialMessages = [
  { from: "bot", text: "Hi! I am your chatbot. Ask me anything." },
];

function App() {
  const [messages, setMessages] = useState(initialMessages);
  const [input, setInput] = useState("");
  const [sessionId, setSessionId] = useState(null);

  const sendMessage = async () => {
    const trimmed = input.trim();
    if (!trimmed) return;

    const userMessage = { from: "user", text: trimmed };
    setMessages((current) => [...current, userMessage]);
    setInput("");

    // POST to backend /chat (Vite proxy will forward to backend)
    try {
      const payload = { message: trimmed };
      if (sessionId) payload.sessionId = sessionId;

      const res = await fetch("/chat", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      // Guard against non-JSON responses (CORS failure, HTML error pages, plain text)
      // to avoid 'Unexpected token' JSON parse errors. Inspect Content-Type first
      // and fall back to text when it's not JSON.
      let data = null;
      const contentType = res.headers.get("content-type") || "";
      if (contentType.includes("application/json")) {
        // parse JSON whether status OK or error body contains JSON
        data = await res.json();
      } else {
        // not JSON: read text and surface it as an error
        const text = await res.text();
        throw new Error(text || `HTTP ${res.status}`);
      }

      if (data && data.sessionId && !sessionId) setSessionId(data.sessionId);

      const botReply = {
        from: "bot",
        text: data && data.message ? data.message : (data && data.error) ? data.error : "No response from server",
      };

      setMessages((current) => [...current, botReply]);
    } catch (err) {
      const botReply = { from: "bot", text: "Error contacting server: " + err.message };
      setMessages((current) => [...current, botReply]);
    }
  };

  const handleKeyDown = (event) => {
    if (event.key === "Enter") {
      event.preventDefault();
      sendMessage();
    }
  };

  return (
    <div className="app-shell">
      <div className="chat-card">
        <header className="chat-header">
          <h1>FirstChatBot</h1>
          <p>React demo frontend for your chatbot.</p>
        </header>

        <div className="message-list">
          {messages.map((message, index) => (
            <div key={index} className={`message ${message.from}`}>
              <div className="message-bubble">{message.text}</div>
            </div>
          ))}
        </div>

        <footer className="chat-input-area">
          <div className="question-field">
            <label htmlFor="question-input">Ask a question</label>
            <textarea
              id="question-input"
              value={input}
              onChange={(event) => setInput(event.target.value)}
              onKeyDown={handleKeyDown}
              placeholder="Ask your question here..."
              rows="2"
            />
          </div>
          <button onClick={sendMessage}>Send</button>
        </footer>
      </div>
    </div>
  );
}

export default App;
