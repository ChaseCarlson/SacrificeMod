package com.chasecarlson.sacrifice.item;

import com.chasecarlson.sacrifice.ModCreativeModeTab;
import com.chasecarlson.sacrifice.Sacrifice;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

public class ItemSoulSucker extends Item {
	public static final String ENTITY_TAG = "entity";
	public static final String ENTITY_NAME_TAG = "entity_name";

	public ItemSoulSucker() {
		super(new Properties()
				.tab(ModCreativeModeTab.SACRIFICE)
				.durability(20)
			);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
		Level level = player.getLevel();
		if (!level.isClientSide()) {
			CompoundTag entityNBT = entity.serializeNBT();
			ItemStack entitySoul = new ItemStack(ModItems.ENTITY_SOUL.get());
			CompoundTag tag = entitySoul.getOrCreateTag();
			tag.put(ENTITY_TAG, entityNBT);
			tag.putString(ENTITY_NAME_TAG, Component.Serializer.toJson(entity.getName()));
			player.getInventory().add(entitySoul);
			entity.remove(Entity.RemovalReason.DISCARDED);
			return InteractionResult.SUCCESS;
		}
		else
		{
			// Clientside Effects
		}
		return InteractionResult.PASS;
	}

	public static EntityType getEntityTypeFromRegistryID(String id) {
		return ForgeRegistries.ENTITIES.getValue(new ResourceLocation(id));
	}
}
