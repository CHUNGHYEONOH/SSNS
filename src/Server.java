import java.net.*;
import java.io.*;

public class Server {
	
	public static void main(String[] args) throws IOException{
		
		User[] users = new User[100];
		int usernumber = 0;
		
		while(true) {
			try {
				ServerSocket server = new ServerSocket(5500);
				Socket client = server.accept(); 
				
				DataInputStream inmessage = new DataInputStream(client.getInputStream());
				String message = inmessage.readUTF();
				System.out.println("message = " + message);
				
				ObjectInputStream inuser = new ObjectInputStream(client.getInputStream()); 
				User user = (User) inuser.readObject();
				System.out.println("ID = " + user.getID());
				
				DataOutputStream outmessage = new DataOutputStream(client.getOutputStream());
				
				String returnmessage = "denied";
				
				if(message.equals("login") == true) {
					for(int i=0; i<usernumber; i++) {
						if((user.getID().equals(users[i].getID()) == true) && (user.getPW().equals(users[i].getPW()) == true)) {
							returnmessage = "certified";
							outmessage.writeUTF(returnmessage);
							ObjectOutputStream outuser = new ObjectOutputStream(client.getOutputStream());
							outuser.writeObject(users[i]);
						}
					}	
					if(returnmessage.equals("denied"))
						outmessage.writeUTF(returnmessage);
				}
				else if(message.equals("signup")) {
					users[usernumber] = user;
					usernumber++;
					outmessage.writeUTF("certified");
				}
				
				inuser.close();
				outmessage.close();
				inmessage.close();
				client.close();
				server.close();
	
				} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
				} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
				}
		}	
	}
	
}
