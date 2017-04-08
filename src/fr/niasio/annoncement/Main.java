package fr.niasio.annoncement;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class Main extends JavaPlugin implements Listener {
  Main plugin;
	
  public static HashMap<String, String> messageData = new HashMap<String, String>();
   
  public void onLoad()
  {
    Server server = getServer();
    ConsoleCommandSender console = server.getConsoleSender();
    
    console.sendMessage(ChatColor.GRAY + ">>----------------------------------");
    console.sendMessage(ChatColor.DARK_PURPLE + "[Annoncement] Loading...");
    console.sendMessage(ChatColor.GRAY + ">>----------------------------------");
  }
  
  public void onDisable()
  {
    Server server = getServer();
    ConsoleCommandSender console = server.getConsoleSender();
    
    console.sendMessage(ChatColor.GRAY + ">>----------------------------------");
    console.sendMessage(ChatColor.DARK_RED + "[Annoncement] Unloaded");
    console.sendMessage(ChatColor.GRAY + ">>----------------------------------");
  }
  
  public void onEnable()
  {
    Server server = getServer();
    ConsoleCommandSender console = server.getConsoleSender();
    
    console.sendMessage(ChatColor.GRAY + ">>----------------------------------");
    console.sendMessage(ChatColor.DARK_GREEN + "[Annoncement] Loaded");
    console.sendMessage(ChatColor.GRAY + ">>----------------------------------");
    
    PluginManager pm = Bukkit.getServer().getPluginManager();
    pm.registerEvents(this, this);
    
    File f = new File(getDataFolder()+File.separator+"messages.yml");
    if (!f.exists()) {
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    setMessage("AnnonceOn", "&4&l[ANNONCEMENT OF &e&l%player&4&l] &6%message");
    setMessage("AnnonceOff", "&4&l[ANNONCEMENT] &6%message");
    setMessage("NoEnoughArgs", "&c&lNot enough arguments !");
    setMessage("AnnonceReload", "&9&lThe plugin was reload ! Enjoy ;)");
    
    FileConfiguration config = YamlConfiguration.loadConfiguration(f);
    for (String message : config.getConfigurationSection("").getKeys(false)) {
        messageData.put(message, config.getString(message));
    }
    console.sendMessage(ChatColor.GRAY + ">>----------------------------------");
    console.sendMessage(ChatColor.GREEN + "[Annoncement] Config Loaded !");
    console.sendMessage(ChatColor.GRAY + ">>----------------------------------");
    getConfig().options().copyDefaults(true);
    saveConfig();
  }
  
  private void setMessage(String name, String message) {
      File f = new File(getDataFolder()+File.separator+"messages.yml");
      FileConfiguration config = YamlConfiguration.loadConfiguration(f);
      if (!config.isSet(name)) {
          config.set(name, message);
          try {
              config.save(f);
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
  }

  public void sendTitle(Player player, String msgTit, String msgSubTit, int ticks1, int ticks2, int ticks3) {
      IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + msgTit + "\"}");
      IChatBaseComponent chatSubTitle = ChatSerializer.a("{\"text\": \"" + msgSubTit + "\"}");
      PacketPlayOutTitle p = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
      PacketPlayOutTitle p2 = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatSubTitle);
      ((CraftPlayer) player).getHandle().playerConnection.sendPacket(p);
      ((CraftPlayer) player).getHandle().playerConnection.sendPacket(p2);
      sendTime(player, ticks1, ticks2, ticks3);
  }
  
  public void sendTime(Player player, int ticks1, int ticks2, int ticks3) {
      PacketPlayOutTitle p = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, ticks1, ticks2, ticks3);
      ((CraftPlayer) player).getHandle().playerConnection.sendPacket(p);
  }

  class SendScroller extends BukkitRunnable {
      Scroller scroller;
      int      max, time;

      public SendScroller(String... strings) {
          String message = "§7[§cInfo§7]§c";
          for (String str : strings)
              message += " " + str;
          this.scroller = new Scroller(message, 32, 4, '&');
          this.max = message.length() / 2;
          runTaskTimer( plugin, 0, 2);
          run();
      }

      @Override
      public void run() {
          String msg = this.scroller.next();
          for (Player player : Bukkit.getOnlinePlayers()) {
              sendTitle(player, "", msg, 0, 20, 0);
          }
          if (this.time == max)
              this.cancel();
          else
              time++;
      }

  }
  
public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    Player p = (Player)sender;
    String annonce = "";
    String[] arrayOfString;
    int j;
    int i;
    if (command.getName().equalsIgnoreCase("annonceon"))
    {
      String message = "";
      j = (arrayOfString = args).length;
      for (i = 0; i < j; i++)
      {
        String part = arrayOfString[i];
        message = message + " " + part;
      }
      
      if (args.length < 1) {
          sender.sendMessage(ChatColor.translateAlternateColorCodes('&',messageData.get("NoEnoughArgs").replace("%player", p.getName())));
          return false;
       }
      
      message = message.replaceFirst(" ", "");

      annonce = ChatColor.translateAlternateColorCodes('&',messageData.get("AnnonceOn").replace("%player", p.getName()).replace("%message", message));
      
      Bukkit.getServer().broadcastMessage(annonce);
    }
    
    if (command.getName().equalsIgnoreCase("annoncetitle"))
    {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(
                    "§c/ialert <message> §8- §7Envoyer un message à tout les joueurs, affiché sur le title");
        } else {
            for (Player players : Bukkit.getOnlinePlayers()) {
                String message = "§7[§cInfo§7]§c";
                for (String str : args)
                    message += " " + str.replace('&', '§');
                players.sendMessage(message);
            }
            new SendScroller(args);
        }
        return true;
    }
    
    
    if (command.getName().equalsIgnoreCase("annoncereload"))
    {
      sender.sendMessage(ChatColor.translateAlternateColorCodes('&',messageData.get("AnnonceReload").replace("%player", p.getName())));
      this.reloadConfig();
      return true;
    }
    
    if (command.getName().equalsIgnoreCase("annonceoff"))
    {
      String message = "";
      j = (arrayOfString = args).length;
      for (i = 0; i < j; i++)
      {
        String part = arrayOfString[i];
        message = message + " " + part;
      }
      
      if (args.length < 1) {
          sender.sendMessage(ChatColor.translateAlternateColorCodes('&',messageData.get("NoEnoughArgs").replace("%player", p.getName())));
          return false;
       }
      
      message = message.replaceFirst(" ", "");
      
      annonce = ChatColor.translateAlternateColorCodes('&',messageData.get("AnnonceOff").replace("%player", p.getName()).replace("%message", message));
      
      Bukkit.getServer().broadcastMessage(annonce);
    }
    return true;
  }

}