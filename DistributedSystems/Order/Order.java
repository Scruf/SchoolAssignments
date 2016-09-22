

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
			if (p.compareTo(new BigInteger("2"))==0)
				System.out.print("It worked");
		}
	}
	 private static void usage()
      {
      System.err.println ("Usage: java pj2 edu.rit.pj2.example.PrimeSeq <number> ...");
      terminate (1);
      }

   /**
    * Specify that this task requires one core.
    */
   protected static int coresRequired()
      {
      return 1;
      }

}