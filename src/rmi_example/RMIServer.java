package rmi_example;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RMIServer {

	public static void main(String args[]) {

		try {
			// remote method
			IAccount rAccount = new AccountService();

			LocateRegistry.createRegistry(6789);

			Naming.bind("rmi://127.0.0.1:6789/RMI", rAccount);

			System.out.println(">>>>INFO: RMI server start");

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
