package mtLog.core;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class LogRecordContext {

  private static final InheritableThreadLocal<Deque<Map<String, Object>>> VARIABLE_MAP_DEQUE =
      new InheritableThreadLocal<>();

  public static void init() {
    Deque<Map<String, Object>> deque = VARIABLE_MAP_DEQUE.get();
    if (deque == null) {
      deque = new ArrayDeque<>();
    }
    deque.push(new HashMap<>());
    VARIABLE_MAP_DEQUE.set(deque);
  }

  public static void clear() {
    VARIABLE_MAP_DEQUE.get().pop();
  }

  public static Map<String, Object> getVariables() {
    Deque<Map<String, Object>> deque = VARIABLE_MAP_DEQUE.get();
    if (deque == null || deque.isEmpty()) {
      return Collections.emptyMap();
    }
    return deque.peek();
  }

  public static void put(String key, Object value) {
    Deque<Map<String, Object>> deque = VARIABLE_MAP_DEQUE.get();
    if (deque == null) {
      VARIABLE_MAP_DEQUE.set(deque = new ArrayDeque<>());
    }
    if (deque.isEmpty()) {
      deque.push(new HashMap<>());
    }
    deque.element().put(key, value);
  }
}
