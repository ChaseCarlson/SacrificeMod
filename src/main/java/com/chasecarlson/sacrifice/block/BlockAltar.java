package com.chasecarlson.sacrifice.block;

import com.chasecarlson.sacrifice.item.ModItems;
import com.chasecarlson.sacrifice.util.AdvancementUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeaconBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BlockAltar extends Block {
	public static final List<EntityType<?extends Animal>> SacrificeWhitelist = Arrays.asList(EntityType.COW, EntityType.PIG, EntityType.SHEEP, EntityType.RABBIT, EntityType.HORSE, EntityType.TURTLE);

	public BlockAltar() {
		super(BlockBehaviour.Properties
				.of(Material.WOOD)
				.strength(2.0f)
				.sound(SoundType.WOOD)
				.noOcclusion()
				.lightLevel(blockState -> {
					return 16;
				})
		);
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (!level.isClientSide()) {
			ServerPlayer sp = (ServerPlayer)player;
			int boxSize = 5;
			AABB entitySearchAABB = AABB.of(BoundingBox.fromCorners(
					new Vec3i(pos.getX()-boxSize, pos.getY(), pos.getZ()-boxSize),
					new Vec3i(pos.getX()+boxSize, pos.getY(), pos.getZ()+boxSize)
			));
			List<Entity> entitiesOnAltar = level.getEntities(null, entitySearchAABB);
			int sacrificedAnimalsCount = 0;
			for (Entity entity : entitiesOnAltar) {
				if (SacrificeWhitelist.contains(entity.getType())) {
					entity.setNoGravity(true);
					entity.setPos(new Vec3(pos.getX()+ 0.5, pos.getY() + 5, pos.getZ() + 0.5));
					entity.addTag("sacrifice");
					sacrificeEntity(entity);
					sacrificedAnimalsCount++;
				}
			}
			if (sacrificedAnimalsCount > 0) {
				StringBuilder builder = new StringBuilder();
				builder.append("You just sacrificed ");
				builder.append(sacrificedAnimalsCount);
				builder.append(" animal");
				if (sacrificedAnimalsCount > 1) {
					builder.append("s");
				}
				builder.append(" and will receive an equal number of Soul Tokens.");
				AdvancementUtil.grantAdvancement(sp, "advancements.sacrifice.sacrifice_animal");
				player.sendMessage(new TextComponent(builder.toString()).withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
			}
			else {
				player.sendMessage(new TextComponent("Interact with the altar whilst an animal lies upon it to receive Soul Tokens.").withStyle(ChatFormatting.RED), Util.NIL_UUID);
				return InteractionResult.FAIL;
			}
		}
		return InteractionResult.SUCCESS;
	}

	private void sacrificeEntity(Entity entity) {
		Level level = entity.getLevel();
		Vec3 ePos = entity.position();
		level.addFreshEntity(new ItemEntity(level, ePos.x, ePos.y, ePos.z, new ItemStack(ModItems.SOUL_TOKEN.get())));
		entity.remove(Entity.RemovalReason.KILLED);
	}
}
