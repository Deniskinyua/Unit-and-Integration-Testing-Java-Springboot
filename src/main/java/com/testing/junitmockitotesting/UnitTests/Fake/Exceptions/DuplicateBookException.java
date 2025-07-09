package com.testing.junitmockitotesting.UnitTests.Fake.Exceptions;

public class DuplicateBookException extends RuntimeException {


    public DuplicateBookException(String message) {
        super(message); // This was missing!
    }
}
