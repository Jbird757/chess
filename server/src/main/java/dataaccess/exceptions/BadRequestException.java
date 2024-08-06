package dataaccess.exceptions;

import Exceptions.DataAccessException;

public class BadRequestException extends DataAccessException {
    public BadRequestException(String message) {
        super(message);
    }
}
