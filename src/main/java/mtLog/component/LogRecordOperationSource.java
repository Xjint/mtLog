package mtLog.component;

import java.lang.reflect.Method;
import mtLog.annotation.LogRecordAnnotation;
import mtLog.entity.LogRecordOps;
import mtLog.operator.entity.Operator;
import mtLog.operator.service.IOperatorGetService;
import org.springframework.stereotype.Component;

@Component
public class LogRecordOperationSource {

  private final IOperatorGetService operatorGetService;

  public LogRecordOperationSource(
      IOperatorGetService operatorGetService) {
    this.operatorGetService = operatorGetService;
  }

  public LogRecordOps computeLogRecordOperations(Method method, Class<?> targetClass) {
    LogRecordOps logRecordOps = new LogRecordOps();
    if (!method.isAnnotationPresent(LogRecordAnnotation.class)) {
      return logRecordOps;
    }
    LogRecordAnnotation annotation = method.getAnnotation(LogRecordAnnotation.class);
    if (annotation.operator().equals("")) {
      Operator operator = operatorGetService.getUser();
      logRecordOps.setOperator(operator.getName());
    } else {
      logRecordOps.setOperator(annotation.operator());
    }
    logRecordOps.setSuccess(annotation.success());
    logRecordOps.setFail(annotation.fail());
    logRecordOps.setBizNo(annotation.bizNo());
    logRecordOps.setCategory(annotation.category());
    logRecordOps.setCondition(annotation.condition());
    logRecordOps.setDetail(annotation.detail());
    return logRecordOps;
  }
}
