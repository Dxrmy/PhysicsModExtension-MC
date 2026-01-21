package com.eni.physics.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.Queue;

public class PhysicsEvents {

    // Adapter for Fabric Event
    public static boolean onBlockBreak(Level world, net.minecraft.world.entity.player.Player player, BlockPos pos,
            BlockState state, @Nullable BlockEntity blockEntity) {
        if (!world.isClientSide()) {
            checkStructure(world, pos);
        }
        return true; // UseBlockCallback requires a boolean return (pass/fail)
    }

    public static void checkStructure(Level level, BlockPos origin) {
        // BFS Flood Fill to find supported blocks
        // ... (Same logic as before, just adapted import names where needed)
        // Simplified for demo:

        BlockPos above = origin.above();
        BlockState aboveState = level.getBlockState(above);

        if (!aboveState.isAir() && !aboveState.is(Blocks.BEDROCK)) {
            if (!hasSupport(level, above)) {
                collapse(level, above);
            }
        }
    }

    private static boolean hasSupport(Level level, BlockPos pos) {
        // Simple check: Is there a block below?
        // "Project 4" rule: Dirt needs solid ground. Wood can overhang by 2.
        return !level.getBlockState(pos.below()).isAir();
    }

    private static void collapse(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

        FallingBlockEntity.fall(level, pos, state);
        // "Creak" sound or particle effect can be spawned here
    }
}
