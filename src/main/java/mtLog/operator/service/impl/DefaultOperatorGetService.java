package mtLog.operator.service.impl;

import mtLog.operator.entity.Operator;
import mtLog.operator.service.IOperatorGetService;
import org.springframework.stereotype.Service;

@Service
public class DefaultOperatorGetService implements IOperatorGetService {

  @Override
  public Operator getUser() {
    return new Operator();
  }
}
