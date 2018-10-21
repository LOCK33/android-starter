package net.bndy.ad.framework.exception;

public class UnsupportedViewException extends RuntimeException {

    public UnsupportedViewException(String viewIdRef) {
        super("The view type of #" + viewIdRef + " is not supported.");
    }
}
