package com.github.yukinomiu.hikari.common.crypto;

import com.github.yukinomiu.hikari.common.exception.HikariDecryptException;
import com.github.yukinomiu.hikari.common.exception.HikariEncryptException;
import com.github.yukinomiu.hikari.common.exception.HikariRuntimeException;
import com.github.yukinomiu.hikari.common.util.Md5Util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;

/**
 * Yukinomiu
 * 2018/1/26
 */
public class AESCrypto implements HikariCrypto {

    private final Cipher encryptCipher;
    private final Cipher decryptCipher;

    public AESCrypto(final String secret) {
        try {
            // get key
            final Md5Util md5Util = Md5Util.getInstance();
            SecretKey secretKey = new SecretKeySpec(md5Util.md5(secret), "AES");

            // get IV
            byte[] ivByte = md5Util.md5(md5Util.md5(secret));
            IvParameterSpec iv = new IvParameterSpec(ivByte);

            encryptCipher = Cipher.getInstance("AES/CFB/NoPadding");
            decryptCipher = Cipher.getInstance("AES/CFB/NoPadding");

            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        } catch (Exception e) {
            throw new HikariRuntimeException("init cipher exception", e);
        }
    }

    @Override
    public void encrypt(ByteBuffer input, ByteBuffer output) {
        try {
            encryptCipher.doFinal(input, output);
        } catch (Exception e) {
            throw new HikariEncryptException("encrypt exception", e);
        }
    }

    @Override
    public void decrypt(ByteBuffer input, ByteBuffer output) {
        try {
            decryptCipher.doFinal(input, output);
        } catch (Exception e) {
            throw new HikariDecryptException("decrypt exception", e);
        }
    }
}
