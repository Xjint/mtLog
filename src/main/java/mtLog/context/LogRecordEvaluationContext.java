package mtLog.context;

import java.lang.reflect.Method;
import java.util.Map;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;

public class LogRecordEvaluationContext extends MethodBasedEvaluationContext {


  public LogRecordEvaluationContext(Method method, Object[] arguments,
      Object ret, String errorMsg) {
    super(null, method, arguments, new DefaultParameterNameDiscoverer());
    Map<String, Object> variables = LogRecordContext.getVariables();
    if (variables != null && variables.size() > 0) {
      for (Map.Entry<String, Object> entry : variables.entrySet()) {
        setVariable(entry.getKey(), entry.getValue());
      }
    }
    setVariable("_ret", ret);
    setVariable("_errorMsg", errorMsg);
  }
}
