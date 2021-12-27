package mtLog.sample;

import mtLog.service.IParseFunction;
import org.springframework.stereotype.Service;

@Service
public class SampleParseFunction implements IParseFunction {

  @Override
  public String functionName() {
    return "getOrderNameById";
  }

  @Override
  public String apply(String value) {
    System.out.println("orderId: " + value);
    return "订单的名字";
  }
}
