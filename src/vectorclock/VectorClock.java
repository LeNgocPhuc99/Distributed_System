package vectorclock;

import java.io.Serializable;

public class VectorClock implements Serializable {

	/**
	 * @author asura
	 */
	private static final long serialVersionUID = 1L;
	private int processVector[];
	private int processID;

	public VectorClock(int size, int id) {
		this.processVector = new int[size];
		this.processID = id;
	}

	synchronized public int[] getProcessVector() {
		return this.processVector;
	}

	synchronized public String toString() {
		String vector = "";
		for (int e : processVector) {
			// System.out.print(e + "\t");
			vector += e + "\t";
		}
		vector += "\n";
		return vector;
	}

	synchronized public void updateVectorClock(VectorClock vc) {
		int[] tempVector = vc.getProcessVector();

		for (int i = 0; i < tempVector.length; i++) {
			this.processVector[i] = Math.max(this.processVector[i], tempVector[i]);
			if (this.processID == i) {
				this.updateVectorClock();
			}
		}
	}

	synchronized public void updateVectorClock() {
		this.processVector[this.processID]++;
	}

}
