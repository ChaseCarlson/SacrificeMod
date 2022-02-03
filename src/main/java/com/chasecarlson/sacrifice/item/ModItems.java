package com.chasecarlson.sacrifice.item;

import com.chasecarlson.sacrifice.Sacrifice;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Sacrifice.MODID);

	public static final RegistryObject<Item> SOUL_TOKEN = ITEMS.register("soul_token", () -> new ItemSoulToken());
	public static final RegistryObject<Item> ENTITY_SOUL = ITEMS.register("entity_soul", () -> new ItemEntitySoul());
	public static final RegistryObject<Item> SOUL_SUCKER = ITEMS.register("soul_sucker", () -> new ItemSoulSucker());
	public static final RegistryObject<Item> SOUL_SMELTER = ITEMS.register("soul_smelter", () -> new ItemSoulSmelter());

	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
