package SES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VPVertor {

	public static void addVP(List<V_P> VPVector, V_P newMess) {

		boolean flag = false;

		for (V_P vp : VPVector) {
			if (vp.getPid() == newMess.getPid()) {
				vp.setTimeStamp(newMess.getTimeStamp());
				flag = true;
				break;
			}
		}

		if (!flag) {
			VPVector.add(newMess);
		}
	}

	public static boolean deliveryCondition(List<V_P> VMVector, V_P currentProcess) {

		for (V_P vp : VMVector) {
			if (vp.getPid() == currentProcess.getPid()) {
				return VectorClock.lessThanEqualTo(vp.getTimeStamp(), currentProcess.getTimeStamp());
			}
		}

		return true;
	}

	public static List<V_P> updateVPVector(List<V_P> owerVP, List<V_P> incomingVP) {

		if (incomingVP.isEmpty()) {
			return owerVP;
		}

		List<V_P> newVPVector = new ArrayList<V_P>();
		for (int i = 0; i < incomingVP.size(); i++) {
			boolean check = false;
			V_P incoming = incomingVP.get(i);
			for (int j = 0; j < owerVP.size(); j++) {
				V_P ower = owerVP.get(j);
				if (incoming.getPid() == ower.getPid()) {
					check = true;
					int[] maxTimeStamp = VectorClock.max(incoming.getTimeStamp(), ower.getTimeStamp());
					V_P temp = new V_P(ower.getPid(), maxTimeStamp);
					newVPVector.add(temp);
				}
			}

			if (!check) {
				newVPVector.add(incomingVP.get(i));
			}
		}

		return newVPVector;
	}

	public static String toString(List<V_P> VPVector) {
		return Arrays.toString(VPVector.toArray());
	}
}
