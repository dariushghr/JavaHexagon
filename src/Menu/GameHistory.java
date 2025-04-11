package Menu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GameHistory extends JDialog {
    
    public GameHistory(JFrame parent) {
        super(parent, "Game History", true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columnNames = {"Player", "Score", "Date", "Time"};
        Object[][] data = {
            {"Player1", "1200", "2023-06-15", "14:30"},
            {"Player2", "980", "2023-06-14", "16:45"},
            {"Player3", "1450", "2023-06-13", "09:20"}
        };
        
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        
        JLabel titleLabel = new JLabel("Game Records");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
} 