package dev.danae.gregorail.model.arguments;

import dev.danae.common.commands.Suggestion;
import dev.danae.common.commands.arguments.ArgumentException;
import dev.danae.common.commands.arguments.ArgumentTypeMismatchException;
import dev.danae.common.commands.arguments.PatternArgumentType;
import dev.danae.gregorail.model.Code;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.ManagerComponent;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;


public final class CodeArgumentType extends ManagerComponent implements PatternArgumentType<Code>
{
  // The pattern for parsing a code
  private static final Pattern CODE_PATTERN = Pattern.compile("([a-zA-Z0-9_]+)|(!)");
  

  // Constructor
  public CodeArgumentType(Manager manager)
  {
    super(manager); 
  }


  // Return the type for the argument type
  @Override
  public Class<Code> getType()
  {
    return Code.class;
  }

  // Return the type name for the argument type
  @Override
  public String getTypeName()
  {
    return "code";
  }

  // Return the pattern for the argument type
  @Override
  public Pattern getPattern()
  {
    return CODE_PATTERN;
  }

  // Parse a code from the specified match result
  @Override
  public Code parseFromMatchResult(MatchResult m) throws ArgumentException 
  {
    // Check for a code
    if (m.group(0) != null)
      return Code.of(m.group(0));
    
    // Check for an empty code
    if (m.group(1) != null)
      return Code.empty();
    
    // Invalid code format
    throw new ArgumentTypeMismatchException(this, m.group());
  }

  // Return suggestions for the specified string
  @Override
  public Stream<String> suggestFromString(String input)
  {
    return Suggestion.find(input, this.getManager().getDefinedCodeTags().keySet().stream()
      .map(code -> code.getId()));
  }
}
