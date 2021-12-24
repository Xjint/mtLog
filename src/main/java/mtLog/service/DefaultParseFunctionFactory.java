package mtLog.service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultParseFunctionFactory implements ParseFunctionFactory {

  private final Map<String, IParseFunction> map = new ConcurrentHashMap<>();

  public DefaultParseFunctionFactory(Collection<IParseFunction> parseFunctions) {
    for (IParseFunction parseFunction : parseFunctions) {
      map.put(parseFunction.functionName(), parseFunction);
    }
  }

  @Override
  public IParseFunction getFunction(String functionName) {
    return map.get(functionName);
  }

  @Override
  public boolean isBeforeFunction(String functionName) {
    return map.get(functionName).executeBefore();
  }
}
