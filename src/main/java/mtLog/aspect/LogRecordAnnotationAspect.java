package mtLog.aspect;

import java.util.ArrayList;
import java.util.List;
import mtLog.annotation.LogRecordAnnotation;
import mtLog.core.ExpressionEvaluator;
import mtLog.core.LogRecordContext;
import mtLog.core.LogRecordOperationSource;
import mtLog.core.entity.ExecutionResult;
import mtLog.core.entity.LogRecordOps;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order
public class LogRecordAnnotationAspect {

  private final Logger log = LoggerFactory.getLogger(getClass());
  private final LogRecordOperationSource logRecordOperationSource;
  private final ExpressionEvaluator expressionEvaluator;

  public LogRecordAnnotationAspect(LogRecordOperationSource logRecordOperationSource,
      ExpressionEvaluator expressionEvaluator) {
    this.logRecordOperationSource = logRecordOperationSource;
    this.expressionEvaluator = expressionEvaluator;
  }

  @Around(value = "@annotation(mtLog.annotation.LogRecordAnnotations) || @annotation(mtLog.annotation.LogRecordAnnotation)")
  public Object around(ProceedingJoinPoint pjp)
      throws Throwable {
    LogRecordAnnotation[] logRecordAnnotations = getLogRecordAnnotations(pjp);
    expressionEvaluator.setJoinPoint(pjp);
    Object ret = null;
    ExecutionResult executionResult;
    LogRecordContext.init();
    List<LogRecordOps> operations = new ArrayList<>();
    try {
      operations = logRecordOperationSource
          .computeLogRecordOperations(logRecordAnnotations);
    } catch (Exception e) {
      log.error("log record parse before function exception", e);
    }
    try {
      ret = pjp.proceed();
      executionResult = ExecutionResult.done(ret);
    } catch (Exception e) {
      executionResult = ExecutionResult.error(e);
    }
    try {
      for (LogRecordOps operation : operations) {
        String success = expressionEvaluator.parseExpression(operation.getSuccess(), String.class,
            executionResult);
        operation.setSuccess(success);
      }
    } catch (Exception t) {
      //记录日志错误不要影响业务
      log.error("log record parse exception", t);
    } finally {
      LogRecordContext.clear();
    }

    if (executionResult.getThrowable() != null) {
      throw executionResult.getThrowable();
    }
    return ret;
  }

  private LogRecordAnnotation[] getLogRecordAnnotations(ProceedingJoinPoint pjp) {
    MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
    return AnnotatedElementUtils
        .getMergedRepeatableAnnotations(methodSignature.getMethod(), LogRecordAnnotation.class)
        .toArray(LogRecordAnnotation[]::new);
  }
}

