package mtLog.config;

import java.util.Collection;
import mtLog.operator.service.IOperatorGetService;
import mtLog.operator.service.impl.DefaultOperatorGetService;
import mtLog.parse.LogRecordExpressionEvaluator;
import mtLog.service.DefaultFunctionService;
import mtLog.service.DefaultParseFunctionFactory;
import mtLog.service.IFunctionService;
import mtLog.service.IParseFunction;
import mtLog.service.ParseFunctionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class mtLogAutoconfiguration {


  @Bean
  @ConditionalOnMissingBean
  public IOperatorGetService defaultOperatorGetService() {
    return new DefaultOperatorGetService();
  }

  @Bean
  @ConditionalOnMissingBean
  public IFunctionService defaultFunctionService(ParseFunctionFactory parseFunctionFactory) {
    return new DefaultFunctionService(parseFunctionFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  public ParseFunctionFactory parseFunctionFactory(
      @Autowired(required = false) Collection<IParseFunction> parseFunctions) {
    return new DefaultParseFunctionFactory(parseFunctions);
  }

  @Bean
  public LogRecordExpressionEvaluator logRecordExpressionEvaluator() {
    return new LogRecordExpressionEvaluator();
  }

}
