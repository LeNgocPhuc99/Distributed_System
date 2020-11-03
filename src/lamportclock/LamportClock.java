package lamportclock;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.PriorityQueue;

public class LamportClock extends Thread {

	private MulticastSocket sock;
	private InetAddress groupAddr;
	private int port;
	// make a FIFO channel
	private PriorityQueue<Request> clockPQ;

	// local time
	private int time;
	// order of process
	private int order;

	public LamportClock(InetAddress groupAddr, int port) throws Exception {
		this.groupAddr = groupAddr;
		this.port = port;

		this.order = -1;

		// set local time
		this.time = 0;

		this.clockPQ = new PriorityQueue<>();

		sock = new MulticastSocket(port);
		sock.setTimeToLive(2);
		// join a multicast group
		sock.joinGroup(groupAddr);

	}

	public LamportClock(InetAddress groupAddr, int port, int order) throws Exception {
		this(groupAddr, port);
		this.order = order;
	}

	public int getOrder() {
		return this.order;
	}

	int getTime() {
		return this.time;
	}

	public int localEvent() {
		++this.time;
		return this.time;
	}

	public int receivedEvent(long senderID, int receivedTime) {
		return this.time;
	}

	public int sendEvent(String msg) throws Exception {
		byte[] data = msg.getBytes();

		// make packet
		DatagramPacket d = new DatagramPacket(data, data.length, groupAddr, port);
		sock.send(d);

		return this.time;
	}

	public void updateTime(Event e) throws Exception {
		int type = e.type;

		switch (type) {
		// local event
		case 0:
			this.localEvent();
			break;
		// send event
		case 1:
			long senderID = e.sendserID;
			long receiverID = e.receiverID;

			e.localTime = ++this.time;
			String content = e.message;

			String msg = Long.toString(senderID) + "-" + Long.toString(receiverID) + "-" + e.localTime + "-" + content;

			sendEvent(msg);
			break;
		// receive event
		case 2:
			// update logical time
			this.time = Math.max(e.localTime, this.time) + 1;
			break;
		// request event
		case 3:
			e.localTime = ++this.time;
			clockPQ.add(new Request(this.time, this.getId()));
			String requestMessage = "REQUEST-" + this.time + "-" + this.getId();
			sendEvent(requestMessage);
			break;
		// reply request event
		case 4:
			// update local clock
			// <=> internal event
			++this.time;
			clockPQ.add(new Request(e.localTime, e.sendserID));
			break;
		// reply event
		case 5:
			e.localTime = ++this.time;
			senderID = e.sendserID;
			break;
		// ack event
		case 6:
			++this.time;
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
		case 3:
			logging += "REQUEST EVENT\n";
			break;
		case 4:
			logging += "RECEIVE REQUEST EVENT\n";
			break;
		case 5:
			logging += "REPLY EVENT\n";
			break;
		case 6:
			logging += "ACK EVENT\n";
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
		try {
			while (true) {
				DatagramPacket d = new DatagramPacket(new byte[256], 256);
				sock.receive(d);
				String s = new String(d.getData());

				String[] meta = s.trim().split("-");
				if (meta[0].equals("REQUEST")) {
					int requestTime = Integer.parseInt(meta[1]);
					long senderID = Long.parseLong(meta[2]);
					if (this.getId() != senderID) {
						// create reply for request event
						Event e = new Event(4, senderID, this.getId(), requestTime, "");
						updateTime(e);
					}
				} else {
					long senderID = Long.parseLong(meta[0]);
					long receiverID = Long.parseLong(meta[1]);
					int localTime = Integer.parseInt(meta[2]);
					String content = "";
					if (meta.length > 4)
						content = meta[4];

					if (this.getId() == receiverID) {
						// local time: time of event
						Event e = new Event(2, senderID, receiverID, localTime, content);
						updateTime(e);
					}
				}
			}

		} catch (Exception e) {
			System.err.println("LC Failed: " + e + "\n");
			return;
		}
	}

}
