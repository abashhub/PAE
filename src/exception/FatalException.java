package exception;

public class FatalException extends RuntimeException {

  /**
   * Default HTTP status code that will we used when a BizzException is thrown. 500 = Internal
   * Server Error.
   */
  public static final int DEFAULT_HTTP_STATUS_CODE = 500;
  private static final long serialVersionUID = 1L;

  public FatalException() {
    super();
  }

  public FatalException(String message) {
    super(message);
  }

  public FatalException(String message, Throwable cause) {
    super(message, cause);
  }

  public FatalException(Throwable cause) {
    super(cause);
  }
}
