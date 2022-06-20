package mtLog.core;

import java.lang.reflect.Method;
import java.util.Map;
import mtLog.core.entity.ExecutionResult;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

/**
 * @author walter.bai
 * @date 2022/6/20 09:33
 */
public class LogEvaluationContext extends MethodBasedEvaluationContext {

  public LogEvaluationContext(Object rootObject, Method method,
      Object[] arguments, ParameterNameDiscoverer parameterNameDiscoverer, ExecutionResult result,
      BeanFactory beanFactory) {
    super(rootObject, method, arguments, parameterNameDiscoverer);
    init(result, beanFactory);
  }

  private void init(ExecutionResult result, BeanFactory beanFactory) {
    Map<String, Object> variables = LogRecordContext.getVariables();
    if (variables != null && variables.size() > 0) {
      for (Map.Entry<String, Object> entry : variables.entrySet()) {
        setVariable(entry.getKey(), entry.getValue());
      }
    }
    setBeanResolver(new BeanFactoryResolver(beanFactory));
    setVariable("_ret", result.getResult());
    setVariable("_errorMsg", result.getErrorMsg());

  }
}
