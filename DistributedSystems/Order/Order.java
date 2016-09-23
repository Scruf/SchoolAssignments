

import java.math.*;
import edu.rit.pj2.*;


public class Order extends Task{

	public void main(String []args)throws Exception{
		if(args.length<2){
			System.err.print("Invalid number of arguments\n");
			System.exit(0);
		}else{
			BigInteger p;
			BigInteger g;
			try{
				Integer a = Integer.parseInt(args[0]);
				Integer b = Integer.parseInt(args[1]);
			}catch(Exception ex){
				System.err.print("Arguments must be of type int\n");
				System.exit(0);
			}
			p = new BigInteger(args[0]);
			g = new BigInteger(args[1]);
			if (p.compareTo(new BigInteger("2"))==-1){
				System.err.print("P must be >=2\n");
				System.exit(0);
			}
			if(!p.isProbablePrime(64)){
				System.err.printf("%s is not prime number\n",p.toString());
				System.exit(0);
			}
			if(g.compareTo(new BigInteger("1"))==-1){
				System.err.print("G must be >= 1\n");
				System.exit(0);
			}
			//fix it
			if (g.compareTo(p.subtract(new BigInteger("1")))!=-1){
				System.err.print("G must be in range 1<=g<=p-1\n");
				System.exit(0);
			}
			
		}
	}

	
}