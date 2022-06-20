package mtLog.core.entity;

/**
 * @author walter.bai
 * @date 2022/6/17 17:57
 */

public class ExecutionResult {

  private final Object result;
  private final Throwable throwable;
  private final String errorMsg;

  private ExecutionResult(Object success, Throwable throwable, String errorMsg) {
    this.result = success;
    this.throwable = throwable;
    this.errorMsg = errorMsg;
  }

  public Object getResult() {
    return result;
  }

  public Throwable getThrowable() {
    return throwable;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public static ExecutionResult error(Throwable throwable) {
    return new ExecutionResult(null, throwable, throwable.getMessage());
  }

  public static ExecutionResult done(Object ret) {
    return new ExecutionResult(ret, null, null);
  }
}
