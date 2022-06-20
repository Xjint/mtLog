package mtLog.config;

import mtLog.core.ExpressionEvaluator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class mtLogAutoconfiguration {

  @Bean
  @ConditionalOnMissingBean
  public ExpressionEvaluator expressionEvaluator(BeanFactory beanFactory) {
    return new ExpressionEvaluator(beanFactory);
  }
}
