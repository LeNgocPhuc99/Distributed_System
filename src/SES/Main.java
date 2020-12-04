package SES;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

	public static void main(String[] args) {
		int numProcesses = 15;
		Thread[] myThreads = new Thread[numProcesses];
		try {
			// Create Registry
			Registry registry = LocateRegistry.createRegistry(1099);

			int[][] destIDs = { { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 }, {}, {}, {}, {}, {}, {}, {}, {}, {},
					{}, {}, {}, {}, {} };
			String[][] messages = { { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14" }, {},
					{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {} };

			/*
			 * int[][] destIDs = { { 1, 2 }, { 2 }, {} }; String[][] messages = { { "1", "2"
			 * }, { "3" }, {} };
			 */
			for (int i = 0; i < numProcesses; i++) {
				SES process = new SES(i, numProcesses);
				Process p = new Process(process, destIDs[i], messages[i]);
				myThreads[i] = new Thread(p);
			}
			for (int i = 0; i < numProcesses; i++) {
				myThreads[i].start();
			}

		} catch (Exception e) {
			System.err.println("Could not create registry exception");
			e.printStackTrace();
		}

	}

}
