# MediQ Backend

Medical appointment management system.

## Quick Start

### Install Docker

```bash
# Linux/macOS
wget -qO get-docker.sh https://get.docker.com && sudo sh get-docker.sh

# macOS (Homebrew)
brew install --cask docker
```

### Run Application

```bash
# Option 1: Set API key in .env file (recommended)
echo "GROQ_API_KEY=your_groq_api_key_here" > .env

# Option 2: Set API key as environment variable
export GROQ_API_KEY="your_groq_api_key_here"

# Build and run (detached)
docker compose up -d --build

# Check status
docker compose ps

# View logs
docker compose logs -f

# Stop
docker compose down
```

**Access:** <http://localhost:7070>