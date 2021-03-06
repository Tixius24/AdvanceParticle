package me.Tixius24;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	private AdvanceParticle plugin;

	public Commands(AdvanceParticle pl) {
		plugin = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(plugin.getMessager().sendMessage("CONSOLE"));			
			return true;
		}

		Player p = (Player) sender;
		String particle;

		if (args.length == 0 || args.length > 3) {
			sendDefaultHelp(p);
			return true;
		}

		if (args.length == 1) {

			if (args[0].equalsIgnoreCase("help")) {

				if (!p.hasPermission("advanceparticle.help")) { 
					p.sendMessage(plugin.getMessager().sendMessage("NOPERM")); 
					return true; 
				}

				sendHelp(p);
				return true;
			}

			if (args[0].equalsIgnoreCase("reload")) {

				if (p.hasPermission("advanceparticle.reload")) { 
					plugin.getManager().loadPluginFile();
					plugin.getMessager().loadPluginMessage();
					p.sendMessage("�aPlugin config.yml and messages.yml files has been reloaded!");
				} else 
					p.sendMessage(plugin.getMessager().sendMessage("NOPERM"));

				return true; 
			}

			if (args[0].equalsIgnoreCase("particle")) {

				if (p.hasPermission("advanceparticle.particle")) { 
					plugin.getAPManager().listParticle(p);
				} else 
					p.sendMessage(plugin.getMessager().sendMessage("NOPERM"));

				return true;
			}

			if (args[0].equalsIgnoreCase("spawner")) {

				if (p.hasPermission("advanceparticle.spawner")) { 
					plugin.getAPManager().listSpawnerParticles(p);
				} else 
					p.sendMessage(plugin.getMessager().sendMessage("NOPERM")); 

				return true;
			}

			if (args[0].equalsIgnoreCase("remove")) {

				if (plugin.getPlayers().containsKey(p)) { 
					p.sendMessage(plugin.getMessager().sendMessage("REMOVE_PARTICLE_PLAYER"));
					plugin.getAPManager().removePlayerData(p);
				} else 
					p.sendMessage(plugin.getMessager().sendMessage("ERROR_PARTICLE_ACTIVE")); 

				return true;
			}

			sendDefaultHelp(p);
		}

		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("add")) {
				particle = args[1].toUpperCase();

				if (!plugin.getAPManager().checkPlayerPerm(p, args[1])) {
					return true; 
				}

				if (!plugin.getAPManager().checkExistParticle(particle, p)) { 
					return true; 
				}

				if (!plugin.getAPManager().checkEnableParticle(p, particle)) { 
					return true; 
				}

				if (plugin.getPlayers().containsKey(p)) { 
					p.sendMessage(plugin.getMessager().sendMessage("ALREADY_PARTICLE_PLAYER")); 
					return true; 
				}

				if (!plugin.getAPManager().checkAllowUseParticle(particle)) {
					p.sendMessage(plugin.getMessager().sendMessage("ERROR_USE_SERVER_VERSION"));
					return true;
				}

				plugin.getAPManager().savePlayerData(p, particle);
				p.sendMessage(plugin.getMessager().sendMessage("ACTIVE_PARTICLE_PLAYER").replace("%particle%", particle));
				return true;
			}

			if (args[0].equalsIgnoreCase("delete")) {

				if (!p.hasPermission("advanceparticle.delete")) { 
					p.sendMessage(plugin.getMessager().sendMessage("NOPERM")); 
					return true; 
				}

				if (!plugin.getStream().getBlockStream().containsKey(args[1])) { 
					p.sendMessage(plugin.getMessager().sendMessage("ERROR_BLOCK")); 
					return true; 
				}

				plugin.getAPManager().deleteBlockData(args[1]);
				p.sendMessage(plugin.getMessager().sendMessage("BLOCK_PARTICLE_DELETE"));
				return true;
			}

			if (args[0].equalsIgnoreCase("info")) {

				if (!p.hasPermission("advanceparticle.info")) { 
					p.sendMessage(plugin.getMessager().sendMessage("NOPERM")); 
					return true; 
				}

				if (!plugin.getStream().getBlockStream().containsKey(args[1])) { 
					p.sendMessage(plugin.getMessager().sendMessage("ERROR_BLOCK")); 
					return true; 
				}

				plugin.getAPManager().getSpawnerInfo(p, args[1]);
				return true;
			}

			if (args[0].equalsIgnoreCase("tp")) {

				if (!p.hasPermission("advanceparticle.teleport")) { 
					p.sendMessage(plugin.getMessager().sendMessage("NOPERM")); 
					return true; 
				}

				if (!plugin.getStream().getBlockStream().containsKey(args[1])) { 
					p.sendMessage(plugin.getMessager().sendMessage("ERROR_BLOCK")); 
					return true; 
				}

				plugin.getAPManager().teleportToSpawner(p, args[1]);
				p.sendMessage(plugin.getMessager().sendMessage("BLOCK_PARTICLE_TP"));
				return true;
			}

			sendDefaultHelp(p);
		}

		if (args.length == 3) {
			particle = args[2].toUpperCase();

			if (!plugin.getAPManager().checkExistParticle(particle, p)) { 
				return true; 
			}

			if (!plugin.getAPManager().checkEnableParticle(p, particle)) { 
				return true; 
			}

			if (plugin.getStream().getBlockStream().containsKey(args[1])) { 
				p.sendMessage(plugin.getMessager().sendMessage("ALREADY_BLOCK_NAME")); 
				return true; 
			}

			if (args[0].equalsIgnoreCase("set")) {

				if (!plugin.getAPManager().checkBlockPerm(p, args[1])) { 
					return true; 
				}

				if (!plugin.getAPManager().checkAllowUseParticle(particle)) {
					p.sendMessage(plugin.getMessager().sendMessage("ERROR_USE_SERVER_VERSION"));
					return true;
				}

				plugin.getAPManager().saveBlockData(p, particle, args[1], p.getLocation().getX(), p.getLocation().getY() - 0.5, p.getLocation().getZ());
				p.sendMessage(plugin.getMessager().sendMessage("BLOCK_PARTICLE_SET"));
				return true;
			}

			if (args[0].equalsIgnoreCase("setlooking")) {

				if (plugin.getVersionNumger() < 8) {
					p.sendMessage(plugin.getMessager().sendMessage("ERROR_USE_SERVER_COMMAND"));
					return true;
				}

				if (!plugin.getAPManager().checkBlockPerm(p, args[1])) { 
					return true; 
				}

				if (!plugin.getAPManager().checkAllowUseParticle(particle)) {
					p.sendMessage(plugin.getMessager().sendMessage("ERROR_USE_SERVER_VERSION"));
					return true;
				}

				Location loc = p.getTargetBlock(null, 50).getLocation();
				plugin.getAPManager().saveBlockData(p, particle, args[1], loc.getX() + 0.5, loc.getY() + 0.5, loc.getZ() + 0.5);
				p.sendMessage(plugin.getMessager().sendMessage("BLOCK_PARTICLE_SET"));
				return true;
			}

			sendDefaultHelp(p);
		}

		return false;
	}

	private void sendDefaultHelp(Player p) {
		p.sendMessage("�8�l> �c�lAdvanceParticle �7plugin by: �a�lTixius24 ");
		p.sendMessage("�8�l> �a�lhttps://www.spigotmc.org/resources/71929/");
		p.sendMessage("�8�l�8�l> �7For more info use �c�l/ap help");
	}

	private void sendHelp(Player p) {
		p.sendMessage("�8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		p.sendMessage("�8> �7Plugin version: �a" + plugin.getDescription().getVersion() + " �7Developed by �aTixius24");
		p.sendMessage("�8> �7Commands help:");
		p.sendMessage("�8> ");
		p.sendMessage("�8> �c/ap help �8- �7Show all commands from plugin");
		p.sendMessage("�8> �c/ap particle �8- �7List of the all particles from plugin");
		p.sendMessage("�8> �c/ap add <particle> �8- �7Set particle effect on the player!");
		p.sendMessage("�8> �c/ap remove �8- �7Remove already activated particle from yourself");

		if (p.hasPermission("advanceparticle.help.admin")) {
			p.sendMessage("�8> ");
			p.sendMessage("�8> �7Commands for administrators:");
			p.sendMessage("�8> �c/ap reload �8- �7Reload plugin messages.yml and config.yml");
			p.sendMessage("�8> �c/ap spawner �8- �7List of the all named particle spawners");
			p.sendMessage("�8> �c/ap set <name> <particle> �8- �7Set particle spawner on the block location");
			p.sendMessage("�8> �c/ap setlooking <name> <particle> �8- �7Set particle spawner on looking block location");
			p.sendMessage("�8> �c/ap info <spawnerName> �8- �7Show more information about the spawner");
			p.sendMessage("�8> �c/ap tp <spawnerName> �8- �7Teleport on the spawner location");
			p.sendMessage("�8> �c/ap delete <spawnerName> �8- �7Delete existed particle spawner from list");
		}

		p.sendMessage("�8> ");
		p.sendMessage("�8> �7Plugin website: �ahttps://www.spigotmc.org/resources/71929/");
		p.sendMessage("�8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}

}