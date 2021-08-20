package pl.visualnet.omomo.exception;

import pl.visualnet.omomo.domain.Error;

public class ReaderException extends RuntimeException {

    private final Error error;

    public ReaderException(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
