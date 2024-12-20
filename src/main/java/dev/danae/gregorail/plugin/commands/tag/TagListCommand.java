package dev.danae.gregorail.plugin.commands.tag;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.common.commands.CommandUsageException;
import dev.danae.common.messages.minimessage.StringComponentLike;
import dev.danae.gregorail.model.Code;
import dev.danae.gregorail.model.CodeTag;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.commands.ManagerCommand;


public class TagListCommand extends ManagerCommand
{
  // Constructor
  public TagListCommand(Manager manager)
  {
    super(manager, "gregorail.tag.list");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {    
    // Validate the number of arguments
    if (!context.hasArgumentsCount(0))
      throw new CommandUsageException();
      
    // Send a message listing the code tags
    context.sendMessage(this.getManager().formatMessage("tag-list", Map.of("count", this.getManager().getDefinedCodeTags().size()))
      .appendNewline()
      .append(this.getManager().getDefinedCodeTags().entrySet().stream()
        .sorted(Comparator.comparing(e -> e.getKey()))
        .map(e -> this.formatCodeEntry(e.getKey(), e.getValue()))
        .collect(Component.toComponent(Component.newline()))));
  }

  // Return suggestions for the specified command context
  @Override
  public Stream<String> suggest(CommandContext context)
  {
    return Stream.empty();
  }


  // Format an code entry to a component
  private Component formatCodeEntry(Code code, CodeTag codeTag)
  {
    return this.getManager().formatMessage("tag-list-item", Map.of(
      "code", this.formatCode(code), 
      "name", this.formatCodeTagName(codeTag), 
      "url", this.formatCodeTagUrl(codeTag)));
  }

  // Format a code to a component
  private Component formatCode(Code code)
  {
    var setCommand = String.format("/gcart set %s", code.getId());

    return Component.text(code.getId())
      .decorate(TextDecoration.UNDERLINED)
      .hoverEvent(HoverEvent.showText(Component.text(setCommand)))
      .clickEvent(ClickEvent.suggestCommand(setCommand));
  }

  // Format the name of a code tag to a component
  private StringComponentLike formatCodeTagName(CodeTag codeTag)
  {
    return arg -> {
      if (codeTag.getName() == null)
        return !arg.isEmpty() ? this.getManager().getMessageFormatter().format(arg) : Component.empty();

      return this.getManager().getMessageFormatter().format(codeTag.getName());
    };
  }

  // Format the URL of a code tag to a component
  private StringComponentLike formatCodeTagUrl(CodeTag codeTag)
  {
    return arg -> {
      if (codeTag.getUrl() == null)
        return !arg.isEmpty() ? this.getManager().getMessageFormatter().format(arg) : Component.empty();

      return Component.text(codeTag.getUrl())
        .decorate(TextDecoration.UNDERLINED)
        .clickEvent(ClickEvent.openUrl(codeTag.getUrl()));
    };
  }
}
