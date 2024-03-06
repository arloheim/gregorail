package dev.danae.gregorail.util.parser;


@FunctionalInterface
public interface ParserSupplier<T>
{
  // Return a result
  public T get() throws ParserException;
  
  
  // Return a result, or return the default value if a parser exception is thrown  
  public static <T> T getOrElse(ParserSupplier<T> supplier, T defaultValue)
  {
    try
    {
      return supplier.get();
    }
    catch (ParserException ex)
    {
      return defaultValue;
    }
  }
}
