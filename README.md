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