package com.testing.junitmockitotesting.UnitTests.Fake.Exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message){
        super(message);
    }
}
