import java.math.*;
import edu.rit.pj2.*;
import edu.rit.util.*;
import edu.rit.pj2.vbl.IntVbl.Min;
import edu.rit.pj2.vbl.*;
import java.util.*;
public class Pollar extends Task{
	private class NonPollar extends Loop{
		private final BigInteger n;
		public IntVbl min_number;
		NonPollar(BigInteger n){
			this.n = n;
		}
		public void compute(){
			//will probalby changed in the future
			int iter = this.n.divide(new BigInteger("2")).intValue();
			// System.out.print(iter);
			parallelFor(2,iter).exec(new Loop(){
				
				public void start(){
					
				};
				public void run(int i){

				
						BigInteger mod_pow = n.mod(new BigInteger(Integer.toString(i)));
						if (mod_pow.compareTo(new BigInteger("0"))==0){
							System.out.print(i);
						} 
						
					
				}
			// 	public void finish(){
					
			// 	}
			});
			// System.out.println(this.min_number.item);
		}
		@Override
		public void run(int i){

		}
	}
	public void main (String []args) throws Exception{
		BigInteger n = new BigInteger("618240007109027021");
		NonPollar test = new NonPollar(n);
		test.compute();
		// NonPollar test  = new NonPollar(n);
		// test.compute();
		//2a)Answer 2147483647
		//BigInteger n1 = new BigInteger(Integer.toString(test.min_number.item)).subtract(new BigInteger("1"));
		// BigInteger n1 = new BigInteger("2147483646");
		// NonPollar test2 = new NonPollar(n1);
		// test2.compute();
		//2b)2

	}
}