package sereneseasons.season;

import glitchcore.event.TickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import static sereneseasons.season.BossBarHandler.getPlayerTemperatureSavedData;

public class PlayerStateHandler {
    private static final Logger log = LoggerFactory.getLogger(PlayerStateHandler.class);
    private static int tickcount;

    public static void onWorldTick(TickEvent.Level event) {
        PlayerTemperatureSavedData savedData = getPlayerTemperatureSavedData(event.getLevel());
        tickcount++;

        if (tickcount >= Integer.MAX_VALUE) {
            tickcount = 0;
        }

        if(tickcount % 110 == 0) {
            if (savedData != null && savedData.playerTemperature > 38.5 && savedData.playerTemperature < 40) {
                for (Player player : event.getLevel().players()) {
                    if (player != null) {
                        applyWeakness(player);
                        applyConfusion(player);
                    }
                }
            }

            else if (savedData != null && savedData.playerTemperature < 35 && savedData.playerTemperature > 33.5) {
                for (Player player : event.getLevel().players()) {
                    if (player != null) {
                        applySlowness(player);
                    }
                }
            }

            else if (savedData != null && savedData.playerTemperature > 40) {
                for (Player player : event.getLevel().players()) {
                    if (player != null) {
                        applyDamage(player);
                        applyConfusion(player);
                        applyWeakness(player);
                    }
                }
            }

            else if (savedData != null && savedData.playerTemperature < 33.5) {
                for (Player player : event.getLevel().players()) {
                    if (player != null) {
                        applyDamage(player);
                        applySlowness(player);
                    }
                }
            }
        }
    }
    private static void applyDamage(Player player) {
        if (player != null) {
            player.hurt(player.damageSources().hotFloor(), 1.0F);
        }
    }

    private static void applySlowness(Player player) {
        if (player != null) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1)); //addEffect(효과, 지속시간(틱), 강도(레벨))
        }
    }

    private  static void applyConfusion(Player player) {
        if (player != null) {
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 120, 1000));
        }
    }

    private static void applyWeakness(Player player) {
        if (player != null) {
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0));
        }
    }
}
