package nrw.janikbau.sfm.util;
// <- Import ->

// <- Static_Import ->

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util{
    // <- Public ->
    // <- Protected ->
    // <- Private->
    // <- Static ->

    // <- Constructor ->
    private Util(){
        throw new IllegalStateException("Do not instantiate !~!");
    }

    // <- Abstract ->
    // <- Object ->
    // <- Getter & Setter ->

    // <- Static ->
    public static String HashToString(final byte[] hash){
        return new BigInteger(1, hash).toString(16);
    }

    public static byte[] Hash(final byte[] data) throws NoSuchAlgorithmException{
        final MessageDigest digest = MessageDigest.getInstance("SHA3-512");

        return digest.digest(data);
    }
}
