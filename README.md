# Chatroom-App

This program is a client-server pair that allows for multiple clients to connect to a single server
and integrates a char functionality.

## ChatClient:
The client application ChatClient handles connections to the server and sending and receiving
data from the server. It contains the following features:

### Main(): 
Initiates the application and handles calls and communication between various methods
and classes used to run the client-side logic. It restarts the program in the event that a connection
cannot be established so that the user can attempt to re-enter details again. It also resets the
hostname and port instance variables if a connection fails. is also responsible for creating an
additional thread used by the Listener class.

### setup(): 
Prompts the user to input the hostname, port & username. Hostname and port have
locally stored variables so that they can be used throughout the application. The user inputs can
be left blank to use the default values for these variables. Username must be entered and cannot
be left blank so that all users.

### connectToServer(): 
This method establishes a socket connection to the server using the locally
stored hostname and port variables and returns the socket to the main() so that it can be passed
to other methods. It returns null if an error is encountered so that the main() can restart the setup()
process and attempt connection again. If no connection is established within 30 seconds, it
triggers a SocketTimeoutException.

### sendUsername(): 
Takes the socket connection received from main() to send the username to the
server so that it can be displayed when a message is broadcast.

### clientMessage(): 
Takes the socket connection received from main() in order to send messages to
the server for broadcast. Continuously prompts the user to enter a message so that multiple
messages can be sent consecutively.

### Class Listener: 
This class handles receiving broadcast messages from the server. It is run on a
separate thread so that messages can be both sent and received simultaneously.


## ChatServer:
The host application ChatServer establishes a server to which clients can connect. It is also
responsible for broadcasting client messages to all connected clients. A maximum of 50 clients
can be connected at a any one time. It contains the following features:

### main(): 
Initiates the application and handles calls and communication between various methods
and classes used to run the server-side logic. It is also responsible for creating the thread pool,
which each contain a client connection and limits the number of clients to 50.

### setup(): 
Prompts the user to enter hostname and port number. These can both be left blank in
order to use default values. Values are stored in instance variable so that they can be used by
other methods. The method insists that the port be an integer and re-prompts the user if
otherwise to prevent errors.

### startServer(): 
This method is responsible for initialising the server on the defined host and port
number and returns the ServerSocket to the caller (main()). It allows for both localhost servers or
specific IP-based servers, and limits the number of clients in queue to 50.

### manageNewClient(): 
This method is responsible for handling new clients who connect to the
server. It stores each client’s InetAddress and their associated PrintWriter object in a
ConcurrentHashMap called clientList, so that this list can later be parsed and messages can be
broadcast. It also creates an instance of ServerTask() and submits each client and their
inputStream to a separate thread from the thread pool.

### broadcastMessages(): 
Receives a username and message from each client and broadcasts it to
every client in the clientList.

### removeClient(): 
Removes a client from the clientList after they disconnect in order to only
broadcast messages to connected clients.

### Class ServerTask: 
This class implements Callable in order to handle communication between a
client and server on separate threads. It receives the username and message from the client and
invokes broadcastMessage to send these details to be broadcast to other clients. Once a
username has been received it continuously waits for more messages from the client. Finally,
when a client disconnects it invokes cleanup().

### cleanup(): 
Invokes removeClient and closes the client-server connection
