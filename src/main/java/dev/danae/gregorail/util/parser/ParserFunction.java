package dev.danae.gregorail.util.parser;


@FunctionalInterface
public interface ParserFunction<T>
{
  // Parse a string
  public T parse(String string) throws ParserException;
}
