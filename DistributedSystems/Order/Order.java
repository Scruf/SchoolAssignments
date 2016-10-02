

import java.math.*;
import edu.rit.pj2.*;
import edu.rit.util.*;
import edu.rit.pj2.vbl.*;
import java.util.*;

public class Order extends Task{


	public void main(String []args)throws Exception{
		 	BigInteger p = new BigInteger("0");
			BigInteger g = new BigInteger("0");
		if(args.length<2){
			System.err.print("Invalid number of arguments\n");
			System.exit(0);
		}else{
			
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
			if(p.compareTo(g)==-1){
				System.err.print("p must be less or equals\n");
				System.exit(0);
			}
		}
		IntVbl count = new IntVbl(1);
		int n =  p.intValue()-1;
		List<BigInteger> successor = new ArrayList<BigInteger>();
		parallelFor(0,n-1).exec(new Loop(){
			BigInteger mod;
			IntList list;
			public void start(){
				  list = new IntList();
				
			}
			public void run(int i ){

				mod = new BigInteger("5").modPow(new BigInteger(Integer.toString(i)),new BigInteger("23"));
				list.add(0,mod.intValue());

			}
			public void finish(){
				System.out.print(list.size());
				System.out.print("\n");
				System.out.print(list.toString());
				System.out.print("\n");

			}
		});
		
		
	}
	

	
}
