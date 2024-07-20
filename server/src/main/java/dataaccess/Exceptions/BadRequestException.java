package dataaccess.Exceptions;

import dataaccess.DataAccessException;

public class BadRequestException extends DataAccessException {
    public BadRequestException(String message) {
        super(message);
    }
}
