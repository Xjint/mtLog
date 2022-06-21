package mtLog.core;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mtLog.core.entity.ExecutionResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * @author walter.bai
 * @date 2022/6/17 17:43
 */
public class ExpressionEvaluator {

  private final SpelExpressionParser parser;

  private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

  private final @Nullable
  ParserContext parserContext;

  private volatile JoinPoint joinPoint = null;

  private final BeanFactory beanFactory;

  private final Map<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>(64);

  public ExpressionEvaluator(SpelExpressionParser parser, ParserContext parserContext,
      BeanFactory beanFactory) {
    Assert.notNull(parser, "SpelExpressionParser must not be null");
    this.parser = parser;
    this.parserContext = parserContext;
    this.beanFactory = beanFactory;
  }

  public ExpressionEvaluator(BeanFactory beanFactory) {
    this(new SpelExpressionParser(), null, beanFactory);
  }

  public SpelExpressionParser getParser() {
    return this.parser;
  }

  public ParserContext getParserContext() {
    return parserContext;
  }

  public ParameterNameDiscoverer getParameterNameDiscoverer() {
    return parameterNameDiscoverer;
  }

  public final void setJoinPoint(JoinPoint joinPoint) {
    this.joinPoint = joinPoint;
  }

  public <T> T parseExpression(String expression, Class<T> returnType,
      ExecutionResult executionResult) {
    return getExpression(expression)
        .getValue(createEvaluationContext(executionResult), returnType);
  }

  public Expression getExpression(String expression) {
    Assert.notNull(joinPoint, "joinPoint must not be null,please call setJoinPoint()");
    ExpressionKey expressionKey = createKey(expression);
    Expression expr = expressionCache.get(expressionKey);
    if (expr == null) {
      expr = getParser().parseExpression(expression, getParserContext());
      expressionCache.put(expressionKey, expr);
    }
    return expr;
  }

  public LogEvaluationContext createEvaluationContext(ExecutionResult executionResult) {
    Assert.notNull(joinPoint, "joinPoint must not be null,please call setJoinPoint()");
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    Object[] args = joinPoint.getArgs();
    return new LogEvaluationContext(null, method, args, getParameterNameDiscoverer(),
        executionResult, beanFactory);
  }

  private ExpressionKey createKey(String expression) {
    Assert.notNull(joinPoint, "joinPoint must not be null,please call setJoinPoint()");
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    Class<?> targetClass = AopUtils.getTargetClass(joinPoint.getTarget());
    AnnotatedElementKey elementKey = new AnnotatedElementKey(method, targetClass);
    return new ExpressionKey(elementKey, expression);
  }

  /**
   * An expression key.
   */
  private static class ExpressionKey implements Comparable<ExpressionKey> {

    private final AnnotatedElementKey element;

    private final String expression;

    protected ExpressionKey(AnnotatedElementKey element, String expression) {
      Assert.notNull(element, "AnnotatedElementKey must not be null");
      Assert.notNull(expression, "Expression must not be null");
      this.element = element;
      this.expression = expression;
    }

    @Override
    public boolean equals(@Nullable Object other) {
      if (this == other) {
        return true;
      }
      if (!(other instanceof ExpressionKey)) {
        return false;
      }
      ExpressionKey otherKey = (ExpressionKey) other;
      return (this.element.equals(otherKey.element) &&
          ObjectUtils.nullSafeEquals(this.expression, otherKey.expression));
    }

    @Override
    public int hashCode() {
      return this.element.hashCode() * 29 + this.expression.hashCode();
    }

    @Override
    public String toString() {
      return this.element + " with expression \"" + this.expression + "\"";
    }

    @Override
    public int compareTo(ExpressionKey other) {
      int result = this.element.toString().compareTo(other.element.toString());
      if (result == 0) {
        result = this.expression.compareTo(other.expression);
      }
      return result;
    }
  }

}
