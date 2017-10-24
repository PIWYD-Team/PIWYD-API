package com.piwyd.crypto;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CipherBlockChainingTest {

	private static final String FILENAME = "cipherTestFile.txt";
	private static final String ENCRYPTED_FILENAME = FILENAME + ".enc";
	private static final String FILE_CONTENT = "ceci est un contenu de test";

	@Before
	public void initTest() {
		try {
			initInputFile();
		} catch (IOException e) {
			e.printStackTrace();
			fail("An unexpected exception has been thrown...");
		}
	}

	@After
	public void cleanTest() {
		File file = new File(FILENAME);
		if(file.exists()) {
			if(!file.delete()) {
				Logger.getAnonymousLogger().log(Level.WARNING, "The test file " + FILENAME + " has not been deleted");
			}
		}
		file = new File(ENCRYPTED_FILENAME);
		if(file.exists()) {
			if(!file.delete()) {
				Logger.getAnonymousLogger().log(Level.WARNING, "The test file " + ENCRYPTED_FILENAME + " has not been deleted");
			}
		}
	}

	@Test
	public void testCipher() {
		FileInputStream fileInputStream = null;
		try {
			// Encrypt the file
			CipherBlockChaining cbcEncrypt = new CipherBlockChaining(CipherBlockChaining.CBCTask.ENCRYPTION, FILENAME);
			cbcEncrypt.process();

			File encryptedFile = new File(ENCRYPTED_FILENAME);
			assertTrue("Check encrypted file exists", encryptedFile.exists());

			// Delete the original file
			if(!new File(FILENAME).delete()) {
				fail("An error occurred while renaming the original file");
			}

			// Decrypt the file
			CipherBlockChaining cbcDecrypt = new CipherBlockChaining(CipherBlockChaining.CBCTask.DECRYPTION, ENCRYPTED_FILENAME);
			cbcDecrypt.process();

			// Check file content
			File decryptedFile = new File(FILENAME);
			fileInputStream = new FileInputStream(decryptedFile);
			byte[] buffer = new byte[255];
			int nbBytesRead = fileInputStream.read(buffer);

			byte[] expected = FILE_CONTENT.getBytes();

			assertEquals("Nb bytes", expected.length, nbBytesRead);
			assertArrayEquals("Compare byte arrays", FILE_CONTENT.getBytes(), Arrays.copyOf(buffer, nbBytesRead));
		} catch (IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
			fail("An unexpected exception has been thrown...");
		} finally {
			if(null != fileInputStream) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					fail("An unexpected exception has been thrown...");
				}
			}
		}
	}

	private void initInputFile() throws IOException {
		File inputFile = new File(FILENAME);
		if(!inputFile.exists() && !inputFile.createNewFile()) {
			fail("An error occurred while creating the cipher test file...");
		}

		FileOutputStream fileOutputStream = new FileOutputStream(inputFile);
		fileOutputStream.write(FILE_CONTENT.getBytes());

		fileOutputStream.close();
	}
}
