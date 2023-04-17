package sg.smu.securecom.keys;

import sg.smu.securecom.utils.Utils;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author bwzhao
 * Paillier private key class
 */
public class PaillierPrivateKey extends PaillierKey implements Serializable {

	/** This Serial ID */
	private static final long serialVersionUID = -2923402770206128149L;

	/** p and q are big prime*/
	protected BigInteger p = null;
	protected BigInteger q = null;
	
	/** Private key lambda = φ(p*q) = (p-1)*(q-1). */
	protected BigInteger lambda = null;
	
	/** The inverse of lambda. Used in the final step of decryption.
	 * lmdInvs: the inverse of lambda
	 * */
	protected BigInteger lmdInvs = null;
	
	/**
	 * @param n, a RSA modulus, the product of two different odd primes p, q.
	 * @param lambda, private key, lambda = φ(p*q) = (p-1)*(q-1).
	 */
	public PaillierPrivateKey(BigInteger n, BigInteger lambda) {
		super(n);
		
		if (!Utils.isPrime(lambda, n))
			throw new IllegalArgumentException("lambda must be relatively prime to n");
		this.lambda = lambda;
		this.lmdInvs = this.lambda.modInverse(n);
	}

	/**
	 * @param p an odd prime
	 * @param q another odd prime different from p
	 * @param lambda, private key, lambda = φ(p*q) = (p-1)*(q-1).
	 */
	public PaillierPrivateKey(BigInteger p, BigInteger q, BigInteger lambda) {
		super(p,q);
		
		this.p = p;
		this.q = q;
		
		if (!Utils.isPrime(lambda, p.multiply(q)))
			throw new IllegalArgumentException("lambda must be relatively prime to p*q");
		this.lambda = lambda;
		this.lmdInvs = this.lambda.modInverse(n);
	}

	public BigInteger getLambda() {
		return lambda;
	}

	public BigInteger getLmdInvs() {
		return lmdInvs;
	}

	public BigInteger getP() {
		return p;
	}

	public BigInteger getQ() {
		return q;
	}
}
