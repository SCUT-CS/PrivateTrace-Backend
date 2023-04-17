package sg.smu.securecom.keys;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author bwzhao
 * Paillier public key class
 */
public class PaillierKey implements Serializable {

	/** This Serial ID */
	private static final long serialVersionUID = 3999027321575922880L;

	/**The modulus n = p*q */
	protected BigInteger n = null;
	
	/**The mid is half of n, i.e., mid=n/2
	 * and is used to determine the sign for a given biginteger b
	 * if b>mid, b<0, otherweise,
	 *  */
	protected BigInteger mid = null;
	
	/**Public key parameter g, g = n + 1 */
	protected BigInteger g = null;
	
	/** The cached value of nsquare = n * n*/
	protected BigInteger nsquare = null;
	
	/** Cached value of nplusone = n + 1.*/
	protected BigInteger nplusone = null;
	
	/** Cached value of nplustwo = n + 2.*/
	protected BigInteger nplustwo = null;
	
	/** Cached value of nsubone = n - 1.*/
	protected BigInteger nsubone = null;
	
	/** Cached value of nsubtwo = n - 2.*/
	protected BigInteger nsubtwo = null;
	
	/**
	 * @param p an odd prime
	 * @param q another odd prime different from p
	 */
	public PaillierKey(BigInteger p, BigInteger q) {
		this(p.multiply(q));
		
		//TO DO Check to see if p and q are of same length, for security purposes
		if (p.bitLength() == q.bitLength()) {
			throw new IllegalArgumentException("p and q shoubld be the same bit size for security");
		}
		
		if(p.compareTo(q) == 0)
			throw new IllegalArgumentException("p and q must be different primes");
	}

	/**
	 * @param n, a RSA modulus, the product of two different odd primes.
	 */
	public PaillierKey(BigInteger n) {
		this.n = n;

		this.mid = n.divide(BigInteger.ONE.add(BigInteger.ONE));
		this.g = this.n.add(BigInteger.ONE);
		this.nsquare = this.n.multiply(this.n);
		this.nplusone = this.g;
		this.nplustwo = this.nplusone.add(BigInteger.ONE);
		this.nsubone = this.n.subtract(BigInteger.ONE);
		this.nsubtwo = this.nsubone.subtract(BigInteger.ONE);
	}
	
	/**
	 * @return the Paillier public key corresponding to this key
	 */
	public PaillierKey getPublicKey() {
		return new PaillierKey(n);
	}
	
	public BigInteger getN() {
		return n;
	}
	
	public BigInteger getMid() {
		return mid;
	}

	public BigInteger getG() {
		return g;
	}

	public BigInteger getNsquare() {
		return nsquare;
	}

	public BigInteger getNplusone() {
		return nplusone;
	}

	public BigInteger getNplustwo() {
		return nplustwo;
	}

	public BigInteger getNsubone() {
		return nsubone;
	}

	public BigInteger getNsubtwo() {
		return nsubtwo;
	}

}
