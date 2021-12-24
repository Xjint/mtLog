package mtLog.parse;

import java.lang.reflect.Method;
import mtLog.service.IFunctionService;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;

public class LogRecordValueParser {

  public LogRecordValueParser(IFunctionService functionService,
      LogRecordExpressionEvaluator expressionEvaluator,
      EvaluationContext evaluationContext) {
    this.functionService = functionService;
    this.expressionEvaluator = expressionEvaluator;
    this.evaluationContext = evaluationContext;
  }

  private final IFunctionService functionService;
  private final LogRecordExpressionEvaluator expressionEvaluator;
  private final EvaluationContext evaluationContext;

  public String parseExpression(String expression, Method method, Class<?> targetClass) {
    return this.parseExpression(expression, new AnnotatedElementKey(method, targetClass));
  }

  public String parseExpression(String expression, AnnotatedElementKey methodKey) {
    return expressionEvaluator.parseExpression(expression, methodKey, this.evaluationContext);
  }


}
