package dataaccess.exceptions;

import Exceptions.DataAccessException;

public class UnauthorizedException extends DataAccessException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
