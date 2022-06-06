package com.chasecarlson.sacrifice.util;

import net.minecraft.world.item.ItemStack;

public class ItemUtil {
	public static void damage(ItemStack item)
	{
		item.setDamageValue(item.getDamageValue() + 1);
	}

	public static boolean isDurabilityRemaining(ItemStack item)
	{
		return item.getDamageValue() < item.getMaxDamage();
	}
}
