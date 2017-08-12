package to.us.mlgfort.NoMyStuff;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;

/**
 * Created on 3/20/2017.
 *
 * Adds a source to plugin-generated TNT
 *
 * Current plugins supported: Crackshot
 *
 * Also adds classic TNT behavior
 *
 * @author RoboMWM
 */
public class TNTSourcer implements Listener
{
    NoMyStuff instance;

    public TNTSourcer(NoMyStuff mountainDewritoes)
    {
        this.instance = mountainDewritoes;
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    //Back in my day, punching TNT ignited TNT you youngens
    //Used with some inspiration from CTBPunchTNT, which seems to have disappeared?
    @EventHandler(ignoreCancelled = true)
    public void onPunch(BlockBreakEvent event)
    {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
            return;
        if (event.getBlock().getType() == Material.TNT)
        {
            Location tnt = event.getBlock().getLocation();
            tnt.setX(tnt.getX() + 0.5D);
            tnt.setY(tnt.getY() + 0.6D);
            tnt.setZ(tnt.getZ() + 0.5D);
            instance.spawnSourcedTNTPrimed(tnt, event.getPlayer());
            //entity.setMetadata("SOURCE", new FixedMetadataValue(instance, event.getPlayer()));
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
        }
    }

    @EventHandler
    private void onCrackShotExplosion(ExplosionPrimeEvent event)
    {
        if (event.getEntityType() != EntityType.PRIMED_TNT)
            return;
        if (!event.getEntity().hasMetadata("CS_Label") || event.getEntity().hasMetadata("MD_CS_Converted"))
            return;

        Player player = null;
        TNTPrimed tnt = (TNTPrimed)event.getEntity();
        if (tnt.hasMetadata("CS_pName"))
            player = instance.getServer().getPlayer(tnt.getMetadata("CS_pName").get(0).asString());
        TNTPrimed betterTNT = instance.spawnSourcedTNTPrimed(tnt.getLocation(), player);

        //Copy over metadata
        copyMetadata(tnt, betterTNT, "CS_Label");
        copyMetadata(tnt, betterTNT, "CS_potex");
        copyMetadata(tnt, betterTNT, "C4_Friendly");
        copyMetadata(tnt, betterTNT, "nullify");
        copyMetadata(tnt, betterTNT, "CS_nodam");
        copyMetadata(tnt, betterTNT, "CS_pName");
        copyMetadata(tnt, betterTNT, "CS_ffcheck");
        copyMetadata(tnt, betterTNT, "0wner_nodam");

        //Copy entity attributes
        betterTNT.setYield(tnt.getYield());
        betterTNT.setIsIncendiary(tnt.isIncendiary());
        betterTNT.setFuseTicks(tnt.getFuseTicks());

        //Add our own metadata so we don't think this is the original CS primed tnt, lol
        betterTNT.setMetadata("MD_CS_Converted", new FixedMetadataValue(instance, true));

        //Cancel and remove original
        event.setCancelled(true);
        event.getEntity().remove();
    }

    void copyMetadata(Metadatable from, Metadatable to, String key)
    {
        if (from.hasMetadata(key))
            to.setMetadata(key, new FixedMetadataValue(instance, from.getMetadata(key).get(0).value()));
    }
}
