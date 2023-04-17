package sg.smu.securecom.protocol;

import java.math.BigInteger;
import java.util.Random;

import sg.smu.securecom.utils.Utils;

public class SecCmp {

	private static final int sigma = 80;//118;
	
	public static int secCmp(BigInteger ex, BigInteger ey, Paillier pai, PaillierThdDec cp,
			PaillierThdDec csp) {
		
		//Step-1
		int pi = new Random().nextInt(2);
		BigInteger mid = pai.getPublicKey().getMid();
		BigInteger r1 = Utils.getRandom(sigma);
		BigInteger r2 = mid.subtract(Utils.getRandomwithUpper(sigma, r1));
				
		BigInteger D;
		if(pi == 0) {
					
			BigInteger er1addr2 = pai.encrypt(r1.add(r2));
			D = pai.add(pai.add(pai.multiply(ex, r1), pai.multiply(ey, r1.negate())),
					er1addr2);
		} else {
					
			BigInteger er2 = pai.encrypt(r2);
			D = pai.add(pai.add(
					pai.multiply(ey, r1), pai.multiply(ex, r1.negate())), 
					er2);
		}
		BigInteger D1 = cp.partyDecrypt(D);
				
		//Step-2
		BigInteger D2 = csp.partyDecrypt(D);
		BigInteger d = csp.finalDecrypt(D1, D2);
		int u = d.compareTo(mid) > 0 ? 0 : 1;
				
		//Step-3
		return pi ^ u;
	}
}
