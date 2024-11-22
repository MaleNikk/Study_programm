package spring.boot.exception;

public class ApplicationException extends RuntimeException{
    public ApplicationException(String message) {
        super(message.concat("\nName thread: ").concat(Thread.currentThread().getName())
                .concat("\nCurrent method: ").concat(Class.class.getEnclosingMethod().getName()));
    }
}
