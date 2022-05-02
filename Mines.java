package mines;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Mines {

	public class Point {
		int i, j;
		Point(int i, int j) {
			this.i = i;
			this.j = j;
		}
	}

	public class Location {
		Point p;
		boolean open = false, flag = false, mine = false;
		int numNear = 0;
		Mines GameBoard;

		Location(int i, int j, Mines currentBoard) {
			p = new Point(i, j);
			GameBoard = currentBoard;
		}

		void Flag() {
			if (flag) {flag = false;} 
			else {flag = true;}
		}

		boolean setAsOpen() {
			open = true;
			return mine;
		}

		boolean setMine() {
			mine = true;
			return mine;
		}

		Set<Location> getNeighbours() { 
			Set<Location> neighbours = new HashSet<>();
			if (p.i - 1 >= 0) {
				neighbours.add(GameBoard.Board[p.i - 1][p.j]);
			}
			if (p.i + 1 < GameBoard.mHeight) {
				neighbours.add(GameBoard.Board[p.i + 1][p.j]);
			}
			if (p.j - 1 >= 0) {
				neighbours.add(GameBoard.Board[p.i][p.j - 1]);
			}
			if (p.j + 1 < GameBoard.mWidth) {
				neighbours.add(GameBoard.Board[p.i][p.j + 1]);
			}
			if (p.i - 1 >= 0 && p.j - 1 >= 0) {
				neighbours.add(GameBoard.Board[p.i - 1][p.j - 1]);
			}
			if (p.i - 1 >= 0 && p.j + 1 < GameBoard.mWidth) {
				neighbours.add(GameBoard.Board[p.i - 1][p.j + 1]);
			}
			if (p.i + 1 < GameBoard.mHeight && p.j - 1 >= 0) {
				neighbours.add(GameBoard.Board[p.i + 1][p.j - 1]);
			}
			if (p.i + 1 < GameBoard.mHeight && p.j + 1 < GameBoard.mWidth) {
				neighbours.add(GameBoard.Board[p.i + 1][p.j + 1]);
			}
			return neighbours;
		}

		public String toString() {
			if (!open) {
				if (flag) {return "F";}
				return ".";
			}
			if (mine) {return "X";}
			if (numNear == 0) {	return " ";}
			return "" + numNear;
		}
	}

	public Location[][] Board;
	private int mHeight, mWidth;
	private boolean showAll = false;

	public Mines(int height, int width, int numMines) {
		mHeight = height;
		mWidth = width;
		int i, j, cnt = numMines;
		Board = new Location[height][width];
		for (i = 0; i < height; i++) {
			for (j = 0; j < width; j++) {
				Board[i][j] = new Location(i, j, this);
			}
		}
		Random rand = new Random(); // add mines randomly
		while (cnt > 0) {
			i = rand.nextInt(height);
			j = rand.nextInt(width);
			if (!Board[i][j].mine) {
				addMine(i,j);
				cnt--;
			}
		}
	}

	public boolean addMine(int i, int j) {
		if (Board[i][j].mine) {return false;}
		Board[i][j].setMine();
		for (Location l : Board[i][j].getNeighbours()) {
			l.numNear++;
		}
		return true;
	}

	public boolean open(int i, int j) {
		if (Board[i][j].mine) {return false;}
		Board[i][j].setAsOpen();
		if (Board[i][j].numNear == 0) {
			for (Location l : Board[i][j].getNeighbours()) {
				if (!l.open) {
					l.GameBoard.open(l.p.i, l.p.j);
				}
			}
		}
		return true;
	}

	public void toggleFlag(int x, int y) { Board[x][y].Flag();	}

	public boolean isDone() { 
		for (int i = 0; i < mHeight; i++) {
			for (int j = 0; j < mWidth; j++) {
				if (!Board[i][j].open && !Board[i][j].mine) {
					return false;
				}
			}
		}
		return true;
	}

	public String get(int i, int j) {
		String str = "";
		if (showAll) {
			boolean flag = Board[i][j].open;
			Board[i][j].open = true;
			str = Board[i][j].toString();
			Board[i][j].open = flag;
			return str;
		}
		return Board[i][j].toString();
	}

	public void setShowAll(boolean showAll) { this.showAll = showAll;}

	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < mHeight; i++) {
			for (int j = 0; j < mWidth; j++)
				s.append(this.get(i, j));
			s.append("\n");
		}
		return s.toString();
	}
}