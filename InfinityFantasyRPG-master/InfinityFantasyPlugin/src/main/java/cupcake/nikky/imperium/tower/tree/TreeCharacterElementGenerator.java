package cupcake.nikky.imperium.tower.tree;

import cupcake.nikky.imperium.tower.tree.builder.element.TreeElement;
import cupcake.nikky.imperium.tower.tree.builder.element.TreeElementBuilder;
import cupcake.nikky.imperium.tower.tree.builder.position.Segment;
import cupcake.nikky.imperium.tower.tree.element.exact.character.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.util.Color;

public class TreeCharacterElementGenerator {

    public Int2ObjectMap<TreeElement> generate(@NotNull final Int2ObjectMap<Segment> segments) {
        return TreeElementBuilder.of(segments, PointType.CHARACTER, NodeType.SMALL_CHARACTER)
                .with(0, TreeIcon.HEALTH, new AddHealthElementProcess(15), new IncreasePhysicalDamageHitElementProcess(10))

                .with(2, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(15))
                .with(3, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(15))
                .with(4, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(15))

                .with(6, TreeIcon.HEALTH, new AddHealthElementProcess(30))
                .with(7, TreeIcon.HEALTH, new AddHealthElementProcess(30))
                .with(8, TreeIcon.HEALTH, new AddHealthElementProcess(30))

                .with(9, TreeIcon.HEALTH, new AddHealthElementProcess(10))

                .with(46, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(15))
                .with(10, TreeIcon.CRITICAL, new IncreaseCriticalChanceElementProcess(10))
                .with(11, TreeIcon.CRITICAL, new IncreaseCriticalChanceElementProcess(10))
                .with(12, TreeIcon.CRITICAL, NodeType.BIG_CHARACTER, new IncreaseCriticalChanceElementProcess(25))

                .with(47, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(15))
                .with(17, TreeIcon.DAMAGE, new AddAccuracyElementProcess(2))
                .with(18, TreeIcon.DAMAGE, new AddAccuracyElementProcess(2))
                .with(19, TreeIcon.DAMAGE, NodeType.BIG_CHARACTER, new AddAccuracyElementProcess(6))

                .with(24, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(8))
                .with(25, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(8))
                .with(26, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(8))
                .with(27, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(8))
                .with(28, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(8), new IncreaseElementalDamageElementProcess(5))

                .with(1, TreeIcon.MANA, NodeType.BIG_CHARACTER, new IncreaseManaElementProcess(10))
                .with(5, TreeIcon.HEALTH, NodeType.BIG_CHARACTER, new IncreaseHealthElementProcess(10))

                .with(29, TreeIcon.ARMOR, new AddArmorElementProcess(2), new AddHealthElementProcess(60))
                .with(30, TreeIcon.ARMOR, new AddArmorElementProcess(2))
                .with(31, TreeIcon.ARMOR, new AddArmorElementProcess(2))
                .with(32, TreeIcon.ARMOR, NodeType.BIG_CHARACTER, new AddArmorElementProcess(6))
                .with(33, TreeIcon.HEALTH, new IncreaseHealthElementProcess(5))
                .with(34, TreeIcon.HEALTH, new IncreaseHealthElementProcess(5))
                .with(35, TreeIcon.HEALTH, NodeType.BIG_CHARACTER, new IncreaseHealthElementProcess(15))

                .with(36, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(5))
                .with(37, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(5))
                .with(38, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(5))

                .with(49, TreeIcon.CRITICAL, new IncreaseCriticalChanceElementProcess(10), new IncreaseCriticalDamageElementProcess(10))
                .with(50, TreeIcon.CRITICAL, new IncreaseCriticalChanceElementProcess(10))
                .with(51, TreeIcon.CRITICAL, new IncreaseCriticalChanceElementProcess(10))
                .with(52, TreeIcon.CRITICAL, NodeType.BIG_CHARACTER, new IncreaseCriticalChanceElementProcess(25))
                .with(53, TreeIcon.CRITICAL, new IncreaseCriticalDamageElementProcess(10))
                .with(54, TreeIcon.CRITICAL, new IncreaseCriticalDamageElementProcess(10))
                .with(55, TreeIcon.CRITICAL, NodeType.BIG_CHARACTER, new IncreaseCriticalDamageElementProcess(25))

                .with(56, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(5))
                .with(57, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(5))
                .with(58, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(5))
                .with(59, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(5))
                .with(60, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(5))

                .with(61, TreeIcon.DAMAGE, NodeType.BIG_CHARACTER, new IncreaseElementalDamageElementProcess(15), new IncreaseCriticalChanceElementProcess(25))
                .with(62, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(5))
                .with(63, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(5))
                .with(64, TreeIcon.DAMAGE, NodeType.BIG_CHARACTER, new IncreaseElementalDamageElementProcess(15))

                .with(65, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(5))
                .with(66, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(5))

                .with(69, TreeIcon.MANA, new IncreaseManaElementProcess(5))
                .with(70, TreeIcon.MANA, new IncreaseManaElementProcess(5))
                .with(71, TreeIcon.MANA, new IncreaseManaElementProcess(5))
                .with(72, TreeIcon.MANA, NodeType.BIG_CHARACTER, new IncreaseManaElementProcess(10), new IncreaseManaReservationEfficiency(6))
                .with(73, TreeIcon.MANA, new IncreaseManaElementProcess(5))
                .with(74, TreeIcon.MANA, new IncreaseManaElementProcess(5))
                .with(75, TreeIcon.MANA, NodeType.BIG_CHARACTER, new IncreaseManaElementProcess(10), new IncreaseManaReservationEfficiency(6))

                .with(76, TreeIcon.FIRE, new IncreaseFireDamageHitElementProcess(15))
                .with(77, TreeIcon.FIRE, new IncreaseFireDamageHitElementProcess(15))
                .with(78, TreeIcon.FIRE, new IncreaseFireDamageHitElementProcess(15))
                .with(79, TreeIcon.FIRE, NodeType.BIG_CHARACTER, new IncreaseFireDamageHitElementProcess(25))
                .with(80, TreeIcon.FIRE, new IncreaseFireDamageHitElementProcess(15))
                .with(81, TreeIcon.FIRE, new IncreaseFireDamageHitElementProcess(15))
                .with(82, TreeIcon.FIRE, NodeType.BIG_CHARACTER, new IncreaseFireDamageHitElementProcess(25))

                .with(83, TreeIcon.LIGHTNING, new IncreaseLightningDamageHitElementProcess(15))
                .with(84, TreeIcon.LIGHTNING, new IncreaseLightningDamageHitElementProcess(15))
                .with(85, TreeIcon.LIGHTNING, new IncreaseLightningDamageHitElementProcess(15))
                .with(86, TreeIcon.LIGHTNING, NodeType.BIG_CHARACTER, new IncreaseLightningDamageHitElementProcess(25))
                .with(87, TreeIcon.LIGHTNING, new IncreaseLightningDamageHitElementProcess(15))
                .with(88, TreeIcon.LIGHTNING, new IncreaseLightningDamageHitElementProcess(15))
                .with(89, TreeIcon.LIGHTNING, NodeType.BIG_CHARACTER, new IncreaseLightningDamageHitElementProcess(25))

                .with(93, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(5))
                .with(94, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(5))
                .with(95, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(5))

                .with(96, TreeIcon.DAMAGE, new AddSwordDamageElementProcess(10), new AddBattleAxeDamageElementProcess(10))
                .with(97, TreeIcon.SWORD, new AddSwordDamageElementProcess(10))
                .with(98, TreeIcon.SWORD, new AddSwordDamageElementProcess(10))
                .with(99, TreeIcon.SWORD, NodeType.BIG_CHARACTER, new AddSwordDamageElementProcess(15))
                .with(100, TreeIcon.BATTLE_AXE, new AddBattleAxeDamageElementProcess(10))
                .with(101, TreeIcon.BATTLE_AXE, new AddBattleAxeDamageElementProcess(10))
                .with(102, TreeIcon.BATTLE_AXE, NodeType.BIG_CHARACTER, new AddBattleAxeDamageElementProcess(15))

                .with(106, TreeIcon.DAMAGE, new AddDaggerDamageElementProcess(10), new AddSpearDamageElementProcess(10))
                .with(107, TreeIcon.DAGGER, new AddDaggerDamageElementProcess(10))
                .with(108, TreeIcon.DAGGER, new AddDaggerDamageElementProcess(10))
                .with(109, TreeIcon.DAGGER, NodeType.BIG_CHARACTER, new AddDaggerDamageElementProcess(15))
                .with(110, TreeIcon.SPEAR, new AddSpearDamageElementProcess(10))
                .with(111, TreeIcon.SPEAR, new AddSpearDamageElementProcess(10))
                .with(112, TreeIcon.SPEAR, NodeType.BIG_CHARACTER, new AddSpearDamageElementProcess(15))

                .with(113, TreeIcon.MOVEMENT_SPEED, new AddMovementSpeedElementProcess(5))
                .with(114, TreeIcon.MOVEMENT_SPEED, new AddMovementSpeedElementProcess(5))
                .with(115, TreeIcon.MOVEMENT_SPEED, new AddMovementSpeedElementProcess(5))
                .with(116, TreeIcon.MOVEMENT_SPEED, NodeType.BIG_CHARACTER, new AddMovementSpeedElementProcess(10))
                .with(117, TreeIcon.MOVEMENT_SPEED, new AddMovementSpeedElementProcess(5))
                .with(118, TreeIcon.MOVEMENT_SPEED, new AddMovementSpeedElementProcess(5))
                .with(119, TreeIcon.MOVEMENT_SPEED, NodeType.BIG_CHARACTER, new AddMovementSpeedElementProcess(10))

                .with(120, TreeIcon.ATTACK_SPEED, new IncreaseAttackSpeedElementProcess(3))
                .with(121, TreeIcon.ATTACK_SPEED, new IncreaseAttackSpeedElementProcess(3))
                .with(122, TreeIcon.ATTACK_SPEED, new IncreaseAttackSpeedElementProcess(3))
                .with(123, TreeIcon.ATTACK_SPEED, NodeType.BIG_CHARACTER, new IncreaseAttackSpeedElementProcess(9))
                .with(124, TreeIcon.ATTACK_SPEED, new IncreaseAttackSpeedElementProcess(3))
                .with(125, TreeIcon.ATTACK_SPEED, new IncreaseAttackSpeedElementProcess(3))
                .with(126, TreeIcon.ATTACK_SPEED, NodeType.BIG_CHARACTER, new IncreaseAttackSpeedElementProcess(9))

                .with(127, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(8))
                .with(128, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(8))
                .with(129, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(8))

                .with(140, TreeIcon.CRITICAL, new IncreaseCriticalChanceElementProcess(10), new IncreaseCriticalDamageElementProcess(10))
                .with(141, TreeIcon.CRITICAL, new IncreaseCriticalChanceElementProcess(10))
                .with(142, TreeIcon.CRITICAL, new IncreaseCriticalChanceElementProcess(10))
                .with(143, TreeIcon.CRITICAL, NodeType.BIG_CHARACTER, new IncreaseCriticalChanceElementProcess(25))
                .with(144, TreeIcon.CRITICAL, new IncreaseCriticalDamageElementProcess(10))
                .with(145, TreeIcon.CRITICAL, new IncreaseCriticalDamageElementProcess(10))
                .with(146, TreeIcon.CRITICAL, NodeType.BIG_CHARACTER, new IncreaseCriticalDamageElementProcess(25))

                .with(147, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(8))
                .with(148, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(8))
                .with(149, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(8))

                .with(150, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(8))
                .with(151, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(8))
                .with(152, TreeIcon.DAMAGE, NodeType.BIG_CHARACTER, new IncreasePhysicalDamageHitElementProcess(20), new IncreaseCriticalChanceElementProcess(25))

                .with(153, TreeIcon.COLD, new IncreaseColdDamageHitElementProcess(15))
                .with(154, TreeIcon.COLD, new IncreaseColdDamageHitElementProcess(15))
                .with(155, TreeIcon.COLD, new IncreaseColdDamageHitElementProcess(15))
                .with(156, TreeIcon.COLD, NodeType.BIG_CHARACTER, new IncreaseColdDamageHitElementProcess(25))
                .with(157, TreeIcon.COLD, new IncreaseColdDamageHitElementProcess(15))
                .with(158, TreeIcon.COLD, new IncreaseColdDamageHitElementProcess(15))
                .with(159, TreeIcon.COLD, NodeType.BIG_CHARACTER, new IncreaseColdDamageHitElementProcess(25))

                .with(160, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(10))
                .with(161, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(10))
                .with(162, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(10))
                .with(163, TreeIcon.DAMAGE, NodeType.BIG_CHARACTER, new IncreaseElementalDamageElementProcess(20))
                .with(164, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(10))
                .with(165, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(10))
                .with(166, TreeIcon.DAMAGE, NodeType.BIG_CHARACTER, new IncreaseElementalDamageElementProcess(20))

                .with(167, TreeIcon.MANA, new IncreaseManaElementProcess(5))
                .with(168, TreeIcon.MANA, new IncreaseManaElementProcess(5))
                .with(169, TreeIcon.MANA, NodeType.BIG_CHARACTER, new IncreaseManaElementProcess(15), new IncreaseManaReservationEfficiency(15))

                .with(170, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(8))
                .with(171, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(8))

                .with(174, TreeIcon.CHAOS, new IncreaseChaosDamageHitElementProcess(15))
                .with(175, TreeIcon.CHAOS, new IncreaseChaosDamageHitElementProcess(15))
                .with(176, TreeIcon.CHAOS, new IncreaseChaosDamageHitElementProcess(15))
                .with(177, TreeIcon.CHAOS, NodeType.BIG_CHARACTER, new IncreaseChaosDamageHitElementProcess(25))
                .with(178, TreeIcon.CHAOS, new IncreaseChaosDamageHitElementProcess(15))
                .with(179, TreeIcon.CHAOS, new IncreaseChaosDamageHitElementProcess(15))
                .with(180, TreeIcon.CHAOS, NodeType.BIG_CHARACTER, new IncreaseChaosDamageHitElementProcess(25))

                .with(184, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(8))
                .with(185, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(8))
                .with(186, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(8))

                .with(187, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(15), new IncreaseElementalDamageElementProcess(10))
                .with(188, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(15))
                .with(189, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(15))
                .with(190, TreeIcon.DAMAGE, NodeType.BIG_CHARACTER, new IncreasePhysicalDamageHitElementProcess(25))
                .with(191, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(10))
                .with(192, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(10))
                .with(193, TreeIcon.DAMAGE, NodeType.BIG_CHARACTER, new IncreaseElementalDamageElementProcess(20))

                .with(197, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(15), new IncreaseElementalDamageElementProcess(10))
                .with(198, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(15))
                .with(199, TreeIcon.DAMAGE, new IncreasePhysicalDamageHitElementProcess(15))
                .with(200, TreeIcon.DAMAGE, NodeType.BIG_CHARACTER, new IncreasePhysicalDamageHitElementProcess(25))
                .with(201, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(10))
                .with(202, TreeIcon.DAMAGE, new IncreaseElementalDamageElementProcess(10))
                .with(203, TreeIcon.DAMAGE, NodeType.BIG_CHARACTER, new IncreaseElementalDamageElementProcess(20))

                .with(204, TreeIcon.MOVEMENT_SPEED, new AddMovementSpeedElementProcess(5))
                .with(205, TreeIcon.MOVEMENT_SPEED, new AddMovementSpeedElementProcess(5))
                .with(206, TreeIcon.MOVEMENT_SPEED, new AddMovementSpeedElementProcess(5))
                .with(207, TreeIcon.MOVEMENT_SPEED, NodeType.BIG_CHARACTER, new AddMovementSpeedElementProcess(10))
                .with(208, TreeIcon.MOVEMENT_SPEED, new AddMovementSpeedElementProcess(5))
                .with(209, TreeIcon.MOVEMENT_SPEED, new AddMovementSpeedElementProcess(5))
                .with(210, TreeIcon.MOVEMENT_SPEED, NodeType.BIG_CHARACTER, new AddMovementSpeedElementProcess(10))

                .with(211, TreeIcon.ATTACK_SPEED, new IncreaseAttackSpeedElementProcess(3))
                .with(212, TreeIcon.ATTACK_SPEED, new IncreaseAttackSpeedElementProcess(3))
                .with(213, TreeIcon.ATTACK_SPEED, new IncreaseAttackSpeedElementProcess(3))
                .with(214, TreeIcon.ATTACK_SPEED, NodeType.BIG_CHARACTER, new IncreaseAttackSpeedElementProcess(9))
                .with(215, TreeIcon.ATTACK_SPEED, new IncreaseAttackSpeedElementProcess(3))
                .with(216, TreeIcon.ATTACK_SPEED, new IncreaseAttackSpeedElementProcess(3))
                .with(217, TreeIcon.ATTACK_SPEED, NodeType.BIG_CHARACTER, new IncreaseAttackSpeedElementProcess(9))

                .with(218, TreeIcon.ARMOR, new AddArmorElementProcess(2), new IncreaseHealthElementProcess(5))
                .with(219, TreeIcon.ARMOR, new AddArmorElementProcess(2), new IncreaseHealthElementProcess(5))
                .with(220, TreeIcon.ARMOR, new AddArmorElementProcess(2), new IncreaseHealthElementProcess(5))

                .with(221, TreeIcon.POISON, new AddChancePoisonElementProcess(20))
                .with(222, TreeIcon.POISON, new AddChancePoisonElementProcess(20))
                .with(223, TreeIcon.POISON, new AddChancePoisonElementProcess(20))

                .with(224, TreeIcon.BLEEDING, new AddChanceBleedElementProcess(20))
                .with(225, TreeIcon.BLEEDING, new AddChanceBleedElementProcess(20))
                .with(226, TreeIcon.BLEEDING, new AddChanceBleedElementProcess(20))

                .with(227, TreeIcon.DAMAGE_OVER_TIME, new IncreaseDamageOverTimeElementProcess(15))
                .with(228, TreeIcon.DAMAGE_OVER_TIME, new IncreaseDamageOverTimeElementProcess(15))
                .with(229, TreeIcon.DAMAGE_OVER_TIME, new IncreaseDamageOverTimeElementProcess(15))
                .with(230, TreeIcon.DAMAGE_OVER_TIME, NodeType.BIG_CHARACTER, new IncreaseDamageOverTimeElementProcess(25))
                .with(231, TreeIcon.DAMAGE_OVER_TIME, new IncreaseDamageOverTimeElementProcess(15))
                .with(232, TreeIcon.DAMAGE_OVER_TIME, new IncreaseDamageOverTimeElementProcess(15))
                .with(233, TreeIcon.DAMAGE_OVER_TIME, NodeType.BIG_CHARACTER, new IncreaseDamageOverTimeElementProcess(25))

                .with(234, TreeIcon.DAMAGE_OVER_TIME, new IncreaseDamageOverTimeElementProcess(15))
                .with(235, TreeIcon.DAMAGE_OVER_TIME, new IncreaseDamageOverTimeElementProcess(15))
                .with(236, TreeIcon.DAMAGE_OVER_TIME, new IncreaseDamageOverTimeElementProcess(15))
                .with(237, TreeIcon.DAMAGE_OVER_TIME, NodeType.BIG_CHARACTER, new IncreaseDamageOverTimeElementProcess(25))
                .with(238, TreeIcon.DAMAGE_OVER_TIME, new IncreaseDamageOverTimeElementProcess(15))
                .with(239, TreeIcon.DAMAGE_OVER_TIME, new IncreaseDamageOverTimeElementProcess(15))
                .with(240, TreeIcon.DAMAGE_OVER_TIME, NodeType.BIG_CHARACTER, new IncreaseDamageOverTimeElementProcess(25))

                .with(241, TreeIcon.IGNITE, new AddChanceToIgniteOnHitElementProcess(5), new IncreaseIgniteDamageElementProcess(10))
                .with(242, TreeIcon.IGNITE, new IncreaseIgniteDamageElementProcess(10))
                .with(243, TreeIcon.IGNITE, new IncreaseIgniteDamageElementProcess(10))
                .with(244, TreeIcon.IGNITE, NodeType.BIG_CHARACTER, new IncreaseIgniteDamageElementProcess(15))
                .with(245, TreeIcon.IGNITE, new AddChanceToIgniteOnHitElementProcess(5))
                .with(246, TreeIcon.IGNITE, new AddChanceToIgniteOnHitElementProcess(5))
                .with(247, TreeIcon.IGNITE, NodeType.BIG_CHARACTER, new AddChanceToIgniteOnHitElementProcess(10))

                .with(248, TreeIcon.SHOCK, new AddChanceToShockOnHitElementProcess(5), new IncreaseShockEffectElementProcess(10))
                .with(249, TreeIcon.SHOCK, new IncreaseShockEffectElementProcess(10))
                .with(250, TreeIcon.SHOCK, new IncreaseShockEffectElementProcess(10))
                .with(251, TreeIcon.SHOCK, NodeType.BIG_CHARACTER, new IncreaseShockEffectElementProcess(25))
                .with(252, TreeIcon.SHOCK, new AddChanceToShockOnHitElementProcess(5))
                .with(253, TreeIcon.SHOCK, new AddChanceToShockOnHitElementProcess(5))
                .with(254, TreeIcon.SHOCK, NodeType.BIG_CHARACTER, new AddChanceToShockOnHitElementProcess(15))

                .with(255, TreeIcon.FREEZE, new AddChanceToFreezeOnHitElementProcess(5), new IncreaseFreezeDurationElementProcess(15))
                .with(256, TreeIcon.FREEZE, new IncreaseFreezeDurationElementProcess(15))
                .with(257, TreeIcon.FREEZE, new IncreaseFreezeDurationElementProcess(15))
                .with(258, TreeIcon.FREEZE, NodeType.BIG_CHARACTER, new IncreaseFreezeDurationElementProcess(50))
                .with(259, TreeIcon.FREEZE, new AddChanceToFreezeOnHitElementProcess(5))
                .with(260, TreeIcon.FREEZE, new AddChanceToFreezeOnHitElementProcess(5))
                .with(261, TreeIcon.FREEZE, NodeType.BIG_CHARACTER, new AddChanceToFreezeOnHitElementProcess(15))

                //.with(262, TreeIcon.MISC, NodeType.KEY_CHARACTER, new ConvertElementStrToStrElementProcess())

                .with(300, TreeIcon.HEALTH, new AddHealthElementProcess(100))
                .with(301, TreeIcon.HEALTH, new IncreaseHealthElementProcess(10))
                .with(302, TreeIcon.HEALTH, new IncreaseHealthElementProcess(10))
                .with(303, TreeIcon.HEALTH, NodeType.BIG_CHARACTER, new IncreaseHealthElementProcess(20), new AddHealthElementProcess(100))
                .with(304, TreeIcon.HEALTH, new IncreaseHealthElementProcess(10))
                .with(305, TreeIcon.HEALTH, new IncreaseHealthElementProcess(10))
                .with(306, TreeIcon.HEALTH, NodeType.BIG_CHARACTER, new IncreaseHealthElementProcess(20), new AddHealthElementProcess(100))

                .with(307, TreeIcon.HEALTH, new AddHealthElementProcess(100))
                .with(308, TreeIcon.HEALTH, new IncreaseHealthElementProcess(10))
                .with(309, TreeIcon.HEALTH, new IncreaseHealthElementProcess(10))
                .with(310, TreeIcon.HEALTH, NodeType.BIG_CHARACTER, new IncreaseHealthElementProcess(20), new AddHealthElementProcess(100))
                .with(311, TreeIcon.HEALTH, new IncreaseHealthElementProcess(10))
                .with(312, TreeIcon.HEALTH, new IncreaseHealthElementProcess(10))
                .with(313, TreeIcon.HEALTH, NodeType.BIG_CHARACTER, new IncreaseHealthElementProcess(20), new AddHealthElementProcess(100))

                .build();
    }

}
