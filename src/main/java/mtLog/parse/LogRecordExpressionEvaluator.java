package mtLog.parse;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mtLog.context.LogRecordEvaluationContext;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

public class LogRecordExpressionEvaluator extends CachedExpressionEvaluator {

  private final Map<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>(64);

  private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);

  public String parseExpression(String conditionExpression, AnnotatedElementKey methodKey,
      EvaluationContext evalContext) {
    return getExpression(this.expressionCache, methodKey, conditionExpression)
        .getValue(evalContext, String.class);
  }

  public EvaluationContext createEvaluationContext(Object rootObject, Method method,
      Object[] arguments,
      ParameterNameDiscoverer parameterNameDiscoverer, Object ret, String errorMsg) {
    return new LogRecordEvaluationContext(rootObject, method, arguments, parameterNameDiscoverer,
        ret, errorMsg);
  }

}
