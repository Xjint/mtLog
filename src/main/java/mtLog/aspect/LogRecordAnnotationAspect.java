package mtLog.aspect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mtLog.component.LogRecordOperationSource;
import mtLog.context.LogRecordContext;
import mtLog.entity.LogRecordOps;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogRecordAnnotationAspect {


  private final Logger log = LoggerFactory.getLogger(getClass());
  private final LogRecordOperationSource logRecordOperationSource;

  public LogRecordAnnotationAspect(LogRecordOperationSource logRecordOperationSource) {
    this.logRecordOperationSource = logRecordOperationSource;
  }

  @Pointcut(value = "@annotation(mtLog.annotation.LogRecordAnnotation)")
  public void pointcut() {
  }

  @Around(value = "pointcut()")
  public Object around(ProceedingJoinPoint pjp) throws Throwable {
    Class<?> targetClass = pjp.getTarget().getClass();
    Object[] args = pjp.getArgs();
    Method method = ((MethodSignature) pjp.getSignature()).getMethod();
    Object ret = null;
    MethodExecuteResult methodExecuteResult = new MethodExecuteResult(true, null, "");
    LogRecordContext.putEmptySpan();
    LogRecordOps operations = new LogRecordOps();
    Map<String, String> functionNameAndReturnMap = new HashMap<>();
    try {
      operations = logRecordOperationSource.computeLogRecordOperations(method, targetClass);
      List<String> spElTemplates = getBeforeExecuteFunctionTemplate(operations);
      //业务逻辑执行前的自定义函数解析
      functionNameAndReturnMap = processBeforeExecuteFunctionTemplate(spElTemplates, targetClass,
          method, args);
    } catch (Exception e) {
      log.error("log record parse before function exception", e);
    }
    try {
      ret = pjp.proceed(args);
    } catch (Exception e) {
      methodExecuteResult = new MethodExecuteResult(false, e, e.getMessage());
    }
    try {
      recordExecute(ret, method, args, operations, targetClass,
          methodExecuteResult.isSuccess(), methodExecuteResult.getErrorMsg(),
          functionNameAndReturnMap);
    } catch (Exception t) {
      //记录日志错误不要影响业务
      log.error("log record parse exception", t);
    } finally {
      LogRecordContext.clear();
    }
    if (methodExecuteResult.throwable != null) {
      throw methodExecuteResult.throwable;
    }
    return ret;
  }


  private void recordExecute(Object ret, Method method, Object[] args,
      LogRecordOps operations, Class<?> targetClass, boolean success, String errorMsg,
      Map<String, String> functionNameAndReturnMap) {

  }

  private Map<String, String> processBeforeExecuteFunctionTemplate(List<String> spElTemplates,
      Class<?> targetClass, Method method, Object[] args) {
    return new HashMap<>();
  }

  private List<String> getBeforeExecuteFunctionTemplate(LogRecordOps operation) {
    return new ArrayList<>();
  }


  private static class MethodExecuteResult {

    private final boolean success;
    private final Throwable throwable;
    private final String errorMsg;

    private MethodExecuteResult(boolean success, Throwable throwable, String errorMsg) {
      this.success = success;
      this.throwable = throwable;
      this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
      return success;
    }

    public Throwable getThrowable() {
      return throwable;
    }

    public String getErrorMsg() {
      return errorMsg;
    }
  }

}

