package TicTacToe;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
	public String getUsername() throws RemoteException;	
	public void sendMsg(String s) throws RemoteException;
	public void promptForGame() throws RemoteException;
	public void makeMove() throws RemoteException;
	public void promptForAnotherGame() throws RemoteException;
	public void waitForTurn() throws RemoteException;
	public void quit() throws RemoteException;
	public void printBoard() throws RemoteException;
}
