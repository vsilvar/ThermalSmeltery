package us.drullk.thermalsmeltery.common.plugins.te;

import java.util.Map;

import us.drullk.thermalsmeltery.ThermalSmeltery;
import us.drullk.thermalsmeltery.common.blocks.MachineHelper;
import us.drullk.thermalsmeltery.common.blocks.ModBlocks;
import us.drullk.thermalsmeltery.common.gui.GuiHandler;
import us.drullk.thermalsmeltery.common.items.ModItems;
import us.drullk.thermalsmeltery.common.network.PacketThermalSmeltery;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import mantle.pulsar.pulse.Handler;
import mantle.pulsar.pulse.Pulse;
import mantle.utils.ItemMetaWrapper;

import us.drullk.thermalsmeltery.common.core.handler.TSmeltConfig;
import us.drullk.thermalsmeltery.common.lib.LibMisc;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(LibMisc.MOD_ID)
@Pulse(id = "TSmelt TE", description = "Thermal Expansion Integration", modsRequired = "ThermalExpansion")
public class TSmeltTE
{
    //public static final GuiHandler guiHandler = new GuiHandler();
    ItemStack nullifier;
    
    @Handler
    public void preInit (FMLPostInitializationEvent event) {
        Map<ItemMetaWrapper, FluidStack> smelteryMap = tconstruct.library.crafting.Smeltery.getSmeltingList();
        Map<ItemMetaWrapper, Integer> tempMap = tconstruct.library.crafting.Smeltery.getTemperatureList();

        for (Map.Entry<ItemMetaWrapper, FluidStack> entry : smelteryMap.entrySet()) {
            ItemStack input = new ItemStack(entry.getKey().item, 1, entry.getKey().meta);
            int energy = tempMap.get(entry.getKey()) * TSmeltConfig.multiplier;
            TE4Helper.addCrucibleRecipe(energy, input, entry.getValue());
        }

        MachineHelper.initialize();
        ModBlocks.initialize();
        PacketThermalSmeltery.initialize();

        nullifier = new ItemStack(GameRegistry.findItem("ThermalExpansion", "Device"), 1, 5);
        TE4Helper.addPulverizerRecipe(20000, nullifier, new ItemStack(ModItems.Tool_Mod_Void, 1, 0));
    }

    @Handler
    public void init (FMLInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(ThermalSmeltery.instance, new GuiHandler());
    }

    @Handler
    public void postInit (FMLPostInitializationEvent event)
    {
        ModBlocks.postInit();
    }
}