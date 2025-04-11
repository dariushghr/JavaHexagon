package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import controller.Controller;
import controller.IAController;
import controller.KeysController;
import model.Constants;
import model.Model;
import Menu.EnterGame;
import Menu.GameMenu;
import Menu.GameOver;

public class Screen extends JPanel {
	private static final long serialVersionUID = 1L;
	private Model model;
	private Controller controller;
	private Renderer renderer;
	private boolean gameOverDialogShown = false;
	private int score = 0;
	private long gameStartTime;
	private Font pauseFont;
	
	public Screen(int width, int height) {
		super();
		
		width -= width%Constants.NB_ROW;
		
		model = new Model(width, height);
		
		renderer = new HexagonalRenderer(model);
		
		setFocusable(true);
		setPreferredSize(new Dimension(width, height));
		
		final KeysController c = new KeysController(model);
		
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {
				c.onKeyReleased(e);
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				c.onKeyPressed(e);
			}
		});
		
		controller = Constants.PLAYED_BY_IA ? new IAController(model) : c;
		
		gameStartTime = System.currentTimeMillis();
		updateLoop();
	}
	
	
	public void updateLoop() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				long lastUpdate = System.currentTimeMillis();
				while (true) {
					long current = System.currentTimeMillis();
					long delta = current - lastUpdate;
					lastUpdate = current;
					update(delta);
					
					if (model.isGameOver() && !gameOverDialogShown) {
						final int finalScore = (int)((System.currentTimeMillis() - gameStartTime) / 1000);
						score = finalScore;
						
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								showGameOverDialog(finalScore);
							}
						});
						
						gameOverDialogShown = true;
					}
					
					repaint();
					Toolkit.getDefaultToolkit().sync();
					try {
						long waitTime = (long) ((1f/Constants.FPS)*1000 - delta);
						if (waitTime > 0) Thread.sleep(waitTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		setBackground(Constants.COLOR.darker().darker().darker().darker());
	}
	
	private void showGameOverDialog(int score) {
		JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		GameOver gameOverDialog = new GameOver(parentFrame, score);
		gameOverDialog.setVisible(true);
		
		if (gameOverDialog.isPlayAgain()) {
			GameMenu newGameMenu = new GameMenu();
			EnterGame enterGameDialog = new EnterGame(newGameMenu);
            enterGameDialog.setVisible(true);
            if (enterGameDialog.isGameStarted()) {
                String username = enterGameDialog.getUsername();
                newGameMenu.startGame(username);
            }
		} else if (gameOverDialog.isBackToMenu()) {
			returnToMainMenu();
		}
	}
	
	private void restartGame() {
		model = new Model(getWidth(), getHeight());
		
		final KeysController c = new KeysController(model);
		controller = Constants.PLAYED_BY_IA ? new IAController(model) : c;
		renderer = new HexagonalRenderer(model);
		
		gameOverDialogShown = false;
		score = 0;
		gameStartTime = System.currentTimeMillis();
	}
	
	private void returnToMainMenu() {
		JFrame currentWindow = (JFrame) SwingUtilities.getWindowAncestor(this);
		currentWindow.dispose();
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GameMenu();
			}
		});
	}
	
	@Override
	public synchronized void paintComponent(Graphics g) {
		super.paintComponent(g);
		renderer.draw((Graphics2D) g);
		
		if (model.isPaused()) {
			drawPauseOverlay((Graphics2D) g);
		}
	}
	
	private void drawPauseOverlay(Graphics2D g) {
		g.setColor(new Color(0, 0, 0, Constants.PAUSE_OVERLAY_ALPHA));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.WHITE);
		g.setFont(pauseFont);
		String pauseMessage = "Game paused press 'P' to Resume!";
		int messageWidth = g.getFontMetrics().stringWidth(pauseMessage);
		g.drawString(pauseMessage, (getWidth() - messageWidth) / 2, getHeight() / 2);
	}
	
	public synchronized void update(long delta) {
		controller.update(delta);
		model.update(delta);
		renderer.update(delta);
	}
	
	public int getScore() {
		return score;
	}
}
