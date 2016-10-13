public class GobbleServer{
	public static void main(String []args){
		if(args.length<2){
			System.err.print("Missing arguments <host> <port>\n");
			System.exit(0);
		}
		Integer port;
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

	}
}