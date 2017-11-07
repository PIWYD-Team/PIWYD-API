package com.piwyd.user.face;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class KairosAPIException extends Exception {

	public KairosAPIException(final String msg) {
		super(msg);
	}
}
