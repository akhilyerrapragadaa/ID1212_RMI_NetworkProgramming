package Common;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InfoText extends Remote  {

	 void infoout(String info)throws RemoteException;
}
