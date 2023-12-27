package com.app.orderapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PricingNotFoundException extends RuntimeException{
    public PricingNotFoundException(String message){
        super(message);
    }
}
