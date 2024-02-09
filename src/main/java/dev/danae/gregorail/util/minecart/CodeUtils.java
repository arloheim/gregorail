package dev.danae.gregorail.util.minecart;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.util.parser.ParserException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;


public class CodeUtils
{
  // Display names for minecart codes
  private static final CodeDisplayNameData displayNames = new CodeDisplayNameData(new File(RailPlugin.getInstance().getDataFolder(), "code_display_names.yml"));
  
  // Patterns for parsing codes
  private static final Pattern pattern = Pattern.compile("[a-z0-9_]+", Pattern.CASE_INSENSITIVE);
  private static final Pattern delimiterPattern = Pattern.compile("\\|");
  
  
  // Check if a code is valid
  public static boolean isValidCode(String id)
  {
    return pattern.matcher(id).matches();
  }
  
  // Assert that a code is valid
  public static void assertIsValidCode(String id) throws ParserException
  {
    if (!isValidCode(id))
      throw new ParserException(String.format("Code \"%s\" is an invalid minecart code; codes may only contain alphanumeric characters and underscores", id));
  }
  
  
  // Create a code from an identifier
  public static Code createCode(String id) throws ParserException
  {
    assertIsValidCode(id);
    
    return new Code(id);
  }
  
  // Create a list of codes from a string containing identfiers separated by pipes
  public static List<Code> createCodes(String string) throws ParserException
  {
    var codeList = new ArrayList<Code>();
    
    var stringComponents = delimiterPattern.split(string);
    for (var stringComponent : stringComponents)
      codeList.add(createCode(stringComponent));
    
    return codeList;
  }
  
  
  // Get all codes for which a display name is defined as a stream
  public static Stream<Code> codesWithDisplayNameAsStream()
  {
    return displayNames.codesAsStream();
  }
  
  // Get all codes for which a display name is defined
  public static Collection<Code> codesWithDisplayName()
  {
    return displayNames.codes();
  }
  
  // Get all entries for codes for which a display name is defined as a stream
  public static Stream<Map.Entry<Code, String>> codeEntriesWithDisplayNameAsStream()
  {
    return displayNames.codeEntriesAsStream();
  }
  
  // Get all entries for codes for which a display name is defined
  public static Collection<Map.Entry<Code, String>> codeEntriesWithDisplayName()
  {
    return displayNames.codeEntries();
  }
  
  // Get the display name for a code
  public static String getDisplayName(Code code)
  {
    return displayNames.getDisplayName(code);
  }
  
  // Set the display name of a code
  public static void setDisplayName(Code code, String displayName)
  {
    displayNames.setDisplayName(code, displayName);
  }
  
  // Remove the display name of a code
  public static void removeDisplayName(Code code)
  {
    displayNames.removeDisplayName(code);
  }
}
