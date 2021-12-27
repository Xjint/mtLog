package mtLog.operator.service.impl;

import mtLog.operator.entity.Operator;
import mtLog.operator.service.IOperatorGetService;

public class DefaultOperatorGetService implements IOperatorGetService {

  @Override
  public Operator getUser() {
    Operator operator = new Operator();
    operator.setId(100);
    operator.setName("admin");
    return operator;
  }
}
