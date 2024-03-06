package dev.danae.gregorail.util.parser;


@FunctionalInterface
public interface ParserFunction<T, R>
{
  // Apply the function to the given argument
  public R apply(T t) throws ParserException;
  
  
  // Apply the function to the given argument, or returns the default value if a parser exception is thrown
  public static <T, R> R applyOrDefault(ParserFunction<T, R> function, T t, R defaultValue)
  {
    try
    {
      return function.apply(t);
    }
    catch (ParserException ex)
    {
      return defaultValue;
    }
  }
}
