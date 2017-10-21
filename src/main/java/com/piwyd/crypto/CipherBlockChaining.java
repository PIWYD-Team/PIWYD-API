package com.piwyd.crypto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.*;
import java.util.Arrays;

import static com.piwyd.crypto.CipherBlockChaining.CBCTask.DECRYPTION;
import static com.piwyd.crypto.CipherBlockChaining.CBCTask.ENCRYPTION;

public class CipherBlockChaining {

	public enum CBCTask {
		ENCRYPTION,
		DECRYPTION
	}

	private static final String INIT_VECTOR = "DEFAULT INITIALIZING VECTOR";
	private static final String ENCRYPTION_EXTENSION = ".enc";

	private CBCTask task;
	private String hashedPassword;
	private File inputFile;
	private String outputFilename;

	public CipherBlockChaining(final CBCTask task, final String filename) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
		this.hashedPassword = passwordEncoder.encode("DEFAULT PASSWORD 12345$");
		this.task = task;
		this.inputFile = new File(filename);

		if(task == ENCRYPTION) {
			this.outputFilename = inputFile.getName() + ".enc";
		} else {
			this.outputFilename = getDecryptedFilename(inputFile.getName());
		}
	}

	public void process() throws FileNotFoundException {
		File outputFile = new File(outputFilename);
		FileInputStream fileInputStream = new FileInputStream(inputFile);
		FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

		byte[] hashPwdBytes = hashedPassword.getBytes();
		byte[] last = INIT_VECTOR.getBytes();
		byte[] buffer = new byte[hashedPassword.length()];
		int nbBytesRead;

		try {
			while ((nbBytesRead = fileInputStream.read(buffer)) > 0) {
				byte[] chunk = (nbBytesRead == buffer.length) ? buffer : Arrays.copyOf(buffer, nbBytesRead);
				byte[] processedBytes;

				if (ENCRYPTION == task) {
					processedBytes = xorBytesArrays(xorBytesArrays(chunk, last), hashPwdBytes);
					last = processedBytes;
				} else if (DECRYPTION == task) {
					processedBytes = xorBytesArrays(xorBytesArrays(chunk, hashPwdBytes), last);
					last = chunk;
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
	private byte[] xorBytesArrays(byte[] seq1, byte[] seq2) {
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
		if(!filename.endsWith(ENCRYPTION_EXTENSION)) {
			return null;
		}

		return filename.substring(0, filename.lastIndexOf('.'));
	}
}
