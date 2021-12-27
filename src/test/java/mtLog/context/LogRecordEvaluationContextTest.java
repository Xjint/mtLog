package mtLog.context;


import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@SpringBootTest
class MyTest {

  /*
   *
   * @LogRecordAnnotation(success = "订单号码: #getOrderNameById(#{})", bizNo = "")
   *
   * @Value(value = "#{@sampleParseFunction.apply('1')}")
   * */
  @Test
  public void test() throws NoSuchMethodException {

    Method method = MyTest.class.getDeclaredMethod("apply", String.class);
    EvaluationContext evaluationContext = new StandardEvaluationContext();
    evaluationContext.setVariable("getOrderNameById", method);
    evaluationContext.setVariable("orderId", "3");
    Object value = new SpelExpressionParser()
        .parseExpression("#getOrderNameById(#orderId)")
        .getValue(evaluationContext);
    System.out.println(value);
  }

  public static String apply(String value) {
    System.out.println("orderId: " + value);
    return "订单的名字";
  }

}