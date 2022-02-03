package com.chasecarlson.sacrifice.item;

import com.chasecarlson.sacrifice.ModCreativeModeTab;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ItemSoulToken extends Item {

	public ItemSoulToken() {
		super(new Properties()
				.tab(ModCreativeModeTab.SACRIFICE)
				.durability(0)
				.stacksTo(64)
		);

	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
		Level level = player.getLevel();
		if (!stack.isEmpty()) {
			if (stack.is(ModItems.SOUL_TOKEN.get())) {
				if (entity instanceof Zombie || entity instanceof Skeleton) {
					BlockPos pos = entity.blockPosition();
					Vec3 v3p = entity.position();
					if (!level.isClientSide()) {
						Villager villager = EntityType.VILLAGER.create(level);
						villager.setPos(entity.position());
						level.addFreshEntity(villager);
						entity.remove(Entity.RemovalReason.DISCARDED);
						stack.shrink(1);
						return InteractionResult.CONSUME;
					}
					level.playSound(null, pos, new SoundEvent(new ResourceLocation("entity.lightning_bolt.thunder")), SoundSource.AMBIENT, 1.0f, 1.0f);
					for (int i=0; i<250; i++) {
						level.addParticle(
								ParticleTypes.CLOUD,
								v3p.x + (level.random.nextFloat() - level.random.nextFloat()) * 2,
								v3p.y + (level.random.nextFloat() - level.random.nextFloat()) * 2,
								v3p.z + (level.random.nextFloat() - level.random.nextFloat()) * 2,
								0,
								0,
								0
						);
					}
				}
			}
		}
		return InteractionResult.PASS;
	}
}
