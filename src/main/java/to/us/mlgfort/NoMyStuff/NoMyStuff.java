package to.us.mlgfort.NoMyStuff;

import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityTNTPrimed;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import to.us.mlgfort.NoMyStuff.custom.UsefulNMSTNTPrimed;

/**
 * Created on 6/8/2017.
 *
 * "NMS"
 *
 * @author RoboMWM
 */
public class NoMyStuff extends JavaPlugin
{
    public void onEnable()
    {
        new TNTSourcer(this);
    }

    //"API" stuff here:


    public TNTPrimed spawnSourcedTNTPrimed(Location location, LivingEntity source)
    {
        CraftServer craftServer = ((CraftServer) Bukkit.getServer());
        CraftWorld craftWorld = (CraftWorld)location.getWorld();
        World nmsWorld = craftWorld.getHandle();
        EntityLiving nmsSource = ((CraftLivingEntity)source).getHandle();
        EntityTNTPrimed newTNT = new EntityTNTPrimed(nmsWorld, location.getX(), location.getY(), location.getZ(), nmsSource);
        UsefulNMSTNTPrimed extendedTNT = new UsefulNMSTNTPrimed(craftServer, newTNT);
        extendedTNT.setSource(source);
        nmsWorld.addEntity(extendedTNT.getHandle(), CreatureSpawnEvent.SpawnReason.CUSTOM);
        return extendedTNT;
    }
}
