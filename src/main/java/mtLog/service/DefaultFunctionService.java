package mtLog.service;


public class DefaultFunctionService implements IFunctionService {

  private final ParseFunctionFactory parseFunctionFactory;


  public DefaultFunctionService(ParseFunctionFactory parseFunctionFactory) {
    this.parseFunctionFactory = parseFunctionFactory;
  }

  @Override
  public String apply(String functionName, String value) {
    IParseFunction function = parseFunctionFactory.getFunction(functionName);
    if (function == null) {
      return value;
    }
    return function.apply(value);
  }

  @Override
  public boolean beforeFunction(String functionName) {
    return parseFunctionFactory.isBeforeFunction(functionName);
  }

}
