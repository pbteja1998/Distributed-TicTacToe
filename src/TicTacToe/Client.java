package TicTacToe;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {		
		try {
			Registry registry = LocateRegistry.getRegistry();
			TicTacToeInterface server = (TicTacToeInterface) registry.lookup("TicTacToe");
			Scanner scan = new Scanner(System.in);
			System.out.print("Pick a username: ");
			String username = scan.next();			
			while(server.isUsernamePresent(username)) {
				System.out.println("Username already exists.");
				System.out.print("Pick another username: ");
				username = scan.next();
			}
//			scan.close();
			ClientImpl clientObj = new ClientImpl(username, server);
			ClientInterface stub = (ClientInterface) UnicastRemoteObject.exportObject(clientObj, 0);
			server.join(stub);
			clientObj.setStub(stub);
		} catch(Exception e) {
			System.err.println("Client Exception: " + e.toString());
			e.printStackTrace();
		}		
	}

}
