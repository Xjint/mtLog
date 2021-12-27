package mtLog.aspect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mtLog.annotation.LogRecordAnnotation;
import mtLog.context.LogRecordContext;
import mtLog.context.LogRecordEvaluationContext;
import mtLog.entity.LogRecordOps;
import mtLog.support.LogRecordOperationSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogRecordAnnotationAspect {


  private final Logger log = LoggerFactory.getLogger(getClass());
  private final LogRecordOperationSource logRecordOperationSource;

  public LogRecordAnnotationAspect(LogRecordOperationSource logRecordOperationSource) {
    this.logRecordOperationSource = logRecordOperationSource;
  }

  @Around(value = "@annotation(logRecordAnnotation)")
  public Object around(ProceedingJoinPoint pjp, LogRecordAnnotation logRecordAnnotation)
      throws Throwable {
    /*
     * 1. 拦截方法
     * 2. 解析注解参数
     * 3. 自定义函数将输入值转换为目标值(这个动作在6中才会实际执行) 得到一个模版字符串
     * 4. 模版字符串解析为expression对象
     * 5. 填充EvaluationContext(需要把3中的自定义函数注册到context中)
     * 6. 根据填充EvaluationContext解析expression对象为业务文本信息
     *
     * 1-4都是固定的 只是每一次构造不同的EvaluationContext进行解析
     *
     *
     *
     * @LogRecordAnnotation(success = "订单号码: #getOrderNameById(#{})", bizNo = "")
     * 模版（string）-> 表达式(expression) -> 获取实际结果(Object)
     * */
    Class<?> targetClass = pjp.getTarget().getClass();
    Object[] args = pjp.getArgs();
    Method method = ((MethodSignature) pjp.getSignature()).getMethod();
    Object ret = null;
    MethodExecuteResult methodExecuteResult = new MethodExecuteResult(true, null, "");
    LogRecordContext.init();
    LogRecordOps operations = new LogRecordOps();
    Map<String, String> functionNameAndReturnMap = new HashMap<>();
    try {
      //2. 解析注解参数
      operations = logRecordOperationSource.computeLogRecordOperations(logRecordAnnotation);
      List<String> spElTemplates = getBeforeExecuteFunctionTemplate(operations);
      //业务逻辑执行前的自定义函数解析
      functionNameAndReturnMap = processBeforeExecuteFunctionTemplate(spElTemplates, targetClass,
          method, args);
    } catch (Exception e) {
      log.error("log record parse before function exception", e);
    }
    try {
      ret = pjp.proceed();
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
    Map<String, String> functionNameAndReturnMap = new HashMap<>();
    for (String template : spElTemplates) {

    }

    return functionNameAndReturnMap;
  }

  private List<String> getBeforeExecuteFunctionTemplate(LogRecordOps operation) {
    ArrayList<String> rtn = new ArrayList<>();
    rtn.add(operation.getSuccess());
    return rtn;
  }

  private EvaluationContext createEvaluationContext(Method method,
      Object[] arguments, Object ret, String errorMsg) {
    //构建

    return new LogRecordEvaluationContext(method, arguments, ret, errorMsg);
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

