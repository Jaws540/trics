package server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	
	private final ServerSocket server;
	private final ConnectionHandler handler;
	
	private boolean shutdown = false;
	
	private final int ACCEPT_TIMEOUT = 5000;
	
	public Server(int port) throws Exception {
		// Create server
		server = new ServerSocket(port);
		server.setSoTimeout(ACCEPT_TIMEOUT);
		
		// Create connection handler pool
		handler = new ConnectionHandler();
		
		// Create connection acceptor
		Thread connectionAcceptor = new Thread(this);
		connectionAcceptor.start();
	}
	
	@Override
	public void run() {
		Socket incomming;
		while(!shutdown) {
			// Attempt to accept a connection
			incomming = null;
			try {
				incomming = server.accept();
			} catch (Exception e) {} // TODO: Probably should catch some of these exceptions...
			
			// Don't act on a failed connection
			if(incomming == null)
				continue;
			
			// Create and pass off the new connection to the handler
			Connection connection = new Connection(incomming, handler);
			handler.addConnection(connection);
		}
	}
	
	public void shutdown() {
		shutdown = true;
	}

}
