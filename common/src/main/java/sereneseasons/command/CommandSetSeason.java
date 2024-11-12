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
import sereneseasons.api.season.Season;
import sereneseasons.season.SeasonHandler;
import sereneseasons.season.SeasonSavedData;
import sereneseasons.season.SeasonTime;

import java.util.Locale;

public class CommandSetSeason
{
    static ArgumentBuilder<CommandSourceStack, ?> register()
    {
        return Commands.literal("set")
            .then(Commands.argument("season", SeasonArgument.season())
            .executes(ctx -> {
                Level world = ctx.getSource().getLevel();
                return setSeason(ctx.getSource(), world, SeasonArgument.getSeason(ctx, "season"));
            }));
    }


    private static int setSeason(CommandSourceStack cs, Level world, Season.SubSeason season)
    {
        if (season != null)
        {
            SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);
            seasonData.seasonCycleTicks = SeasonTime.ZERO.getSubSeasonDuration() * season.ordinal();
            seasonData.setDirty();
            SeasonHandler.sendSeasonUpdate(world);
            cs.sendSuccess(() -> Component.translatable("commands.sereneseasons.setseason.success", Component.translatable("desc.sereneseasons."+ season.toString().toLowerCase(Locale.ROOT))), true);
        }
        else
        {
            cs.sendFailure(Component.translatable("commands.sereneseasons.setseason.fail", Component.translatable("desc.sereneseasons."+ season.toString().toLowerCase(Locale.ROOT))));
        }

        return 1;
    }

}
