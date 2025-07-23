package it.gurux.e_shop.exception;

public class ResourceNotFoundException extends RuntimeException {
    /*
    Base for resource not found response
     */
    public ResourceNotFoundException(String message) {
                super(message);
    }
}
