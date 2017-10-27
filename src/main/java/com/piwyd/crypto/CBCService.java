package com.piwyd.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

public interface CBCService {
    byte[] process(final CBCServiceImpl.CBCTask task, String password, InputStream inputStream) throws IOException, NoSuchAlgorithmException;
}
