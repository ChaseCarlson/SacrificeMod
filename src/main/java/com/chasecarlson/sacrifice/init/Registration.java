package com.chasecarlson.sacrifice.init;

import com.chasecarlson.sacrifice.block.ModBlocks;
import com.chasecarlson.sacrifice.item.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;

public class Registration {

	public static void register(IEventBus eventBus) {
		ModBlocks.register(eventBus);
		ModItems.register(eventBus);
	}

}
