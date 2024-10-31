package dev.danae.gregorail.plugin.commands.cart;

import java.util.Collection;
import java.util.List;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.CommandException;
import dev.danae.common.commands.CommandUsageException;
import dev.danae.common.commands.arguments.ArgumentType;
import dev.danae.gregorail.model.Code;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.Minecart;
import dev.danae.gregorail.plugin.GregoRailPlugin;
import dev.danae.gregorail.plugin.commands.ManagerQueryCommand;
import dev.danae.gregorail.plugin.commands.ManagerQueryCommandType;
import dev.danae.gregorail.plugin.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;


public class CartPromptSetCommand extends ManagerQueryCommand
{  
  // The options for the command
  private final CartPromptOptions options;
  
  // The plugin of the command
  private final GregoRailPlugin plugin;
  
  // Keys to store properties in the command
  private final NamespacedKey commandCartKey;
  private final NamespacedKey commandCodeKey;
  
  
  // Constructor
  public CartPromptSetCommand(Manager manager, ManagerQueryCommandType type, CartPromptOptions options, GregoRailPlugin plugin)
  {
    super(manager, type, "gregorail.cart.promptset");
    
    this.options = options;
    this.plugin = plugin;
    this.commandCartKey = new NamespacedKey(plugin, "command_cart");
    this.commandCodeKey = new NamespacedKey(plugin, "command_code");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException
  {
    try
    {
      // Assert that the command sender has a location
      var senderLocation = context.assertSenderHasLocation();
      
      // Assert the the command sender is a player, or get the nearest player to the sender
      var player = context.nearestPlayerOrSender(this.options.getPlayerSearchRadius());
      if (player == null)
        throw new CommandException("No player found");
      
      // Validate the number of arguments
      if (!context.hasAtLeastArgumentsCount(1))
        throw new CommandUsageException();
      
      // Create a scanner for the arguments
      var scanner = context.getArgumentsScanner();
      
      // Parse the properties
      var properties = scanner.wrapInPropertyBag();
      var radius = properties.getUnsignedInt("radius", this.getManager().getBlockSearchRadius());
      var distance = properties.getUnsignedInt("distance", this.getManager().getCartSearchDistance());
      
      // Parse the arguments
      var result = this.matchQueryMatcher(scanner, () -> scanner.nextCodeList(), () -> this.getManager().findNearestOrRidingCart(scanner.nextLocation(senderLocation, radius, null), distance, context.getSender()));
      
      // Execute the command
      if (result.getCart() != null)
      {
        // Create and open an inventory containing the codes
        var inventory = this.createCodeMetaInventory(player, this.options.getTitle(), result.getValue(), result.getCart());
        player.openInventory(inventory);
      }
      else
      {
        context.sendMessage(Formatter.formatCart(result.getCart()));
      }
    }
    catch (ParserException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
  
  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    switch (this.getType())
    {
      case ALWAYS:
      {
        if (context.hasAtLeastArgumentsCount(2))
          return this.handleLocationTabCompletion(context, 1, false);
        else if (context.hasArgumentsCount(1))
          return this.handleCodesTabCompletion(context.getArgument(0));
        else
          return List.of();
      }
        
      case CONDITIONAL:
      {
        var separatorIndex = context.findLastArgumentIndex("||");
        if (separatorIndex == 2)
          return this.handleCodesTabCompletion(context.getLastArgument(0));
        else if (separatorIndex == 1)
          return this.handleCodesTabCompletion(context.getLastArgument(0));
        else if (context.hasAtLeastArgumentsCount(3))
          return this.handleLocationTabCompletion(context, separatorIndex >= 0 ? context.getArgumentsCount() - separatorIndex + 2 : 2, true);
        else if (context.hasArgumentsCount(2))
          return this.handleCodesTabCompletion(context.getArgument(1));
        else if (context.hasArgumentsCount(1))
          return this.handleCodesTabCompletion(context.getArgument(0));
        else
          return List.of();
      }
        
      default:
        return List.of();
    }
  }
  
  
  // Event listener for when an inventory item is clicked
  @EventHandler
  public void onInventoryClick(InventoryClickEvent e)
  { 
    // Check if the click type was a left click
    if (!e.getClick().isLeftClick())
      return;
      
    // Check if the sender is a player
    if (!(e.getWhoClicked() instanceof Player player))
      return;
      
    // Check if there is a current item of the item material
    var item = e.getCurrentItem();
    if (item == null || item.getType() != this.options.getItemMaterial())
      return;
        
    // Get the item meta associated with the event
    var itemMeta = item.getItemMeta();
    if (!itemMeta.getPersistentDataContainer().has(this.commandCartKey, this.getManager().getMinecartDataType()) || !itemMeta.getPersistentDataContainer().has(this.commandCodeKey, this.getManager().getCodeDataType()))
      return;
    
    // Parse the arguments
    var cart = itemMeta.getPersistentDataContainer().get(this.commandCartKey, this.getManager().getMinecartDataType());    
    var code = itemMeta.getPersistentDataContainer().get(this.commandCodeKey, this.getManager().getCodeDataType());
    
    // Update the code of the cart
    if (cart != null)
    {
      var originalCode = cart.getCode();
      if (this.getManager().updateCartCode(cart, code))
        player.spigot().sendMessage(Formatter.formatCartCodeChangedMessage(cart, originalCode, code));
      else
        player.spigot().sendMessage(Formatter.formatCartCodeRetainedMessage(cart, originalCode));
    }
    else
    {
      player.spigot().sendMessage(Formatter.formatCart(cart));
    }
    
    // Cancel the event and close the inventory
    e.setCancelled(true);
    Bukkit.getScheduler().runTask(this.plugin, () -> e.getWhoClicked().closeInventory());
  }


  // Return the minimal inventory size for the specified size
  private int minimalInventorySize(int size)
  {
    if (size < 0 || size > 54)
      throw new IllegalArgumentException("size must be between 0 and 54");
    
    while (size % 9 > 0) 
      size ++;
    return Math.max(9, size);
  }

  // Create an inventory containing item stacks containing a code each
  private Inventory createCodeMetaInventory(InventoryHolder owner, String title, Collection<Code> codes, Minecart minecart)
  {
    // Check the size of the codes
    if (codes.size() > 54)
      throw new IllegalArgumentException("codes must contain at most 54 elements");

    // Create the inventory
    var inventory = Bukkit.createInventory(owner, this.minimalInventorySize(codes.size()), title);

    // Add the codes to the inventory
    for (var code : codes)
      inventory.addItem(this.createCodeMeta(code, minecart));

    // Return the inventory
    return inventory;
  }

  // Create an item stack containing a code
  private ItemStack createCodeMeta(Code code, Minecart cart)
  {
    // Create an item stack
    var itemStack = new ItemStack(this.options.getItemMaterial());
    var itemMeta = itemStack.getItemMeta();
    
    // Set the persistent data of the item meta
    itemMeta.getPersistentDataContainer().set(this.commandCartKey, this.getManager().getMinecartDataType(), cart);
    itemMeta.getPersistentDataContainer().set(this.commandCodeKey, this.getManager().getCodeDataType(), code);

    // Set the display name of the item meta
    var codeTag = this.getManager().getCodeTag(code);
    itemMeta.displayName(codeTag != null && codeTag.getName() != null ? codeTag.getName() : code.getId());
  
    // Set the item meta and return the item stack
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }
}
