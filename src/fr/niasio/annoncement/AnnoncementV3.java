package fr.niasio.annoncement;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.niasio.annoncement.commands.AnnonceOffCommand;
import fr.niasio.annoncement.commands.AnnonceOnCommand;

public class AnnoncementV3 extends JavaPlugin {
	
	  public static AnnoncementV3 plugin;
	  public static String format0;
	  public static String format1;
	  public static String format2;
	  public static String format3;
	  public static String format4;
	  
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
	  
	  public void onEnable() {
		    Server server = getServer();
		    ConsoleCommandSender console = server.getConsoleSender();
		    
		    console.sendMessage(ChatColor.GRAY + ">>----------------------------------");
		    console.sendMessage(ChatColor.DARK_GREEN + "[Annoncement] Loaded");
		    console.sendMessage(ChatColor.GRAY + ">>----------------------------------");
		    
		    getCommand("annonceon").setExecutor(new AnnonceOnCommand());
		    getCommand("annonceoff").setExecutor(new AnnonceOffCommand());
		    
		    ConfigFile();
		    
		    console.sendMessage(ChatColor.GRAY + ">>----------------------------------");
		    console.sendMessage(ChatColor.GREEN + "[Annoncement] Config Loaded !");
		    console.sendMessage(ChatColor.GRAY + ">>----------------------------------");
	  }
	  
	  
	  public static AnnoncementV3 getPlugin() {
	    return plugin;
	  }
	  
	  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	  {
	    Player p = (Player)sender;
	    
	    if (command.getName().equalsIgnoreCase("annoncereload"))
	    {
		  reloadConfig();
		  saveConfig();
		    format0 = getConfig().getString("Annonce.AnnonceOn");
		    format1 = getConfig().getString("Annonce.AnnonceOff");
		    format2 = getConfig().getString("AnnonceReload");
		    format3 = getConfig().getString("NoEnoughArgs");
		    format4 = getConfig().getString("NoPerm");
	      sender.sendMessage(AnnoncementV3.format2.replaceAll("&", "§").replace("%player", p.getName()));
	      return true;
	    }  
	    return true;
	  }
	  
	  public void ConfigFile()
	  {
	    if (!getDataFolder().exists()) {
	      getDataFolder().mkdirs();
	    }
	    getConfig().addDefault("Annonce.AnnonceOn", "&6&l[ANNONCEMENT OF &e&l%player&4&l] &6%message");
	    getConfig().addDefault("Annonce.AnnonceOff", "&4&l[ANNONCEMENT] &6%message");
	    getConfig().addDefault("AnnonceReload", "&9&lThe plugin was reload ! Enjoy ;)");
	    getConfig().addDefault("NoEnoughArgs", "&c&lNot enough arguments ?");
	    getConfig().addDefault("NoPerm", "&c&lYou don't have the permission !");
	    getConfig().options().copyDefaults(true);
	    saveConfig();
	    format0 = getConfig().getString("Annonce.AnnonceOn");
	    format1 = getConfig().getString("Annonce.AnnonceOff");
	    format2 = getConfig().getString("AnnonceReload");
	    format3 = getConfig().getString("NoEnoughArgs");
	    format4 = getConfig().getString("NoPerm");
	  }
	  
}
