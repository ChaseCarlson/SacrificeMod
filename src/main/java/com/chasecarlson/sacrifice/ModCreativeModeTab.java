package com.chasecarlson.sacrifice;

import com.chasecarlson.sacrifice.item.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;

public class ModCreativeModeTab {
	public static final CreativeModeTab SACRIFICE = new CreativeModeTab("sacrifice") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModItems.SOUL_TOKEN.get());
		}
	};

}
