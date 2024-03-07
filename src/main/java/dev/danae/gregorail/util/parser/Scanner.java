package dev.danae.gregorail.util.parser;

import dev.danae.gregorail.model.Code;
import dev.danae.gregorail.model.Query;
import dev.danae.gregorail.model.QueryMatcher;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;


public class Scanner
{  
  // Pattern for parsing properties
  private static final Pattern PROPERTY_PATTERN = Pattern.compile("#([a-z][a-z0-9-]*)(?:=(.+))?");
  
  
  // The arguments that will be iterated over
  private final String[] arguments;
  
  // The current index of the scanner
  private int index;
  
  
  // Constructor
  public Scanner(String[] arguments)
  {
    this.arguments = arguments;
    this.index = -1;
  }
  
  
  // Return if the scanner reached the end of the tokens at the next index
  private boolean isAtEnd()
  {
    return this.isAtEnd(1);
  }
  
  // Return if the scanner reached the end of the tokens at the index with the specified lookahead
  private boolean isAtEnd(int lookahead)
  {
    return this.index + lookahead >= this.arguments.length;
  }

  // Return the token that has just been scanned
  private String current()
  {
    return this.index >= 0  ? this.arguments[this.index] : null;
  }

  // Return the token at the next index
  private String peek()
  {
    return !this.isAtEnd() ? this.arguments[this.index + 1] : null;
  }
  
  // Advance the index to the next position
  private String advance(String expected) throws ParserException
  {
    if (this.isAtEnd())
      throw new ParserException(String.format("Expected %s, but reached end of arguments", expected));
    
    this.index ++;
    return this.current();
  }
  
  // Advance the index the specified amount of positions
  private String advanceMany(int amount, String expected) throws ParserException
  {
    var string = "";
    for (var i = 0; i < amount; i ++)      
      string += (!string.isBlank() ? " " : "") + this.advance(expected);
    return string;
  }
  
  // Check if the next token matches the predicate
  private boolean check(Predicate<String> predicate)
  {
    return !this.isAtEnd() && predicate.test(this.peek());
  }
  
  // Check if the next token matches the pattern
  private MatchResult check(Pattern pattern)
  {
    if (this.isAtEnd())
      return null;
    
    var matcher = pattern.matcher(this.peek());
    return matcher.matches() ? matcher.toMatchResult() : null;
  }
  
  // Check if the next token matches the predicate and advance if so
  private boolean match(Predicate<String> predicate, String expected) throws ParserException
  {
    var result = this.check(predicate);
    if (result)
      this.advance(expected);
    return result;
  }
  
  // Check if the next token matches the pattern and advance if so
  private MatchResult match(Pattern pattern, String expected) throws ParserException
  {
    var result = this.check(pattern);
    if (result != null)
      this.advance(expected);
    return result;
  }
  
  // Advance the index while the next token matches the predicate
  private String matchWhile(Predicate<String> predicate, String expected) throws ParserException
  {
    var string = "";
    while (this.check(predicate))
      string += (!string.isBlank() ? " " : "") + this.advance(expected);
    return string;
  }
  
  
  // Take the next token and parse it
  private <T> T take(ParserFunction<String, T> parser, String expected) throws ParserException
  {    
    var previousIndex = this.index;
    try
    {
      return parser.apply(this.advance(expected));
    }
    catch (ParserException ex)
    {
      this.index = previousIndex;
      throw ex;
    }
  }

  // Take an amount of tokens and parse them
  private <T> T takeMany(ParserFunction<String, T> parser, int amount, String expected) throws ParserException
  {    
    var previousIndex = this.index;
    try
    {
      return parser.apply(this.advanceMany(amount, expected));
    }
    catch (ParserException ex)
    {
      this.index = previousIndex;
      throw ex;
    }
  }
  
  // Take tokens while they match the predicate and parse them
  private <T> T takeWhile(ParserFunction<String, T> parser, Predicate<String> predicate, String expected) throws ParserException
  {
    var previousIndex = this.index;
    try
    {
      return parser.apply(this.matchWhile(predicate, expected));
    }
    catch (ParserException ex)
    {
      this.index = previousIndex;
      throw ex;
    }
  }
  
  
  // Return the rest of the elements
  public String rest(String expected) throws ParserException
  {
    var string = "";
    while (!this.isAtEnd())
      string += (!string.isBlank() ? " " : "") + this.advance(expected);
    return string;
  }
  
  // Return the rest of the elements, or the default value if no such element exists
  public String rest(String expected, String defaultValue)
  {
    return ParserSupplier.getOrElse(() -> this.rest(expected), defaultValue);
  }
  
  // Return the next element in the scanner as an integer
  public int nextInt() throws ParserException
  {
    return this.take(Parser::parseInt, "integer number");
  }
  
  // Return the next element in the scanner as an integer, or the default value if no such element exists
  public int nextInt(int defaultValue)
  {
    return ParserSupplier.getOrElse(() -> this.nextInt(), defaultValue);
  }
  
  // Return the next element in the scanner as a long
  public long nextLong() throws ParserException
  {
    return this.take(Parser::parseLong, "integer number");
  }
  
  // Return the next element in the scanner as a long, or the default value if no such element exists
  public long nextLong(long defaultValue)
  {
    return ParserSupplier.getOrElse(() -> this.nextLong(), defaultValue);
  }
  
  // Return the next element in the scanner as an unsigned integer
  public int nextUnsignedInt() throws ParserException
  {
    return this.take(Parser::parseUnsignedInt, "unsigned integer number");
  }
  
  // Return the next element in the scanner as an unsigned integer, or the default value if no such element exists
  public int nextUnsignedInt(int defaultValue)
  {
    return ParserSupplier.getOrElse(() -> this.nextUnsignedInt(), defaultValue);
  }
  
  // Return the next element in the scanner as an unsigned long
  public long nextUnsignedLong() throws ParserException
  {
    return this.take(Parser::parseUnsignedLong, "unsigned integer number");
  }
  
  // Return the next element in the scanner as an unsigned long, or the default value if no such element exists
  public long nextUnsignedLong(long defaultValue)
  {
    return ParserSupplier.getOrElse(() -> this.nextUnsignedLong(), defaultValue);
  }
  
  // Return the next element in the scanner as a float
  public float nextFloat() throws ParserException
  {
    return this.take(Parser::parseFloat, "floating-point number");
  }
  
  // Return the next element in the scanner as a float, or the default value if no such element exists
  public float nextFloat(float defaultValue)
  {
    return ParserSupplier.getOrElse(() -> this.nextFloat(), defaultValue);
  }
  
  // Return the next element in the scanner as a double
  public double nextDouble() throws ParserException
  {
    return this.take(Parser::parseDouble, "floating-point number");
  }
  
  // Return the next element in the scanner as a double, or the default value if no such element exists
  public double nextDouble(double defaultValue)
  {
    return ParserSupplier.getOrElse(() -> this.nextDouble(), defaultValue);
  }
  
  // Return the next element in the scanner as an identifier
  public String nextIdentifier() throws ParserException
  {
    return this.take(Parser::parseIdentifier, "identifier");
  }
  
  // Return the next element in the scanner as an identifier
  public String nextIdentifier(String defaultValue)
  {
    return ParserSupplier.getOrElse(() -> this.nextIdentifier(), defaultValue);
  }
  
  
  // Return the next element in the scanner as a namespaced key
  public NamespacedKey nextKey() throws ParserException
  {
    return this.take(Parser::parseKey, "namespaced key");
  }
  
  // Return the next element in the scanner as a namespaced key, or the default value if no such element exists
  public NamespacedKey nextKey(NamespacedKey defaultValue)
  {
    return ParserSupplier.getOrElse(() -> this.nextKey(), defaultValue);
  }
  
  // Return the next element in the scanner as an enum of the specified class
  public <T extends Enum<T>> T nextEnum(Class<T> cls, String expected) throws ParserException
  {
    return this.take(s -> Parser.parseEnum(s, cls), expected);
  }
  
  // Return the next element in the scanner as an enum of the specified class, or the default value if no such element exists
  public <T extends Enum<T>> T nextEnum(Class<T> cls, String expected, T defaultValue)
  {
    return ParserSupplier.getOrElse(() -> this.nextEnum(cls, expected), defaultValue);
  }
  
  // Return the next element in the scanner as an enum set of the specified class
  public <T extends Enum<T>> EnumSet<T> nextEnumSet(Class<T> cls, String expected) throws ParserException
  {
    return this.take(s -> Parser.parseEnumSet(s, ",", cls), expected);
  }
  
  // Return the next element in the scanner as an enum set of the specified class, or the default value if no such element exists
  public <T extends Enum<T>> EnumSet<T> nextEnumSet(Class<T> cls, String expected, EnumSet<T> defaultValue)
  {
    return ParserSupplier.getOrElse(() -> this.nextEnumSet(cls, expected), defaultValue);
  }
  
  // Return the next element in the scanner as a material
  public Material nextMaterial(boolean requireBlock) throws ParserException
  {
    return this.take(s -> Parser.parseMaterial(s, requireBlock), "material");
  }
  
  // Return the next element in the scanner as a material, or the default value if no such element exists
  public Material nextMaterial(boolean requireBlock, Material defaultValue)
  {
    return ParserSupplier.getOrElse(() -> this.nextMaterial(requireBlock), defaultValue);
  }
  
  // Return the next element in the scanner as a code
  public Code nextCode() throws ParserException
  {
    return this.take(Parser::parseCode, "code");
  }
  
  // Return the next element in the scanner as a code, or the default value if no such element exists
  public Code nextCode(Code defaultValue)
  {
    return ParserSupplier.getOrElse(() -> this.nextCode(), defaultValue);
  }
  
  // Return the next element in the scanner as a list of codes
  public List<Code> nextCodeList() throws ParserException
  {
    return this.take(Parser::parseCodeList, "code list");
  }
  
  // Return the next element in the scanner as a list of codes, or the default value if no such element exists
  public List<Code> nextCodeList(List<Code> defaultValue)
  {
    return ParserSupplier.getOrElse(() -> this.nextCodeList(), defaultValue);
  }
  
  // Return the next element in the scanner as a query
  public Query nextQuery() throws ParserException
  {
    return this.take(Parser::parseQuery, "query");
  }
  
  // Return the next element in the scanner as a query, or the default value if no such element exists
  public Query nextQuery(Query defaultValue)
  {
    return ParserSupplier.getOrElse(() -> this.nextQuery(), defaultValue);
  }
  
  // Return the next element in the scanner as a location
  public Location nextLocation(Location origin, int radius) throws ParserException
  {
    try
    {
      return this.takeMany(s -> Parser.parseLocation(s, origin, radius), 3, "location");
    }
    catch (ParserException ex)
    {
      return this.take(s -> Parser.parseLocation(s, origin, radius), "location");
    }
  }
  
  // Return the next element in the scanner as a location, or the default value if no such element exists
  public Location nextLocation(Location origin, int radius, Location defaultValue)
  {
    return ParserSupplier.getOrElse(() -> this.nextLocation(origin, radius), defaultValue);
  }
  
  
  // Return the elements in the scanner wrapped in a property bag
  public Bag wrapInPropertyBag() throws ParserException
  {
    var propertyBag = new Bag();
    
    while (!this.isAtEnd())
    {
      var result = this.match(PROPERTY_PATTERN, "property");
      if (result == null)
        break;
      
      propertyBag.add(result.group(1), result.group(2));
    }
    
    return propertyBag;
  }
  
  // Return elements in the scanner wrapped in a query matcher
  public <T> QueryMatcher<T> wrapInMatcher(ParserSupplier<T> supplier) throws ParserException
  {
    var matcher = new QueryMatcher<T>();
      
    while (!this.isAtEnd())
    {
      var query = this.nextQuery();
      var result = supplier.get();
        
      matcher.addBranch(query, result);
      
      if (!this.match(s -> s.equals("||"), "separator"))
        break;
    }
      
    return matcher;
  }
}
