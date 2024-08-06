package dataaccess.exceptions;

import Exceptions.DataAccessException;

public class AlreadyTakenException extends DataAccessException {
    public AlreadyTakenException(String message) {
        super(message);
    }
}
