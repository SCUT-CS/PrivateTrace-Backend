package sg.smu.securecom.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.fraction.Fraction;

public class TestFraction {

	public static void main(String[] args) {
		
		BigFraction frac = new BigFraction(0.3333333333333333);
		System.out.println(frac);
		BigInteger cont = BigInteger.valueOf(2).pow(80);
		frac = frac.multiply(cont);
		System.out.println(frac.getDenominator());
		
		frac = frac.divide(cont);
		System.out.println(frac);
		
		BigInteger cost = BigInteger.valueOf(33000);
		System.out.println(cost.longValue());
		System.out.println(cost.longValueExact());
		frac = new BigFraction(1.0 / cost.longValue());
		System.out.println(frac.getDenominator().getClass());
		System.out.println(frac.multiply(cont));
	}
}

