package com.chasecarlson.sacrifice.util;

import com.chasecarlson.sacrifice.Sacrifice;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Criterion;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class AdvancementUtil {
	public static void grantAdvancement(ServerPlayer player, String advancementName)
	{
		if (player == null) {
			Sacrifice.LOGGER.info("Player is NULL");
			return;
		}
		MinecraftServer server = player.getServer();
		if (server == null) {
			Sacrifice.LOGGER.info("Server is NULL");
			return;
		}
		Advancement advancement = server.getAdvancements().getAdvancement(new ResourceLocation(advancementName));
		if (advancement == null) {
			Sacrifice.LOGGER.info("Advancement is NULL");
			return;
		}
		AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
		for (String critierion : progress.getRemainingCriteria()) {
			player.getAdvancements().award(advancement, critierion);
		}
	}
}
