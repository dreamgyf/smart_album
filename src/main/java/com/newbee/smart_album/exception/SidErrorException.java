package com.newbee.smart_album.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,reason = "sid expired or not exist")
public class SidErrorException extends RuntimeException {
}
