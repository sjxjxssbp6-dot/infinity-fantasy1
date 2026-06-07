package cupcake.nikky.imperium.tower.handler;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.client.protocol.packet.PacketService;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.*;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.ailment.AilmentPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.*;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.craftbench.CraftBenchModifierList;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.craftbench.CraftBenchOpen;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.craftbench.CraftBenchSlotClick;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.craftbench.CraftBenchTryCraft;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.fragment.FragmentDataPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.guide.GuideOpenPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.incubator.IncubatorUpdatePacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.pandora.PandoraChestRemovePacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.pandora.PandoraChestSpawnPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.particle.*;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.settings.SettingsPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.trade.player.*;
import cupcake.nikky.imperium.tower.cooldown.CooldownService;
import cupcake.nikky.imperium.tower.data.*;
import cupcake.nikky.imperium.tower.data.pandora.PandoraStorage;
import cupcake.nikky.imperium.tower.data.prompt.cursor.CursorPromptStorage;
import cupcake.nikky.imperium.tower.handler.key.KeyHandler;
import cupcake.nikky.imperium.tower.hud.TeleportIndicator;
import cupcake.nikky.imperium.tower.hud.notification.NotificationData;
import cupcake.nikky.imperium.tower.particle.Particles;
import dev.xdark.clientapi.item.ItemStack;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.animation.Animation;
import ru.cristalix.enginex.renderer.contexts.Overlay;
import ru.cristalix.enginex.util.ButtonColor;
import ru.cristalix.enginex.util.Color;
import ru.cristalix.enginex.util.Palette;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ClientPacketHandler {

    public ClientPacketHandler(@NotNull final FantasyMod mod) {
        PacketService.getInstance().addListener(PurpleExplosionPacket.class, purpleExplosionPacket -> {
            Particles.PURPLE_EXPLOSION_EDGE.spawn(purpleExplosionPacket.getX(), purpleExplosionPacket.getY(), purpleExplosionPacket.getZ());
        });

        PacketService.getInstance().addListener(BinaParticle.class, tornadoParticle -> {
            Particles.TORNADO.spawn(new Color(tornadoParticle.getHexDecimalColor()), tornadoParticle.getDuration(), tornadoParticle.getX(), tornadoParticle.getY(), tornadoParticle.getZ());
        });

        PacketService.getInstance().addListener(EntityHealParticlePacket.class, entityHealParticlePacket -> Particles.HEAL.spawn(entityHealParticlePacket.getEntityId()));

        PacketService.getInstance().addListener(PandoraChestSpawnPacket.class, pandoraChestSpawnPacket -> {
            CooldownService.getInstance().getAndExtendCooldown("block_click", 1);
            PandoraStorage.getInstance().addChest(pandoraChestSpawnPacket.getUuid(), pandoraChestSpawnPacket.getX(), pandoraChestSpawnPacket.getY(), pandoraChestSpawnPacket.getZ());
        });

        PacketService.getInstance().addListener(PandoraChestRemovePacket.class, pandoraChestRemovePacket -> {
            val pandora = PandoraStorage.getInstance().findPandora(pandoraChestRemovePacket.getUuid());
            if (pandora == null) return;

            PandoraStorage.getInstance().invalidatePandora(pandora.getUuid());
        });

        PacketService.getInstance().addListener(PandoraChestOpenParticle.class, pandoraChestOpenParticle -> {
            Particles.PANDORA_CHEST_OPEN.spawn(pandoraChestOpenParticle.getX(), pandoraChestOpenParticle.getY(), pandoraChestOpenParticle.getZ());
        });

        PacketService.getInstance().addListener(PandoraChestSpawnParticle.class, pandoraChestSpawnParticle -> {
            Particles.PANDORA_CHEST_SPAWN.spawn(pandoraChestSpawnParticle.getX(), pandoraChestSpawnParticle.getY(), pandoraChestSpawnParticle.getZ());
        });

        PacketService.getInstance().addListener(CaseChangeAmountPacket.class, caseChangeAmountPacket -> {
            val caseContext = mod.getCaseContext();
            caseContext.getCaseAmountContext().setAmount(caseChangeAmountPacket.getAmount());
            caseContext.updateButtonTitle();
        });

        PacketService.getInstance().addListener(CaseUpdatePacket.class, caseUpdatePacket -> {
            val caseContext = mod.getCaseContext();
            caseContext.updateTitle(caseUpdatePacket.getCaseData().getTitle());
            caseContext.updatePrice(caseUpdatePacket.getCaseData().getPrice(), caseUpdatePacket.getCaseData().getCurrency());
            caseContext.fillItems(caseUpdatePacket.getCaseData().getCaseRewards().getExactItems());
        });

        PacketService.getInstance().addListener(CaseRollPacket.class, caseRollPacket -> {
            val caseContext = mod.getCaseContext();
            caseContext.roll(caseRollPacket.getWinId());
        });

        PacketService.getInstance().addListener(CaseOpenPacket.class, caseOpenPacket -> {
            val caseContext = mod.getCaseContext();
            caseContext.updateTitle(caseOpenPacket.getCaseData().getTitle());
            caseContext.updatePrice(caseOpenPacket.getCaseData().getPrice(), caseOpenPacket.getCaseData().getCurrency());
            caseContext.fillItems(caseOpenPacket.getCaseData().getCaseRewards().getExactItems());

            caseContext.open();
        });

        PacketService.getInstance().addListener(LocationDeathParticlePacket.class, locationDeathParticlePacket -> {
            switch (locationDeathParticlePacket.getDeathType()) {
                case SMALL_DARK_EXPLOSION: {
                    Particles.DARK_EXPLOSION_DEATH.spawn(locationDeathParticlePacket.getX(), locationDeathParticlePacket.getY(), locationDeathParticlePacket.getZ());
                    break;
                }
                case PURPLE_EXPLOSION: {
                    Particles.PURPLE_EXPLOSION_DEATH.spawn(locationDeathParticlePacket.getX(), locationDeathParticlePacket.getY(), locationDeathParticlePacket.getZ());
                    break;
                }
                case RED_LINE_UP: {
                    Particles.RED_LINE_UP_DEATH.spawn(locationDeathParticlePacket.getX(), locationDeathParticlePacket.getY(), locationDeathParticlePacket.getZ());
                    break;
                }
                default: break;
            }
        });

        PacketService.getInstance().addListener(AilmentPacket.class, ailmentPacket -> {
            switch (ailmentPacket.getType()) {
                case SHOCK: {
                    Particles.ENTITY_SHOCK.spawn(ailmentPacket.getEntityId(), ailmentPacket.getWhenReset());
                    break;
                }
                case IGNITE: {
                    Particles.ENTITY_IGNITE.spawn(ailmentPacket.getEntityId(), ailmentPacket.getWhenReset());
                    break;
                }
                case FREEZE: {
                    Particles.ENTITY_FREEZE.spawn(ailmentPacket.getEntityId(), ailmentPacket.getWhenReset());
                    break;
                }
            }
        });

        PacketService.getInstance().addListener(SettingsPacket.class, settingsPacket -> {
            SettingsStorage.getInstance().getSettings().putAll(settingsPacket.getSettings());
            mod.getSettingsContext().readCachedSettings();
        });

        PacketService.getInstance().addListener(EntitySparksParticlePacket.class, entitySparksParticlePacket -> {
            Particles.ENTITY_SPARKS.spawn(entitySparksParticlePacket.getEntityId(), entitySparksParticlePacket.getRadius());
        });

        PacketService.getInstance().addListener(EdgeZoneParticlePacket.class, edgeZoneParticlePacket -> {
            edgeZoneParticlePacket.getZones().forEach(zone -> Particles.EDGE_FRAGMENT_ZONE.addZone(zone));
        });

        PacketService.getInstance().addListener(EntityIceExplosionParticlePacket.class, entityIceExplosionParticlePacket -> {
            Particles.ENTITY_ICE_EXPLOSION.spawn(entityIceExplosionParticlePacket.getEntityId(), entityIceExplosionParticlePacket.getRadius());
        });

        PacketService.getInstance().addListener(EntityLevelUpParticlePacket.class, entityLevelUpParticlePacket -> {
            Particles.ENTITY_LEVEL_UP.spawn(entityLevelUpParticlePacket.getEntityId());
        });

        PacketService.getInstance().addListener(EntityBloodParticlePacket.class, entityBloodParticlePacket -> {
            switch (entityBloodParticlePacket.getBloodType()) {
                case DEFAULT: {
                    Particles.BASE_BLOOD.spawn(entityBloodParticlePacket.getEntityId());
                    break;
                }
                case WHITE: {
                    Particles.WHITE_BLOOD.spawn(entityBloodParticlePacket.getEntityId());
                    break;
                }
                case MULTI_COLORED: {
                    Particles.MULTI_COLORED_BLOOD.spawn(entityBloodParticlePacket.getEntityId());
                    break;
                }
                case HEART: {
                    Particles.HEART_BLOOD.spawn(entityBloodParticlePacket.getEntityId());
                    break;
                }
                default: {
                    Particles.BASE_BLOOD.spawn(entityBloodParticlePacket.getEntityId());
                }
            }
        });

        PacketService.getInstance().addListener(PremiumPacket.class, premiumPacket -> {
            UserMetadata.getInstance().setSubscribed(premiumPacket.isSubscribed());
            mod.getHotBarContext().updateLevel();
        });

        PacketService.getInstance().addListener(CraftBenchOpen.class, craftBenchOpen -> mod.getCraftBenchContext().open());

        PacketService.getInstance().addListener(CraftBenchTryCraft.class, craftBenchTryCraft -> {
            if (craftBenchTryCraft.isSuccess()) {
                mod.getCraftBenchContext().getInventoryContext().fillItems();
            }
        });

        PacketService.getInstance().addListener(CraftBenchSlotClick.class, craftBenchSlotClick -> {
            mod.getCraftBenchContext().click(craftBenchSlotClick.getSlot());
        });

        PacketService.getInstance().addListener(CraftBenchModifierList.class, craftBenchModifierList -> {
            CraftBenchStorage.getInstance().reset();

            CraftBenchStorage.getInstance().getPrefixes().putAll(craftBenchModifierList.getPrefix());
            CraftBenchStorage.getInstance().getSuffixes().putAll(craftBenchModifierList.getSuffix());

            mod.getCraftBenchContext().updateList();
        });

        PacketService.getInstance().addListener(IncubatorUpdatePacket.class, incubatorUpdatePacket -> {
            IncubatorStorage.getInstance().clear();
            IncubatorStorage.getInstance().getData().addAll(incubatorUpdatePacket.getProgress());
            mod.getIncubatorContext().update();
        });

        PacketService.getInstance().addListener(FragmentDataPacket.class, fragmentDataPacket -> {
            FragmentStorage.getInstance().setTime(fragmentDataPacket.getTime());
            FragmentStorage.getInstance().setRespawns(fragmentDataPacket.getRespawns());
        });

        PacketService.getInstance().addListener(GuideOpenPacket.class, guideOpenPacket -> mod.getGuideContext().open());

        PacketService.getInstance().addListener(TradeInventoryUpdate.class, tradeInventoryUpdate -> {
            mod.getTradePlayerContext().getOwnInventory().fillItems();
        });

        PacketService.getInstance().addListener(TradeParticipantConfirmPacket.class, tradeParticipantConfirmPacket -> {
            if (tradeParticipantConfirmPacket.isAccepted()) {
                mod.getTradePlayerContext().getParticipantInventory().setOutlineColor(Palette.GREEN);
            } else {
                mod.getTradePlayerContext().getParticipantInventory().setOutlineColor((Color) null);
            }
        });

        PacketService.getInstance().addListener(TradeTryConfirmPacket.class, tradeTryConfirmPacket -> {
            if (tradeTryConfirmPacket.isAccepted()) {
                val playerTradeContext = mod.getTradePlayerContext();
                if (!playerTradeContext.isConfirmed()) playerTradeContext.getConfirmButton().setButtonColor(ButtonColor.RED);
                else playerTradeContext.getConfirmButton().setButtonColor(ButtonColor.BLUE);

                playerTradeContext.setConfirmed(!playerTradeContext.isConfirmed());
                if (playerTradeContext.isConfirmed()) {
                    playerTradeContext.getConfirmButton().setText("Отменить");
                    mod.getTradePlayerContext().getOwnInventory().setOutlineColor(Palette.GREEN);
                } else {
                    playerTradeContext.getConfirmButton().setText("Подтвердить");
                    mod.getTradePlayerContext().getOwnInventory().setOutlineColor((Color) null);
                }
            }
        });

        PacketService.getInstance().addListener(TradeTryClickItemPacket.class, tradeTryClickItemPacket -> {
            if (tradeTryClickItemPacket.isAccepted()) {
                val currentState = mod.getTradePlayerContext().getOwnInventory().getCachedFlags().getOrDefault(tradeTryClickItemPacket.getSlot(), 0);
                mod.getTradePlayerContext().getOwnInventory().updateItemColor(tradeTryClickItemPacket.getSlot(), currentState == 0 ? 1 : 0);
            }
        });

        PacketService.getInstance().addListener(TradeUpdateCurrencyPacket.class, tradeUpdateCurrencyPacket -> {
            mod.getTradePlayerContext().updateParticipantEconomy(tradeUpdateCurrencyPacket.getGran(), tradeUpdateCurrencyPacket.getFirm());
        });

        PacketService.getInstance().addListener(TradeUpdateItemsPacket.class, tradeUpdateItemsPacket -> {
            val items = tradeUpdateItemsPacket.getItems().stream().map(ItemStack.class::cast).collect(Collectors.toList());
            mod.getTradePlayerContext().getParticipantInventory().fillItems(items);
        });

        PacketService.getInstance().addListener(TradePlayerNamePacket.class, tradePlayerNamePacket -> {
            mod.getTradePlayerContext().getTraderName().setValue(tradePlayerNamePacket.getName());
        });

        PacketService.getInstance().addListener(TradeStatePacket.class, tradeStatePacket -> {
            if (tradeStatePacket.isClosed())
                mod.getTradePlayerContext().close();
            else mod.getTradePlayerContext().open();
        });

        PacketService.getInstance().addListener(ManaBarPacket.class, manaBarPacket -> {
            UserMetadata.getInstance().setMaxMana(manaBarPacket.getMaxMana());
            UserMetadata.getInstance().setMana(manaBarPacket.getCurrentMana());
            UserMetadata.getInstance().setManaBlocked(manaBarPacket.isBlocked());
            mod.getHotBarContext().updateMana();
        });

        PacketService.getInstance().addListener(StoragePacket.class, storagePacket -> {
            PlayerInventoryStorage.getInstance().setStorageData(storagePacket.getStorageData());
            mod.getStoragePagesContext().update();
        });

        PacketService.getInstance().addListener(TeleportIndicatorPacket.class, teleportIndicatorPacket -> {
            if (teleportIndicatorPacket.getState() == TeleportIndicatorPacket.TeleportState.SKIP && mod.getCurrentIndicator() != null) {
                mod.getCurrentIndicator().stop(teleportIndicatorPacket.getMillis());
            } else {
                if (mod.getCurrentIndicator() != null) {
                    Animation.stop(mod.getCurrentIndicator());
                    Overlay.get().removeChild(mod.getCurrentIndicator());
                }

                if (teleportIndicatorPacket.getState() == TeleportIndicatorPacket.TeleportState.START)
                    new TeleportIndicator(mod, teleportIndicatorPacket.getMillis()).start(mod);
            }
        });

        PacketService.getInstance().addListener(LevelUpTitlePacket.class, levelUpTitlePacket -> {
            mod.getLevelUpTitle().show(levelUpTitlePacket.getLevel());
        });

        PacketService.getInstance().addListener(InventoryOverlayTextPacket.class, inventoryOverlayTextPacket -> {
            InventoryOverlayTextStorage.getInstance().text(inventoryOverlayTextPacket.getText());
            InventoryOverlayTextStorage.getInstance().seasonUrl(inventoryOverlayTextPacket.getSeasonUrl());
            mod.getItemPromptContext().update();
        });

        PacketService.getInstance().addListener(HealthBarPacket.class, healthBarPacket -> {
            UserMetadata.getInstance().setMaxHealth(healthBarPacket.getMaxHealth());
            UserMetadata.getInstance().setHealth(healthBarPacket.getCurrentHealth());
            mod.getHotBarContext().updateHealth();
        });

        PacketService.getInstance().addListener(TreePointIndicatorPacket.class, treePointIndicatorPacket -> mod.getTreeIndicator().setCount(treePointIndicatorPacket.getCount()));

        PacketService.getInstance().addListener(NotificationPacket.class, notificationPacket -> {
            notificationPacket.setText(Arrays.stream(notificationPacket.getText()).map(text -> {
                String result = text;

                for (KeyHandler value : KeyHandler.values()) {
                    result = result.replace("[" + value.name() + "]", "[" + value.container().keyChar() + "]");
                }

                return result;
            }).toArray(String[]::new));

            mod.getNotificationContext().addNext(new NotificationData(notificationPacket.getType(), notificationPacket.getText()));
        });

        PacketService.getInstance().addListener(CurrencyPacket.class, currencyPacket -> {
            EconomyStorage.getInstance().setGran(currencyPacket.getGran());
            EconomyStorage.getInstance().setMine(currencyPacket.getMines());
            EconomyStorage.getInstance().setSpecial(currencyPacket.getSpecial());
            EconomyStorage.getInstance().setPrivateSpecial(currencyPacket.getPrivateSpecial());
            EconomyStorage.getInstance().setFlower(currencyPacket.getFlowers());
        });

        PacketService.getInstance().addListener(CursorPromptPacket.class, cursorPromptPacket -> {
            CursorPromptStorage.getInstance().clear();

            CursorPromptStorage.getInstance().addAll(cursorPromptPacket.getData());
            mod.getCursorPromptContext().update();
        });

        PacketService.getInstance().addListener(LevelPacket.class, levelPacket -> {
            UserMetadata.getInstance().setLevel(levelPacket.getActualLevel());
            UserMetadata.getInstance().setLevelColor(levelPacket.getLevelColor());
            UserMetadata.getInstance().setCurrentExp(levelPacket.getActualExp());
            UserMetadata.getInstance().setMaxExp(levelPacket.getRequirementExt());
            UserMetadata.getInstance().setMultiplier(levelPacket.getExpMultiplier() / 100D);
            mod.getHotBarContext().updateLevel();
        });

        PacketService.getInstance().addListener(AttributePacket.class, attributePacket -> {
            attributePacket.getValues().forEach((attribute, value) -> AttributeStorage.getInstance().updateValue(attribute, value));
            mod.getAttributeContext().update();
        });

        PacketService.getInstance().addListener(AttackSpeedPacket.class, attackSpeedPacket -> {
            if (attackSpeedPacket.isRemove()) {
                mod.getAttackSpeedContext().disable();
            } else {
                mod.getAttackSpeedContext().enable(attackSpeedPacket.getDuration());
            }
        });

        PacketService.getInstance().addListener(BattleDurationPacket.class, battleDurationPacket ->
                mod.getHotBarContext().updateBattle(battleDurationPacket.getDuration(), !battleDurationPacket.isRemove()));
    }

}
