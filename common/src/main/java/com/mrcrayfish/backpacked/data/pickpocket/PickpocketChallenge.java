package com.mrcrayfish.backpacked.data.pickpocket;

import com.mrcrayfish.backpacked.core.ModSyncedDataKeys;
import com.mrcrayfish.backpacked.platform.Services;
import com.mrcrayfish.backpacked.util.Serializable;
import com.mrcrayfish.framework.api.sync.IDataSerializer;
import com.mrcrayfish.framework.entity.sync.Updatable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PickpocketChallenge implements Serializable
{
    public static final IDataSerializer<PickpocketChallenge> SERIALIZER = new Serializer();

    private final Updatable updatable;
    private boolean initialized = false;
    private boolean backpack = false;
    private boolean spawnedLoot = false;
    private final Map<Player, Long> detectedPlayers = new HashMap<>();
    private final Map<UUID, Long> dislikedPlayers = new HashMap<>();

    public PickpocketChallenge(Updatable updatable)
    {
        this.updatable = updatable;
    }

    public boolean isInitialized()
    {
        return this.initialized;
    }

    public void setInitialized()
    {
        this.initialized = true;
        this.updatable.markDirty();
    }

    public void setBackpackEquipped(boolean equipped)
    {
        this.backpack = equipped;
        this.updatable.markDirty();
    }

    public boolean isBackpackEquipped()
    {
        return this.backpack;
    }

    public boolean isLootSpawned()
    {
        return this.spawnedLoot;
    }

    public void setLootSpawned()
    {
        this.spawnedLoot = true;
        this.updatable.markDirty();
    }

    public Map<Player, Long> getDetectedPlayers()
    {
        return this.detectedPlayers;
    }

    public boolean isDislikedPlayer(Player player)
    {
        return this.dislikedPlayers.containsKey(player.getUUID());
    }

    public void addDislikedPlayer(Player player, long time)
    {
        this.dislikedPlayers.put(player.getUUID(), time);
    }

    public Map<UUID, Long> getDislikedPlayers()
    {
        return this.dislikedPlayers;
    }

    @Override
    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Initialized", this.initialized);
        tag.putBoolean("EquippedBackpack", this.backpack);
        tag.putBoolean("SpawnedLoot", this.spawnedLoot);
        return tag;
    }

    @Override
    public void deserialize(CompoundTag tag)
    {
        this.initialized = tag.getBoolean("Initialized");
        this.backpack = tag.getBoolean("EquippedBackpack");
        this.spawnedLoot = tag.getBoolean("SpawnedLoot");
    }

    public static Optional<PickpocketChallenge> get(Entity entity)
    {
        if(entity instanceof WanderingTrader trader)
        {
            return Optional.ofNullable(ModSyncedDataKeys.PICKPOCKET_CHALLENGE.getValue(trader));
        }
        return Optional.empty();
    }

    private static class Serializer implements IDataSerializer<PickpocketChallenge>
    {
        @Override
        public void write(FriendlyByteBuf buf, PickpocketChallenge value)
        {
            buf.writeBoolean(value.initialized);
            buf.writeBoolean(value.backpack);
            buf.writeBoolean(value.spawnedLoot);
        }

        @Override
        @SuppressWarnings("removal")
        public PickpocketChallenge read(FriendlyByteBuf buf)
        {
            throw new UnsupportedOperationException("Call new method");
        }

        @Override
        public PickpocketChallenge read(Updatable updatable, FriendlyByteBuf buf)
        {
            PickpocketChallenge challenge = new PickpocketChallenge(updatable);
            challenge.initialized = buf.readBoolean();
            challenge.backpack = buf.readBoolean();
            challenge.spawnedLoot = buf.readBoolean();
            return challenge;
        }

        @Override
        public Tag write(PickpocketChallenge value)
        {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("Initialized", value.initialized);
            tag.putBoolean("EquippedBackpack", value.backpack);
            tag.putBoolean("SpawnedLoot", value.spawnedLoot);
            return tag;
        }

        @Override
        @SuppressWarnings("removal")
        public PickpocketChallenge read(Tag nbt)
        {
            throw new UnsupportedOperationException("Call new method");
        }

        @Override
        public PickpocketChallenge read(Updatable updatable, Tag nbt)
        {
            PickpocketChallenge challenge = new PickpocketChallenge(updatable);
            if(nbt instanceof CompoundTag tag)
            {
                challenge.initialized = tag.getBoolean("Initialized");
                challenge.backpack = tag.getBoolean("EquippedBackpack");
                challenge.spawnedLoot = tag.getBoolean("SpawnedLoot");
            }
            return challenge;
        }
    }
}
