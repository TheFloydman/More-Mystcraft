/*
 * Use this class to generate a maze using the Recursive Backtracker method.
 * Instantiate the class to generate the maze, and call the getMaze method
 * to retrieve a two-dimensional int array (int[][]) that represents the maze.
 * Walls and walkways have the same width. Use an odd number for width and
 * height if you want a wall around the entire maze; otherwise, there will only
 * be walls at the top and left side. Odd numbers are good for finite mazes, and
 * even numbers are good for generating tiles for infinite mazes. You will still
 * need to add your own entrance and exit once you retrieve the generated maze.
 * 
 * 0 = unvisited
 * 1 = wall
 * 2 = walkway
 * 
 * Example: int[][] maze = new MazeGeneratorRecursiveBacktracker(17, 17).getMaze();
 */

package thefloydman.moremystcraft.world.gen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MazeGeneratorRecursiveBacktracker {

	private int[][] maze;
	private int width;
	private int height;
	private List<int[]> unvisitedCells = new ArrayList<int[]>();

	public MazeGeneratorRecursiveBacktracker(final int width, final int height) {
		this.width = width;
		this.height = height;
		int[] currentCell = new int[2];
		int[] nextCell = new int[2];
		int[] middleCell = new int[2];
		List<int[]> neighborCells = new ArrayList<int[]>();
		// Generate proto-maze with all walls intact.
		this.maze = new int[this.width][this.height];
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				if (i % 2 == 0 || j % 2 == 0) {
					this.maze[i][j] = 1;
				} else {
					this.maze[i][j] = 0;
					this.unvisitedCells.add(new int[] { i, j });
				}
			}
		}
		// Make the initial cell the current cell and mark it as visited
		currentCell[0] = (int) ((Math.random() * (this.width - 1)) + 1);
		currentCell[0] += currentCell[0] % 2 == 0 ? 1 : 0;
		currentCell[1] = (int) ((Math.random() * (this.height - 1)) + 1);
		currentCell[1] += currentCell[1] % 2 == 0 ? 1 : 0;
		this.maze[currentCell[0]][currentCell[1]] = 2;
		for (int i = 0; i < unvisitedCells.size(); i++) {
			if (this.unvisitedCells.get(i)[0] == currentCell[0] && this.unvisitedCells.get(i)[1] == currentCell[1]) {
				this.unvisitedCells.remove(i);
				break;
			}
		}
		neighborCells = getNeighbors(currentCell);
		List<int[]> stack = new ArrayList<int[]>();
		// While there are unvisited cells
		while (this.unvisitedCells.size() > 0) {
			// If the current cell has any neighbors which have not been visited
			// System.out.println(neighborCells.size());
			if (neighborCells.size() > 0) {
				// Choose randomly one of the unvisited neighbors
				nextCell = neighborCells.get((int) (Math.random() * neighborCells.size()));
				// Push the current cell to the stack
				stack.add(0, currentCell);
				// Remove the wall between the current cell and the chosen cell
				if (nextCell[0] == currentCell[0]) {
					middleCell[0] = currentCell[0];
					if (nextCell[1] > currentCell[1]) {
						middleCell[1] = currentCell[1] + 1;
					} else {
						middleCell[1] = currentCell[1] - 1;
					}
					this.maze[middleCell[0]][middleCell[1]] = 2;
				} else if (nextCell[1] == currentCell[1]) {
					middleCell[1] = currentCell[1];
					if (nextCell[0] > currentCell[0]) {
						middleCell[0] = currentCell[0] + 1;
					} else {
						middleCell[0] = currentCell[0] - 1;
					}
					this.maze[middleCell[0]][middleCell[1]] = 2;
				}
				// Make the chosen cell the current cell and mark it as visited
				currentCell = nextCell;
				this.maze[currentCell[0]][currentCell[1]] = 2;
				for (int i = 0; i < unvisitedCells.size(); i++) {
					if (this.unvisitedCells.get(i)[0] == currentCell[0]
							&& this.unvisitedCells.get(i)[1] == currentCell[1]) {
						this.unvisitedCells.remove(i);
						break;
					}
				}
				neighborCells = getNeighbors(currentCell);
				// Else if stack is not empty
			} else if (stack.size() > 0) {
				// Pop a cell from the stack and make it the current cell
				currentCell = stack.get(0);
				stack.remove(0);
				neighborCells = getNeighbors(currentCell);
			}

		}
	}

	private List<int[]> getNeighbors(int[] mainCell) {
		List<int[]> neighborCells = new ArrayList<int[]>();
		// Check top neighbor.
		int[] up = new int[] { mainCell[0], mainCell[1] - 2 };
		if (mainCell[1] - 2 >= 0) {
			for (int i = 0; i < unvisitedCells.size(); i++) {
				if (this.unvisitedCells.get(i)[0] == up[0] && this.unvisitedCells.get(i)[1] == up[1]) {
					neighborCells.add(up);
					break;
				}
			}
		}
		// Check left neighbor.
		int[] left = new int[] { mainCell[0] - 2, mainCell[1] };
		if (mainCell[0] - 2 >= 0) {
			for (int i = 0; i < unvisitedCells.size(); i++) {
				if (this.unvisitedCells.get(i)[0] == left[0] && this.unvisitedCells.get(i)[1] == left[1]) {
					neighborCells.add(left);
					break;
				}
			}

		}
		// Check right neighbor.
		int[] right = new int[] { mainCell[0] + 2, mainCell[1] };
		if (mainCell[0] + 2 < this.width) {
			for (int i = 0; i < unvisitedCells.size(); i++) {
				if (this.unvisitedCells.get(i)[0] == right[0] && this.unvisitedCells.get(i)[1] == right[1]) {
					neighborCells.add(right);
					break;
				}
			}

		}
		// Check bottom neighbor.
		int[] down = new int[] { mainCell[0], mainCell[1] + 2 };
		if (mainCell[1] + 2 < this.height) {
			for (int i = 0; i < unvisitedCells.size(); i++) {
				if (this.unvisitedCells.get(i)[0] == down[0] && this.unvisitedCells.get(i)[1] == down[1]) {
					neighborCells.add(down);
					break;
				}
			}
		}
		return neighborCells;
	}

	public int[][] getMaze() {
		return this.maze;
	}

}
