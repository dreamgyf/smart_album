package com.newbee.smart_album.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "page not exist")
public class PageNotExistException extends RuntimeException {
}
