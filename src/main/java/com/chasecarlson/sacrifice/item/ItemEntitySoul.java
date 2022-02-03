package com.chasecarlson.sacrifice.item;

import com.chasecarlson.sacrifice.Sacrifice;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.SummonCommand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

public class ItemEntitySoul extends Item {
	public ItemEntitySoul() {
		super(new Properties()
				.stacksTo(1)
			);
	}

	@Override
	public boolean isFoil(ItemStack stack)
	{
		return true;
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level level = ctx.getLevel();
		ItemStack item = ctx.getItemInHand();
		BlockPos clickedPos = ctx.getClickedPos();
		CompoundTag entityTag = item.getTagElement(ItemSoulSucker.ENTITY_TAG);
		if (!level.isClientSide()) {
			if (entityTag != null) {
				//String entityResourceLocationString = entityTag.getString("id");
				//final EntityType savedEntityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityResourceLocationString));
				//Optional<Entity> entityOptional = savedEntityType.create(entityTag, level);
				Optional<Entity> entityOptional = EntityType.create(entityTag, level);
				Sacrifice.LOGGER.info(entityTag.toString());
				if (entityOptional.isPresent()) {
					Entity entity = entityOptional.get();
					entity.setPos(clickedPos.getX() + 0.5, clickedPos.getY() + 1, clickedPos.getZ() + 0.5);
					level.addFreshEntity(entity);
				}
			}
			item.shrink(1);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		Tag entityNameTag = tag.get(ItemSoulSucker.ENTITY_NAME_TAG);
		if (entityNameTag != null) {
			return new TextComponent(Component.Serializer.fromJson(entityNameTag.getAsString()).getString() + "'s Soul");
		}
		return super.getName(stack);
	}
}
