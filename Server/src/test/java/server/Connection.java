package server;

import java.net.Socket;

public class Connection {
	
	private final Socket socket;
	
	public Connection(Socket socket, ConnectionHandler handler) {
		this.socket = socket;
	}
	
	protected void read() {
		
	}

}
