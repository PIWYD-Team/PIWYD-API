package com.piwyd.crypto;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static com.piwyd.crypto.CipherBlockChaining.CBCTask.DECRYPTION;
import static com.piwyd.crypto.CipherBlockChaining.CBCTask.ENCRYPTION;

public class CipherBlockChaining {

	public enum CBCTask {
		ENCRYPTION,
		DECRYPTION
	}

	/**
	 * The length of the INIT_VECTOR should be equal or greater than the hashed password length.
	 * For MD5 hashed password, the length must be at least 16 bytes
	 */
	private static final String INIT_VECTOR = "my init vectorAA";
	private static final String PASSWORD = "Default password 12345$";
	private static final String ENCRYPTION_EXTENSION = ".enc";

	private CBCTask task;
	private byte[] hashedPwdBytes;
	private File inputFile;
	private String outputFilename;

	public CipherBlockChaining(final CBCTask task, final String filename) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		//BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
		//this.hashedPassword = passwordEncoder.encode("");
		MessageDigest md = MessageDigest.getInstance("MD5");
		this.hashedPwdBytes = md.digest(PASSWORD.getBytes("utf-8"));
		this.task = task;
		this.inputFile = new File(filename);

		if(task == ENCRYPTION) {
			this.outputFilename = inputFile.getName() + ENCRYPTION_EXTENSION;
		} else {
			this.outputFilename = getDecryptedFilename(inputFile.getName());
		}
	}

	public void process() throws FileNotFoundException, UnsupportedEncodingException {
		File outputFile = new File(outputFilename);
		FileInputStream fileInputStream = new FileInputStream(inputFile);
		FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

		byte[] last = INIT_VECTOR.getBytes("utf-8");
		byte[] buffer = new byte[hashedPwdBytes.length];
		int nbBytesRead;

		try {
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

				fileOutputStream.write(processedBytes);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileInputStream.close();
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Xor byte per byte the 2 arrays. The length of the resulted byte array is equal to the smallest byte array.
	 * @param seq1 first byte array
	 * @param seq2 second byte array
	 * @return the xored byte array of the 2 input byte arrays
	 */
	private byte[] xorByteArrays(byte[] seq1, byte[] seq2) {
		int length = (seq1.length > seq2.length) ? seq2.length : seq1.length;
		byte[] result = new byte[length];

		for (int i=0; i<length; i++) {
			result[i] = (byte) (seq1[i] ^ seq2[i]);
		}

		return result;
	}

	/**
	 * Return the initial name of the file, before encryption.
	 * Ex: "toto.txt.enc" => "toto.txt"
	 * @param filename the name of the encrypted file
	 * @return the filename of the file before encryption
	 */
	private String getDecryptedFilename(String filename) {
		if (!filename.endsWith(ENCRYPTION_EXTENSION)) {
			return null;
		}

		return filename.substring(0, filename.lastIndexOf('.'));
	}
}
