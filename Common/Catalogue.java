package Common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;


public interface Catalogue extends Remote {
	 public static final String CATALOGUE = "catalogue";
	 void registerUser(String username, String password) throws RemoteException, NullPointerException, SQLException;
	 String loginUser(String username, String password,InfoText infoText) throws SQLException, RemoteException;
	 void uploadFiles(String name, String FileName) throws FileNotFoundException, SQLException, IOException;
	 String accessFiles() throws RemoteException, NullPointerException, SQLException;
	 String accessMyFiles(String name) throws RemoteException, NullPointerException, SQLException;
	 void downloadFiles(String FileName)throws RemoteException, NullPointerException, SQLException, IOException;
	 byte[] getFile(String filename) throws RemoteException, NullPointerException, SQLException, FileNotFoundException, IOException;
	 byte[] writeFile(String filename, String info) throws RemoteException, NullPointerException, SQLException, FileNotFoundException, IOException;
     void notifyFile(String name, String filename, InfoText infoText, String type)throws RemoteException, NullPointerException, SQLException;
     void notifyAll(String name)throws RemoteException, NullPointerException, SQLException;
     void logout(String username)throws RemoteException, NullPointerException, SQLException;
}
