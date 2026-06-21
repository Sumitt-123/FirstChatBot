# FirstChatBot - Backend

A Java Spring Boot backend for FirstChatBot with integrated LLM (Language Model) support.

## Overview

This backend provides RESTful APIs for the FirstChatBot application:
- **Chat API**: Send messages and receive AI-powered responses
- **Session Management**: Maintain conversation context
- **LLM Integration**: Uses free Hugging Face Inference API

## Architecture

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/firstchatbot/backend/
│   │   │   ├── FirstChatBotBackendApplication.java    # Main Spring Boot app
│   │   │   ├── controller/
│   │   │   │   └── ChatController.java                # REST endpoints
│   │   │   ├── service/
│   │   │   │   ├── ChatService.java                   # Chat business logic
│   │   │   │   ├── LLMService.java                    # LLM interface
│   │   │   │   └── HuggingFaceLLMService.java         # HF API implementation
│   │   │   ├── model/
│   │   │   │   ├── ChatMessage.java                   # Message model
│   │   │   │   ├── ChatRequest.java                   # Request DTO
│   │   │   │   ├── ChatResponse.java                  # Response DTO
│   │   │   │   └── ChatParameters.java                # LLM parameters
│   │   │   └── config/
│   │   │       └── CorsConfig.java                    # CORS configuration
│   │   └── resources/
│   │       └── application.properties                  # Configuration
│   └── test/                                           # Unit tests
├── pom.xml                                             # Maven configuration
└── README.md                                           # This file
```

## Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **Internet connection** (for downloading dependencies and accessing Hugging Face API)
- **Hugging Face API Token** (free, from https://huggingface.co/settings/tokens)

## Setup

### 1. Get Hugging Face API Token

1. Go to https://huggingface.co/settings/tokens
2. Click "New token"
3. Create a token with **read** access
4. Copy the token value

### 2. Set Environment Variable

**Windows:**
```cmd
setx HUGGINGFACE_API_TOKEN "your-token-here"
# Restart terminal/IDE for changes to take effect
```

**Linux/Mac:**
```bash
export HUGGINGFACE_API_TOKEN="your-token-here"
```

Or add to your shell profile (~/.bashrc, ~/.zshrc, etc.)

### 3. Install Dependencies

```bash
cd backend
mvn clean install
```

This downloads all required dependencies defined in `pom.xml`.

## Running the Backend

### Option 1: Maven

```bash
cd backend
mvn spring-boot:run
```

### Option 2: Build and Run JAR

```bash
cd backend

# Build the application
mvn clean package

# Run the generated JAR
java -jar target/firstchatbot-backend-0.1.0.jar
```

### Option 3: IDE

1. Open the project in your IDE (IntelliJ, Eclipse, VS Code)
2. Set environment variable HUGGINGFACE_API_TOKEN
3. Run `FirstChatBotBackendApplication.java` as a Java Application

The server will start at `http://localhost:8080`

## API Endpoints

### 1. Chat Endpoint

**POST** `/api/chat`

Send a message and receive a response.

**Request:**
```json
{
  "message": "Hello, how are you?",
  "sessionId": "optional-session-id",
  "parameters": {
    "temperature": 0.7,
    "maxLength": 100,
    "topP": 0.9,
    "topK": 50
  }
}
```

**Response:**
```json
{
  "message": "I'm doing well, thank you!",
  "status": "success",
  "sessionId": "generated-or-provided-session-id",
  "model": "gpt2",
  "processingTime": 1500
}
```

**Status Codes:**
- `200 OK` - Success
- `400 Bad Request` - Invalid input
- `500 Internal Server Error` - Server error

### 2. Health Check Endpoint

**GET** `/api/chat/health`

Check API and LLM service status.

**Response:**
```json
{
  "status": "up",
  "llmAvailable": true,
  "timestamp": 1624567890123
}
```

### 3. Clear Session Endpoint

**DELETE** `/api/chat/session/{sessionId}`

Clear conversation history for a session.

**Response:**
```json
{
  "message": "Session cleared successfully"
}
```

## Configuration

Edit `backend/src/main/resources/application.properties` to customize:

```properties
# Server configuration
server.port=8080
server.servlet.context-path=/api

# LLM Configuration
llm.api.type=huggingface
llm.api.endpoint=https://api-inference.huggingface.co/models/
llm.api.model=gpt2                    # Model name
llm.api.token=${HUGGINGFACE_API_TOKEN}  # From environment
llm.api.timeout=30                    # Seconds
llm.api.max-length=100                # Tokens

# CORS Configuration
cors.allowed-origins=http://localhost:5173
```

## Models Available

### Recommended Free Models on Hugging Face

1. **gpt2** (default)
   - Small, fast
   - Good for conversation
   - ~124M parameters

2. **distilgpt2**
   - Even smaller and faster
   - Good performance for speed trade-off

3. **EleutherAI/gpt-neo-125M**
   - Better quality
   - Slightly larger

4. **mistralai/Mistral-7B-v0.1**
   - Better quality, slower
   - Requires more resources

To use a different model, change `llm.api.model` in `application.properties`.

## Integration with Frontend

### Environment Setup

Make sure frontend is configured to call the backend:

```javascript
// frontend/src/App.jsx
const API_BASE_URL = 'http://localhost:8080/api';

const sendMessage = async (message) => {
  try {
    const response = await fetch(`${API_BASE_URL}/chat`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        message: message,
        parameters: {
          temperature: 0.7,
          maxLength: 100
        }
      })
    });
    
    const data = await response.json();
    return data.message;
  } catch (error) {
    console.error('Error:', error);
    return 'Error communicating with backend';
  }
};
```

### Running Both Frontend and Backend

```bash
# Terminal 1 - Backend
cd backend
mvn spring-boot:run

# Terminal 2 - Frontend
cd frontend
npm run dev
```

## Troubleshooting

### Issue: "LLM service is not available"

**Solution:** 
- Verify HUGGINGFACE_API_TOKEN is set: `echo $HUGGINGFACE_API_TOKEN`
- Token should not be "test-token" (default)
- Restart your IDE/terminal after setting environment variable

### Issue: Connection timeout

**Solution:**
- Check internet connection
- Verify firewall is not blocking requests
- Increase timeout in `application.properties`: `llm.api.timeout=60`

### Issue: Model not found (404)

**Solution:**
- Verify model name is correct
- Model must be publicly available on Hugging Face
- Check https://huggingface.co/models for valid models

### Issue: Rate limited

**Solution:**
- Free tier has rate limits (~30 calls/min)
- Use `temperature` to control randomness
- Consider caching repeated responses
- Set up scheduled requests instead of real-time

### Issue: Port 8080 already in use

**Solution:**
- Change port in `application.properties`: `server.port=8081`
- Or kill process using port: `lsof -i :8080` (Mac/Linux)

## Building for Production

### Create Executable JAR

```bash
cd backend
mvn clean package -DskipTests
```

This creates `target/firstchatbot-backend-0.1.0.jar`

### Run in Production

```bash
# Set environment variables
export HUGGINGFACE_API_TOKEN="your-production-token"

# Run the JAR
java -jar firstchatbot-backend-0.1.0.jar
```

### Docker Deployment (Optional)

Create a `Dockerfile`:

```dockerfile
FROM openjdk:17-slim
COPY target/firstchatbot-backend-0.1.0.jar app.jar
ENV HUGGINGFACE_API_TOKEN=${HUGGINGFACE_API_TOKEN}
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
docker build -t firstchatbot-backend .
docker run -e HUGGINGFACE_API_TOKEN="your-token" -p 8080:8080 firstchatbot-backend
```

## Development

### Adding New Features

1. **New endpoints:** Add methods to `ChatController.java`
2. **Business logic:** Add methods to `ChatService.java`
3. **Data models:** Create new classes in `model/` package
4. **Configuration:** Update `application.properties`

### Running Tests

```bash
cd backend
mvn test
```

### Logging

View detailed logs by changing level in `application.properties`:
```properties
logging.level.com.firstchatbot=DEBUG
```

## Technologies Used

- **Spring Boot 3.2.0** - Application framework
- **Java 17** - Language
- **Maven 3.6+** - Build tool
- **OkHttp3** - HTTP client
- **Gson** - JSON processing
- **Lombok** - Reduce boilerplate
- **SLF4J + Logback** - Logging

## Dependencies

See `pom.xml` for complete list. Key dependencies:
- spring-boot-starter-web
- okhttp3
- gson
- lombok
- httpclient5

## API Best Practices Implemented

✅ RESTful design with proper HTTP methods
✅ Meaningful HTTP status codes
✅ JSON request/response format
✅ Error handling and messages
✅ Session management
✅ CORS support
✅ Request validation
✅ Proper logging
✅ Configuration externalization
✅ Modular architecture

## Performance Considerations

- **Connection pooling:** OkHttp manages connection reuse
- **Timeouts:** 30-second default for external API calls
- **Caching:** Can be added to repeated requests
- **Async processing:** Can be added for heavy operations
- **Load balancing:** Can deploy multiple instances

## Security Notes

- **API Token:** Never commit tokens to version control
- **CORS:** Configure for your specific domains
- **HTTPS:** Use in production
- **Rate limiting:** Implement on production
- **Input validation:** All endpoints validate input

## Support

For issues or questions:
1. Check troubleshooting section above
2. Review logs: `logging.level.com.firstchatbot=DEBUG`
3. Check Hugging Face API status
4. Verify environment configuration

## Next Steps

1. ✅ Backend running locally
2. Update frontend to call backend API
3. Test end-to-end integration
4. Add authentication (optional)
5. Add database (optional)
6. Deploy to production

## License

This project is open source and available for educational and commercial use.

