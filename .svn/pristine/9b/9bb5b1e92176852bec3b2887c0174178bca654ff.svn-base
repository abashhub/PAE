package exception;

public class BizzException extends RuntimeException {

  /**
   * Default HTTP status code that will we used when a BizzException is thrown. 400 = Bad Request.
   */
  public static final int DEFAULT_HTTP_STATUS_CODE = 400;

  public static int getDefaultHttpStatusCode() {
    return DEFAULT_HTTP_STATUS_CODE;
  }

  private static final long serialVersionUID = 1L;

  public BizzException() {
    super();
  }

  public BizzException(String message) {
    super(message);
  }

  public BizzException(String message, Throwable cause) {
    super(message, cause);
  }

  public BizzException(Throwable cause) {
    super(cause);
  }
}
