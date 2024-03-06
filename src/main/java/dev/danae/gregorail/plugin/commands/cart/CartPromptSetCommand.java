package dev.danae.gregorail.plugin.commands.cart;

import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.plugin.GregoRailPlugin;
import dev.danae.gregorail.plugin.commands.CommandContext;
import dev.danae.gregorail.plugin.commands.CommandException;
import dev.danae.gregorail.plugin.commands.CommandUsageException;
import dev.danae.gregorail.plugin.commands.ManagerQueryCommand;
import dev.danae.gregorail.plugin.commands.QueryCommandType;
import dev.danae.gregorail.plugin.Formatter;
import dev.danae.gregorail.util.parser.ParserException;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


public class CartPromptSetCommand extends ManagerQueryCommand
{  
  // The options for the cart prompt
  private final CartPromptOptions options;
  
  // The plugin of the cart prompt
  private final GregoRailPlugin plugin;
  
  // Keys to store properties in the cart prompt
  private final NamespacedKey commandCartKey;
  private final NamespacedKey commandCodeKey;
  
  
  // Constructor
  public CartPromptSetCommand(Manager manager, QueryCommandType type, CartPromptOptions options, GregoRailPlugin plugin)
  {
    super(manager, type, "gregorail.cart.promptset");
    
    this.options = options;
    this.plugin = plugin;
    
    this.commandCartKey = new NamespacedKey(plugin, "command_cart");
    this.commandCodeKey = new NamespacedKey(plugin, "command_code");
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
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
      var scanner = context.getArgumentsScanner(this.getManager());
      
      // Parse the properties
      var properties = scanner.wrapInPropertyBag();
      var radius = properties.getUnsignedInt("radius", this.getManager().getBlockSearchRadius());
      var distance = properties.getUnsignedInt("distance", this.getManager().getCartSearchDistance());
      
      // Parse the arguments
      var result = this.matchQueryMatcher(scanner, () -> scanner.nextCodeList(), () -> this.getManager().findNearestOrRidingCart(scanner.nextLocation(senderLocation, radius, null), distance, context.getSender()));
      
      // Execute the command
      if (result.getCart() != null)
      {
        // Create the inventory
        var inventory = CartPromptUtils.createInventory(player, result.getValue(), code -> {
          var itemStack = new ItemStack(this.options.getItemMaterial());
          var itemMeta = itemStack.getItemMeta();
      
          itemMeta.setDisplayName(this.getManager().getDisplayName(code));
          itemMeta.getPersistentDataContainer().set(this.commandCartKey, this.getManager().getMinecartDataType(), result.getCart());
          itemMeta.getPersistentDataContainer().set(this.commandCodeKey, this.getManager().getMinecartCodeDataType(), code);
        
          itemStack.setItemMeta(itemMeta);
          return itemStack;
        }, this.options.getTitle());
      
        // Open the inventory
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
    if (context.hasAtLeastArgumentsCount(2))
      return this.handleLocationTabCompletion(context, 1);
    else if (context.hasArgumentsCount(1))
      return this.handleCodesTabCompletion(context.getArgument(0));
    else
      return null;
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
      
    // Parse the arguments
    var itemMeta = item.getItemMeta();
    var itemMetaContainer =itemMeta.getPersistentDataContainer();
    if (!itemMetaContainer.has(this.commandCartKey, this.getManager().getMinecartDataType()) || !itemMetaContainer.has(this.commandCodeKey, this.getManager().getMinecartCodeDataType()))
      return;
    
    var cart = itemMetaContainer.get(this.commandCartKey, this.getManager().getMinecartDataType());    
    var code = itemMetaContainer.get(this.commandCodeKey, this.getManager().getMinecartCodeDataType());
    
    // Execute the command
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
}
