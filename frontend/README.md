# FirstChatBot - Frontend

A React-based chatbot UI built with Vite and React 18.

## Overview

This is the frontend application for FirstChatBot, featuring a clean and modern chat interface with real-time message updates.

## Quick Start

### Prerequisites
- Node.js (v16 or higher)
- npm (v8 or higher)

### Installation

```bash
# Install dependencies
npm install
```

### Development

```bash
# Start the development server with hot module replacement
npm run dev
```

The application will be available at `http://localhost:5173` (or another port if 5173 is in use).

### Production Build

```bash
# Build for production
npm run build

# Preview the production build locally
npm run preview
```

## Project Structure

```
frontend/
├── src/
│   ├── App.jsx              # Main chat application component
│   ├── main.jsx             # React DOM entry point
│   └── styles.css           # Global styles (dark theme)
├── index.html               # HTML template
├── vite.config.js           # Vite configuration
├── package.json             # Dependencies and scripts
├── package-lock.json        # Locked dependency versions
├── .gitignore               # Git ignore rules
├── .vite/                   # Vite cache (auto-generated)
├── dist/                    # Production build output (auto-generated)
└── node_modules/            # Project dependencies
```

## Features

- **Chat UI**: Modern, responsive chat interface
- **Message Bubbles**: User and bot message differentiation with styling
- **Real-time Updates**: Messages update instantly as they're added
- **Dark Theme**: Professional dark mode styling
- **Textarea Input**: Multi-line message input with Enter key support

## Technologies

- **React 18.3.1** - UI library
- **Vite 5.4.1** - Build tool and dev server
- **@vitejs/plugin-react 4.5.1** - React plugin for Vite

## Development Notes

### Keyboard Shortcuts
- `Enter` - Send message
- `Shift + Enter` - New line in message input

### File Descriptions

#### App.jsx
Main React component that handles:
- Message state management with `useState`
- Message sending logic
- Chat UI rendering with message list and input area
- Event handlers for user interactions

Currently includes placeholder bot responses. To integrate with a real backend:
1. Replace the `sendMessage()` function's bot reply logic
2. Add API call to your backend service
3. Handle async responses appropriately

#### styles.css
Complete styling including:
- Root CSS variables for theming
- Chat card layout
- Message bubbles (user vs bot)
- Input area styling
- Responsive design considerations
- Dark theme with gradient backgrounds

#### main.jsx
Entry point that:
- Imports React and ReactDOM
- Loads the App component
- Imports global styles
- Mounts the app to the DOM root element

## Building & Deployment

### Development Build
```bash
npm run dev
```

### Production Build
```bash
npm run build
```

Output is generated in the `dist/` folder. This folder can be deployed to any static hosting service (Netlify, Vercel, GitHub Pages, etc.).

### Preview Production Build
```bash
npm run preview
```

This starts a local server to preview the production build before deployment.

## Environment & Configuration

### Vite Configuration
- Root: Frontend folder itself
- Plugins: React Fast Refresh for HMR (Hot Module Replacement)
- Build output: `dist/` folder

### Browser Compatibility
Vite targets modern browsers with ES2020 support by default.

## Next Steps

1. **Connect Backend**: Update `App.jsx` to call your backend API instead of using placeholder responses
2. **Add Features**: Implement additional features like message persistence, typing indicators, etc.
3. **Deploy**: Build and deploy to your hosting platform
4. **Styling**: Customize `styles.css` to match your brand

## Scripts

| Script | Description |
|--------|-------------|
| `npm run dev` | Start development server with HMR |
| `npm run build` | Create production build |
| `npm run preview` | Preview production build locally |

## Troubleshooting

### Port Already in Use
If port 5173 is already in use, Vite will automatically try the next available port. Check the terminal output for the actual URL.

### Dependencies Not Installed
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
```

### Build Errors
```bash
# Clean build
npm run build
```

## Support & Contribution

For issues or contributions, please refer to the main project repository.

