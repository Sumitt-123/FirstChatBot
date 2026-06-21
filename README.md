# FirstChatBot

A modern React-based chatbot application with a clean, responsive frontend built with Vite.

## Project Overview

FirstChatBot is structured as a modular project with clear separation between frontend and project-level concerns:

- **Frontend**: React 18 application with Vite bundler
- **Scalable**: Ready to integrate with backend services
- **Modern Stack**: Built with latest technologies and best practices

## Project Structure

```
FirstChatBot/
├── frontend/                      # React frontend application
│   ├── src/
│   │   ├── App.jsx               # Main chatbot component
│   │   ├── main.jsx              # React entry point
│   │   └── styles.css            # Global styles (dark theme)
│   ├── index.html                # HTML template
│   ├── package.json              # Frontend dependencies
│   ├── package-lock.json         # Locked dependency versions
│   ├── vite.config.js            # Vite build configuration
│   ├── README.md                 # Frontend documentation
│   ├── .gitignore                # Frontend git ignores
│   ├── node_modules/             # Dependencies
│   └── dist/                     # Production build (auto-generated)
│
├── backend/                       # Java Spring Boot backend with LLM
│   ├── src/
│   │   ├── main/java/com/firstchatbot/backend/
│   │   │   ├── FirstChatBotBackendApplication.java
│   │   │   ├── controller/
│   │   │   │   └── ChatController.java      # REST endpoints
│   │   │   ├── service/
│   │   │   │   ├── ChatService.java         # Business logic
│   │   │   │   ├── LLMService.java          # LLM interface
│   │   │   │   └── HuggingFaceLLMService.java
│   │   │   ├── model/
│   │   │   │   ├── ChatMessage.java
│   │   │   │   ├── ChatRequest.java
│   │   │   │   ├── ChatResponse.java
│   │   │   │   └── ChatParameters.java
│   │   │   └── config/
│   │   │       └── CorsConfig.java
│   │   └── resources/
│   │       └── application.properties       # Configuration
│   ├── pom.xml                   # Maven configuration
│   ├── README.md                 # Backend documentation
│   ├── .gitignore                # Backend git ignores
│   └── target/                   # Build output (auto-generated)
│
├── README.md                      # Project documentation (this file)
├── .git/                          # Version control
└── .idea/                         # IDE configuration
```

## Quick Start

### Frontend Setup

```bash
# Navigate to frontend folder
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

The frontend will be available at `http://localhost:5173`

### Backend Setup

```bash
# Set environment variable (Hugging Face API token)
# Windows:
setx HUGGINGFACE_API_TOKEN "your-token-here"

# Linux/Mac:
export HUGGINGFACE_API_TOKEN="your-token-here"

# Navigate to backend folder
cd backend

# Build and run
mvn spring-boot:run
```

The backend will be available at `http://localhost:8080/api`

### Full Application

Once both are running:

1. **Frontend** communicates with **Backend API**
2. **Backend** processes requests and calls **Hugging Face LLM API**
3. **Bot responses** are displayed in real-time

## Frontend & Backend Documentation

- **Frontend Details:** See [frontend/README.md](./frontend/README.md)
- **Backend Details:** See [backend/README.md](./backend/README.md)

Both folders contain comprehensive setup, configuration, and troubleshooting guides.

### Key Features

- **React 18 Frontend**: Modern, responsive chat UI with dark theme
- **Java Spring Boot Backend**: RESTful APIs with proper architecture
- **LLM Integration**: Free Hugging Face Inference API for AI responses
- **Real-time Chat**: Messages update instantly in the UI
- **Session Management**: Maintain conversation context across messages
- **CORS Enabled**: Secure communication between frontend and backend
- **Modular Design**: Separate frontend and backend for scalability

### Frontend Technologies

- **React 18.3.1** - UI library
- **Vite 5.4.1** - Build tool and dev server
- **@vitejs/plugin-react 4.5.1** - React plugin for Vite

### Backend Technologies

- **Spring Boot 3.2.0** - Java framework
- **Java 17** - Language
- **Maven** - Build tool
- **Hugging Face API** - Free LLM service
- **OkHttp3** - HTTP client for API calls

## Next Steps

### For Frontend Development

1. **Update UI**: Connect the frontend to call the backend APIs
2. **Add Features**: Implement additional UI features
3. **Testing**: Add tests for components
4. **Styling**: Customize the theme

### For Backend Development

1. **API Testing**: Test endpoints with curl or Postman
2. **Database**: Add persistent storage (PostgreSQL, MongoDB, etc.)
3. **Authentication**: Implement user authentication
4. **Model Selection**: Try different LLM models for better responses
5. **Caching**: Add response caching for frequently asked questions

### For Project Expansion

- Add user authentication and profiles
- Implement message history persistence
- Add multiple conversation types
- Deploy to cloud (AWS, Azure, GCP)
- Add CI/CD pipeline
- Implement analytics and monitoring

## Development Workflow

```bash
# 1. Terminal 1 - Start Backend
cd backend
mvn spring-boot:run
# Server runs at http://localhost:8080/api

# 2. Terminal 2 - Start Frontend
cd frontend
npm run dev
# App runs at http://localhost:5173

# 3. Terminal 3 - Make changes and test
# Changes auto-reload in both frontend and backend (with Spring Boot DevTools)

# 4. When ready to deploy
# Frontend
cd frontend && npm run build

# Backend
cd backend && mvn clean package
```

## API Endpoints

### Chat Endpoint

**POST** `/api/chat`

Send a message and receive AI response.

```bash
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Hello!",
    "parameters": {
      "temperature": 0.7,
      "maxLength": 100
    }
  }'
```

### Health Check

**GET** `/api/chat/health`

Check API and LLM service status.

```bash
curl http://localhost:8080/api/chat/health
```

For detailed API documentation, see [backend/README.md](./backend/README.md)

## Development Workflow

```bash
# 1. Navigate to frontend
cd frontend

# 2. Install dependencies (first time only)
npm install

# 3. Start development server
npm run dev

# 4. Make changes to src/ files
# (changes are automatically reflected with HMR)

# 5. When ready to deploy
npm run build
```

## Project Scripts

| Location | Script | Description |
|----------|--------|-------------|
| `frontend/` | `npm run dev` | Start development server |
| `frontend/` | `npm run build` | Create production build |
| `frontend/` | `npm run preview` | Preview production build |

## Deployment

### Frontend Deployment

The production-ready frontend is generated in `frontend/dist/` after running `npm run build`.

This folder can be deployed to any static hosting service:
- Netlify
- Vercel
- GitHub Pages
- AWS S3 + CloudFront
- Any web server

### Backend Deployment

Build a production-ready JAR:

```bash
cd backend
mvn clean package
java -jar target/firstchatbot-backend-0.1.0.jar
```

Backend can be deployed to:
- AWS EC2 / Elastic Beanstalk
- Google Cloud Run
- Azure App Service
- Heroku
- DigitalOcean
- Any server with Java 17+
- Docker containers

## Troubleshooting

### Backend won't start

1. **Check Java version**: `java -version` (should be 17+)
2. **Check Maven**: `mvn -version`
3. **Port conflict**: Change port in `backend/src/main/resources/application.properties`
4. **Missing API token**: Set HUGGINGFACE_API_TOKEN environment variable

### API calls failing

1. **Backend not running**: Check server is at `http://localhost:8080`
2. **CORS errors**: Check frontend URL is in CORS whitelist
3. **Rate limited**: Hugging Face free tier has request limits

### Frontend can't connect to backend

1. **Verify both are running**:
   - Frontend: `http://localhost:5173`
   - Backend: `http://localhost:8080`
2. **Check API endpoint** in frontend code
3. **Verify CORS configuration** in backend

For detailed troubleshooting, see:
- [frontend/README.md](./frontend/README.md)
- [backend/README.md](./backend/README.md)

## Contributing

1. Create feature branches from main
2. Keep commits focused and descriptive
3. Test changes in the frontend before committing
4. Update documentation as needed

## Project Support

For frontend-specific issues, see [frontend/README.md](./frontend/README.md)

For project-level questions or to expand the project with backend services, refer to this README.
