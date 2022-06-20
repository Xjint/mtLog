package mtLog.core;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import mtLog.annotation.LogRecordAnnotation;
import mtLog.core.entity.LogRecordOps;
import org.springframework.stereotype.Component;

@Component
public class LogRecordOperationSource {

  public List<LogRecordOps> computeLogRecordOperations(LogRecordAnnotation[] annotations) {
    return Arrays.stream(annotations)
        .map(this::computeLogRecordOperations)
        .collect(Collectors.toList());
  }

  public LogRecordOps computeLogRecordOperations(LogRecordAnnotation annotation) {
    LogRecordOps logRecordOps = new LogRecordOps();
    logRecordOps.setOperator(annotation.operator());
    logRecordOps.setSuccess(annotation.success());
    logRecordOps.setFail(annotation.fail());
    logRecordOps.setBizNo(annotation.bizNo());
    logRecordOps.setCategory(annotation.category());
    logRecordOps.setCondition(annotation.condition());
    logRecordOps.setDetail(annotation.detail());
    return logRecordOps;
  }
}
