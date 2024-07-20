package dataaccess.Exceptions;

import dataaccess.DataAccessException;

public class AlreadyTakenException extends DataAccessException {
    public AlreadyTakenException(String message) {
        super(message);
    }
}
