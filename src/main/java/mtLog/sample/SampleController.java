package mtLog.sample;


import mtLog.annotation.LogRecordAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @GetMapping("/sample")
  //spel中可以通过#a{index}或#p{index}直接获取对应下标的形参
  //也可以通过#{形参名}来获取
  @LogRecordAnnotation(success = "@sampleParseFunction.apply(#s1)", bizNo = "", operator = "")
  @LogRecordAnnotation(success = "@sampleParseFunction.apply(#p0)", bizNo = "", operator = "")
  public String sample(String s1) {
    log.info(s1);
    return "ok";
  }
}
