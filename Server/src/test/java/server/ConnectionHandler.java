package server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionHandler {

	private final List<Connection> connections;
	private final ExecutorService pool;
	
	protected ConnectionHandler() {
		connections = new ArrayList<Connection>();
		pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}
	
	/**
	 * Used by a connection to run a protocol task
	 * @param task
	 */
	protected void newTask(Runnable task) {
		pool.execute(task);
	}
	
	protected void addConnection(Connection c) {
		synchronized(connections) {
			connections.add(c);
		}
	}
	
	/**
	 * Should only be called from a connection instance to remove itself once
	 * the connection should be terminated
	 * @param connection
	 */
	protected void removeConnection(Connection connection) {
		synchronized(connections) {
			connections.remove(connection);
		}
	}

}
