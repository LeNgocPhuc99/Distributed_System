package SES;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String msg;
	private List<V_P> vmVector;
	private int[] timeStamp;

	public Message(String msg, List<V_P> vmVector, int[] timeStamp) {
		this.msg = msg;
		this.vmVector = vmVector;
		this.timeStamp = timeStamp;
	}

	public String getMessage() {
		return this.msg;
	}

	public void setMessage(String msg) {
		this.msg = msg;
	}

	public int[] getTimeStamp() {
		return this.timeStamp;
	}

	public void setTimeStamp(int[] timeStamp) {
		this.timeStamp = timeStamp;
	}

	public List<V_P> getvmVector() {
		return this.vmVector;
	}

	public void setvmVector(List<V_P> vmVector) {
		this.vmVector = vmVector;
	}

	public String toString() {

		String s = "Message: " + this.msg + "\nVPVector: " + VPVertor.toString(this.vmVector) + "\nTimeStamp: "
				+ VectorClock.toString(timeStamp);
		return s;
	}

}
