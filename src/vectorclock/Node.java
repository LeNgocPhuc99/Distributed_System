package vectorclock;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Node {
	int myPort;
	int myID;
	VectorClock vc;

	public Node(int port, int ID) {
		this.myPort = port;
		this.myID = ID;
		this.vc = new VectorClock(5, ID);
	}

	private void startClientComponet() {
		(new Thread(new ClientHandler(vc))).start();
	}

	@SuppressWarnings("resource")
	private void startServerComponent() {
		try {
			ServerSocket server = new ServerSocket(myPort);
			System.out.println("Server running in port: " + myPort);
			Executor executor = Executors.newFixedThreadPool(100);

			Socket client = null;
			ServerHandler serverWorker;

			while (true) {
				client = server.accept();
				serverWorker = new ServerHandler(vc, client);
				executor.execute(serverWorker);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		startClientComponet();
		startServerComponent();
	}
}
