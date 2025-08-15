# Chatroom-App

This program is a client-server pair that allows for multiple clients to connect to a single server
and integrates a chat functionality.

##  Prerequisites

- **Java Development Kit (JDK)** – version 8 or newer  
- **Command-line terminal** – to compile and run the Java files  
- **Networking enabled** – use `localhost` for local testing or a network IP for remote connections

- ---

## Instructions

### 1️⃣ Compile the source files
```bash
javac ChatServer.java
javac ChatClient.java
```

### 2️⃣ Start the server
```bash
java ChatServer
```
- You will be prompted for:
  - **Hostname** (press Enter for default)
  - **Port** (press Enter for default)

### 3️⃣ Start one or more clients (in separate terminal windows)
```bash
java ChatClient
```
- You will be prompted for:
  - **Hostname** – match the server's hostname  
  - **Port** – match the server's port  
  - **Username** – required to join the chat  

