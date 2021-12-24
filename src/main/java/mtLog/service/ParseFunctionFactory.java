package mtLog.service;

public interface ParseFunctionFactory {

  IParseFunction getFunction(String functionName);

  boolean isBeforeFunction(String functionName);
}
