package com.chasecarlson.sacrifice.item;

import com.chasecarlson.sacrifice.ModCreativeModeTab;
import com.chasecarlson.sacrifice.Sacrifice;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemSoulSmelter extends Item {
	public ItemSoulSmelter() {
		super(new Properties()
				.tab(ModCreativeModeTab.SACRIFICE)
				.durability(120)
				.setNoRepair()
		);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level level = ctx.getLevel();
		Sacrifice.LOGGER.info("Soul Smelter USED");
		ItemStack soulSmelterItem = ctx.getItemInHand();
		BlockPos pos = ctx.getClickedPos();
		Block blockClicked = level.getBlockState(pos).getBlock();
		Item blockItem = blockClicked.asItem();
		ItemStack blockItemStack = new ItemStack(blockItem);
		Sacrifice.LOGGER.info("Soul Smelter used on "+blockClicked.getRegistryName());


		ItemStack smeltedItemStack = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(blockItemStack), level)
				.map(SmeltingRecipe::getResultItem)
				.filter(itemStack -> !itemStack.isEmpty())
				.map(itemStack -> ItemHandlerHelper.copyStackWithSize(itemStack, blockItemStack.getCount() * itemStack.getCount()))
				.orElse(null);
		if (smeltedItemStack != null) {
			if (soulSmelterItem.getDamageValue() < this.getMaxDamage(soulSmelterItem)) {
				if (!level.isClientSide()) {
					level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
					level.updateNeighborsAt(pos, Blocks.AIR);
					ItemEntity itemOnGround = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, smeltedItemStack, 0, 0.2, 0);
					level.addFreshEntity(itemOnGround);
					soulSmelterItem.setDamageValue(soulSmelterItem.getDamageValue() + 1);
					Sacrifice.LOGGER.info("Soul Smelter functions");
					return InteractionResult.SUCCESS;
				}
				level.playSound(null, pos, new SoundEvent(new ResourceLocation("block.fire.ambient")), SoundSource.AMBIENT, 1.0f, 2.0f);
			}
			else {
				level.playSound(null, pos, new SoundEvent(new ResourceLocation("block.dispenser.fail")), SoundSource.AMBIENT, 1.0f, 1.5f);
			}
		}
		return InteractionResult.PASS;
	}
}