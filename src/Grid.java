

import java.util.Objects;

public class Grid {

	private int row;
	private int column;

	public Grid() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Grid(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public int hashCode() {
		return Objects.hash(column, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Grid other = (Grid) obj;
		return column == other.column && row == other.row;
	}

	@Override
	public String toString() {
		return "Grid [row=" + row + ", column=" + column + "]";
	}
	
	

}
