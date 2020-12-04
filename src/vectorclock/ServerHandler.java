package vectorclock;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerHandler implements Runnable {

	private VectorClock vc;
	private Socket client;
	private ObjectInputStream ois;

	public ServerHandler(VectorClock vc, Socket clinet) {
		this.vc = vc;
		this.client = clinet;
		try {
			ois = new ObjectInputStream(this.client.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		VectorClock receivingClock;
		try {
			receivingClock = (VectorClock) ois.readObject();
			vc.updateVectorClock(receivingClock);
			String hostname = this.client.getInetAddress().getHostName();
			int port = this.client.getPort();
			System.out.println("Receive from " + hostname + ", " + port);
			System.out.print(vc.toString());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (!this.client.isClosed()) {
				try {
					this.client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
