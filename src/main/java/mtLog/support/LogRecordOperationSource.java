package mtLog.support;

import mtLog.annotation.LogRecordAnnotation;
import mtLog.entity.LogRecordOps;
import mtLog.operator.service.IOperatorGetService;
import org.springframework.stereotype.Component;

@Component
public class LogRecordOperationSource {

  private final IOperatorGetService operatorGetService;

  public LogRecordOperationSource(
      IOperatorGetService operatorGetService) {
    this.operatorGetService = operatorGetService;
  }

  public LogRecordOps computeLogRecordOperations(LogRecordAnnotation annotation) {
    LogRecordOps logRecordOps = new LogRecordOps();
    logRecordOps.setOperator(
        annotation.operator().equals("") ?
            operatorGetService.getUser().getName() : annotation.operator());
    logRecordOps.setSuccess(annotation.success());
    logRecordOps.setFail(annotation.fail());
    logRecordOps.setBizNo(annotation.bizNo());
    logRecordOps.setCategory(annotation.category());
    logRecordOps.setCondition(annotation.condition());
    logRecordOps.setDetail(annotation.detail());
    return logRecordOps;
  }
}
