package com.chasecarlson.sacrifice.item;

import com.chasecarlson.sacrifice.ModCreativeModeTab;
import com.chasecarlson.sacrifice.Sacrifice;
import com.chasecarlson.sacrifice.util.ItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
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
		Sacrifice.LOGGER.debug("Soul Smelter USED");
		ItemStack soulSmelterItem = ctx.getItemInHand();
		BlockPos pos = ctx.getClickedPos();
		Block blockClicked = level.getBlockState(pos).getBlock();
		Item blockItem = blockClicked.asItem();
		ItemStack blockItemStack = new ItemStack(blockItem);
		Sacrifice.LOGGER.debug("Soul Smelter used on "+blockClicked.getRegistryName());

		if (!ItemUtil.isDurabilityRemaining(soulSmelterItem)) {
			level.playSound(null, pos, new SoundEvent(new ResourceLocation("block.dispenser.fail")), SoundSource.AMBIENT, 1.0f, 1.5f);
			return InteractionResult.PASS;
		}

		ItemStack smeltedItemStack = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(blockItemStack), level)
				.map(SmeltingRecipe::getResultItem)
				.filter(itemStack -> !itemStack.isEmpty())
				.map(itemStack -> ItemHandlerHelper.copyStackWithSize(itemStack, blockItemStack.getCount() * itemStack.getCount()))
				.orElse(null);

		if (smeltedItemStack == null)
			return InteractionResult.PASS;

		level.playSound(null, pos, new SoundEvent(new ResourceLocation("block.fire.ambient")), SoundSource.AMBIENT, 1.0f, 2.0f);
		ItemUtil.damage(soulSmelterItem);
		if (!level.isClientSide()) {
			if (smeltedItemStack.getItem() instanceof BlockItem)
			{
				BlockItem smeltedItemBlock = (BlockItem) smeltedItemStack.getItem();
				Block smeltedBlock = smeltedItemBlock.getBlock();
				level.setBlock(pos, smeltedBlock.defaultBlockState(), 3);
				level.updateNeighborsAt(pos, smeltedBlock);
			}
			else
			{
				level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
				level.updateNeighborsAt(pos, Blocks.AIR);
				ItemEntity itemOnGround = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, smeltedItemStack, 0, 0.2, 0);
				level.addFreshEntity(itemOnGround);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
		if (!ItemUtil.isDurabilityRemaining(stack))
		{
			return InteractionResult.PASS;
		}
		if (entity.isOnFire()) {
			return InteractionResult.PASS;
		}

		ItemUtil.damage(stack);
		entity.getLevel().playSound(null, entity.blockPosition(), new SoundEvent(new ResourceLocation("entity.blaze.shoot")), SoundSource.AMBIENT, 1.0f, 1.5f);
		if (!entity.getLevel().isClientSide()) {
			entity.setSecondsOnFire(10);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
