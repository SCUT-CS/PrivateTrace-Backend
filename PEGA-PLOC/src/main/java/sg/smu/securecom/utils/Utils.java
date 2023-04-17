package sg.smu.securecom.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

public class Utils {
    private static Random random = null;

    public static void setRandom(Random random){
        Utils.random = random;
    }

    public static Random getRandom(){
        if(random == null)
            return new Random();
        return Utils.random;
    }
    /**
     * compute log_10(x)
     * @param x     the number to compute
     * @return      return log_10(x)
    */
    public static BigInteger log(BigInteger x){
        BigDecimal xc = new BigDecimal(x);
        BigDecimal rc = Logarithms.log10(xc);
        return rc.toBigInteger();
    }

    /**
     * @param n the modular
     * @return random number with the same bit size as n and coprime with n
     */
    public static BigInteger getCoprimeRandom(BigInteger n) {
        BigInteger r;
        do {
            r = new BigInteger(n.bitLength(), getRandom()).add(BigInteger.ONE);
        } while (r.compareTo(n)>=0 || !r.gcd(n).equals(BigInteger.ONE));
        return r;
    }

    /**
     * Checks if a given number is in Z_N.
     *
     * @param a         the BigInteger to be checked
     * @param n         the BigInteger modulus
     * @return          'true' iff a is less than n
     */
    public static boolean inModN(BigInteger a, BigInteger n) {
        return (a.compareTo(n) < 0);
    }

    /**
     * Checks if a given number a is relatively prime to n.
     * @param a         the BigInteger we are checking
     * @return          'true' iff a is non-negative, less than n, and relatively prime to n
     */
    public static boolean isPrime(BigInteger a, BigInteger n) {
        return (a.gcd(n).equals(BigInteger.ONE) && inModN(a, n));
        // Note that if a is zero, then the gcd(a,n) = gcd(0,n) = n, which is not one.
    }

    /**
     * @param n the modular of RSA n = p * q
     * @return random number with the same bit size as n
     */
    public static BigInteger getRandom(BigInteger n) {
        BigInteger r;
        do {
            r = new BigInteger(n.bitLength(), getRandom()).add(BigInteger.ONE);
        } while (r.compareTo(n)>=0 || !isPrime(r, n));
        return r;
    }
    
    /**
     * @param len the binary length of a random number r
     * @param n the modular of RSA n = p * q
     * @return random number r with length len bits and \gcd(r, n)=1
     */
    public static BigInteger getRandom(int len, BigInteger n) {
        BigInteger r;
        do {
            r = new BigInteger(len, getRandom()).add(BigInteger.ONE);
        } while (r.compareTo(n)>=0 || !isPrime(r, n));
        return r;
    }
    
    public static BigInteger getRandom(int len) {
    	
    	BigInteger r;
    	do {
    		r = new BigInteger(len, getRandom());
    	}while(r.compareTo(BigInteger.ZERO) == 0);
    	
    	return r;
    }
    
    public static BigInteger getRandomwithUpper(int len, BigInteger up) {
    	
    	BigInteger r;
    	do {
    		r = new BigInteger(len, getRandom());
    	}while(r.compareTo(BigInteger.ZERO) == 0 || r.compareTo(up) >= 0);
    	
    	return r;
    }
    
    public static BigInteger getRndCpsOd(int len) {
    	
    	BigInteger r;
    	do {
    		r = new BigInteger(len, getRandom());
    	}while(r.bitLength()<len || 
    			r.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO));
    	
    	return r;
    }

    /**
     * @param n the modular of RSA n = p * q
     * @return random number with the same bit size as n
     */
    public static BigInteger getRandomSquare(BigInteger n) {
        BigInteger r;
        do {
            r = new BigInteger(n.bitLength(), getRandom()).add(BigInteger.ONE);
        } while (r.pow(2).compareTo(n)>=0);
        return r;
    }
    public static BigInteger getRandomWithinInterval(int r){
/*        BigInteger res;
        do {
            res = new BigInteger(r, rand);
        }while (res.bitLength() != r);
        return res;*/
        BigInteger base = BigInteger.ONE.shiftLeft(r - 1);
        BigInteger res = new BigInteger(r - 1, getRandom()).add(base);
        return res;
    }

    public static BigInteger not(BigInteger x){
        String xs = x.abs().toString(2);
        String tmp = "";
        int t;
        for (int i = 0; i < xs.length(); i++) {
            tmp += 1 - xs.charAt(i) + '0';
        }
        BigInteger res = new BigInteger(tmp, 2);
        if(x.signum() < 0)
            res = res.negate();
        return res;
    }
}
