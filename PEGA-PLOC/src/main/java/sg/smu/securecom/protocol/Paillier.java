package sg.smu.securecom.protocol;

import sg.smu.securecom.keys.KeyGen;
import sg.smu.securecom.keys.PaillierKey;
import sg.smu.securecom.keys.PaillierPrivateKey;
import sg.smu.securecom.utils.Utils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

public class Paillier implements Serializable {

	/** Public Key allowing encryption. */
	protected PaillierKey pubkey = null;

	/** Private Key allowing decryption; should be same as public key. */
	protected PaillierPrivateKey prikey = null;

	/**
	 * Default constructor. This constructor can be used if there is
	 * no need to generate public/private key pair.
	 */
	public Paillier(){}

	/**
	 * Constructs a new encryption object which uses the specified
	 * key for encryption.
	 *
	 * @param key  Public key used for encryption
	 */
	public Paillier(PaillierKey key) {
		this.pubkey = key;
	}

	/**
	 * Constructs a new encryption/decryption object which uses the specified
	 * key for both encryption and decryption.
	 *
	 * @param prikey  Private key used for decryption and encryption
	 */
	public Paillier(PaillierPrivateKey prikey) {
		this(prikey.getPublicKey());
		setDecryption(prikey);
	}

	/**
	 * Sets the mode for this object to encrypt and will use the provided
	 * key to encrypt messages.
	 *
	 * @param key Public key which this class will use to encrypt
	 */
	public void setEncryption(PaillierKey key) {
		this.pubkey = key;
	}

	/**
	 * Sets the mode for this object to decrypt and will use the provided key
	 * to decrypt only.  (Encryption will continue to be done using the key
	 * provided in {@link #setEncryption(PaillierKey)}.)
	 *
	 * @param key Private key which this class will use to decrypt
	 */
	public void setDecryption(PaillierPrivateKey key) {
		this.prikey = key;
	}

	/**
	 * Sets the mode for this object to decrypt and encrypt using the provided
	 * key.
	 *
	 * @param key   Private key which this class will use to encrypt and decrypt
	 */
	public void setDecryptEncrypt(PaillierPrivateKey key) {
		setDecryption(key);
		setEncryption(key);
	}

	/**
	 * @return      the current public key in use by this encryption object.
	 */
	public PaillierKey getPublicKey() {
		return pubkey.getPublicKey();
	}

	/**
	 * @return      the current private key in use by this encryption object.
	 */
	public PaillierPrivateKey getPrivateKey(){
		return prikey;
	}

	/**
	 * @param m         plaintext to be encrypted; must be less than n
	 * @return cipertext c = g^m*r^n mod n^2
	 */
	public BigInteger encrypt(BigInteger m) {
		BigInteger r = Utils.getCoprimeRandom(pubkey.getN());
		return encrypt(m, r, pubkey);
	}

	/**
	 * Produces the encryption <i>E</i>({@code m}, {@code r}) using the
	 * message {@code m} and the randomization {@code r}
	 * in the Paillier cryptosystem.
	 *
	 * @param m         plaintext to be encrypted; must be less than
	 * 					<code>n</code><sup><i>s</i></sup>
	 * @param r            randomizer integer for the encryption; must be
	 * 					relatively prime to <code>n</code> and less than
	 * 					<code>n</code>
	 * @return the encryption <i>E</i>(<code>m</code>,<code>r</code>)
	 */
	public BigInteger encrypt(BigInteger m, BigInteger r) {
		return encrypt(m, r, pubkey);
	}

	/**
	 * Produces a random encryption of {@code m}
	 *
	 * @param m         Message to be encoded; {@code m<ns}
	 * @param key       Public Key doing the encoding
	 * @return          The encryption <i>E</i>(<code>m</code>, <i>r</i>) using
	 *                  the public key {@code key} with random <i>r</i>
	 */
	public static BigInteger encrypt(BigInteger m, PaillierKey key) {
		return encrypt(m, Utils.getCoprimeRandom(key.getN()), key.getG(), key.getN(), key.getNsquare());
	}

	/**
	 * Produces the encryption <i>E</i>(<code>m, r</code>).
	 *
	 * @param m         Message to be encoded; {@code m<ns}
	 * @param r         Random number in <i>Z</i><sup>*</sup><sub>{@code n}</sub>
	 * @param key       Public Key doing the encoding
	 * @return          The encryption <i>E</i>(<code>m, r</code>) using
	 *                  the public key {@code key}
	 */
	public static BigInteger encrypt(BigInteger m, BigInteger r, PaillierKey key) {
		return encrypt(m, r, key.getG(), key.getN(), key.getNsquare());
	}

	/**
	 * Produces the encryption <i>E</i>(<code>m, r</code>).
	 *
	 * @param m         Message to be encoded; {@code m<ns}
	 * @param r         Random number in <i>Z</i><sup>*</sup><sub>{@code n}</sub>
	 * @param g         a generator of Z_N
	 * @param n         RSA modulus
	 * @param nsquare   n^2
	 * @return          The encryption <i>E</i>(<code>m, r</code>) using
	 *                  the public key {@code n}
	 */
	public static BigInteger encrypt(BigInteger m, BigInteger r, BigInteger g, BigInteger n, BigInteger nsquare) {
		if(!Utils.inModN(m, n)) {
			System.out.println("m="+m+"\nn="+n);
			throw new IllegalArgumentException("m must be less than n");
		}

		if(!(Utils.isPrime(r,n))) {
			throw new IllegalArgumentException("r must be relatively prime to n and 0 <= r < n");
		}

		return (g.modPow(m, nsquare).multiply(r.modPow(n, nsquare)).mod(nsquare));
	}

	/**
	 * Decrypts the given ciphertext.
	 *
	 * @param c     Ciphertext as BigInteger c
	 * @return      Decrypted value D(c) as BigInteger
	 */
	public BigInteger decrypt(BigInteger c) {
		if(!Utils.inModN(c, prikey.getNsquare()))
			throw new IllegalArgumentException("ciphertext must be less than n^2");

		//first we calculate c^\lambda mod n^2
		BigInteger c1 = c.modPow(prikey.getLambda(),prikey.getNsquare());

		//after we calculate c1=c^d mod n^2 = (1+mn*lambda) mod n^2
		// we now find (c1-1)/n=m*lambda mod n
		//therefore m= lambda^-1*(c1-1)/n mod n
		BigInteger m = c1.subtract(BigInteger.ONE).divide(prikey.getN()).multiply(prikey.getLmdInvs()).mod(prikey.getN());
		if(m.compareTo(prikey.getMid()) > 0)
			return m.subtract(prikey.getN());
		return m;
	}

	/**
	 * Calculates E(m1+m2) given E(m1) and E(m2)
	 *
	 * @param c1    the encryption E(m1)
	 * @param c2     the encryption E(m2)
	 * @return the encryption E(m1+m2)
	 */
	public BigInteger add(BigInteger c1, BigInteger c2) {

		if(!(Utils.inModN(c1, pubkey.getNsquare())))
			throw new IllegalArgumentException("c1 must be less than n^2");
		if(!(Utils.inModN(c1, pubkey.getNsquare())))
			throw new IllegalArgumentException("c2 must be less than n^2");
		return (c1.multiply(c2)).mod(pubkey.getNsquare());
	}

	/**
	 * Calculates E(m1-m2) given E(m1) and E(m2)
	 *
	 * @param c1    the encryption E(m1)
	 * @param c2     the encryption E(m2)
	 * @return the encryption E(m1+m2)
	 */
	public BigInteger sub(BigInteger c1, BigInteger c2) {

		return add(c1, multiply(c2, BigInteger.ONE.negate()));
	}

	/**
	 * Calculates E(cons*m) given E(m) and the constant cons, under our current public key.
	 *
	 * @param c        the encryption E(m)
	 * @param cons      the integer multiplicand
	 * @return          the encryption E(cons*m)
	 */
	public BigInteger multiply(BigInteger c, long cons) {
		// In order to multiply, we need to raise the ciphertext cons power mod nsquare
		return multiply(c, BigInteger.valueOf(cons));
	}

	/**
	 * Calculates E(cons*m) given E(m) and the constant cons, under our current public key.
	 *
	 * @param c        the encryption E(m)
	 * @param cons      the integer multiplicand
	 * @return          the encryption E(cons*m)
	 */
	public BigInteger multiply(BigInteger c, BigInteger cons) {
		if(!(Utils.inModN(c, pubkey.getNsquare())))
			throw new IllegalArgumentException("c must be less than n^2");
		return c.modPow(cons, pubkey.getNsquare());
	}

	/**
	 * This main method basically tests the different features of the
	 * Paillier encryption
	 */
	public static void test() {
		Random rd=new Random();
		long num=0;
		long num1=0;
		BigInteger m=null;
		BigInteger c=null;
		int numberOfTests=10;
		int j=0;
		BigInteger decryption=null;
		Paillier esystem= new Paillier();
		PaillierPrivateKey key=KeyGen.PaillierKey(512,122333356, rd);
		esystem.setDecryptEncrypt(key);
		//let's test our algorithm by encrypting and decrypting a few instances
		for(int i=0; i<numberOfTests; i++) {
			num=Math.abs(rd.nextLong());
			m=BigInteger.valueOf(num);
			System.out.println("number chosen  : " +m.toString());
			c=esystem.encrypt(m);
			System.out.println("encrypted value: "+c.toString());
			decryption=esystem.decrypt(c);
			System.out.println("decrypted value: "+decryption.toString());
			if(m.compareTo(decryption)==0) {
				System.out.println("OK");
				j++;
			} else
				System.out.println("PROBLEM");
		}
		System.out.println("out of "+(new Integer(numberOfTests)).toString()
				+"random encryption,# many of "+(new Integer(j)).toString()
				+" has passed");
		// Let us check the commutative properties of the paillier encryption
		System.out.println("Checking the additive properteries of the Paillier encryption" );
		//   Obviously 1+0=1
		System.out.println("1+0="+(esystem.decrypt(
						esystem.add(
								esystem.encrypt(BigInteger.ONE),
								esystem.encrypt(BigInteger.ZERO)
						)
				)).toString()
		);
		// 1+1=2
		System.out.println("1+1="+(esystem.decrypt(
						esystem.add(
								esystem.encrypt(BigInteger.ONE),
								esystem.encrypt(BigInteger.ONE)
						)
				)).toString()
		);

		// 1+1+1=3
		System.out.println("1+1+1="+(esystem.decrypt(
						esystem.add(
								esystem.add(
										esystem.encrypt(BigInteger.ONE),
										esystem.encrypt(BigInteger.ONE)
								),
								esystem.encrypt(BigInteger.ONE)
						)
				)).toString()
		);

		// 0+0=0
		System.out.println("0+0="+(esystem.decrypt(
						esystem.add(
								esystem.encrypt(BigInteger.ZERO),
								esystem.encrypt(BigInteger.ZERO)
						)
				)).toString()
		);
		// 1+-1=0
		System.out.println("1+-1="+(esystem.decrypt(
						esystem.add(
								esystem.encrypt(BigInteger.valueOf(-1).mod(key.getN())),
								esystem.encrypt(BigInteger.ONE)
						)
				)).toString()
		);

//		do {
//			num=rd.nextLong();
//		} while(PaillierKey.inModN(BigInteger.valueOf(num), pubkey.getN()) == false);
//		do {
//			num1=rd.nextLong();
//		} while(key.inModN(BigInteger.valueOf(num1)) == false);
		BigInteger numplusnum1 = BigInteger.valueOf(num).add(BigInteger.valueOf(num1));
		BigInteger summodnsquare = numplusnum1.mod(key.getN());
		//D(E(num)+E(num1))=num+num1
		System.out.println(numplusnum1.toString());
		System.out.println(summodnsquare.toString() + "=\n"
						+esystem.decrypt(
						esystem.add(
								esystem.encrypt(BigInteger.valueOf(num)),
								esystem.encrypt(BigInteger.valueOf(num1))
						)
				).toString()
		);
		// Let us check the multiplicative properties
		System.out.println("Checking the multiplicative properties");
		// D(multiply(E(2),3))=6
		System.out.println("6="+ esystem.decrypt(esystem.multiply(esystem.add(
								esystem.encrypt(BigInteger.ONE),
								esystem.encrypt(BigInteger.ONE)
						),3
				))
		);

	}


	public static void testICDE() {
		// Number of total operations
		int numberOfTests=5;
		//Length of the p, note that n=p.q
		int lengthp=512;

		Paillier esystem= new Paillier();
		Random rd=new Random();
		PaillierPrivateKey key=KeyGen.PaillierKey(lengthp,122333356, rd);
		esystem.setDecryptEncrypt(key);
		//let's test our algorithm by encrypting and decrypting few instances


		long start = System.currentTimeMillis();
		for(int i=0; i<numberOfTests; i++) {
			BigInteger m1=BigInteger.valueOf(Math.abs(rd.nextLong()));
			BigInteger m2=BigInteger.valueOf(Math.abs(rd.nextLong()));
			BigInteger c1=esystem.encrypt(m1);
			BigInteger c2=esystem.encrypt(m2);
			BigInteger c3=esystem.multiply(c1,m2);
			c1=esystem.add(c1,c2);
			c1=esystem.add(c1,c3);

			esystem.decrypt(c1);
		}
		long stop = System.currentTimeMillis();
		System.out.println("Running time per comparison in milliseconds: "
				+ ((stop-start)/numberOfTests));

	}

	public static void main(String[] args) {

		test();
		testICDE();
		PaillierPrivateKey key=KeyGen.PaillierKey(512,122333356, new Random());
		Paillier pal = new Paillier(key);
		BigInteger m1 = new BigInteger("-2");
        BigInteger m2 = new BigInteger("-122");
        BigInteger em1 = pal.encrypt(m1);
        BigInteger em2 = pal.encrypt(m2);

        /* test homomorphic properties -> D(E(m1)*E(m2) mod n^2) = (m1 + m2) mod n */
        BigInteger product_em1em2 = em1.multiply(em2).mod(key.getNsquare());
        BigInteger sum_m1m2 = m1.add(m2).mod(key.getN());
        System.out.println("original sum: " + sum_m1m2);
        System.out.println("decrypted sum: " + pal.decrypt(product_em1em2).toString());

        /* test homomorphic properties -> D(E(m1)^m2 mod n^2) = (m1*m2) mod n */
        BigInteger expo_em1m2 = em1.modPow(m2, key.getNsquare());
        BigInteger prod_m1m2 = m1.multiply(m2).mod(key.getN());
        System.out.println("original product: " + prod_m1m2.subtract(key.getN()));
        System.out.println("decrypted product: " + pal.decrypt(expo_em1m2).toString());
	}
}
