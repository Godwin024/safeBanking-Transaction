package ng.safebanking.exception;

public class WithdrawalLimitExceededException extends RuntimeException {
    public WithdrawalLimitExceededException(final String message) {
        super(message);
    }
}
