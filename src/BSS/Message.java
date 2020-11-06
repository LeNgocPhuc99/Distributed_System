package BSS;

public class Message {
	String message;
	int[] vectorClock;

	Message(String m, int[] vectorclock) {
		this.message = m;
		this.vectorClock = vectorclock;
	}

	public String getMessage() {
		return this.message;
	}

	public int[] getVectorClock() {
		return this.vectorClock;
	}

}
