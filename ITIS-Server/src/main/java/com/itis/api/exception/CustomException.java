package com.itis.api.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomException extends  RuntimeException{

    private  String errorCode;
    public CustomException(String message , String errorCode ){
        super(message);
        this.errorCode = errorCode;
    }
}
