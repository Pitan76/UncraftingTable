package net.pitan76.uncraftingtable.neoforge.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.pitan76.uncraftingtable.UncraftingTable;
import net.pitan76.uncraftingtable.client.UncraftingScreen;
import net.pitan76.uncraftingtable.client.UncraftingTableClient;

public class UncraftingTableNeoForgeClient {
    private static final Logger LOGGER = LoggerFactory.getLogger("uncraftingtable76");
    
    public static void clientInit(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("正在通过事件初始化客户端...");
            try {
                UncraftingTableClient.init();
                LOGGER.info("客户端初始化完成");
            } catch (Exception e) {
                LOGGER.error("客户端初始化失败: {}", e.getMessage());
                LOGGER.error("异常堆栈", e);
            }
        });
    }
    
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        LOGGER.info("注册NeoForge菜单屏幕 - 菜单类型: {}:uncraftingtable", UncraftingTable.MOD_ID);
        try {
            event.register(
                UncraftingTable.UNCRAFTING_TABLE_MENU.get(), 
                UncraftingScreen::new
            );
            LOGGER.info("NeoForge菜单屏幕注册成功");
        } catch (Exception e) {
            LOGGER.error("NeoForge菜单屏幕注册失败: {}", e.getMessage());
            LOGGER.error("异常堆栈", e);
        }
    }
}
