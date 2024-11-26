/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package sereneseasons.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import sereneseasons.init.ModConfig;
import sereneseasons.season.SeasonHandler;
import sereneseasons.season.SeasonSavedData;
import sereneseasons.season.SeasonTime;
import sereneseasons.season.TemperatureHandler;

import java.util.Locale;

public class CommandGetTemperature
{
    static ArgumentBuilder<CommandSourceStack, ?> register()
    {
        return Commands.literal("temp")
                .executes(ctx -> {
                    Level world = ctx.getSource().getLevel();
                    return getTemp(ctx.getSource(), world);
                });
    }

    private static int getTemp(CommandSourceStack cs, Level world)
    {
        float temp = TemperatureHandler.getTemp(world);
        cs.sendSuccess(() -> Component.translatable("commands.sereneseasons.gettemperature.success", temp ), true);

        return 1;
    }
}
