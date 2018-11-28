package TicTacToe;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends TicTacToeImpl {

	public Server() {
		
	}

	public static void main(String[] args) {
		try {
			TicTacToeImpl obj = new TicTacToeImpl();
			TicTacToeInterface stub = (TicTacToeInterface) UnicastRemoteObject.exportObject(obj, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("TicTacToe", stub);
			System.err.println("Server Ready");
		} catch(Exception e) {
			System.err.println("Server Exception: " + e.toString());
			e.printStackTrace();
		}
	}

}
