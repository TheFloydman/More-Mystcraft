package thefloydman.moremystcraft.world.gen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Biomes;

/**
 * Generates a two-dimensional byte array that represents rule B5678/S45678.
 * 
 * @author Floydman
 *
 */
public class NoiseGeneratorCellularAutomata {

	private byte[][] noiseArray;
	private List<int[]> unverifiedCells;
	private final int width;
	private final int height;

	public NoiseGeneratorCellularAutomata(final int mapWidth, final int mapHeight, final int alivePercentage,
			final int passes, final int deadToAlive, final int aliveToDead) {

		this.width = mapWidth;
		this.height = mapHeight;

		// Initialize array.
		this.noiseArray = new byte[this.height][this.width];
		// Generate initial noise.
		for (int i = 0; i < this.noiseArray.length; i++) {
			for (int j = 0; j < this.noiseArray[i].length; j++) {
				int centerX = this.width / 2;
				int centerY = this.height / 2;
				double distanceFromCenter = Math
						.sqrt(((j - centerX) * (j - centerX)) + ((i - centerY) * (i - centerY)));
				if (distanceFromCenter < ((this.height + this.width) / 2) / 6
						|| Math.random() * 100 < alivePercentage) {
					this.noiseArray[i][j] = 1;
				} else {
					this.noiseArray[i][j] = 0;
				}
			}
		}
		// Run iterations of rule B5678/S45678.
		for (int i = 0; i < passes; i++) {

			for (int j = 0; j < this.noiseArray.length; j++) {
				for (int k = 0; k < this.noiseArray[0].length; k++) {
					if (this.noiseArray[j][k] == 0) {
						if (livingNeighbors(j, k) > deadToAlive - 1) {
							this.noiseArray[j][k] = 1;
						}
					} else if (this.noiseArray[j][k] == 1) {
						if (livingNeighbors(j, k) < aliveToDead + 1) {
							this.noiseArray[j][k] = 0;
						}
					}
				}
			}
			this.unverifiedCells = new ArrayList<int[]>();
			for (int j = 0; j < this.noiseArray.length; j++) {
				for (int k = 0; k < this.noiseArray[0].length; k++) {
					this.unverifiedCells.add(new int[] { j, k });
				}
			}
			while (this.unverifiedCells.size() > 0) {
				List<int[]> blobCells = new ArrayList<int[]>();
				if (this.noiseArray[this.unverifiedCells.get(0)[0]][this.unverifiedCells.get(0)[1]] == 1) {
					blobCells = getBlobCells(this.unverifiedCells.get(0));
				} else {
					this.unverifiedCells.remove(0);
				}
				// Remove small blobs.
				if (blobCells.size() > 8) {
					if (findBlobWidth(blobCells) < 2 || findBlobHeight(blobCells) < 2) {
						for (int j = 0; j < blobCells.size(); j++) {
							this.noiseArray[blobCells.get(j)[0]][blobCells.get(j)[1]] = 0;
						}
					}
				}
			}
		}
	}

	private int livingNeighbors(final int row, final int column) {
		int count = 0;
		if (row - 1 >= 0 && column - 1 >= 0) {
			if (this.noiseArray[row - 1][column - 1] == 1) {
				count++;
			}
		}
		if (row - 1 >= 0) {
			if (this.noiseArray[row - 1][column] == 1) {
				count++;
			}
		}
		if (row - 1 >= 0 && column + 1 < this.width) {
			if (this.noiseArray[row - 1][column + 1] == 1) {
				count++;
			}
		}
		if (column - 1 >= 0) {
			if (this.noiseArray[row][column - 1] == 1) {
				count++;
			}
		}
		if (column + 1 < this.width) {
			if (this.noiseArray[row][column + 1] == 1) {
				count++;
			}
		}
		if (row + 1 < this.height && column - 1 >= 0) {
			if (this.noiseArray[row + 1][column - 1] == 1) {
				count++;
			}
		}
		if (row + 1 < this.height) {
			if (this.noiseArray[row + 1][column] == 1) {
				count++;
			}
		}
		if (row + 1 < this.height && column + 1 < this.width) {
			if (this.noiseArray[row + 1][column + 1] == 1) {
				count++;
			}
		}
		return count;
	}

	private List<int[]> getBlobCells(final int[] startCell) {
		List<int[]> allCells = new ArrayList<int[]>();
		List<int[]> unvisitedCells = new ArrayList<int[]>();
		unvisitedCells.add(startCell);
		int[] motherCell;
		int[] currentCell = new int[2];
		while (unvisitedCells.size() > 0) {
			motherCell = unvisitedCells.remove(0);
			allCells.add(motherCell);
			if (this.unverifiedCells.indexOf(motherCell) == -1) {
				continue;
			}
			this.unverifiedCells.remove(this.unverifiedCells.indexOf(motherCell));
			if (motherCell[0] - 1 >= 0) {
				currentCell[0] = motherCell[0] - 1;
				currentCell[1] = motherCell[1];
				if (this.noiseArray[currentCell[0]][currentCell[1]] == 1) {
					if (!allCells.contains(currentCell) && !unvisitedCells.contains(currentCell)) {
						unvisitedCells.add(currentCell);
					}
				}
			}
			if (motherCell[0] + 1 < this.height) {
				currentCell[0] = motherCell[0] + 1;
				currentCell[1] = motherCell[1];
				if (this.noiseArray[currentCell[0]][currentCell[1]] == 1) {
					if (!allCells.contains(currentCell) && !unvisitedCells.contains(currentCell)) {
						unvisitedCells.add(currentCell);
					}
				}
			}
			if (motherCell[1] - 1 >= 0) {
				currentCell[0] = motherCell[0];
				currentCell[1] = motherCell[1] - 1;
				if (this.noiseArray[currentCell[0]][currentCell[1]] == 1) {
					if (!allCells.contains(currentCell) && !unvisitedCells.contains(currentCell)) {
						unvisitedCells.add(currentCell);
					}
				}
			}
			if (motherCell[1] + 1 < this.width) {
				currentCell[0] = motherCell[0];
				currentCell[1] = motherCell[1] + 1;
				if (this.noiseArray[currentCell[0]][currentCell[1]] == 1) {
					if (!allCells.contains(currentCell) && !unvisitedCells.contains(currentCell)) {
						unvisitedCells.add(currentCell);
					}
				}
			}
		}
		return allCells;
	}

	private int findBlobHeight(List<int[]> blob) {
		int currentY = blob.get(0)[0];
		int largestY = currentY;
		int smallestY = currentY;
		for (int i = 1; i < blob.size(); i++) {
			currentY = blob.get(i)[0];
			if (currentY > largestY) {
				largestY = currentY;
			}
			if (currentY < smallestY) {
				smallestY = currentY;
			}
		}
		return (largestY - smallestY) + 1;
	}

	private int findBlobWidth(List<int[]> blob) {
		int currentX = blob.get(0)[1];
		int largestX = currentX;
		int smallestX = currentX;
		for (int i = 1; i < blob.size(); i++) {
			currentX = blob.get(i)[1];
			if (currentX > largestX) {
				largestX = currentX;
			}
			if (currentX < smallestX) {
				smallestX = currentX;
			}
		}
		return (largestX - smallestX) + 1;
	}

	public byte[][] getnoiseArray() {
		return this.noiseArray;
	}
}
