package lamportclockSimple;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class LamportClock extends Thread {
	private MulticastSocket sock;
	private InetAddress groupAddr;
	private int port;
	private int time;
	private int order;

	public LamportClock(InetAddress groupAddr, int port) throws Exception {
		this.port = port;
		this.groupAddr = groupAddr;
		// local time
		this.time = 0;
		this.order = -1;

		this.sock = new MulticastSocket(port);
		sock.setTimeToLive(2);
		sock.joinGroup(groupAddr);

	}

	public LamportClock(InetAddress groupAddr, int port, int order) throws Exception {
		this(groupAddr, port);
		this.order = order;
	}

	public int getOrder() {
		return this.order;
	}

	public int getTime() {
		return this.time;
	}

	public int localEvent() {
		++this.time;
		return this.time;
	}

	public int receiverEvet(long senderID, int receiveTime) {
		return this.time;
	}

	public int sendEvent(String mgs) throws Exception {
		byte[] data = mgs.getBytes();
		DatagramPacket d = new DatagramPacket(data, data.length, groupAddr, port);
		sock.send(d);

		return this.time;
	}

	public void updateEvent(Event e) throws Exception {
		int type = e.type;

		switch (type) {
		// local event
		case 0:
			localEvent();
			break;
		// sent event
		case 1:
			String msg = "";
			long sendrID = e.sendserID;
			long receiverID = e.receiverID;

			e.localTime = ++this.time;
			String content = e.message;

			msg = Long.toString(sendrID) + "-" + Long.toString(receiverID) + "-" + Integer.toString(e.localTime)
					+ content;
			sendEvent(msg);
			break;
		case 2:
			// receive event
			this.time = Math.max(this.time, e.localTime) + 1;
			break;
		default:
			break;
		}

		printTime(e);
	}

	public void printTime(Event e) {
		String logging = "-------------------------------------\n";
		logging += "Process " + this.getId() + "\n";
		logging += "Process local time " + this.getTime() + "\n";
		logging += "\tEvent type: ";

		switch (e.type) {
		case 0:
			logging += "LOCAL EVENT\n";
			break;
		case 1:
			logging += "SEND EVENT\n";
			break;
		case 2:
			logging += "RECEIVE EVENT\n";
			break;
		default:
			break;
		}

		logging += "\tEvent sender's ID: " + e.sendserID + "\n";
		logging += "\tEvent receiver's ID: " + e.receiverID + "\n";
		logging += "\tEvent local time: " + e.localTime + "\n";
		logging += "\tEvent message: " + e.message + "\n";
		logging += "-------------------------------------\n";
		System.out.print(logging);

	}

	public void run() {
		String greeting = "";
		greeting = "Uniqueue ID " + this.getId() + "is initialized with clock " + this.time + "\n";
		if (this.order != -1)
			greeting = "Process " + this.order + " " + greeting;

		System.out.print(greeting);
		while (true) {
			try {

				while (true) {
					DatagramPacket d = new DatagramPacket(new byte[256], 256);
					sock.receive(d);
					String s = new String(d.getData());

					String[] meta = s.trim().split("-");
					long senderID = Long.parseLong(meta[0]);
					long receiverID = Long.parseLong(meta[1]);
					int localTime = Integer.parseInt(meta[2]);
					String message = "";
					if (meta.length > 4)
						message = meta[3];

					if (this.getId() == receiverID) {
						Event e = new Event(2, senderID, receiverID, localTime, message);
						updateEvent(e);
					}
				}

			} catch (Exception e) {
				System.err.println("LC Failed: " + e + "\n");
				return;
			}
		}
	}

}
