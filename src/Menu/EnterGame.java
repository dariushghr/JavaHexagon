package Menu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class EnterGame extends JDialog {
    private JTextField usernameField;
    private JButton startButton;
    private boolean gameStarted = false;
    private String username = "";
    private Color backgroundColor = new Color(20, 20, 40);
    private Color highlightColor = new Color(255, 120, 0);
    private Color textColor = new Color(240, 240, 240);

    public EnterGame(JFrame parent) {
        super(parent, "Enter Game", true);
        setUndecorated(true);
        setSize(350, 250);
        setLocationRelativeTo(parent);
        setShape(new RoundRectangle2D.Double(0, 0, 350, 250, 15, 15));
        
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
        
        JLabel titleLabel = new JLabel("ENTER GAME");
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
        
        JLabel usernameLabel = new JLabel("Enter Username:");
        usernameLabel.setForeground(textColor);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        contentPanel.add(usernameLabel, gbc);
        
        usernameField = new JTextField(15) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(40, 40, 60));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        usernameField.setOpaque(false);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        usernameField.setCaretColor(textColor);
        usernameField.setForeground(textColor);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(usernameField, gbc);
        
        startButton = new JButton("START GAME") {
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
        
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        startButton.addActionListener(e -> {
            username = usernameField.getText().trim();
            if (username.isEmpty()) {
                JLabel message = new JLabel("Please enter a username");
                message.setFont(new Font("Arial", Font.PLAIN, 14));
                
                JOptionPane optionPane = new JOptionPane(
                    message, 
                    JOptionPane.ERROR_MESSAGE, 
                    JOptionPane.DEFAULT_OPTION
                );
                
                JDialog dialog = optionPane.createDialog(this, "Error");
                dialog.setVisible(true);
                return;
            }
            gameStarted = true;
            dispose();
        });
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(startButton, gbc);
        
        mainPanel.add(titleBar, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        MouseAdapter dragListener = new MouseAdapter();
        titleBar.addMouseListener(dragListener);
        titleBar.addMouseMotionListener(dragListener);
        
        getContentPane().add(mainPanel);
    }
    
    private class MouseAdapter extends javax.swing.event.MouseInputAdapter {
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
    
    public boolean isGameStarted() {
        return gameStarted;
    }
    
    public String getUsername() {
        return username;
    }
}
