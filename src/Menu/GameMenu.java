package Menu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

import view.MusicPlayer;
import view.Window;

public class GameMenu extends JFrame {
    private int highScore = 0;
    private JLabel highScoreLabel;
    private boolean musicEnabled = true;
    private boolean savingProgressEnabled = true;
    private Color backgroundColor = new Color(20, 20, 40);
    private Color highlightColor = new Color(255, 120, 0);
    private Color buttonColor = new Color(60, 60, 100);
    private Color textColor = new Color(240, 240, 240);
    
    public GameMenu() {
        super("Super Hexagon Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 500, 600, 20, 20));
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, backgroundColor, 
                    0, getHeight(), new Color(10, 10, 30)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(new Color(40, 40, 70, 100));
                int size = 50;
                for (int x = -size; x < getWidth() + size; x += size * 2) {
                    for (int y = -size; y < getHeight() + size; y += size * 2) {
                        drawHexagon(g2d, x, y, size);
                    }
                }
            }
            
            private void drawHexagon(Graphics2D g2d, int x, int y, int size) {
                int[] xPoints = new int[6];
                int[] yPoints = new int[6];
                for (int i = 0; i < 6; i++) {
                    double angle = 2 * Math.PI / 6 * i;
                    xPoints[i] = x + (int) (size * Math.cos(angle));
                    yPoints[i] = y + (int) (size * Math.sin(angle));
                }
                g2d.drawPolygon(xPoints, yPoints, 6);
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        JButton closeButton = new JButton("X");
        closeButton.setForeground(textColor);
        closeButton.setBackground(new Color(180, 20, 20));
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> System.exit(0));
        titleBar.add(closeButton, BorderLayout.EAST);
        mainPanel.add(titleBar);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("SUPER HEXAGON");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(highlightColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel subtitleLabel = new JLabel("The Ultimate Challenge");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitleLabel.setForeground(textColor);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(titlePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        JPanel scorePanel = new JPanel();
        scorePanel.setOpaque(false);
        scorePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(highlightColor, 2),
            new EmptyBorder(10, 20, 10, 20)
        ));
        
        highScoreLabel = new JLabel("HIGH SCORE: " + highScore);
        highScoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        highScoreLabel.setForeground(textColor);
        scorePanel.add(highScoreLabel);
        
        JPanel scorePanelWrapper = new JPanel();
        scorePanelWrapper.setOpaque(false);
        scorePanelWrapper.add(scorePanel);
        scorePanelWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(scorePanelWrapper);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        mainPanel.add(createStyledButton("ENTER GAME", e -> {
            EnterGame enterGameDialog = new EnterGame(GameMenu.this);
            enterGameDialog.setVisible(true);
            if (enterGameDialog.isGameStarted()) {
                String username = enterGameDialog.getUsername();
                startGame(username);
            }
        }));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        mainPanel.add(createStyledButton("GAME HISTORY", e -> showGameHistory()));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        mainPanel.add(createStyledButton("GAME SETTINGS", e -> showSettings()));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        mainPanel.add(createStyledButton("EXIT GAME", e -> System.exit(0)));
        
        mainPanel.add(Box.createVerticalGlue());
        JLabel footerLabel = new JLabel("© Super Hexagon | Version 1.0");
        footerLabel.setForeground(new Color(150, 150, 150));
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(footerLabel);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JPanel createStyledButton(String text, ActionListener action) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(280, 60));
        
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient;
                if (getModel().isPressed()) {
                    gradient = new GradientPaint(0, 0, buttonColor.darker(), 0, getHeight(), buttonColor);
                } else if (getModel().isRollover()) {
                    gradient = new GradientPaint(0, 0, buttonColor.brighter(), 0, getHeight(), buttonColor);
                } else {
                    gradient = new GradientPaint(0, 0, buttonColor, 0, getHeight(), buttonColor.darker());
                }
                
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2d.setColor(getModel().isRollover() ? highlightColor : new Color(100, 100, 150));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 15, 15);
                
                FontMetrics fm = g2d.getFontMetrics();
                Rectangle textRect = fm.getStringBounds(text, g2d).getBounds();
                int x = (getWidth() - textRect.width) / 2;
                int y = (getHeight() - textRect.height) / 2 + fm.getAscent();
                
                g2d.setColor(textColor);
                g2d.drawString(text, x, y);
                g2d.dispose();
            }
        };
        
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(250, 50));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.addActionListener(action);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        
        buttonPanel.add(button);
        return buttonPanel;
    }
    
    public void startGame(String username) {
        setVisible(false);
        dispose();
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Starting game for user: " + username);
                    Window gameWindow = new Window();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, 
                        "Error starting game: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private void showGameHistory() {
        GameHistory historyDialog = new GameHistory(this);
        historyDialog.setVisible(true);
    }
    
    private void showSettings() {
        GameSettings settingsDialog = new GameSettings(this, musicEnabled, savingProgressEnabled);
        settingsDialog.setVisible(true);
        if (settingsDialog.isSettingsUpdated()) {
            this.musicEnabled = settingsDialog.isMusicEnabled();
            this.savingProgressEnabled = settingsDialog.isSavingProgressEnabled();
        }
    }
    
    public void updateHighScore(int score) {
        if (score > highScore) {
            highScore = score;
            highScoreLabel.setText("HIGH SCORE: " + highScore);
        }
    }
    private Point initialClick;
    {
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                initialClick = e.getPoint();
            }
        });

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent e) {
                int thisX = getLocation().x;
                int thisY = getLocation().y;

                int xMoved = thisX + e.getX() - initialClick.x;
                int yMoved = thisY + e.getY() - initialClick.y;

                setLocation(xMoved, yMoved);
            }
        });
    }
    
    public static void main(String[] args) {
        MusicPlayer music = new MusicPlayer("./src/view/remix_ahmagh.mp3");
		music.playLoop();
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameMenu();
            }
        });
    }
} 