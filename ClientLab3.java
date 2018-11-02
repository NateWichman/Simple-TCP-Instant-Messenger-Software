import java.io.*;
import java.net.Socket;
import java.util.Scanner;
/***********************************************************************************************
Runs the client side of the simple instant messenging software. Allows for continous converation
between the server and the client through threads. This class only uses one thread instead of 
two because using two is kind of redundant in the other one. The server uses two because I want
to modify it later to handle multiple clients at once.

@Author Nathan Wichman
@Version Fall 2018
***********************************************************************************************/
public class ClientLab3{

    public static void main(String args[]) throws IOException{
	String serverHostname;
	int portNumber;
	Scanner scn = new Scanner(System.in);

	try{
	    System.out.print("IP address: ");
	    serverHostname = scn.next();
	    System.out.print("\nPort number: ");
	    portNumber = Integer.parseInt(scn.next());
	    System.out.print("\n");

	    System.out.println ("Attemping to connect to host " +
				serverHostname + " on port " + portNumber);

	    Socket socket = null;
	    PrintWriter out = null;
	    BufferedReader in = null;
	    //Setting up the Servers socket
	    socket = new Socket(serverHostname, portNumber);
	    //For sending informtaiont
	    out = new PrintWriter(socket.getOutputStream(), true);
	    //For receiving information
	    in = new BufferedReader(new InputStreamReader(
							  socket.getInputStream()));
	    //For typing information
	    BufferedReader stdIn = new BufferedReader(
						      new InputStreamReader(System.in));
	    //This string holds the last typed message.
	    String userInput;
		
            //Starts a new thread to receive information.
	    new receiveThread2(socket).start();

	     //This block is essentailly the other thread, sends information
	    System.out.println("Input: ");
	    while ((userInput = stdIn.readLine()) != null) {
		//Sending information out the print stream
		out.println(userInput);
		System.out.print ("Input: ");
		if(userInput.equals("Exit")){
		    break;
		}
		} 

	    out.close();
	    in.close();
	    stdIn.close();
	}catch(IOException e){
	    System.out.println("ERROR");
	}
    }    
}

class receiveThread2 extends Thread{
	Socket socket;

	receiveThread2(Socket socket){
		this.socket = socket;
	}

	public void run(){
		try{
		BufferedReader in = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));

		String inputLine;

		while((inputLine = in.readLine()) != null){
			System.out.println("Server: " + inputLine);

			if(inputLine.equals("Exit")){
				System.exit(0);
				break;
			}
		}

		in.close();
		socket.close();
		}catch(IOException e){
			System.out.println("Error Receiving Message");
		}

	}
}


