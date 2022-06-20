package mtLog.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SampleParseFunction {

  private final Logger log = LoggerFactory.getLogger(getClass());

  public String apply(String value) {
    log.info("orderId:{}", value);
    return "订单的名字";
  }
}
