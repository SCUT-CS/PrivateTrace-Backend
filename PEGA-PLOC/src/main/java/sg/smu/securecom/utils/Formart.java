package sg.smu.securecom.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Formart{

	public static BigInteger bigDcm2BigInt(BigDecimal bd, int f) {
		
		return bd.movePointRight(f).toBigInteger();
	}
	
//	public static BigInteger bigIntdot10f
	
	public static void main(String[] args) {
		
		BigDecimal bd = new BigDecimal("1.3333333333333333333333333333333333333333");
		System.out.println(bd.movePointRight(30));
		BigDecimal bd1 = new BigDecimal(1000);
		BigDecimal res = bd.divide(bd1, 2, RoundingMode.DOWN);
		System.out.println(res);
		System.out.println(bigDcm2BigInt(bd, 30));
	}
}
