package TicTacToe;

import java.rmi.RemoteException;
import java.util.Hashtable;

public class TicTacToeImpl implements TicTacToeInterface {

	public class Game {
		String player0;
		String player1;
		char[] board;
		
		public Game(String p0, String p1) {
			String boardString = "..........";
			this.board = boardString.toCharArray();
			if(Math.random() < 0.5) {
				this.player0 = p0;
				this.player1 = p1;
			} else {
				this.player0 = p1;
				this.player1 = p0;
			}
		}
		
		public String getOpponent(String username) {
			return this.player0.equals(username) ? this.player1 : this.player0;
		}
		
		public String getPlayer0() {
			return this.player0;
		}
		
		public String getPlayer(int player) {
			return player == 0 ? this.player0 : this.player1; 
		}
		
		public String getPlayer1() {
			return this.player1;
		}
		
		public boolean isValidMove(int move) {
			return move >= 1 && move <= 9 && board[move] == '.'; 			
		}
		
		public void makeMove(String player, int move) {
			if(this.player0.equals(player)) {
				this.board[move] = 'x';
			} else {
				this.board[move] = 'o';
			}
		}
		
		public int checkWin() {
			if(
				(this.board[1] == 'x' && this.board[2] == 'x' && this.board[3] == 'x') ||
				(this.board[4] == 'x' && this.board[5] == 'x' && this.board[6] == 'x') ||
				(this.board[7] == 'x' && this.board[8] == 'x' && this.board[9] == 'x') ||
				(this.board[1] == 'x' && this.board[4] == 'x' && this.board[7] == 'x') ||
				(this.board[2] == 'x' && this.board[5] == 'x' && this.board[8] == 'x') ||
				(this.board[3] == 'x' && this.board[6] == 'x' && this.board[9] == 'x') ||
				(this.board[1] == 'x' && this.board[5] == 'x' && this.board[9] == 'x') ||
				(this.board[3] == 'x' && this.board[5] == 'x' && this.board[7] == 'x')
			  ) {
				return 0;
			}
			if(
				(this.board[1] == 'o' && this.board[2] == 'o' && this.board[3] == 'o') ||
				(this.board[4] == 'o' && this.board[5] == 'o' && this.board[6] == 'o') ||
				(this.board[7] == 'o' && this.board[8] == 'o' && this.board[9] == 'o') ||
				(this.board[1] == 'o' && this.board[4] == 'o' && this.board[7] == 'o') ||
				(this.board[2] == 'o' && this.board[5] == 'o' && this.board[8] == 'o') ||
				(this.board[3] == 'o' && this.board[6] == 'o' && this.board[9] == 'o') ||
				(this.board[1] == 'o' && this.board[5] == 'o' && this.board[9] == 'o') ||
				(this.board[3] == 'o' && this.board[5] == 'o' && this.board[7] == 'o')
			  ) {
				return 1;
			}
			if(
				this.board[1] != '.' && this.board[2] != '.' && this.board[3] != '.' &&
				this.board[4] != '.' && this.board[5] != '.' && this.board[6] != '.' &&
				this.board[7] != '.' && this.board[8] != '.' && this.board[9] != '.'
			  ) {
				return -1;
			}
			return -2;
		}
	}
	
	private Hashtable<String, ClientInterface> clients = new Hashtable<String, ClientInterface>();
	private Hashtable<String, Game> gamesMap = new Hashtable<String, Game>();
	private String waitingClient = null; 
	
	public void printMsg() throws RemoteException {
		System.out.println("This is an example RMI application");
	}

	public ClientInterface getClient(String username) throws RemoteException {
		ClientInterface client = (ClientInterface) clients.get(username);
		return client;
	}
	
	public boolean isUsernamePresent(String username) throws RemoteException {
		return clients.get(username) != null;
	}
	
	public void join(ClientInterface client) throws RemoteException {
		clients.put(client.getUsername(), client);
		client.sendMsg("[Server] Welcome " + client.getUsername());
		client.promptForGame();
	}
	
	public void startGame(String username) throws RemoteException {
		if(waitingClient == null) {
			waitingClient = username;
			ClientInterface client = this.getClient(username);
			client.sendMsg("[Server] Waiting for other player to join");
		} else {
			Game G = new Game(this.waitingClient, username);			
			gamesMap.put(waitingClient, G);
			gamesMap.put(username, G);
			this.waitingClient = null;
			ClientInterface player0 = this.getClient(G.getPlayer0());
			ClientInterface player1 = this.getClient(G.getPlayer1());
			player0.sendMsg("\n[Server] You are player 'x'. You get first turn");
			player1.printBoard();
			player1.sendMsg("\n[Server] You are player 'o'. You get second turn");
			player0.makeMove();
		}
	}
	
	public Game getGame(String username) {
		return gamesMap.get(username);
	}
	
	public char[] getBoard(String username) throws RemoteException {
		return this.getGame(username).board;		
	}
	
	public boolean isValidMove(String username, int move) throws RemoteException {
		return this.getGame(username).isValidMove(move);
	}
	
	public void makeMove(String username, int move) throws RemoteException {
		Game G = this.getGame(username);
		G.makeMove(username, move);
		
		if(G.checkWin() == -2) {
			ClientInterface currentPlayer = this.getClient(username);
			currentPlayer.waitForTurn();			
			ClientInterface opponent = this.getClient(G.getOpponent(username));
			opponent.makeMove();
			return;
		}
		
		int playerWon = G.checkWin();
		String u0 = G.player0;
		String u1 = G.player1;
		if(playerWon == -1) {			
			ClientInterface p0 = this.getClient(G.player0);
			ClientInterface p1 = this.getClient(G.player1);
			p0.sendMsg("\n[Server] Draw Game!");
			p1.sendMsg("\n[Server] Draw Game!");
			gamesMap.remove(u0);
			gamesMap.remove(u1);
			p0.promptForAnotherGame();
			p1.promptForAnotherGame();
		} else {
			ClientInterface winner = this.getClient(G.getPlayer(playerWon));
			ClientInterface loser = this.getClient(G.getOpponent(G.getPlayer(playerWon)));
			winner.sendMsg("\n[Server] You won the game!");
			loser.sendMsg("\n[Server] You lost the game!");
			gamesMap.remove(u0);
			gamesMap.remove(u1);
			winner.promptForAnotherGame();
			loser.promptForAnotherGame();
		}		
	}
	
	public void giveUp(String loser) throws RemoteException {
		Game G = this.getGame(loser);
		String winner = G.getOpponent(loser);
		ClientInterface clientWon = this.getClient(winner);
		ClientInterface clientLost = this.getClient(loser);
		clientWon.sendMsg("\n[Server] Your opponent gave up! You won the game!");
		clientLost.sendMsg("\n[Server] You did not make any valid move in 10 seconds! You lost the game");
		gamesMap.remove(clientWon);
		gamesMap.remove(clientLost);
		clientLost.sendMsg("\n[Server] Wait for prompt to start another game!");
		clientWon.promptForAnotherGame();
		clientLost.promptForAnotherGame();
	}
	
	public void quit(String username) throws RemoteException {
		ClientInterface client = this.getClient(username);
		clients.remove(username);
		client.sendMsg("\n[Server] You are removed from the server");
		client.quit();
	}
}
