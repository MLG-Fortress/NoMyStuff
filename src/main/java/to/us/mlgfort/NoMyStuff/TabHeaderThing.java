package to.us.mlgfort.NoMyStuff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created on 6/17/2017.
 *
 * Bungee gets this, spigot doesn't, ok
 */

public class TabHeaderThing implements Listener
{
    JavaPlugin instance;

    public TabHeaderThing(JavaPlugin plugin)
    {
        instance = plugin;
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                sendTablist(event.getPlayer(), "MLG Fortress", "IP: mlgfort.us.to");
            }
        }.runTaskLater(instance, 20L);
    }

    public static Class<?> getNmsClass(String nmsClassName)
            throws ClassNotFoundException
    {
        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + nmsClassName);
    }

    public String getServerVersion()
    {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

    public void sendTablist(Player p, String msg, String msg2)
    {
        try
        {
            if (getServerVersion().equalsIgnoreCase("v1_12_R1"))
            {
                Object header = getNmsClass("ChatComponentText").getConstructor(new Class[] { String.class }).newInstance(new Object[] { ChatColor.translateAlternateColorCodes('&', msg) });
                Object footer = getNmsClass("ChatComponentText").getConstructor(new Class[] { String.class }).newInstance(new Object[] { ChatColor.translateAlternateColorCodes('&', msg2) });

                Object ppoplhf = getNmsClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[0]).newInstance(new Object[0]);

                Field fa = ppoplhf.getClass().getDeclaredField("a");
                fa.setAccessible(true);
                fa.set(ppoplhf, header);
                Field fb = ppoplhf.getClass().getDeclaredField("b");
                fb.setAccessible(true);
                fb.set(ppoplhf, footer);

                Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
                Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);

                pcon.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(pcon, new Object[] { ppoplhf });
            }
            //Idk why either, I'm no NMS expert but I guess I will have to become one soooooooooon
            else if (getServerVersion().equalsIgnoreCase("v1_12_R1"))
            {
                Object header = getNmsClass("ChatComponentText").getConstructor(new Class[] { String.class }).newInstance(new Object[] { ChatColor.translateAlternateColorCodes('&', msg) });
                Object footer = getNmsClass("ChatComponentText").getConstructor(new Class[] { String.class }).newInstance(new Object[] { ChatColor.translateAlternateColorCodes('&', msg2) });

                Object ppoplhf = getNmsClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[] { getNmsClass("IChatBaseComponent") }).newInstance(new Object[] { header });

                Field f = ppoplhf.getClass().getDeclaredField("b");
                f.setAccessible(true);
                f.set(ppoplhf, footer);

                Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
                Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);

                pcon.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(pcon, new Object[] { ppoplhf });
            }
            //ChatSerializer vs. ChatComponentText?
            else
            {
                Object header = getNmsClass("ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{'text': '" + msg + "'}" });
                Object footer = getNmsClass("ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{'text': '" + msg2 + "'}" });

                Object ppoplhf = getNmsClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[] { getNmsClass("IChatBaseComponent") }).newInstance(new Object[] { header });

                Field f = ppoplhf.getClass().getDeclaredField("b");
                f.setAccessible(true);
                f.set(ppoplhf, footer);

                Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
                Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);

                pcon.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(pcon, new Object[] { ppoplhf });
            }
        }
        catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException|ClassNotFoundException|InstantiationException|NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }
}
