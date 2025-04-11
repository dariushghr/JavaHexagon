package Menu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class GameOver extends JDialog {
    private boolean playAgain = false;
    private boolean backToMenu = false;
    private Color backgroundColor = new Color(20, 20, 40);
    private Color highlightColor = new Color(255, 120, 0);
    private Color textColor = new Color(240, 240, 240);
    private String finalScore;

    public GameOver(JFrame parent, int score) {
        super(parent, "Game Over", true);
        this.finalScore = String.valueOf(score);
        setUndecorated(true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setShape(new RoundRectangle2D.Double(0, 0, 400, 300, 15, 15));
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, backgroundColor, 
                    0, getHeight(), new Color(10, 10, 30)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        titleBar.setBorder(new EmptyBorder(10, 15, 5, 15));
        
        JLabel titleLabel = new JLabel("GAME OVER");
        titleLabel.setForeground(highlightColor);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 18));
        closeButton.setForeground(textColor);
        closeButton.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());
        
        titleBar.add(titleLabel, BorderLayout.WEST);
        titleBar.add(closeButton, BorderLayout.EAST);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(10, 25, 20, 25));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel messageLabel = new JLabel("You Lost!");
        messageLabel.setForeground(textColor);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 22));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(messageLabel, gbc);
        
        JLabel scoreLabel = new JLabel("Your Score: " + finalScore);
        scoreLabel.setForeground(textColor);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        contentPanel.add(scoreLabel, gbc);
        
        JButton playAgainButton = createStyledButton("PLAY AGAIN", e -> {
            playAgain = true;
            dispose();
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(playAgainButton, gbc);
        
        JButton menuButton = createStyledButton("BACK TO MENU", e -> {
            backToMenu = true;
            dispose();
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPanel.add(menuButton, gbc);
        
        mainPanel.add(titleBar, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        MouseDragAdapter dragListener = new MouseDragAdapter();
        titleBar.addMouseListener(dragListener);
        titleBar.addMouseMotionListener(dragListener);
        
        getContentPane().add(mainPanel);
    }
    
    private JButton createStyledButton(String text, ActionListener action) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient;
                if (getModel().isPressed()) {
                    gradient = new GradientPaint(0, 0, highlightColor.darker(), 
                                                0, getHeight(), highlightColor);
                } else if (getModel().isRollover()) {
                    gradient = new GradientPaint(0, 0, highlightColor.brighter(), 
                                                0, getHeight(), highlightColor);
                } else {
                    gradient = new GradientPaint(0, 0, highlightColor, 
                                                0, getHeight(), highlightColor.darker());
                }
                
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                FontMetrics fm = g2d.getFontMetrics();
                Rectangle textRect = fm.getStringBounds(getText(), g2d).getBounds();
                int x = (getWidth() - textRect.width) / 2;
                int y = (getHeight() - textRect.height) / 2 + fm.getAscent();
                
                g2d.setColor(Color.WHITE);
                g2d.drawString(getText(), x, y);
                g2d.dispose();
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 40));
        button.addActionListener(action);
        
        return button;
    }
    
    private class MouseDragAdapter extends javax.swing.event.MouseInputAdapter {
        private Point initialClick;
        
        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            initialClick = e.getPoint();
        }
        
        @Override
        public void mouseDragged(java.awt.event.MouseEvent e) {
            Point currentLocation = getLocation();
            setLocation(
                currentLocation.x + e.getX() - initialClick.x,
                currentLocation.y + e.getY() - initialClick.y
            );
        }
    }
    
    public boolean isPlayAgain() {
        return playAgain;
    }
    
    public boolean isBackToMenu() {
        return backToMenu;
    }
} 