# Socket_Programming
# UDP Client-Server Application in Java

## Project Overview
This project implements a simple Client-Server system using UDP sockets in Java.
The server handles multiple clients simultaneously and manages access to files based on user permissions.

The system demonstrates:
- UDP communication
- Client management
- File operations on the server
- Access control using permissions

---

## Technologies Used
- Java (JDK 8+)
- UDP Sockets (DatagramSocket, DatagramPacket)
- IntelliJ IDEA

---

## How to Run the Project

### 1. Start the Server
Run:
UDPServer.java

Expected output:
UDP Server is running...
PORT: 9876

### 2. Start Clients
Run UDPClient.java at least 4 times.

For each client, enter:

127.0.0.1
9876



---

## Client Commands

- Register:
REGISTER <username>


- List files:
LIST

- Read file:
READ notes.txt



- Write to file (admin only):
WRITE notes.txt Your message



- Execute file (admin only):
EXECUTE test.bat


- Exit:
EXIT

---

## Permissions System

- admin1
  - READ
  - WRITE
  - EXECUTE

- Other clients
  - READ only

---

## Example Test Scenario

Client 1:
REGISTER admin1

WRITE notes.txt Hello from admin


Client 2:
REGISTER client2

WRITE notes.txt test


Access denied

Client 3:

REGISTER client3

READ notes.txt



Client 4:

REGISTER client4

LIST

---

## Network Requirements
- Minimum 4 clients connected
- Can run:
  - On same machine (localhost)
  - On local network (using real IP)

---

## Key Features
- UDP communication (connectionless)
- Multi-client support
- File management on server
- Permission-based access control
- Command-line interface

---

## Notes
- UDP does not guarantee delivery or order of messages
- Server processes requests independently
- Ensure port 9876 is open


---

## License
This project is for educational purposes.