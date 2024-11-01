package sereneseasons.init;

import glitchcore.util.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import sereneseasons.api.SSBlocks;
import sereneseasons.api.SSItems;
import sereneseasons.core.SereneSeasons;
import sereneseasons.item.CalendarItem;
import sereneseasons.item.FoodItem;

import java.util.function.BiConsumer;

import static sereneseasons.api.SSItems.*;

public class ModItems
{
    public static void setup(BiConsumer<ResourceLocation, Item> func)
    {
        registerItems(func);
        registerBlockItems(func);

        if (Environment.isClient())
        {
            ModClient.registerItemProperties();
        }
    }

    public static void registerItems(BiConsumer<ResourceLocation, Item> func)
    {
    	// SS Creative Tab Icon
        SSItems.SS_ICON = register(func, "ss_icon", new Item(new Item.Properties()));

        // Main Items
        SSItems.CALENDAR = register(func, "calendar", new CalendarItem(new Item.Properties().stacksTo(1)));
        SSItems.FOOD = register(func, "food", new FoodItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(1.2f)
                .build()
        )));
    }

    public static void registerBlockItems(BiConsumer<ResourceLocation, Item> func)
    {
        SEASON_SENSOR = register(func, "season_sensor", new BlockItem(SSBlocks.SEASON_SENSOR, new Item.Properties()));
    }

    private static Item register(BiConsumer<ResourceLocation, Item> func, String name, Item item)
    {
        func.accept(ResourceLocation.fromNamespaceAndPath(SereneSeasons.MOD_ID, name), item);
        return item;
    }
}
