package it.gurux.e_shop.exception;

public class ResourceNotFound extends RuntimeException {
    /*
    Base for resource not found response
     */
    public ResourceNotFound(String message) {
                super(message);
    }
}
