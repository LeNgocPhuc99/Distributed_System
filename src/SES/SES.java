package SES;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SES extends UnicastRemoteObject implements SESInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Message> messBuffer;
	private List<V_P> VPVector;
	private int[] timeStamp;
	private String logFile;
	public int pid;

	public SES(int pid, int numProcesses) throws RemoteException {
		super();
		this.messBuffer = new ArrayList<Message>();
		this.VPVector = new ArrayList<V_P>();
		this.timeStamp = new int[numProcesses];
		// this.logFile = "log" + this.pid + ".txt";
		this.pid = pid;

		// Bind remote object (stub)

		try {
			Registry registry = LocateRegistry.getRegistry();

			registry.rebind("SES-" + this.pid, this);
			println("Process " + this.pid + " ready");
			// writeLogFile("Process " + this.pid + " ready");
		} catch (Exception e) {
			System.err.println("Server exception");
			e.printStackTrace();
		}

	}

	// receive mess
	public synchronized void receive(Message m) {

		V_P owerVP = new V_P(this.pid, this.timeStamp);
		if (VPVertor.deliveryCondition(m.getvmVector(), owerVP)) {
			deliver(m);
			checkBuffer();
		} else {
			println("Buffering mess: " + m.toString());
			// writeLogFile("Buffering mess: " + m.toString());
			this.messBuffer.add(m);
		}
	}

	// send mess

	public synchronized void send(int destinationID, String message)
			throws MalformedURLException, RemoteException, NotBoundException {

		SESInterface dest = (SESInterface) Naming.lookup("SES-" + destinationID);

		timeStamp[pid]++;
		List<V_P> copy = new ArrayList<V_P>();
		for (V_P element : this.VPVector) {
			copy.add(element.copy());
		}

		Message messageObject = new Message(message, copy, Arrays.copyOf(timeStamp, timeStamp.length));

		println("Sending - " + messageObject.toString());
		// writeLogFile("Sending - " + messageObject.toString());
		int wait = (int) (Math.random() * 10000);

		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				try {
					dest.receive(messageObject);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}, wait);

		V_P destVP = new V_P(destinationID, this.timeStamp);
		VPVertor.addVP(this.VPVector, destVP);

	}

	// check buffer and delivery mess
	private synchronized void checkBuffer() {
		for (int i = 0; i < this.messBuffer.size(); i++) {
			Message m = this.messBuffer.get(i);
			V_P owerVP = new V_P(this.pid, this.timeStamp);
			if (VPVertor.deliveryCondition(m.getvmVector(), owerVP)) {
				deliver(m);
				this.messBuffer.remove(i);
				checkBuffer();
				break;
			}
		}
	}

	private synchronized void deliver(Message m) {

		this.VPVector = VPVertor.updateVPVector(this.VPVector, m.getvmVector());
		println("Delivery - " + m.toString());
		// writeLogFile("Delivery - " + m.toString());

		// update local time
		this.timeStamp = VectorClock.max(this.timeStamp, m.getTimeStamp());
		this.timeStamp[this.pid]++;

	}

	private void println(String message) {
		String pidStr = "(" + this.pid + ") ";
		System.err.println(pidStr + message);
	}

	@SuppressWarnings("unused")
	private void writeLogFile(String message) {
		try {

			File file = new File(this.logFile);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getName(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(message);
			bw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
