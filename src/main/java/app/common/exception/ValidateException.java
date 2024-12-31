package app.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ValidateException extends RuntimeException {
    private final String errorMessage;
    private final HttpStatus httpStatus;
    public ValidateException(String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);
        this.errorMessage  = errorMessage ;
        this.httpStatus = httpStatus;
    }
}
