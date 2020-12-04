package SES;

import java.util.Arrays;

public class VectorClock {

	public static boolean lessThanEqualTo(int[] tm1, int[] tm2) {
		for (int i = 0; i < tm1.length; i++) {
			if (tm1[i] > tm2[i]) {
				return false;
			}
		}
		return true;
	}

	public static int[] max(int[] tm1, int[] tm2) {
		int[] maxTimeStamp = tm1;
		for (int i = 0; i < tm1.length; i++) {
			maxTimeStamp[i] = (tm1[i] >= tm2[i]) ? tm1[i] : tm2[i];
		}

		return maxTimeStamp;
	}

	public static String toString(int[] vectorClock) {
		return Arrays.toString(vectorClock);

	}
}
