package TicTacToe;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientImpl implements ClientInterface {

	private String username;
	private TicTacToeInterface server;
	private ClientInterface stub;
	
	public ClientImpl(String username, TicTacToeInterface server) throws RemoteException{
		setUsername(username);
		setServer(server);
	}

	public void setStub(ClientInterface c) {
		this.stub = c;
	}
	
	public void sendMsg(String s) throws RemoteException {
		System.out.println(s);
	}

	public String getUsername() throws RemoteException {
		return username;
	}

	private void setUsername(String username) throws RemoteException {
		this.username = username;
	}

	public TicTacToeInterface getServer() throws RemoteException {
		return server;
	}

	private void setServer(TicTacToeInterface server) throws RemoteException {
		this.server = server;
	}

	public void promptForGame() throws RemoteException {
		System.out.print("Do you want to start the game? (1/0): ");
		int startGame = 0;
		try {
			Scanner scan = new Scanner(System.in);
			startGame = scan.nextInt();
		} catch (Exception e) {
			System.out.println("Invalid Response!");
			this.server.quit(this.username);
			return;
		}
		if(startGame == 1) {
			this.server.startGame(this.username);
		} else {
			this.server.quit(this.username);
		}
//		scan.close();
	}
	
	public void printBoard() throws RemoteException {
		char[] board = this.server.getBoard(this.username);		
		for(int i = 1; i <= 9; i++) {
			char move = board[i];
			if(move == '.' ) {
				move = (char) (i + '0'); 
			}
			if(i % 3 > 0) {
				System.out.print(" " + move + " |");				
			} else if(i % 3 == 0) {
				System.out.print(" " + move + " \n");
			}
		}
	}
	
	public void makeMove() throws RemoteException {
		this.printBoard();
		System.out.println("Make a valid move in 10 seconds. Otherwise you will lose the game");
		System.out.print("Your move: ");
		
		ConsoleInput con = new ConsoleInput(
		    1,
	        10,
	        TimeUnit.SECONDS
	    );
		String input ;
		try {
			input = con.readLine();			
		} catch(InterruptedException e) {
			this.server.giveUp(this.username);
			return;
		}
		
		if(input == null) {
			this.server.giveUp(this.username);
			return;
		}
		
		int move = 0;
		try {
			move = Integer.parseInt(input);
		} catch (Exception e) {
			
		}
		if(!this.server.isValidMove(this.username, move)) {
			this.server.giveUp(this.username);
			return;
		}
		this.server.makeMove(this.username, move);
//		scan.close();
	}
	
	public void promptForAnotherGame() throws RemoteException {
		System.out.println("Please provide valid response in 10 seconds. Otherwise you will be removed from the server!");
		System.out.print("Do you want to start another game? (1/0): ");
		
		ConsoleInput con = new ConsoleInput(
			    1,
		        10,
		        TimeUnit.SECONDS
		    );
		String input ;
		try {
			input = con.readLine();			
		} catch(InterruptedException e) {
			this.server.quit(this.username);
			return;
		}
		
		if(input == null) {
			this.server.quit(this.username);
			return;
		}
		
		int startGame = Integer.parseInt(input);
		if(startGame == 1) {
			this.server.startGame(this.username);
		} else {
			System.out.println("[System] Exited");
		}
//		scan.close();
	}
	
	public void waitForTurn() throws RemoteException {
		this.printBoard();
		System.out.println("\nWait for your turn!");
	}
	
	public void quit() throws RemoteException {
//		try {
//			boolean quitSuccessfully = UnicastRemoteObject.unexportObject(this.stub, true);
//			if(quitSuccessfully) {
//				System.out.println("Successfully exited the server!");
//			}
//		} catch (NoSuchObjectException e) {
//			System.exit(0);
//		}
	}
}
