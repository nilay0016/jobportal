package com.jobportal.exception;

public class UserLoginSignUpException extends Exception{

    // -1 = User does not exists
    // 500 = Can't login
    Integer errorCode;
    public UserLoginSignUpException(Integer code){
        this.errorCode = code;
    }
    public String toString(){
        return ("Exception has Occured :  "+errorCode) ;
    }

}
