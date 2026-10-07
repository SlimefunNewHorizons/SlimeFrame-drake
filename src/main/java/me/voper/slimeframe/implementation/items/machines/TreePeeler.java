package me.voper.slimeframe.implementation.items.machines;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;

import me.voper.slimeframe.implementation.items.abstracts.AbstractProcessorMachine;

public class TreePeeler extends AbstractProcessorMachine implements RecipeDisplayItem {

    private static final Map<Material, Material> STRIPPED_WOODS_LOGS = new EnumMap<>(Material.class);

    static {
        for (Material m : Material.values()) {
            if (!m.isItem() || m.isLegacy()) continue;
            String name = m.name();
            if (!name.startsWith("STRIPPED_") && (name.endsWith("_LOG") || name.endsWith("_WOOD") || name.endsWith("_STEM") || name.endsWith("_HYPHAE"))) {
                Material stripped = Material.getMaterial("STRIPPED_" + name);
                if (stripped != null) {
                    STRIPPED_WOODS_LOGS.put(m, stripped);
                }
            }
        }
    }

    private static final int TIME = 5;

    public TreePeeler(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void postRegister() {
        STRIPPED_WOODS_LOGS
                .forEach((wood, stripped) -> registerRecipe(TIME, new ItemStack(wood), new ItemStack(stripped)));
    }

    @Override
    protected ItemStack getProgressBar() {
        return new ItemStack(Material.IRON_AXE);
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return STRIPPED_WOODS_LOGS.entrySet().stream()
                .flatMap(entry -> Stream.of(new ItemStack(entry.getKey()), new ItemStack(entry.getValue())))
                .toList();
    }
}
