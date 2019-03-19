package com.newbee.smart_album.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,reason = "upload failed")
public class UploadFailedException extends RuntimeException {
}
