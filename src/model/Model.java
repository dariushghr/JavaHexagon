package model;

public class Model {
	private Player player;
	private Row[] rows;
	private boolean gameover = false;
	private boolean paused = false;
	private int width, height;
	
	
	public Model(int width, int height) {
		this.width = width;
		this.height = height;
		this.rows = new Row[Constants.NB_ROW];
		int rowWidth = width/rows.length;
		for (int i = 0; i < rows.length; ++i) {
			rows[i] = new Row(rowWidth * i, 0, rowWidth, height);
		}
		this.player = new Player(50, rows[0].getLineY() - 7, 4);
	}
	
	
	public Player getPlayer() {
		return player;
	}
	
	public Row[] getRows() {
		return rows;
	}
	
	public void movePlayer(float x, float y) {
		if (gameover || paused) return;
		float resx = player.getX() + x;
		float resy = player.getY() + y;
		
		if (resx <= 0) resx = width + resx;
		resx = resx % width;
		
		for (Row row  : rows) {
			if (row.contain(resx)) {
				for (Shape obstacle : row.getObstacles()) {
					if (obstacle.getYPoints()[2] > resy - player.getSize()) return;
				}
			}
		}
		
		player.setX(resx);
		player.setY(resy);
	}
	
	public void update(long delta) {
		if (gameover || paused) return;
		for (Row row  : rows) {
			row.update(delta);
			if (row.contain(player.getX())) {
				for (Shape obstacle : row.getObstacles()) {
					if (obstacle.getYPoints()[2] > player.getY() - player.getSize()) {
						if (!Constants.INVINCIBLE) gameover = true;
					}
				}
			}
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean isGameOver() {
		return gameover;
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	public void togglePause() {
		paused = !paused;
	}
}
