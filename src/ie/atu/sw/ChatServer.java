package ie.atu.sw;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.*;

public class ChatServer {
    public static int port = 8000; //  port
    private static String hostName = "localhost"; // Default host name
    private static final ConcurrentHashMap<String, PrintWriter> clientList = new ConcurrentHashMap<>();

    public static void main(String[] args) {
    	while (true) {
            try {
                setup();  // Call setup and return only if no error occurs
                break;    // Exit the loop if setup is successful
            } catch (Exception e) {
            	//Reset defaults
            	port = 8000;
            	hostName = null;
            	
                System.err.println("Error in setup: " + e.getMessage());
                System.out.println("Retrying setup...");
            }
        }
        ExecutorService pool = Executors.newFixedThreadPool(50); // Allow up to 50 clients to chat
        ServerSocket server = startServer(); // Start the server

        // Accept and handle new clients indefinitely
        while (true) {
            manageNewClient(server, pool);
        }
    }
    
    //Method to initiate setup. Prompts user to input set up hostname(optional) & port
    private static void setup() {
    	// Prepare user input
        Scanner s = new Scanner(System.in);
        
        // Promt user to enter hostname
        System.out.println("Enter Host Name/IP (Leave blank to use default \"localhost\"): ");
        String hostInput = s.nextLine().trim();
        //Set hostname
        if (hostInput.isEmpty()) {
            hostName = "localhost";  // use Default hostname if no input
        } else {
            hostName = hostInput; // Use user input
        }

        
		while (true) {
			System.out.println("Enter Port Number (Leave blank to use default 8000): ");
			String portInput = s.nextLine().trim(); // User input
			if (!portInput.isEmpty()) {
				try {
					port = Integer.parseInt(portInput); // User input as int
					break;
					
				} catch (NumberFormatException e) {
					System.out.println("Invalid port number.");
				}
				
			}else {
				port = 8000; //use default
				break;
			}
		}
        
    }

    // Method to start the server
    private static ServerSocket startServer() {
        try {
            if (!hostName.isEmpty()) {
                ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(hostName));
                System.out.println("Listening for connections on port " + port);
                return server;
            } else {
                ServerSocket server = new ServerSocket(port, 50);
                System.out.println("Listening for connections on port " + port);
                return server;
            }
        } catch (IOException ex) {
            System.err.println("Couldn't start server: " + ex.getMessage());
            System.exit(1);
        }
        return null;
    }


    // Method to handle a new client connection
    private static void manageNewClient(ServerSocket server, ExecutorService pool) {
        try {
        	//Accept client connection & get ID
            Socket client = server.accept(); // Client Connection
            String clientId = client.getInetAddress() + ":" + client.getPort();
            
            //Prepare send/receive data
            PrintWriter output = new PrintWriter(client.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            
            //Add new client to the client list
            clientList.put(clientId, output);
            System.out.println("Client connected from " + clientId);
            
            //Handle each client on a separate thread
            Callable<Void> task = new ServerTask(client, clientId, input);
            pool.submit(task);
        } catch (IOException ex) {
            System.err.println("Error accepting connection: " + ex.getMessage());
        }
    }

    // Method to broadcast a message to all connected clients
    private static void broadcastMessage(String username, String message) {
    	// Iterate through the client list and broadcast to all clients
        for (PrintWriter clientOut : clientList.values()) {
            clientOut.println(username + ": " + message);
        }
    }

    // Method to remove a disconnected client
    private static void removeClient(String clientId) {
        clientList.remove(clientId);
        System.out.println("Client disconnected: " + clientId);
    }

    
    
    // Inner class to handle client communication
    private static class ServerTask implements Callable<Void> {
        private final Socket connection;
        private final String clientId;
        private final BufferedReader in;
        
        // Each client is handled in a separate thread
        ServerTask(Socket connection, String clientId, BufferedReader in) {
            this.connection = connection;
            this.clientId = clientId;
            this.in = in;
        }
        @Override
        public Void call() {
            try {
            	//Receive username and confirm connection
                String username = in.readLine();
                System.out.println("New client connected: " + username + " (" + clientId + ")");

                String message; // client message
                while ((message = in.readLine()) != null) {
                    System.out.println(clientId + " (" + username + "): " + message);
                    broadcastMessage(username, message); // broadcast message
                }
            } catch (IOException ex) {
                System.err.println("Error handling client " + clientId + ": " + ex.getMessage());
            } finally {
                // Clean up when client disconnects
                cleanup();
            }
            return null;
        }

        // Method to clean up resources when a client disconnects
        private void cleanup() {
            removeClient(clientId); //Remove client from list
            try {
                connection.close(); // Close connection
            } catch (IOException ex) {
                System.err.println("Error closing connection for " + clientId + ": " + ex.getMessage());
            }
        }
    }
}