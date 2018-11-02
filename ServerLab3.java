import java.io.*;
import java.net.*;
import java.util.Scanner;
/***********************************************************
 Creates a basic chat service between a client and a server.
 Allows for continous chat, regardless of the order of who
 is sending messages. This is completed using threads.

 @author Nathan Wichman
 @Version Fall 2018
 * ********************************************************/
public class ServerLab3{
    public static void main(String args[]) throws IOException{
	int portNumber;
	Scanner scn = new Scanner(System.in);

	System.out.print("Port number: ");
	portNumber = Integer.parseInt(scn.next());
	System.out.print("\n");
	
	//Socket for the server to operate on
	ServerSocket serverSocket = null; 
	
    try { 
         serverSocket = new ServerSocket(portNumber); 
        } 
    catch (IOException e) 
        { 
         System.err.println("Could not listen on port:"); 
         System.exit(1); 
        } 
   // while(true){
   	//Socket for the client
	Socket clientSocket = null; 
		System.out.println ("Waiting for connection.....");

		try { 
		    //Setting the client socket to the incomming client
		    clientSocket = serverSocket.accept(); 
		} 
		catch (IOException e) 
		    { 
			System.err.println("Accept failed."); 
			System.exit(1); 
		    } 

		System.out.println ("Connection successful");
		System.out.println ("Waiting for input.....");
       
		//Starting two new threads, one for sending information
		//the other for receiving information.
		new receiveThread(serverSocket, clientSocket).start();
		new sendThread(serverSocket, clientSocket).start();
 //   }
    
   } 

	
}

class receiveThread extends Thread{
    ServerSocket serverSocket;
    Socket clientSocket;
	receiveThread(ServerSocket serverSocket, Socket clientSocket){
	    this.serverSocket = serverSocket;
	    this.clientSocket = clientSocket;
	}

	public void run(){
	    try{
		//For sending information to the client
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);

		//For receiving information from the client	
		BufferedReader in = new BufferedReader( 
						       new InputStreamReader( clientSocket.getInputStream())); 

		//Will hold the last sent message from the client
		String inputLine; 
		
		//Receiving lines from the client (readLine() pauses until it gets something)
		while ((inputLine = in.readLine()) != null) 
		    { 
			System.out.println ("Client: " + inputLine); 

			if (inputLine.equals("Quit")){
			    //I had issues with this closing properally with just
			    //the break, so I used System.exit to bypass that
			    //problem
			    System.exit(0);
			    break;
			}
		    } 

		out.close(); 
		in.close(); 
		clientSocket.close();
	    }catch(IOException e){
		System.out.println("ERROR with Receiving");
	    }
	}
}

class sendThread extends Thread{
	ServerSocket serverSocket;
	Socket clientSocket;

	sendThread(ServerSocket serverSocket, Socket clientSocket){
	    this.serverSocket = serverSocket;
	    this.clientSocket = clientSocket;
	}

	public void run(){
		try{
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
			BufferedReader stdIn = new BufferedReader(
								new InputStreamReader(System.in));

			String outputLine;

			System.out.print("Input: ");
			while((outputLine = stdIn.readLine()) != null){
				out.println(outputLine);
				System.out.print("Input: ");
				if(outputLine.equals("Quit")){
					break;
				}
			}

			out.close();
			stdIn.close();
			clientSocket.close();

		}catch(IOException e){
			System.out.println("Error Sending Messages");
		}
	}
}
