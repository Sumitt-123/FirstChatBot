# FirstChatBot - Complete Setup Guide

This guide walks you through setting up and running the complete FirstChatBot application with both frontend and backend.

## Prerequisites

### System Requirements

- **Java 17 or higher**: Download from https://www.oracle.com/java/technologies/downloads/
- **Maven 3.6+**: Download from https://maven.apache.org/download.cgi (or use IDE bundled Maven)
- **Node.js 16+**: Download from https://nodejs.org/
- **Git** (optional but recommended)
- **Internet Connection**: For downloading dependencies and API calls

### Accounts Required

- **Hugging Face Account** (FREE): Sign up at https://huggingface.co/
  - Create API token at: https://huggingface.co/settings/tokens
  - This token is needed for the chatbot LLM functionality

## Step-by-Step Setup

### Step 1: Java Installation

1. Download Java 17+ from https://www.oracle.com/java/technologies/downloads/
2. Run the installer and follow instructions
3. Verify installation:
   ```bash
   java -version
   ```
   Should show Java 17 or higher

### Step 2: Maven Installation

**Option A: Manual Installation**
1. Download Maven from https://maven.apache.org/download.cgi
2. Extract to a folder (e.g., `C:\apache-maven`)
3. Add Maven to PATH environment variable
4. Verify:
   ```bash
   mvn -version
   ```

**Option B: Use IDE-bundled Maven**
- IntelliJ IDEA: Has Maven integrated
- VS Code: Install Maven extension
- Eclipse: Has Maven integrated

### Step 3: Node.js Installation

1. Download Node.js from https://nodejs.org/
2. Run the installer (includes npm)
3. Verify:
   ```bash
   node -version
   npm -version
   ```

### Step 4: Get Hugging Face API Token

1. Go to https://huggingface.co/ and sign up (free)
2. Go to https://huggingface.co/settings/tokens
3. Click "New token"
4. Select "read" access level
5. Create the token and copy it
6. **Save this token** - you'll need it in the next step

### Step 5: Set Environment Variables

Set the Hugging Face API token so the backend can access the LLM service.

**Windows (Command Prompt as Administrator):**
```cmd
setx HUGGINGFACE_API_TOKEN "your-actual-token-here"
```
Then close and reopen your terminal/IDE for changes to take effect.

**Windows (PowerShell as Administrator):**
```powershell
[Environment]::SetEnvironmentVariable("HUGGINGFACE_API_TOKEN", "your-actual-token-here", "User")
```

**Linux/Mac (add to ~/.bashrc or ~/.zshrc):**
```bash
export HUGGINGFACE_API_TOKEN="your-actual-token-here"
source ~/.bashrc  # or ~/.zshrc
```

**Verify the token is set:**
```bash
echo %HUGGINGFACE_API_TOKEN%  # Windows
echo $HUGGINGFACE_API_TOKEN   # Linux/Mac
```

## Running the Application

### Option 1: Run Both Frontend and Backend (Recommended)

**Terminal 1 - Start Backend:**
```bash
cd backend
mvn spring-boot:run
```

Backend will start at: `http://localhost:8080/api`

**Terminal 2 - Start Frontend:**
```bash
cd frontend
npm install    # First time only
npm run dev
```

Frontend will start at: `http://localhost:5173`

Open your browser to `http://localhost:5173` and start chatting!

### Option 2: Build and Run Backend (Production-like)

```bash
cd backend

# Build the application
mvn clean package

# Run the generated JAR
java -jar target/firstchatbot-backend-0.1.0.jar
```

### Option 3: Run in IDE

**IntelliJ IDEA:**
1. Open project
2. Right-click `FirstChatBotBackendApplication.java`
3. Select "Run"

**VS Code:**
1. Install Java Extension Pack
2. Open Run and Debug (Ctrl+Shift+D)
3. Select "Java" configuration

## Testing the Application

### 1. Test Backend Health

Open browser or use curl:
```bash
curl http://localhost:8080/api/chat/health
```

Expected response:
```json
{
  "status": "up",
  "llmAvailable": true,
  "timestamp": 1624567890123
}
```

### 2. Test Chat API with curl

```bash
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Hello, how are you?",
    "parameters": {
      "temperature": 0.7,
      "maxLength": 100
    }
  }'
```

### 3. Test Full Application

1. Open `http://localhost:5173` in browser
2. Type a message in the input field
3. Press Send or hit Enter
4. Wait for the bot response
5. See the AI-generated response appear

### 4. Test with Postman

1. Install Postman from https://www.postman.com/
2. Create new POST request
3. URL: `http://localhost:8080/api/chat`
4. Headers: `Content-Type: application/json`
5. Body (JSON):
   ```json
   {
     "message": "Test message",
     "parameters": {
       "temperature": 0.7
     }
   }
   ```
6. Send and check response

## Troubleshooting

### Problem: "mvn: command not found"

**Solution:**
- Maven not installed or not in PATH
- Install Maven or add to PATH
- Or use IDE's bundled Maven

### Problem: HUGGINGFACE_API_TOKEN not recognized

**Solution:**
- Restart IDE/terminal after setting environment variable
- Verify token is set: `echo $HUGGINGFACE_API_TOKEN`
- Use full token value without quotes

### Problem: Port 8080 already in use

**Solution:**
- Change port in `backend/src/main/resources/application.properties`
- Find what's using port: `lsof -i :8080` (Mac/Linux)
- Kill the process or choose different port

### Problem: Frontend can't connect to backend

**Solution:**
- Verify backend is running at `http://localhost:8080/api/chat/health`
- Check CORS configuration allows frontend origin
- Check network - try from terminal: `curl http://localhost:8080/api/chat/health`

### Problem: "Unauthorized" or "API token invalid"

**Solution:**
- Verify HUGGINGFACE_API_TOKEN is correct
- Regenerate token at https://huggingface.co/settings/tokens
- Make sure token is not the default "test-token"

### Problem: Slow or no responses from LLM

**Solution:**
- Hugging Face API can be slow on free tier
- Try different model: Change `llm.api.model` in `application.properties`
- Reduce `max-length` for faster responses
- Wait for model to load (first call can take 30+ seconds)

### Problem: Build fails with Maven

**Solution:**
```bash
# Clear Maven cache
mvn clean

# Rebuild
mvn clean install -U

# If specific dependency fails
mvn dependency:tree
```

## Project Structure Overview

```
FirstChatBot/
├── frontend/          # React UI (npm, Vite)
├── backend/           # Java backend (Maven, Spring Boot)
└── README.md          # This file
```

## Common Tasks

### Update Frontend to Call Backend

Edit `frontend/src/App.jsx`:
```javascript
const API_URL = 'http://localhost:8080/api';

const sendMessage = async (message) => {
  const response = await fetch(`${API_URL}/chat`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ message })
  });
  return response.json();
};
```

### Change LLM Model

Edit `backend/src/main/resources/application.properties`:
```properties
llm.api.model=distilgpt2  # Try: gpt2, distilgpt2, EleutherAI/gpt-neo-125M
```

### Change Server Port

Edit `backend/src/main/resources/application.properties`:
```properties
server.port=9000
```

### View Backend Logs

Set debug logging in `backend/src/main/resources/application.properties`:
```properties
logging.level.com.firstchatbot=DEBUG
```

## Next Steps

1. ✅ Environment setup complete
2. ✅ Backend and frontend running
3. Start customizing the application:
   - Modify the chat UI
   - Add new features
   - Connect to a real database
   - Deploy to cloud

## Documentation

For detailed information, see:

- **Frontend**: See [frontend/README.md](./frontend/README.md)
  - React setup, Vite configuration, styling
  
- **Backend**: See [backend/README.md](./backend/README.md)
  - Spring Boot setup, API endpoints, LLM configuration, deployment

- **Main Project**: See [README.md](./README.md)
  - Project overview, architecture, API examples

## Support Resources

- **Java/Maven Issues**: https://maven.apache.org/
- **Spring Boot**: https://spring.io/projects/spring-boot
- **React/Vite**: https://vitejs.dev/
- **Hugging Face API**: https://huggingface.co/docs/inference-api
- **Project Issues**: Check README files above

## Deployment

When ready to deploy:

1. **Frontend Build**:
   ```bash
   cd frontend
   npm run build
   # Deploy `dist/` folder to hosting (Netlify, Vercel, etc.)
   ```

2. **Backend Build**:
   ```bash
   cd backend
   mvn clean package
   # Deploy JAR to server with Java 17+
   ```

See detailed deployment guides in [frontend/README.md](./frontend/README.md) and [backend/README.md](./backend/README.md)

## Quick Reference

| Task | Command |
|------|---------|
| Start backend | `cd backend && mvn spring-boot:run` |
| Start frontend | `cd frontend && npm run dev` |
| Build frontend | `cd frontend && npm run build` |
| Build backend | `cd backend && mvn clean package` |
| Check health | `curl http://localhost:8080/api/chat/health` |
| Test chat API | See "Testing the Application" above |

## Congratulations!

Your FirstChatBot is now set up and running! The application features:

✅ Modern React frontend with real-time chat UI
✅ Java Spring Boot backend with REST APIs
✅ Free Hugging Face LLM integration
✅ Session management for conversations
✅ Proper error handling and logging
✅ Production-ready architecture

Enjoy chatting with your AI-powered bot! 🤖💬

