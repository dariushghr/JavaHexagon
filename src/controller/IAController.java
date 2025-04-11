package controller;

import model.Constants;
import model.Model;

public class IAController extends Controller {

	public IAController(Model model) {
		super(model);
	}
	
	@SuppressWarnings("unused")
	private void bestRowIA(long delta) {
		int bestRow = 0;
		int actualRow = 0;
		for (int i = 0; i < getModel().getRows().length; ++i) {
			if (getModel().getRows()[i].contain(getModel().getPlayer().getX())) actualRow = i;
			
			if (getModel().getRows()[i].getObstacles().isEmpty()) {
				bestRow = i;
				break;
			}
			int challengerY = getModel().getRows()[i].getObstacles().getFirst().getYPoints()[2];
			int championY = getModel().getRows()[bestRow].getObstacles().getFirst().getYPoints()[2];
			if (challengerY < championY) bestRow = i;
		}
		
		if (actualRow == bestRow) return;
		
		getModel().movePlayer((actualRow - bestRow < 0 ? Constants.PLAYER_SPEED : -Constants.PLAYER_SPEED)*delta, 0);
	}
	
	
	private void safestNeighbourIA(long delta) {
		int dir = 0, newRow = 0, actualRow = 0;
		int[] heights = new int[3];
		for (int i = 0; i < getModel().getRows().length; ++i) {
			if (getModel().getRows()[i].contain(getModel().getPlayer().getX())) newRow = actualRow = i;
		}
		
		for (int i = -1; i <= 1; ++i) {
			heights[i+1] = -1;
			int rowId = (actualRow + i) % getModel().getRows().length;
			
			if (rowId < 0) rowId = getModel().getRows().length - 1;
			
			if (getModel().getRows()[rowId].getObstacles().isEmpty()) {
				newRow = i;
				dir = i;
				break;
			}
			
			int challengerY = getModel().getRows()[rowId].getObstacles().getFirst().getYPoints()[2];
			heights[i+1] = challengerY;
			if (getModel().getRows()[newRow].getObstacles().isEmpty()) {
				dir = 0;
				break;
			}
			int championY = getModel().getRows()[newRow].getObstacles().getFirst().getYPoints()[2];
			
			if (challengerY < championY) {
				newRow = rowId;
				dir = i;
			}
		}
		
		
		getModel().movePlayer(dir*Constants.PLAYER_SPEED*delta, 0);
	}

	@Override
	public void update(long delta) {
		safestNeighbourIA(delta);
	}

}
