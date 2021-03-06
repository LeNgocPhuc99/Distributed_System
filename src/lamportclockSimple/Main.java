package lamportclockSimple;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.print("Usage: java Main (number of processes)\n");
			return;
		}

		String input;
		String out = "exit";

		try {
			int n = Integer.parseInt(args[0]);
			LamportClock[] clocks = new LamportClock[n];
			System.setProperty("java.net.preferIPv4Stack", "true");
			InetAddress group = InetAddress.getByName("224.255.255.255");

			for (int i = 0; i < n; i++) {
				int port = 8888;
				LamportClock cl = new LamportClock(group, port, i);
				cl.start();
				clocks[i] = cl;
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				input = in.readLine();
				if (input.equals(out)) {
					break;
				}

				String[] splits = input.split(" ");
				if (splits.length == 0) {
					continue;
				}

				switch (splits[0].toUpperCase()) {
				case "SEND":
					// send 1 2: send a mess from 1 to 2
					int clockArrayID = Integer.parseInt(splits[1]);
					long firstProcessID = clocks[clockArrayID].getId();
					long secondProcessID = clocks[Integer.parseInt(splits[2])].getId();
					String messageContent = "";

					// make mess
					if (splits.length >= 3) {
						List<String> wordsList = Arrays.asList(Arrays.copyOfRange(splits, 3, splits.length));
						messageContent = String.join(" ", wordsList);
					}

					Event e = new Event(1, firstProcessID, secondProcessID, messageContent);
					clocks[clockArrayID].updateEvent(e);
					break;
				case "LOCAL":
					clockArrayID = Integer.parseInt(splits[1]);
					firstProcessID = clocks[clockArrayID].getId();
					e = new Event(0, firstProcessID, -1, "");
					clocks[clockArrayID].updateEvent(e);
					break;
				default:
					throw new RuntimeException("Invalid event name");

				}

			}

		} catch (Exception e) {
			System.err.print(e);
			return;
		}

	}

}
