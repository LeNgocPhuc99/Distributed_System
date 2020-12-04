package SES;

import java.io.Serializable;
import java.util.Arrays;

public class V_P implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int pid;
	int[] timeStamp;

	public V_P(int pip, int[] timeStamp) {
		this.pid = pip;
		this.timeStamp = timeStamp;
	}

	public int[] getTimeStamp() {
		return this.timeStamp;
	}

	public void setTimeStamp(int[] timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getPid() {
		return this.pid;
	}

	public void setPid(int pip) {
		this.pid = pip;
	}

	public V_P copy() {
		V_P newS = new V_P(this.pid, Arrays.copyOf(this.timeStamp, this.timeStamp.length));

		return newS;
	}

	public String toString() {

		String s = "(" + this.pid + ", " + Arrays.toString(this.timeStamp) + ")";
		return s;
	}
}
