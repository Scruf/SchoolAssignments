import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class Gobble {
	public static void main(String args[]){
		if(args.length<3){
			System.err.print("Missing arguments <host> <port> <username>\n");
			System.exit(0);
		}
		Integer port=0;
		try{
			port = Integer.parseInt(args[1]);
		}catch(Exception ex){
			System.err.print("Port must be of type int\n");
			System.exit(0);
		}
		if(args[0].length()<3){
			System.err.print("Invlaid <host>\n");
			System.exit(0);
		}
		try{
			Socket  socket = new Socket();
			socket.connect(new InetSocketAddress(args[0], port));
			GobbleUI gobble = GobbleUI.create(args[2]);
		}catch(IOException ie){
			ie.printStackTrace();
			System.exit(0);
		}

	}
}