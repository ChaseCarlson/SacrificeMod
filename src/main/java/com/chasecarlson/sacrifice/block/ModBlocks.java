package com.chasecarlson.sacrifice.block;

import com.chasecarlson.sacrifice.ModCreativeModeTab;
import com.chasecarlson.sacrifice.Sacrifice;
import com.chasecarlson.sacrifice.item.ItemSoulToken;
import com.chasecarlson.sacrifice.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Sacrifice.MODID);

	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> blockSupplier, CreativeModeTab tab) {
		RegistryObject<T> blockRegistryObject = BLOCKS.register(name, blockSupplier);
		registerBlockItem(name, blockRegistryObject, tab);
		return blockRegistryObject;
	}

	private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
		ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
	}

	public static final RegistryObject<BlockAltar> ALTAR = registerBlock("altar", () -> new BlockAltar(), ModCreativeModeTab.SACRIFICE);
	public static final RegistryObject<Block> SOUL_BLOCK = registerBlock("soul_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(5.0f).requiresCorrectToolForDrops()), ModCreativeModeTab.SACRIFICE);

	public static void register(IEventBus eventBus) {
		BLOCKS.register(eventBus);
	};
}
