package com.gec.killdeathadvice.mod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class DeathNoticeMod implements ModInitializer {
    private boolean isProcessingDamage = false;
    private final Map<Entity, Float> lastHealthMap = new HashMap<>();
    private int tickCounter = 0;
    private final Set<Entity> processingEntities = new HashSet<>();

    @Override
    public void onInitialize() {
        // 死亡通知监听器
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) entity;
                double x = player.getX();
                double y = player.getY();
                double z = player.getZ();

                Box box = new Box(
                        x - 16, y - 16, z - 16,
                        x + 16, y + 16, z + 16
                );

                List<Entity> nearbyEntities = player.getWorld().getOtherEntities(player, box);

                // 添加调试信息
                System.out.println("检测到玩家死亡: " + player.getName().getString());
                System.out.println("找到周围实体数量: " + nearbyEntities.size());

                StringBuilder message = new StringBuilder();
                message.append("玩家 ").append(player.getName().getString()).append(" 已死亡\n");
                message.append("周围16格内的生物：\n");

                boolean foundAnyEntities = false;
                for (Entity nearbyEntity : nearbyEntities) {
                    if (nearbyEntity instanceof LivingEntity) {
                        String entityName = nearbyEntity.getName().getString();
                        message.append("- ").append(entityName).append("\n");
                        System.out.println("发现生物: " + entityName); // 调试信息
                        foundAnyEntities = true;
                    }
                }

                if (!foundAnyEntities) {
                    message.append("- 周围没有发现生物\n");
                }

                String finalMessage = message.toString();
                System.out.println("准备发送消息:\n" + finalMessage); // 调试信息

                for (ServerPlayerEntity serverPlayer : player.getServer().getPlayerManager().getPlayerList()) {
                    serverPlayer.sendMessage(Text.literal(finalMessage));
                }
            }
        });

        // 实体加入世界时设置显示名称
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof LivingEntity && !(entity instanceof PlayerEntity)) {
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.setCustomNameVisible(true);
                updateEntityDisplayName(livingEntity);
                lastHealthMap.put(entity, livingEntity.getHealth());
            }
        });

        // 实体受伤时更新显示名称
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (isProcessingDamage) {
                return true;
            }

            try {
                isProcessingDamage = true;

                if (entity instanceof LivingEntity && !(entity instanceof PlayerEntity)) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    // 处理一击必杀逻辑
                    Entity attacker = source.getSource();
                    if (attacker instanceof PlayerEntity) {
                        if (entity.isAlive()) {
                            float maxHealth = livingEntity.getMaxHealth();
                            entity.damage(source, maxHealth * 2);
                            updateEntityDisplayName(livingEntity);
                            lastHealthMap.put(entity, livingEntity.getHealth());
                        }
                        return false;
                    }
                }
                return true;
            } finally {
                isProcessingDamage = false;
            }
        });

        // 修改定期检查实体血量变化的逻辑
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            tickCounter++;
            if (tickCounter < 20) {
                return;
            }
            tickCounter = 0;

            for (ServerWorld world : server.getWorlds()) {
                for (ServerPlayerEntity player : world.getPlayers()) {
                    checkAndUpdateEntities(world, player);
                }
            }

            // 清理已经不存在的实体
            lastHealthMap.keySet().removeIf(entity ->
                    !entity.isAlive() ||
                            entity.isRemoved() ||
                            processingEntities.contains(entity)
            );
        });
    }

    private void updateEntityDisplayName(LivingEntity entity) {
        if (processingEntities.contains(entity)) {
            return;
        }

        try {
            processingEntities.add(entity);

            if (!entity.isAlive()) {
                entity.setCustomName(null);
                entity.setCustomNameVisible(false);
                return;
            }

            // 完全移除当前的显示名称
            entity.setCustomName(null);
            entity.setCustomNameVisible(false);

            // 等待一个tick
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // 忽略中断异常
            }

            float currentHealth = entity.getHealth();
            float maxHealth = entity.getMaxHealth();
            String entityName = entity.getName().getString();

            // 使用更简单的显示格式
            String displayText = entityName + " §c❤" +
                    Math.round(currentHealth) + "/" + Math.round(maxHealth);

            // 设置新的显示名称
            entity.setCustomName(Text.literal(displayText));
            entity.setCustomNameVisible(true);

        } finally {
            processingEntities.remove(entity);
        }
    }

    private void checkAndUpdateEntities(ServerWorld world, ServerPlayerEntity player) {
        // 修改搜索范围为16格
        List<Entity> nearbyEntities = world.getOtherEntities(player,
                player.getBoundingBox().expand(16.0), // 从32.0改为16.0
                entity -> entity instanceof LivingEntity &&
                        !(entity instanceof PlayerEntity) &&
                        !processingEntities.contains(entity)
        );

        // 获取超出范围的实体并隐藏它们的名称
        List<Entity> farEntities = world.getOtherEntities(player,
                player.getBoundingBox().expand(64.0), // 搜索更大范围来找到需要隐藏名称的实体
                entity -> entity instanceof LivingEntity &&
                        !(entity instanceof PlayerEntity) &&
                        !nearbyEntities.contains(entity) // 排除已经在近处的实体
        );

        // 隐藏超出范围实体的名称
        for (Entity entity : farEntities) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.setCustomName(null);
                livingEntity.setCustomNameVisible(false);
                lastHealthMap.remove(entity);
            }
        }

        // 更新范围内实体的显示
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity livingEntity) {
                float currentHealth = livingEntity.getHealth();
                Float lastHealth = lastHealthMap.get(entity);

                if (lastHealth == null || Math.abs(lastHealth - currentHealth) > 0.5f) {
                    updateEntityDisplayName(livingEntity);
                    lastHealthMap.put(entity, currentHealth);
                }
            }
        }
    }
}