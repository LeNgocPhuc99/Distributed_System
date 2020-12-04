package lamportclockSimple;

public class Event {

	public int type;
	public long sendserID;
	public long receiverID;
	public int localTime;
	public String message;

	public Event(int type, long senderID, long receiverID, String message) {
		this.type = type;
		this.sendserID = senderID;
		this.receiverID = receiverID;
		this.message = message;
		this.localTime = 0;
	}

	public Event(int type, long senderID, long receiverID, int localTime, String message) {
		this(type, senderID, receiverID, message);
		this.localTime = localTime;
	}

}
