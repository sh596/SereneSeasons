package sereneseasons.season;

import glitchcore.event.TickEvent;
import glitchcore.event.player.PlayerEvent;
import net.minecraft.server.level.ServerPlayer;
import sereneseasons.bossbar.BossBar;

public class BossBarHandler {

    public static float progress = 0;
    public static void onJoinLevel(PlayerEvent.JoinLevel event) {
        if(event.getPlayer() instanceof ServerPlayer serverPlayer){
            BossBar.showBossBar(serverPlayer);
        }
    }
    public static void onWorldTick(TickEvent.Level event){
        progress += 0.0001f;
        BossBar.updateBossBar(progress);

        //event.getLevel().tickRateManager().setTickRate(1);
    }
}
