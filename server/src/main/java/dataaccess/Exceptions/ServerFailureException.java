package dataaccess.Exceptions;

import dataaccess.DataAccessException;

public class ServerFailureException extends DataAccessException {
    public ServerFailureException(String message) {
        super(message);
    }
}
