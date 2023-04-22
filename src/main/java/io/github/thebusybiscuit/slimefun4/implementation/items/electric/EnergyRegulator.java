package io.github.thebusybiscuit.slimefun4.implementation.items.electric;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.Cooldown;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.entity.Slime;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.HologramOwner;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNet;
import io.github.thebusybiscuit.slimefun4.implementation.handlers.SimpleBlockBreakHandler;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

/**
 * The {@link EnergyRegulator} is a special type of {@link SlimefunItem} which serves as the heart of every
 * {@link EnergyNet}.
 *
 * @author TheBusyBiscuit
 *
 * @see EnergyNet
 * @see EnergyNetComponent
 *
 */
public class EnergyRegulator extends SlimefunItem implements HologramOwner {

//    Cooldown cd = new Cooldown();
//    int cooldown = 21600;

    @ParametersAreNonnullByDefault
    public EnergyRegulator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        addItemHandler(onBreak());
    }

    @Nonnull
    private BlockBreakHandler onBreak() {
        return new SimpleBlockBreakHandler() {

            @Override
            public void onBlockBreak(@Nonnull Block b) {
                removeHologram(b);
            }
        };
    }

    @Nonnull
    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                updateHologram(e.getBlock(), "&7Connecting...");
            }

        };
    }

    @Override
    public void preRegister() {
        addItemHandler(onPlace());

        addItemHandler(new BlockTicker() {

            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                EnergyRegulator.this.tick(b);
            }
        });
    }

    private void tick(@Nonnull Block block) {
        EnergyNet network = EnergyNet.getNetworkFromLocationOrCreate(block.getLocation());
        network.tick(block);
        //turnOff(block);
    }

//    private void turnOff(Block block) {
//        if (!cd.onCooldown(block, cooldown))  return;
//        if (cd.getTimeLeft(block) > 0) return;
//        BlockStorage.clearBlockInfo(block);
//        Bukkit.getScheduler().runTask(Slimefun.instance(), () -> {
//                    removeHologram(block);
//                    replaceBlock(block, SlimefunItems.ENERGY_REGULATOR);
//                }
//        );
//    }

//    private static void replaceBlock(Block block, SlimefunItemStack stack) {
//        block.setType(Material.CHEST);
//        BlockState state = block.getState();
//        if (state instanceof Container) {
//            Container container = (Container) state;
//            if (Objects.isNull(container.getInventory())) return;
//            if (!container.getInventory().isEmpty()) return;
//            container.getInventory().addItem(stack);
//        }
//    }
}
