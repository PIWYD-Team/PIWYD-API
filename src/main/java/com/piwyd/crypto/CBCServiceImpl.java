package com.piwyd.crypto;

import org.springframework.stereotype.Service;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static com.piwyd.crypto.CBCServiceImpl.CBCTask.DECRYPTION;
import static com.piwyd.crypto.CBCServiceImpl.CBCTask.ENCRYPTION;

@Service
public class CBCServiceImpl implements CBCService {

    public enum CBCTask {
        ENCRYPTION,
        DECRYPTION
    }

    private static final String INIT_VECTOR = "my init vectorAA";

    public byte[] process(final CBCTask task, String password, InputStream inputStream) throws IOException, NoSuchAlgorithmException {
        FileInputStream fileInputStream = (FileInputStream) inputStream;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashedPwdBytes = md.digest(password.getBytes("utf-8"));

        byte[] last = INIT_VECTOR.getBytes("utf-8");
        byte[] buffer = new byte[hashedPwdBytes.length];
        int nbBytesRead;

        while ((nbBytesRead = fileInputStream.read(buffer)) > 0) {
            byte[] chunk = (nbBytesRead == buffer.length) ? buffer : Arrays.copyOf(buffer, nbBytesRead);
            byte[] processedBytes;

            if (ENCRYPTION == task) {
                processedBytes = xorByteArrays(xorByteArrays(chunk, last), hashedPwdBytes);
                last = processedBytes;
            } else if (DECRYPTION == task) {
                processedBytes = xorByteArrays(xorByteArrays(chunk, hashedPwdBytes), last);
                last = Arrays.copyOf(chunk, chunk.length);
            } else {
                throw new IllegalArgumentException("Unknown task of CBC");
            }

            output.write(processedBytes);
        }

        return output.toByteArray();
    }

    private byte[] xorByteArrays(byte[] seq1, byte[] seq2) {
        int length = (seq1.length > seq2.length) ? seq2.length : seq1.length;
        byte[] result = new byte[length];

        for (int i=0; i<length; i++) {
            result[i] = (byte) (seq1[i] ^ seq2[i]);
        }

        return result;
    }
}
