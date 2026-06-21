import { useState } from "react";

const initialMessages = [
  { from: "bot", text: "Hi! I am your chatbot. Ask me anything." },
];

function App() {
  const [messages, setMessages] = useState(initialMessages);
  const [input, setInput] = useState("");

  const sendMessage = () => {
    const trimmed = input.trim();
    if (!trimmed) return;

    const userMessage = { from: "user", text: trimmed };
    const botReply = {
      from: "bot",
      text: `You said: "${trimmed}". Replace this with your backend response.`,
    };

    setMessages((current) => [...current, userMessage, botReply]);
    setInput("");
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
