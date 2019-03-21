package com.newbee.smart_album.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN,reason = "already logged in")
public class AlreadyLogInException extends RuntimeException {
}
