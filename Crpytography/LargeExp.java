import java.math.*;
public class LargeExp{
	public static void main(String []args){
		BigInteger result;
		BigInteger mod = new BigInteger("101");
		BigInteger exponent = new BigInteger("179");
		result = new BigInteger("3").modPow(exponent,mod);
		System.out.print(result);
	}
}
