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
import sereneseasons.item.CucumberItem;

import sereneseasons.item.*;


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
//        SSItems.FOOD = register(func, "food", new FoodItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
//                .nutrition(6)
//                .saturationModifier(1.2f)
//                .build()
//        )));
        SSItems.TOMATO_SEED = register(func, "tomato_seed", new BlockItem(SSBlocks.TOMATO_CROP, new Item.Properties().stacksTo(64)));

        SSItems.SPINACH_SEED = register(func, "spinach_seed", new BlockItem(SSBlocks.SPINACH_CROP, new Item.Properties().stacksTo(64)));

        SSItems.GARLIC_SEED = register(func, "garlic_seed", new BlockItem(SSBlocks.GARLIC_CROP, new Item.Properties().stacksTo(64)));

        SSItems.CUCUMBER_SEED = register(func, "cucumber_seed", new BlockItem(SSBlocks.CUCUMBER_CROP, new Item.Properties().stacksTo(64)));

        SSItems.RADISH_SEED = register(func, "radish_seed", new BlockItem(SSBlocks.RADISH_CROP, new Item.Properties().stacksTo(64)));

        SSItems.CORN_SEED = register(func, "corn_seed", new BlockItem(SSBlocks.CORN_CROP, new Item.Properties().stacksTo(64)));

        SSItems.CHIVES_SEED = register(func, "chives_seed", new BlockItem(SSBlocks.CHIVES_CROP, new Item.Properties().stacksTo(64)));

        SSItems.CABBAGE_SEED = register(func, "cabbage_seed", new BlockItem(SSBlocks.CABBAGE_CROP, new Item.Properties().stacksTo(64)));

        SSItems.NAPACABBAGE_SEED = register(func, "napacabbage_seed", new BlockItem(SSBlocks.NAPACABBAGE_CROP, new Item.Properties().stacksTo(64)));

        SSItems.CHILIPEPPER_SEED = register(func, "chilipepper_seed", new BlockItem(SSBlocks.CHILIPEPPER_CROP, new Item.Properties().stacksTo(64)));

        SSItems.CUCUMBER = register(func, "cucumber", new CucumberItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(1.2f)
                .build()
        )));
        GARLIC = register(func, "garlic", new GarlicItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(1.2f)
                .build()
        )));

        SSItems.CABBAGE =  register(func, "cabbage", new CabbageItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(1.2f)
                .build()
        )));
        NAPACABBAGE = register(func, "napacabbage", new NapaCabbageItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(1.2f)
                .build()
        )));

        SSItems.CORN = register(func, "corn", new CornItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(1.2f)
                .build()
        )));
        SPINACH = register(func, "spinach", new SpinachItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(1.2f)
                .build()
        )));

        SSItems.TOMATO = register(func, "tomato", new TomatoItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(1.2f)
                .build())));
        RADISH = register(func, "radish", new RadishItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(1.2f)
                .build()
        )));
        CHILIPEPPER = register(func, "chilipepper", new ChilipepperItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(1.2f)
                .build()
        )));
        CHIVES = register(func, "chives", new ChivesItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()

                .nutrition(6)
                .saturationModifier(1.2f)
                .build()
        )));

        CUCUMBERSOUP = register(func, "cucumbersoup", new CucumberSoupItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(1.2f)
                .build()
        )));
        GIMCHI = register(func, "gimchi", new GimchiItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(1.2f)
                .build()
        )));

        CAPRESE = register(func, "caprese", new CapreseItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(1.2f)
                .build()
        )));
        SALAD = register(func, "salad", new SaladItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(1.2f)
                .build()
        )));
        POPCORN = register(func, "popcorn", new PopcornItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(1.2f)
                .build()
        )));
        CUCUMBER_GIMCHI = register(func, "cucumber_gimchi", new CucumberGimchiItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(1.2f)
                .build()
        )));


        FRENCHFRIES = register(func, "frenchfries", new FrenchFries(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(1.2f)
                .build()
        )));
        PASTA = register(func, "pasta", new PastaItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
                .nutrition(6)
                .saturationModifier(1.2f)
                .build()
        )));
        PIZZA = register(func, "pizza", new PizzaItem(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder()
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
