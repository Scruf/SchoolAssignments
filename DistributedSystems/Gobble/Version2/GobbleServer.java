import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class GobbleServer{
	public static void main(String []args){
		if(args.length<2){
			System.err.print("Missing arguments <host> <port>\n");
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
			ServerSocket serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(args[0], port));
			while(true){
				Socket socket = serverSocket.accept();
				System.out.print("Something happened");
			}
		}catch (IOException ie){
			ie.printStackTrace();
			System.exit(0);
		}

	}
}