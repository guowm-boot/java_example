package com.solaris.javatest;

import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

public class MySecure {
    @Test
    public void sha() throws Exception {
        MessageDigest sha256 = MessageDigest.getInstance("sha-1");
        String text = "“Upon my death, my property shall be divided equally among my children; however, my son George shall receive nothing.";
        sha256.update(text.getBytes());
        byte[] hash = sha256.digest();
        String d = "";
        for (int i = 0; i < hash.length; i++)  {
            int v = hash[i] & 0xFF;
            if (v < 16) d += "0";
            d += Integer.toString(v, 16).toUpperCase();
        }
        System.out.println(d);
    }

    @Test
    public void aes() throws  Exception {
        Cipher cph = Cipher.getInstance("AES");

        //generateKey
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(new SecureRandom());
        SecretKey key = keygen.generateKey();
        //SecretKey key = new SecretKeySpec(byte[] keyData, "AES");

        System.out.println(System.getProperty("java.security.egd"));

        cph.init(Cipher.ENCRYPT_MODE, key);
        int blockSize=cph.getBlockSize();
        int outputSize= cph.getOutputSize(blockSize);
        byte[] inBytes = new byte[blockSize];
        byte[] outBytes = new byte[outputSize];
        int outLength = cph.update(inBytes, 0, inBytes.length, outBytes);
        byte[] finalByte = cph.doFinal();

    }
}
