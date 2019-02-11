package thefloydman.moremystcraft.world.gen;

import net.minecraft.block.state.IBlockState;

/**
 * Used to store three-dimensional block information. Useful in cases where
 * IBlockState access is restricted to a single chunk.
 * 
 * @author Floydman
 *
 */
public class StructureBlockInfo {

	protected byte[][][] blockInfo;

	protected StructureBlockInfo(final int x, final int y, final int z) {
		this.blockInfo = new byte[x][y][z];
	}
	
	/**
	 * Sets the IBlockState in three-dimensional space using local coordinates.
	 * 
	 * @param x The local x-coordinate of the block to be set.
	 * @param y The local y-coordinate of the block to be set.
	 * @param z The local z-coordinate of the block to be set.
	 * @param state A byte representing the IBlockState to be set.
	 */
	protected void setBlockState(final int x, final int y, final int z, final byte state) {
		this.blockInfo[x][y][z] = state;
	}

	/**
	 * 
	 * @return A three-dimensional byte array that represents the entire structure
	 *         from y = 0 to y = 255.
	 */
	public byte[][][] getStructureWhole() {
		return blockInfo;
	}

	/**
	 * 
	 * @param x The local x-coordinate where the chunk starts.
	 * @param z The local z-coordinate where the chunk starts.
	 * @return A three-dimensional byte array that represents an entire chunk (65536 blocks).
	 */
	public byte[][][] getStructureChunk(final int x, final int z) {
		if (x < 0 || z < 0 || x > getStructureWhole().length || z > getStructureWhole()[0][0].length) {
			System.out.println("Coordinates outside of structure.");
		}
		byte[][][] chunk = new byte[16][256][16];
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 256; j++) {
				for (int k = 0; k < 16; k++) {
					chunk[i][j][k] = this.getStructureWhole()[x + i][j][z + k];
				}
			}
		}
		return chunk;
	}

}
