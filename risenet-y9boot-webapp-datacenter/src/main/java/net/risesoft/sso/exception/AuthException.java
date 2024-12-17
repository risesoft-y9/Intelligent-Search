package net.risesoft.sso.exception;

public class AuthException extends RuntimeException {
    private static final long serialVersionUID = -3249547323156208132L;

    public AuthException(String message) {
        super(message);
    }
}
