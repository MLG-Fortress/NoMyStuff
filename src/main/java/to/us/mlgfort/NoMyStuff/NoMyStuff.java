package to.us.mlgfort.NoMyStuff;

//import net.minecraft.server.v1_13_R1.EntityLiving;
//import net.minecraft.server.v1_13_R1.EntityTNTPrimed;
//import net.minecraft.server.v1_13_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
//import org.bukkit.craftbukkit.v1_13_R1.CraftServer;
//import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
//import org.bukkit.craftbukkit.v1_13_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
//import to.us.mlgfort.NoMyStuff.custom.UsefulNMSTNTPrimed;

/**
 * Created on 6/8/2017.
 *
 * "NMS"
 *
 * @author RoboMWM
 */
public class NoMyStuff extends JavaPlugin implements Listener
{
    public void onEnable()
    {
        try
        {
            new TNTSourcer(this);
        }
        catch (Throwable rock)
        {
            getLogger().severe("We need an NMS update! TNT source tracking disabled.");
        }

        //new TabHeaderThing(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        return false;
    }



    //"API" stuff here:

//    public TNTPrimed spawnSourcedTNTPrimed(Location location, LivingEntity source)
//    {
//        CraftServer craftServer = ((CraftServer) Bukkit.getServer());
//        CraftWorld craftWorld = (CraftWorld)location.getWorld();
//        World nmsWorld = craftWorld.getHandle();
//        EntityLiving nmsSource = ((CraftLivingEntity)source).getHandle();
//        EntityTNTPrimed newTNT = new EntityTNTPrimed(nmsWorld, location.getX(), location.getY(), location.getZ(), nmsSource);
//        UsefulNMSTNTPrimed extendedTNT = new UsefulNMSTNTPrimed(craftServer, newTNT);
//        extendedTNT.setSource(source);
//        nmsWorld.addEntity(extendedTNT.getHandle(), CreatureSpawnEvent.SpawnReason.CUSTOM);
//        return extendedTNT;
//    }

    public TNTPrimed spawnSourcedTNTPrimed(TNTPrimed tnt, LivingEntity source)
    {
        try
        {
            final Class<? extends TNTPrimed> tntClass = tnt.getClass();
            final Method getHandle = tntClass.getMethod("getHandle");
            final Object handle = getHandle.invoke(tntClass);
            Field f = handle.getClass().getDeclaredField("source");
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            f.set(handle, source);
            return tnt;
        }
        catch (Exception e)
        {
            getLogger().severe("Update reflection.");
            e.printStackTrace();
            return tnt;
        }
    }
}
