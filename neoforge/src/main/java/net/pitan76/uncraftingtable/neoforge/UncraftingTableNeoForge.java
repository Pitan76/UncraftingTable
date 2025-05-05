package net.pitan76.uncraftingtable.neoforge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.pitan76.uncraftingtable.Config;
import net.pitan76.uncraftingtable.UncraftingTable;
import net.pitan76.uncraftingtable.client.UncraftingTableClient;
import net.pitan76.uncraftingtable.neoforge.client.UncraftingTableNeoForgeClient;

@Mod(UncraftingTable.MOD_ID)
public class UncraftingTableNeoForge {
    private static final Logger LOGGER = LoggerFactory.getLogger("uncraftingtable76");

    public UncraftingTableNeoForge(ModContainer modContainer) {
        IEventBus bus = modContainer.getEventBus();

        Config.init(FMLPaths.CONFIGDIR.get().toFile());
        if (bus == null) throw new IllegalStateException("bus is null");

        new UncraftingTable();

        // 添加客户端事件监听
        bus.addListener(UncraftingTableNeoForgeClient::clientInit);
        
        // 添加菜单屏幕注册事件
        if (FMLEnvironment.dist.isClient()) {
            LOGGER.info("注册菜单屏幕事件监听器");
            bus.addListener(UncraftingTableNeoForgeClient::registerMenuScreens);
        }
        
        // 如果是客户端环境，直接初始化客户端
        if (FMLEnvironment.dist.isClient()) {
            LOGGER.info("尝试直接初始化客户端...");
            try {
                // 标记为NeoForge环境
                UncraftingTableClient.markAsNeoForge();
                UncraftingTableClient.init();
                LOGGER.info("客户端初始化完成");
            } catch (Exception e) {
                LOGGER.error("客户端初始化失败: {}", e.getMessage());
                LOGGER.error("异常堆栈", e);
            }
        }
    }
}