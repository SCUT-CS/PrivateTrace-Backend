package sg.smu.securecom.keys;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author bwzhao
 * Keygeneration
 */
public class KeyGen {

	/**
	 * @param bitLen Specifies the bit size required for the prime factor of n, 
	 *        i.e., bit size of p, q.
	 * @param random, Specifies the randomness handler for the random number generator used.
	 * @return Private key for the generalized Paillier cryptosystem with Threshold Decryption
	 */
	public static PaillierPrivateKey PaillierKey(int bitLen, int cert, Random random) {
		if (bitLen <= 0) 
			throw new IllegalArgumentException("Number of bits set is less than 0");
		
		BigInteger p;
		BigInteger q;
		BigInteger lambda;
		BigInteger n;
		
		boolean ok = false;
		
		do {
			p = new BigInteger(bitLen, cert, random);
			q = new BigInteger(bitLen, cert, random);
			BigInteger min = q.min(p);
			BigInteger max = q.max(p);
			//Make the smallest prime p, maximum q
			p = min;
			q = max;
			// Now verify that  p-1 does not divide q
			if((q.mod(p.subtract(BigInteger.ONE))).compareTo(BigInteger.ZERO)!=0) {
				ok=true;
			}
		} while(!ok);
		
		//n=p*q
		n = p.multiply(q);
		//lambda=(p-1)*(q-1)
		lambda = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

//		System.out.println("p:" + p + ",q:" + q);
		
		return new PaillierPrivateKey(n, lambda);
	}
	
	/**
	 * Generate threshold private key (sk1,n,nsquare) and (sk2,n,nsquare)
	 * sk1+sk2=0 mod lambda and sk1+sk2=1 mod n
	 * sk1+sk2=lambda*(lambda^-1 mod n) mod n
	 * @param lambda, private key of Paillier cryptosystem, lambda = (p-1)*(q-1)
	 * @param n, public key parameter, n = p*q
	 * @param nsquare, nsquare = n*n
	 * @param random, Specifies the randomness handler for the random number generator used.
	 * @return two paillier threshold private key
	 */
	public static PaillierThdPrivateKey[] genThdKey(BigInteger lambda, BigInteger n, BigInteger nsquare, Random random) {
		
		PaillierThdPrivateKey[] ThdKey = new PaillierThdPrivateKey[2];
		
		BigInteger s = lambda.multiply(lambda.modInverse(n)).mod(lambda.multiply(n));
		
		BigInteger sk1 = null;
		do {
			sk1 = new BigInteger(s.bitLength(), random).add(BigInteger.ONE);
		}while(!(sk1.compareTo(s)<0));

		BigInteger sk2 = s.subtract(sk1);
		
		ThdKey[0] = new PaillierThdPrivateKey(sk1, n, nsquare);
		ThdKey[1] = new PaillierThdPrivateKey(sk2, n, nsquare);
		
		return ThdKey;
	}

	public static PaillierThdPrivateKey[] genThdKey(PaillierPrivateKey sk, Random random) {
		BigInteger lambda = sk.getLambda();
		BigInteger n = sk.getN();
		BigInteger nsquare = sk.getNsquare();

		PaillierThdPrivateKey[] ThdKey = new PaillierThdPrivateKey[2];

		BigInteger s = lambda.multiply(lambda.modInverse(n)).mod(lambda.multiply(n));

		BigInteger sk1 = null;
		do {
			sk1 = new BigInteger(s.bitLength(), random).add(BigInteger.ONE);
		}while(!(sk1.compareTo(s)<0));

		BigInteger sk2 = s.subtract(sk1);

		ThdKey[0] = new PaillierThdPrivateKey(sk1, n, nsquare);
		ThdKey[1] = new PaillierThdPrivateKey(sk2, n, nsquare);

		return ThdKey;
	}
}
