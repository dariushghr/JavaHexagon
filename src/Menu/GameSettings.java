package Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameSettings extends JDialog {
    private boolean musicEnabled;
    private boolean savingProgressEnabled;
    private boolean settingsUpdated = false;
    
    private JToggleButton musicToggle;
    private JToggleButton savingToggle;
    
    public GameSettings(JFrame parent, boolean initialMusicEnabled, boolean initialSavingEnabled) {
        super(parent, "Game Settings", true);
        
        this.musicEnabled = initialMusicEnabled;
        this.savingProgressEnabled = initialSavingEnabled;
        
        setSize(350, 250);
        setLocationRelativeTo(parent);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Game Settings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        JLabel musicLabel = new JLabel("Game Music:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(musicLabel, gbc);
        
        musicToggle = new JToggleButton(musicEnabled ? "Enabled" : "Disabled");
        musicToggle.setSelected(musicEnabled);
        musicToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                musicEnabled = musicToggle.isSelected();
                musicToggle.setText(musicEnabled ? "Enabled" : "Disabled");
            }
        });
        gbc.gridx = 1;
        mainPanel.add(musicToggle, gbc);
        
        JLabel savingLabel = new JLabel("Save Progress:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(savingLabel, gbc);
        
        savingToggle = new JToggleButton(savingProgressEnabled ? "Enabled" : "Disabled");
        savingToggle.setSelected(savingProgressEnabled);
        savingToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savingProgressEnabled = savingToggle.isSelected();
                savingToggle.setText(savingProgressEnabled ? "Enabled" : "Disabled");
            }
        });
        gbc.gridx = 1;
        mainPanel.add(savingToggle, gbc);
        
        JPanel buttonPanel = new JPanel();
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsUpdated = true;
                dispose();
            }
        });
        buttonPanel.add(saveButton);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
    }
    
    public boolean isMusicEnabled() {
        return musicEnabled;
    }
    
    public boolean isSavingProgressEnabled() {
        return savingProgressEnabled;
    }
    
    public boolean isSettingsUpdated() {
        return settingsUpdated;
    }
} 