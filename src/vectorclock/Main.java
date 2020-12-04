package vectorclock;

public class Main {
	public final static int OFFSET = 9990;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length < 1) {
			System.err.println("ERROR IN ARGS");
			System.exit(1);
		}

		int myPort = Integer.parseInt(args[0]);
		int myID = myPort % OFFSET;

		Node currentNode = new Node(myPort, myID);
		currentNode.start();
	}

}
