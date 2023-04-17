package sg.smu.securecom.protocol;

import sg.smu.securecom.utils.Utils;

import java.math.BigInteger;

public class SecMul {

    private static final int SIGMA = 80;//118;

    public static BigInteger secMul(BigInteger ex, BigInteger ey, Paillier pai, PaillierThdDec cp, PaillierThdDec csp) {
        // Step-1 CP
        BigInteger r1 = Utils.getRandom(SIGMA);
        BigInteger r2 = Utils.getRandom(SIGMA);
        BigInteger x = pai.add(ex, pai.encrypt(r1));
        BigInteger y = pai.add(ey, pai.encrypt(r2));
        BigInteger x1 = cp.partyDecrypt(x);
        BigInteger y1 = cp.partyDecrypt(y);

        // Step-2 CSP
        BigInteger x2 = csp.partyDecrypt(x);
        BigInteger maskedX = csp.finalDecrypt(x1, x2);
        BigInteger y2 = csp.partyDecrypt(y);
        BigInteger maskedY = csp.finalDecrypt(y1, y2);
        BigInteger maskedXY = maskedX.multiply(maskedY);
        BigInteger eXY = pai.encrypt(maskedXY);

        // Step-3 CP
        BigInteger e_r2_x = pai.multiply(ex, r2);
        BigInteger e_r1_y = pai.multiply(ey, r1);
        BigInteger e_r1_r2 = pai.encrypt(r1.multiply(r2));
        pai.getPublicKey().getN();
        return pai.add(
                eXY,
                pai.multiply(
                        pai.add(pai.add(e_r2_x, e_r1_y), e_r1_r2),
                        pai.getPublicKey().getN().subtract(BigInteger.valueOf(1))
                )
        );
    }
}
