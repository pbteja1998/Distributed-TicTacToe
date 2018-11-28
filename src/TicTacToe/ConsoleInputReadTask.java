package TicTacToe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

public class ConsoleInputReadTask implements Callable<String> {

	public ConsoleInputReadTask() {
		// TODO Auto-generated constructor stub
	}
	
	public String call() throws Exception {
		BufferedReader br = new BufferedReader(
	    new InputStreamReader(System.in));
	    String input;
	    do {
	      try {
	        // wait until we have data to complete a readLine()
	        while (!br.ready()) {
	          Thread.sleep(200);
	        }
	        input = br.readLine();
	      } catch (InterruptedException e) {
	        return null;
	      }
	    } while ("".equals(input));
	    return input;
	}

}
