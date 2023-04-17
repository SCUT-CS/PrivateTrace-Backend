package sg.smu.securecom.protocol;

import java.math.BigInteger;

import org.apache.commons.math3.fraction.BigFraction;

public class SecDiv {

	public static BigInteger secDiv(BigInteger ev, BigInteger precision, Paillier pai, 
			PaillierThdDec cp, PaillierThdDec csp) {
		
		//Step-1
		BigInteger ev1 = cp.partyDecrypt(ev);
		
		//Step-2
		BigInteger ev2 = csp.partyDecrypt(ev);
		long den = csp.finalDecrypt(ev1, ev2).longValue();
		return new BigFraction(1.0 / den).multiply(precision).getNumerator();
	}
	
	public static BigInteger secDiv(BigInteger ex, BigInteger ey, BigInteger precision, 
			PaillierThdDec cp, PaillierThdDec csp, Paillier pai) {
		
		//Step-1
		BigInteger ey1 = cp.partyDecrypt(ey);
		
		//Step-2
		BigInteger ey2 = csp.partyDecrypt(ey);
		long den = csp.finalDecrypt(ey1, ey2).longValue();
		BigInteger num = new BigFraction(1.0 / den).multiply(precision).getNumerator();
		
		//Step-3
		return pai.multiply(ex, num);
	}
}
