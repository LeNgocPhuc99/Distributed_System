package lamportclock;

public class Request implements Comparable<Request> {

	public int time;
	public long processID;

	public Request(int time, long processID) {
		this.time = time;
		this.processID = processID;
	}

	public int getTime() {
		return this.time;
	}

	public long getProcessID() {
		return this.processID;
	}

	@Override
	public int compareTo(Request other) {
		if (this.getTime() == other.getTime())
			return 0;
		else if (this.getTime() > other.getTime())
			return 1;
		else
			return -1;
	}
}
