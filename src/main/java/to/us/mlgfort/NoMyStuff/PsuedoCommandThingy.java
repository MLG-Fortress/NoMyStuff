package to.us.mlgfort.NoMyStuff;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created on 7/31/2017.
 *
 * @author RoboMWM
 */
public class PsuedoCommandThingy implements Listener
{
    private Method muhHandle;
    private Field ping;

    JavaPlugin instance;

    PsuedoCommandThingy(JavaPlugin plugin) throws Exception
    {
        instance = plugin;
        muhHandle = ReflectionHandler.getMethod("CraftPlayer", ReflectionHandler.PackageType.CRAFTBUKKIT_ENTITY, "getHandle");
        ping = ReflectionHandler.getField("EntityPlayer", ReflectionHandler.PackageType.MINECRAFT_SERVER, true, "ping");
    }

    private int getPing(Player player)
    {
        try
        {
            return ping.getInt(muhHandle.invoke(player));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onWantPing(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        String[] message = event.getMessage().split(" ");
        String command = message[0].substring(1).toLowerCase();

        switch (command)
        {
            case "ping":
                sendMessage(player, "Your ping: " + getPing(player));
                if (message.length > 1)
                {
                    Player target = instance.getServer().getPlayer(message[1]);
                    if (target != null)
                    {
                        sendMessage(player, target.getDisplayName() + "'s ping: " + getPing(target));
                        event.setCancelled(true);
                    }
                }
        }
    }

    private void sendMessage(Player player, String message)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                player.sendMessage(message);
            }
        }.runTask(instance);
    }
}
