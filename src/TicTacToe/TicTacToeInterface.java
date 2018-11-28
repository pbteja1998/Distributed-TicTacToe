package TicTacToe;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TicTacToeInterface extends Remote {
	public class Game {
		
	}
	void printMsg() throws RemoteException;
	public ClientInterface getClient(String username) throws RemoteException;
	public boolean isUsernamePresent(String username) throws RemoteException;
	public void join(ClientInterface c) throws RemoteException;
	public void startGame(String username) throws RemoteException;
	public char[] getBoard(String username) throws RemoteException;
	public boolean isValidMove(String username, int move) throws RemoteException;
	public void makeMove(String username, int move) throws RemoteException;
	public void giveUp(String loser) throws RemoteException;
	public void quit(String username) throws RemoteException;
}
