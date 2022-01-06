package com.mrcrayfish.backpacked.inventory.container.slot;

import com.mojang.datafixers.util.Pair;
import com.mrcrayfish.backpacked.Backpacked;
import com.mrcrayfish.backpacked.Config;
import com.mrcrayfish.backpacked.item.BackpackItem;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class InventoryBackpackSlot extends Slot
{
    public InventoryBackpackSlot(Container inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon()
    {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, Backpacked.EMPTY_BACKPACK_SLOT);
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return stack.getItem() instanceof BackpackItem;
    }

    @Override
    public boolean mayPickup(Player player)
    {
        if(!Config.SERVER.lockBackpackIntoSlot.get())
            return true;
        CompoundTag tag = this.getItem().getTag();
        return tag == null || tag.getList("Items", Tag.TAG_COMPOUND).isEmpty();
    }
}
