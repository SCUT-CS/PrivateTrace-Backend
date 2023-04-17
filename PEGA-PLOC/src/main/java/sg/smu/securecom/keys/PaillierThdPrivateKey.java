package sg.smu.securecom.keys;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author bwzhao
 * Private key class of Paillier Cryptosystem with Threshold Decryption
 * PaillierThdPrivateKey: Paillier Threshold Private Key
 */
public class PaillierThdPrivateKey implements Serializable {

	/**
	 * The first parameter of threshold private key
	 * sk1+sk2=0 mod lambda and sk1+sk2=1 mod n
	 */
	protected BigInteger sk;
	
	/**
	 * The public key parameter n of Paillier cryptosystem
	 */
	protected BigInteger n;
	
	/**
	 * nsquare=n*n
	 */
	protected BigInteger nsquare;
	
	public PaillierThdPrivateKey(BigInteger sk, BigInteger n, BigInteger nsquare) {
		this.sk = sk;
		this.n = n;
		this.nsquare = nsquare;
	}

	public BigInteger getSk() {
		return sk;
	}

	public BigInteger getN() {
		return n;
	}

	public BigInteger getNsquare() {
		return nsquare;
	}
}
