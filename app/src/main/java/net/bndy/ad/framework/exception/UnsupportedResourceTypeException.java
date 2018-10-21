package net.bndy.ad.framework.exception;

public class UnsupportedResourceTypeException extends RuntimeException {

    public UnsupportedResourceTypeException(int id, String type) {
        super("The resource type " + type + " of #" + id + " is not supported.");
    }
}
