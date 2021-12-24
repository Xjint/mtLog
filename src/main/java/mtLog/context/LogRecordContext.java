package mtLog.context;

import java.util.Map;
import java.util.Stack;

public class LogRecordContext {

  private static final InheritableThreadLocal<Stack<Map<String, Object>>> variableMapStack =
      new InheritableThreadLocal<>();

  public static void putEmptySpan() {

  }

  public static void clear() {
    variableMapStack.get().pop();
  }

  public static Map<String, Object> getVariables() {
    return variableMapStack.get().peek();
  }
}
