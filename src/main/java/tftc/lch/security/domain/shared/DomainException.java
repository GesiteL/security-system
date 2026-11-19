package tftc.lch.security.domain.shared;

/** 领域异常基类，所有业务规则违反均抛此异常或其子类。 */
public class DomainException extends RuntimeException {

    private final int errorCode;

    public DomainException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
