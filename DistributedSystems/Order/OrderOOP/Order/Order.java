//******************************************************************************
//
// File:    Order.java
// This Java source file is copyright (C) 2013 by Egor Kozitski. All rights
// reserved. For further information, contact the author, Egor Kozitski, at
// ark@cs.rit.edu.
//
// This Java source file is part of the Parallel Java 2 Library ("PJ2"). PJ2 is
// free software; you can redistribute it and/or modify it under the terms of
// the GNU General Public License as published by the Free Software Foundation;
// either version 3 of the License, or (at your option) any later version.
//
// PJ2 is distributed in the hope that it will be useful, but WITHOUT ANY
// WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
// A PARTICULAR PURPOSE. See the GNU General Public License for more details.
//
// A copy of the GNU General Public License is provided in the file gpl.txt. You
// may also obtain a copy of the GNU General Public License on the World Wide
// Web at http://www.gnu.org/licenses/gpl.html.
//
//******************************************************************************

import java.math.*;
import edu.rit.pj2.*;
import edu.rit.util.*;
import edu.rit.pj2.vbl.IntVbl.Min;
import edu.rit.pj2.vbl.*;
import java.util.*;

/**
 * Class Order  will compute the modular power operation
 * <P>
 * Usage: <TT>java pj2 Order <I>p</I> <I>g</I></TT>
 * <BR><TT><I>p</I></TT> = the modulus
 * <BR><TT><I>g</I></TT> = is the generator
 * <P>
 * @author  Egor Kozitski
 * @version October 6, 2016
*/


/*
*Main program
*/
//
public class Order extends Task{
	
	public IntVbl max_number;
	public void main(String []args) throws Exception{
		//if the number of arguments is <2 than		
		if(args.length<2){
			//disaplya and erro and exit 
			System.err.print("Invalid number of arguments\n");
			System.exit(0);
		}else{
			//Check whether command line arguments are of type Int
			try{
				Integer a = Integer.parseInt(args[0]);
				Integer b = Integer.parseInt(args[1]);
				//if one of them is not INT than throw an error
			}catch(Exception ex){
				System.err.print("Arguments must be of type int\n");
				System.exit(0);
			}
			//@param p will is the modulus
			BigInteger p1 = new BigInteger(args[0]);
			//@param g is generator
			BigInteger g1 = new BigInteger(args[1]);
			//if p is less than 2
			if (p1.compareTo(new BigInteger("2"))==-1){
				//display error message and exit
				System.err.print("P must be >=2\n");
				System.exit(0);
			}
			//if p is not prime than throw an error
			if(!p1.isProbablePrime(64)){
				System.err.printf("%s is not prime number\n",p1.toString());
				System.exit(0);
			}
			//if g is less than one than throw an error
			if(g1.compareTo(new BigInteger("1"))==-1){
				//Display and error message
				System.err.print("G must be >= 1\n");
				//exit
				System.exit(0);
			}
			//if p is less than g 
			if(p1.compareTo(g1)==-1){
				//display error message
				System.err.print("p must be less or equals\n");
				//exit on error
				System.exit(0);
			}
			//initialize ferma class to calculate the successive powers
			final BigInteger p = p1;
			final BigInteger g = g1;
				int n = p.intValue()-1;
			//initialize max_number with a max value 
			max_number = new IntVbl.Min(Integer.MAX_VALUE);
			//generate n sequential numbers
			parallelFor(1,n).exec(new Loop(){
				//@param local_max is the number which will hold the minimum
				IntVbl local_max;
				//@method start will initialize local variable
				public void start(){
					local_max = threadLocal(max_number);
				}
				//@method run will callculate and the mod and find whether the exponent is equals to 1
				public void run(int i){
					//if the result is one than 
					if(g.modPow(new BigInteger(Integer.toString(i)),p).compareTo(new BigInteger("1"))==0){
						//local_max will reduce the given shared variable into this shared variable
						local_max.reduce(new IntVbl(i));
					}
				}
			});
			//print out the result
			System.out.print(this.max_number.item+"\n");
		}
	}
}