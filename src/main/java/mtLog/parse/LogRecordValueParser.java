package mtLog.parse;

import java.lang.reflect.Method;
import mtLog.service.IFunctionService;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;

public class LogRecordValueParser {

  public LogRecordValueParser(IFunctionService functionService,
      LogRecordExpressionEvaluator expressionEvaluator) {
    this.functionService = functionService;
    this.expressionEvaluator = expressionEvaluator;
  }

  private final IFunctionService functionService;
  private final LogRecordExpressionEvaluator expressionEvaluator;

  public String parseExpression(String expression, Method method, Class<?> targetClass,
      EvaluationContext evaluationContext) {
    return this.parseExpression(expression, new AnnotatedElementKey(method, targetClass),
        evaluationContext);
  }

  public String parseExpression(String expression, AnnotatedElementKey methodKey,
      EvaluationContext evaluationContext) {
    return expressionEvaluator.parseExpression(expression, methodKey, evaluationContext);
  }


}
