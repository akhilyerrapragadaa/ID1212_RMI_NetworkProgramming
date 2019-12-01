package Server;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.*; 
import java.util.*;
import javax.sql.rowset.serial.SerialBlob;
import Common.Catalogue;
import Common.InfoText;


public class ServerDBC implements Catalogue {
	static  Connection conn = null; 
    static  Statement stmt = null;  
    static HashMap<String,InfoText> userInfo =  new HashMap<String,InfoText>();
    
    // User obj;
	public void JDBCRun() throws ClassNotFoundException, SQLException {
	// JDBC driver name and database URL 
    // String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
     String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";   
     String DB_URL = "jdbc:mysql://localhost:3306/test";  
     
     // Database credentials 
     String USER = "root"; 
     String PASS = "Akgreato1510@";  
     
  //   Connection conn = null; 
  //   Statement stmt = null;  
     
     //Register JDBC driver 
     Class.forName(JDBC_DRIVER);   
     
     //Open a connection
     System.out.println("Connecting to a selected database..."); 
     conn = DriverManager.getConnection(DB_URL, USER, PASS); 
     System.out.println("Connected database successfully...");  
     
     //Execute a query 
     System.out.println("Creating statement..."); 
     stmt = conn.createStatement();  
	}
	 
	public void registerUser(String username, String password) throws SQLException, RemoteException {
		boolean flag = false;
	      String sql = "SELECT * FROM tasks"; 
	      ResultSet rs = stmt.executeQuery(sql);  
	      while(rs.next() && !flag) { 
	    	  String user = rs.getString("username");
	    	  if(user.equals(username)) {
	    		  flag = true;
	    		  throw new RemoteException("Username already exist! Please choose another name");
	    	  }	   
	      }
		
		if(!flag) {
		String query = " insert into tasks (username, password)"+ " values (?, ?)";
		PreparedStatement preparedStmt = conn.prepareStatement(query);
		 preparedStmt.setString (1, username);
	      preparedStmt.setString (2, password);
	      preparedStmt.execute();   
	      } 
	    
	}
	
	public String loginUser(String username, String password, InfoText infoText) throws SQLException, RemoteException {
		String name="";
		int i =0;
		
	      String sql = "SELECT * FROM tasks"; 
	      ResultSet rs = stmt.executeQuery(sql);  
	      while(rs.next()) { 
	    	  String user = rs.getString("username");
	    	  String pass = rs.getString("password");
	    	  if(user.equals(username)&&pass.equals(password)) {	  
	    		  userInfo.put(username,infoText);
	    		  name = username;
	    		  i =1;
	    		  break;
	    	  }
	    	  
	      } 
	      if(i==0) {
	    	  name = "";
    		  throw new RemoteException("Invalid Credentials!");
	      }
		return name;
	}
	
	
	
	public String accessFiles() throws SQLException {
		List<String> AllF = new ArrayList<String>();
		String sqll = "SELECT * FROM files";
	      PreparedStatement psf = conn.prepareStatement(sqll); 
	      ResultSet rsf = psf.executeQuery();
 	  while(rsf.next()) {
 		 String MFN = rsf.getString("filename");
 		  AllF.add(MFN);
 	  }
 	  return AllF.toString();
	
	}
	
	public String accessMyFiles(String name) throws SQLException {
		 List<String> MyF = new ArrayList<String>();
		String sqll = "SELECT * FROM files where username=?";
 	      PreparedStatement psf = conn.prepareStatement(sqll);
 	      psf.setString(1, name); 
 	      ResultSet rsf = psf.executeQuery();
   	  while(rsf.next()) {
   		 String MFN = rsf.getString("filename");
   		  MyF.add(MFN);
   	  }
   	  return MyF.toString();
	}
	
	
	public void uploadFiles(String name, String FileName) throws SQLException, IOException {
		boolean exist = false;
		String root = "C:\\Users\\Akhil\\eclipse-workspace\\JavaRMIDB\\src\\Resources";
		Path workingDir = Paths.get(root);
	    Path path = workingDir.resolve(Paths.get(FileName));
	    byte[] data = Files.readAllBytes(path);
	    Blob blob = new SerialBlob(data);
	 //   String sql = "SELECT * FROM tasks";
	  //  PreparedStatement ps = conn.prepareStatement(sql);
	   // ps.setString(1, name);
	  //  ResultSet rs = ps.executeQuery();
	     
	    //  while(rs.next()) { 
	   /* 	String query = "UPDATE tasks set file_data = ?";
	  	    PreparedStatement preparedStmt = conn.prepareStatement(query);
	  	    preparedStmt.setBlob(1, blob);
    			 preparedStmt.executeUpdate(); */	
	    	  String sqll = "SELECT * FROM files";
	  	      PreparedStatement psf = conn.prepareStatement(sqll);
	  	    //  psf.setString(1, name); 
	  	      ResultSet rsf = psf.executeQuery();
	    	  while(rsf.next()) {
	    		  String FN = rsf.getString("filename");
	    		  if(FileName.equals(FN)) {
	    			  exist = true;
	    		   }
	    	  }
	    //  }
	    	if(!exist) {  
    	    String query = "insert into files (file_data,username,filename)"+ " values (?,?,?)";
    	    PreparedStatement preparedStmt = conn.prepareStatement(query);
	  	    preparedStmt.setBlob(1, blob);
	  	    preparedStmt.setString(2, name);
	  	    preparedStmt.setString(3, FileName);
	  	    preparedStmt.execute();
	  	    }
	  	    else {
   		             throw new RemoteException("Cannot upload this file!");
   	          }
	    	
	      	 
		}

	public void downloadFiles(String filename) throws NullPointerException, SQLException, IOException {
		// TODO Auto-generated method stub
		int b = 0;
		String sqll = "SELECT * FROM files where filename=?";
	      PreparedStatement psf = conn.prepareStatement(sqll); 
	      psf.setString(1, filename); 
	      ResultSet rsf = psf.executeQuery();
	      System.out.println(rsf);
 	  if(rsf.next()) {
 		 Blob dat = rsf.getBlob("file_data");
 		String fn = rsf.getString("filename");
        InputStream is = dat.getBinaryStream();
        FileOutputStream fos = new FileOutputStream("C:\\Users\\Akhil\\eclipse-workspace\\JavaRMIDB\\src\\Resources"+ "\\" + fn);
           while ((b = is.read()) != -1)
           {
               fos.write(b); 
           }		
 	  }
	}

	
	public byte[] getFile( String filename)
			throws RemoteException, NullPointerException, SQLException, FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		int b=0;
		 ByteBuffer bb = ByteBuffer.allocate(1024);
		String sqll = "SELECT * FROM files where filename=?";
	      PreparedStatement psf = conn.prepareStatement(sqll); 
	      psf.setString(1, filename); 
	      ResultSet rsf = psf.executeQuery();
	  if(rsf.next()) {
		 Blob dat = rsf.getBlob("file_data");
		String fn = rsf.getString("filename");
		 InputStream is = dat.getBinaryStream();	
		 while ((b = is.read()) != -1)
         { 
			    bb.putInt(b); 
			    
         }		
	  }
	  return bb.array();
	}

	
	public byte[] writeFile(String fileName, String info)
			throws RemoteException, NullPointerException, SQLException, FileNotFoundException, IOException {
		int b=0;
		 ByteBuffer bb = ByteBuffer.allocate(1024);
	    	  byte[] byteData = info.getBytes("UTF-8");
	    	  Blob givenInfo = new SerialBlob(byteData);
	    	  String query = "UPDATE files set file_data = ? where filename=?"; 
	    	  PreparedStatement preparedStmt = conn.prepareStatement(query);
		  	   preparedStmt.setBlob(1, givenInfo);
		  	   preparedStmt.setString(2, fileName);
	    	   preparedStmt.executeUpdate();
	    	   InputStream is = givenInfo.getBinaryStream();	
	  		   while ((b = is.read()) != -1)
	           { 
	  			    bb.putInt(b); 
	  			    
	           }		  
	      return bb.array();
	}
    
	public void notifyFile(String name, String filename, InfoText infotext, String type) throws SQLException, RemoteException {
		String sqll = "SELECT * FROM files where filename=?";
	      PreparedStatement psf = conn.prepareStatement(sqll); 
	      psf.setString(1, filename); 
	      ResultSet rsf = psf.executeQuery();
	  if(rsf.next()) {
		  String fusername = rsf.getString("username");
		  if(!name.equals(fusername)) {
		  InfoText infoText =  userInfo.get(fusername);
		  if(userInfo.containsKey(fusername)) {
		  if(type.equals("READ")) {
		  infoText.infoout("Your file '"+filename+"' was READ by "+name);
		  }
		  else if(type.equals("UPDATE")) {
			  infoText.infoout("Your file '"+filename+"' was UPDATED by "+name); 
		  }
		  else {
			  infoText.infoout("Your file '"+filename+"' was DOWNLOADED by "+name);
		  }
		  }
		  }
	  }
	}
	
	
	public void logout(String username) throws RemoteException, NullPointerException, SQLException {
		// TODO Auto-generated method stub
		userInfo.remove(username);
	}

	public void notifyAll(String name) throws RemoteException, NullPointerException, SQLException {
		// TODO Auto-generated method stub
		  String sql = "SELECT * FROM tasks"; 
	      ResultSet rs = stmt.executeQuery(sql);  
	      while(rs.next()) {   
	    	  String fusername = rs.getString("username");
	    	  if(!name.equals(fusername)) {
	    	  InfoText infoText =  userInfo.get(fusername);
			  infoText.infoout("NEW  file was uploaded by "+name+" checkout!");
	    	  }
	      }
		
	}
	
}
	
	
	
	

