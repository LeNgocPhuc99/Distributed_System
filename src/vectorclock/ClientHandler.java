package vectorclock;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {
	public final static int OFFSET = 9990;
	private VectorClock vc;

	public ClientHandler(VectorClock vc) {
		this.vc = vc;
	}

	@SuppressWarnings("resource")
	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		String choise;
		int destinationPort;

		while (true) {
			System.out.println("MESSAGGIO OR LOCAL ?");
			choise = scanner.next();

			vc.updateVectorClock();
			System.out.println("VC: " + vc.toString());

			if (choise.equalsIgnoreCase("messaggio")) {
				System.out.println("ENTER PORT: ");
				destinationPort = scanner.nextInt();

				sendVCTo(destinationPort);
			}
		}
	}

	private void sendVCTo(int destinationPort) {
		Socket sender = null;

		try {
			sender = new Socket("localhost", destinationPort);
			ObjectOutputStream oos = new ObjectOutputStream(sender.getOutputStream());
			oos.writeObject(vc);
			System.out.println("VC SENT");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (!sender.isClosed()) {
				try {
					sender.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
