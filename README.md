# Chatroom App 💬

A simple multi-user **client-server chat application** written in Java.  
This program allows multiple clients (up to 50) to connect to a central server and exchange messages in real time.

---

## 📌 Features
- **Multi-client support** – up to 50 concurrent users  
- **Threaded server** – handles each client on a separate thread  
- **Broadcast messaging** – messages are sent to all connected clients  
- **Configurable hostname & port** – choose at runtime or use defaults  
- **Graceful connection handling** – clients can join and leave at any time  

---

## 🛠 Prerequisites
Before you run the program, make sure you have:

- **Java Development Kit (JDK)** – version 8 or newer  
- **Command-line terminal** – to compile and run the Java files  
- **Networking enabled** – use `localhost` for local testing or a network IP for remote connections

---

## 📂 Project Structure
```
Chatroom-App/
│
├── ChatServer.java   # The server application
├── ChatClient.java   # The client application
└── README.md         # Project documentation
```

---

## 🚀 How to Run

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

---

## 🖥 Example Usage
**Server terminal:**
```
Enter hostname [default: localhost]: 
Enter port [default: 12345]:
Server started on localhost:12345
Waiting for clients to connect...
```

**Client terminal:**
```
Enter hostname [default: localhost]: 
Enter port [default: 12345]: 
Enter your username: Donal
Connected to chatroom. Type your messages below:
```

---

## 📜 License
This project is open-source and available under the [MIT License](LICENSE).

---

## 👤 Author
**Dónal Murphy**  
- [LinkedIn](https://linkedin.com/in/donalmur)  
- [GitHub](https://github.com/Donal-Murphy)
