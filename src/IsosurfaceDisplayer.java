import java.awt.Color;
import java.awt.Graphics2D;

public class IsosurfaceDisplayer extends Displayer {

	private final int CELL_SIZE; 
	private final int LINE_SIZE;
	private Cell[] cells;
	float tick = 0;
	private int superWidth, superHeight;
	
	public IsosurfaceDisplayer(int displayWidth, int displayHeight, AudioLoader loader, AudioDisplayConfig config) {
		super(1000, 300, loader, config);
		superWidth = displayWidth;
		superHeight = displayHeight;
		LINE_SIZE = 20;
		CELL_SIZE = superWidth/LINE_SIZE;
		cells = new Cell[LINE_SIZE*LINE_SIZE];
		clearCells();
		
		init();
		analyseMusic();
	}

	private void clearCells() {
		int isize = 0;
		for(int yy = 0; yy < LINE_SIZE; yy++) {
			for (int xx=0; xx < LINE_SIZE; xx++) {
				isize++;
				cells[yy*LINE_SIZE + xx] = new Cell(xx*CELL_SIZE, yy*CELL_SIZE);
			}
		}
		System.out.println("ISize: " + isize);
		for (int i = 0; i < cells.length; i++) {
			//cells[i] = new Cell(0,0);
		}
		System.out.println("Cells Size: " + cells.length);
	}
	
	private void analyseMusic() {
		int max = 0, 
			min = 0;
		
		for (int i=0; i < sampleHeights.length; i++) {
			if (sampleHeights[i] < min) {
				
			}
			if (sampleHeights[i] > max) {
				
			}
		}
	}
	
	@Override
	public void update() {
		super.update();
		tick += 1f/60f;
	}

	@Override
	public void draw(Graphics2D g) {
		/*for(int i = 0; i < cells.length; i++) {
			if(cells[i] == null) {
				//System.out.println("Cell " + i + " is null!");
			}
			//assert(cells[i] != null);
			cells[i].draw(getCellColor(i, i, i), g);
		}*/
		for(int yy = 0; yy < LINE_SIZE; yy++) {
			for (int xx=0; xx < LINE_SIZE; xx++) {
				//sampleHeights[totalSeconds];
				cells[yy*LINE_SIZE + xx].draw(getCellColor(xx*CELL_SIZE, yy*CELL_SIZE, tick), g);;
			}
		}
	}
	
	private Color getCellColor(int x, int y, float v) {
		
		//int value = (int)MathUtils.map(0, 255, 0, displayWidth, ((float)x+(v*(float)CELL_SIZE)+(float)y)/2f);
		int aval = (int)MathUtils.map(0, 255, 0, displayWidth, x);
		int bval = (int)MathUtils.map(0, 255, 0, displayHeight, y);
		return new Color(aval, 0, bval, 255);
	}
	
	
	
	private class Cell {
		
		private int x;
		private int y;
		
		public Cell(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		
		public void draw(Color c, Graphics2D g) {
			g.setColor(c);
			g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
		}
		
	}
	
}
