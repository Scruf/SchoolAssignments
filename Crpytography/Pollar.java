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
			int iter = this.n.intValue()/4;
			this.min_number = new IntVbl.Min(Integer.MAX_VALUE);
			parallelFor(2,iter).exec(new Loop(){
				IntVbl local_min;
				public void start(){
					local_min = threadLocal(min_number);
				};
				public void run(int i){
					if (new BigInteger(Integer.toString(i)).isProbablePrime(64)) 
						if(n.mod(new BigInteger(Integer.toString(i))).compareTo(new BigInteger("0"))==0) 
						{
							local_min.reduce(new IntVbl(i));
						}
				}
			});
			System.out.println(this.min_number.item);
		}
		@Override
		public void run(int i){

		}
	}
	public void main (String []args) throws Exception{
		BigInteger n = new BigInteger("618240007109027021");
		NonPollar test  = new NonPollar(n);
		// test.compute();
		//2a)Answer 2147483647
		BigInteger n1 = new BigInteger(Integer.toString(test.min_number.item)).subtract(new BigInteger("1"));
		NonPollar test2 = new NonPollar(n1);
		test2.compute();
		//2b)

	}
}