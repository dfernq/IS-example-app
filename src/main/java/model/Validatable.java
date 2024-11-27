package model;

public interface Validatable {
    void validate() throws ValidationException;

    class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
}
