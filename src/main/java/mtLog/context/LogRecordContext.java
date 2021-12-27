package mtLog.context;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class LogRecordContext {

  private static final InheritableThreadLocal<Deque<Map<String, Object>>> variableMapStack =
      new InheritableThreadLocal<>();

  public static void init() {
    Deque<Map<String, Object>> stack = variableMapStack.get();
    if (stack == null) {
      stack = new ArrayDeque<>();
    }
    stack.push(new HashMap<>());
    variableMapStack.set(stack);
  }

  public static void clear() {
    variableMapStack.get().pop();
  }

  public static Map<String, Object> getVariables() {
    Deque<Map<String, Object>> stack = variableMapStack.get();
    if (stack == null || stack.isEmpty()) {
      return Collections.emptyMap();
    }
    return stack.peek();
  }
}
