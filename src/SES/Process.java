package SES;

public class Process implements Runnable {

	int[] destIDs;
	String[] messages;
	SES process;

	public Process(SES process, int[] destIDs, String[] messages) {
		this.destIDs = destIDs;
		this.process = process;
		this.messages = messages;
		// this.messages = makeMessage();

	}

	/*
	 * private String[] makeMessage() { String[] messages = null;
	 * 
	 * return messages; }
	 */

	@Override
	public void run() {
		for (int i = 0; i < destIDs.length; i++) {
			try {
				Thread.sleep(1000);
				process.send(this.destIDs[i], this.messages[i]);
			} catch (Exception e) {
				System.err.println("Client exception");
				e.printStackTrace();
			}
		}
	}

}
