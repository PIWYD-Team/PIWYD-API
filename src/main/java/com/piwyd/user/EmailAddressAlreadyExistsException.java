package com.piwyd.user;

public class EmailAddressAlreadyExistsException extends Throwable {

    public EmailAddressAlreadyExistsException(String email) {
        super("This email already exists : " + email);
    }
}
