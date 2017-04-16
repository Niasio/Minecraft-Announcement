package fr.niasio.annoncement.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.niasio.annoncement.AnnoncementV3;

public class AnnonceOnCommand implements CommandExecutor{

	  public boolean onCommand(CommandSender s, Command cmd, String arg, String[] args)
	  {
		Player p = (Player)s;
	    if (s.hasPermission("annoncement.on"))
	    {
	      if (args.length != 0)
	      {
	        String msg = "";
	        String[] arrayOfString;
	        int j = (arrayOfString = args).length;
	        for (int i = 0; i < j; i++)
	        {
	          String kas = arrayOfString[i];
	          msg = msg + kas + " ";
	        }
			Bukkit.broadcastMessage(AnnoncementV3.format0.replaceAll("&", "§").replace("%message", msg).replace("%player", p.getName()));
	      }
	      else
	      {
	        s.sendMessage(AnnoncementV3.format3.replaceAll("&", "§").replace("%player", p.getName()));
	      }
	    }
	    else {
	      s.sendMessage(AnnoncementV3.format4.replaceAll("&", "§").replace("%player", p.getName()));
	    }
	    return false;
	  }
	
}
