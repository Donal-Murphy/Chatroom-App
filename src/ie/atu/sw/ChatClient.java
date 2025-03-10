package ie.atu.sw;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
	
	private static String hostName = "localhost"; // Default host name
    public static int port = 8000; // Default port number
    private static String userName = ""; // Client's custom username
    private static boolean keepRunning = true;
    
    //Main method
    public static void main(String[] args) {
    	
    	while (keepRunning) {
    		 // Prompt user to configure setup
            setup();

            // Try to connect to the server
            Socket connection = connectToServer();

            // Once connection is established, proceed with the application
            if (connection != null) {
                // Stop prompting for setup if connection is successful
                keepRunning = false;

                // Send username to the server
                sendUsername(connection);

                // Start a thread to listen for incoming messages
                new Thread(new Listener(connection)).start();

                // Handle user input on the main thread
                clientMessage(connection);
            } else {
            	//Reset to defaults
            	hostName = "localhost";
            	port = 8000;
            	
                // Optionally handle failed connection attempts
                System.out.println("Connection failed. Retrying...");
            }
        }
    	
    }
    
    //Method to initiate setup. Prompts user to input set up hostname, port & username
    private static void setup() {
    	
    	Scanner s = new Scanner(System.in); // New scanner for user input
    	
    	//Prompt user to input hostname
        System.out.println("Enter Host Name (Leave blank to use default \"localhost\"): ");
        String hostInput = s.nextLine().trim(); // User input
        if (!hostInput.isEmpty()) {
            hostName = hostInput; // Use user inputed hostname, otherwise use hardcoded default
        }
        
        // Prompt user to enter port number
		while (true) {
			System.out.println("Enter Port Number (Leave blank to use default 8000): ");
			String portInput = s.nextLine().trim(); // User input
			if (!portInput.isEmpty()) {
				try {
					// Parse user input for port number, otherwise use hardcoded default
					port = Integer.parseInt(portInput);
					break;
				} catch (NumberFormatException e) {
					// If user enters non-integer, send error, try again
					System.out.println("Invalid port number. Using default 8000.");
				}
			}else {
				break;
			}
		}
        
		//Prompt user to enter username
        while(true) {
        	System.out.println("Enter Username: ");
            userName = s.nextLine().trim(); // User input
            if(userName.isEmpty()) {
            	System.out.println("Username cannot be blank"); //Username cannot be blank, try again
            }else {
            	break;
            }
        }
        
    }

    // Method to connect to the server
    private static Socket connectToServer() {
        try {
            SocketAddress address = new InetSocketAddress(hostName, port);
            Socket socket = new Socket();
            socket.connect(address, 30000);  // 30-second timeout
            System.out.println("Connected to Server on host " + hostName);
            return socket;
        } catch (SocketTimeoutException e) {
            System.err.println("Connection timed out.");
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
        }
        return null;
    }
    
    // Method to send the username to the server
    private static void sendUsername(Socket connection) {
        try {
            PrintWriter output = new PrintWriter(connection.getOutputStream(), true); //Prepare to send to server
            output.println(userName); // Send username to server
            System.out.println("Username sent to server: " + userName);
        } catch (IOException e) {
            System.err.println("Error sending username: " + e.getMessage());
        }
    }

    // Method to handle user input and send messages to the server
    private static void clientMessage(Socket connection) {
        try {
            PrintWriter output = new PrintWriter(connection.getOutputStream(), true); //Prepare to send to server
            Scanner s = new Scanner(System.in); // Prepare to handle user input
            System.out.println("Type your message:");
            // Receive user input and send to server
            while (s.hasNextLine()) {
            	
                String message = s.nextLine();
                output.println(message);
            }
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
    
    // Runnable class to handle incoming messages from the server
    private static class Listener implements Runnable {
        private final Socket socket;

        Listener(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
            	//Prepare received messages
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                
                //Receive mesages. If null, indicates disconnect from server
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.err.println("Error reading from server: " + e.getMessage());
            } finally {
            	// In the event of server disconnect, close the socket and inform the user
                try {
                    socket.close();
                    System.out.println("Disconnected from server");
                } catch (IOException e) {
                    System.err.println("Error closing socket: " + e.getMessage());
                }
            }
        }
    }
}