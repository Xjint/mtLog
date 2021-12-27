package mtLog.sample;


import mtLog.annotation.LogRecordAnnotation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

  @GetMapping("/sample")
  @LogRecordAnnotation(success = "订单号码: @sampleParseFunction.apply('1')", bizNo = "")
  public String sample() {
    System.out.println("sample run");
    return "ok";
  }
}
