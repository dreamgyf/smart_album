package com.newbee.smart_album.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED,reason = "not logged in")
public class NotLogInException extends RuntimeException {
}
