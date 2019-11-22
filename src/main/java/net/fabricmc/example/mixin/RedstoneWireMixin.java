package net.fabricmc.example.mixin;

import net.minecraft.block.*;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.enums.WireConnection;
import net.minecraft.entity.EntityContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;

@Mixin(RedstoneWireBlock.class)
abstract public class RedstoneWireMixin extends Block {
	public RedstoneWireMixin(Block.Settings settings) { super(settings); }

	@Shadow public static @Final IntProperty POWER;
	@Shadow abstract int increasePower(int int_1, BlockState class_2680_1);
	@Shadow private boolean wiresGivePower;


	@Overwrite
	private BlockState update(World class_1937_1, BlockPos class_2338_1, BlockState class_2680_1) {
		BlockState class_2680_2 = class_2680_1;
		int int_1 = (Integer)class_2680_1.get(POWER);
		this.wiresGivePower = false;
		int int_2 = class_1937_1.getReceivedRedstonePower(class_2338_1);
		this.wiresGivePower = true;
		int int_3 = 0;
		if (int_2 < 15) {
			Iterator var8 = Direction.Type.HORIZONTAL.iterator();

label43:
			while(true) {
				while(true) {
					if (!var8.hasNext()) {
						break label43;
					}

					Direction class_2350_1 = (Direction)var8.next();
					BlockPos class_2338_2 = class_2338_1.offset(class_2350_1);
					BlockState class_2680_3 = class_1937_1.getBlockState(class_2338_2);
					int_3 = this.increasePower(int_3, class_2680_3);
					BlockPos class_2338_3 = class_2338_1.up();
					if (class_2680_3.isSimpleFullBlock(class_1937_1, class_2338_2) && !class_1937_1.getBlockState(class_2338_3).isSimpleFullBlock(class_1937_1, class_2338_3)) {
						int_3 = this.increasePower(int_3, class_1937_1.getBlockState(class_2338_2.up()));
					} else if (!class_2680_3.isSimpleFullBlock(class_1937_1, class_2338_2)) {
						int_3 = this.increasePower(int_3, class_1937_1.getBlockState(class_2338_2.down()));
					}
				}
			}
		}

		int int_4 = int_3 - 1;
		if (int_2 > int_4) {
			int_4 = int_2;
		}

		if (int_1 != int_4) {
			class_2680_1 = (BlockState)class_2680_1.with(POWER, int_4);
			if (class_1937_1.getBlockState(class_2338_1) == class_2680_2) {
				class_1937_1.setBlockState(class_2338_1, class_2680_1, 2);
			}

			//this.affectedNeighbors.add(class_2338_1);
			class_1937_1.updateNeighborsAlways(class_2338_1, this);

			Direction[] var14 = Direction.values();
			int var15 = var14.length;

			for(int var16 = 0; var16 < var15; ++var16) {
				Direction class_2350_2 = var14[var16];
				//this.affectedNeighbors.add(class_2338_1.offset(class_2350_2));
				class_1937_1.updateNeighborsAlways(class_2338_1.offset(class_2350_2), this);
			}
		}

		return class_2680_1;
	}
}
