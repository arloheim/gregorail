package dev.danae.gregorail.util.minecart;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class CodeUtils
{
  // Pattern to parsing codes
  private static final Pattern pattern = Pattern.compile("[a-z0-9_]+", Pattern.CASE_INSENSITIVE);
  private static final Pattern delimiterPattern = Pattern.compile("\\|");
  
  
  // Check if a code is valid
  public static boolean isValidCode(String id)
  {
    return pattern.matcher(id).matches();
  }
  
  // Assert that a code is valid
  public static void assertIsValidCode(String id) throws InvalidCodeException
  {
    if (!isValidCode(id))
      throw new InvalidCodeException(String.format("Code \"%s\" is an invalid minecart code; codes may only contain alphanumeric characters and underscores", id));
  }
  
  // Create a code from an identifier
  public static Code createCode(String id) throws InvalidCodeException
  {
    assertIsValidCode(id);
    
    return new Code(id);
  }
  
  // Create a list of codes from a string containing identfiers separated by pipes
  public static List<Code> createCodes(String string) throws InvalidCodeException
  {
    var codeList = new ArrayList<Code>();
    
    var stringComponents = delimiterPattern.split(string);
    for (var stringComponent : stringComponents)
      codeList.add(createCode(stringComponent));
    
    return codeList;
  }
}
