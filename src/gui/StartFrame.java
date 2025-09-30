package gui;

import javax.swing.*;
import java.awt.*;

public class StartFrame extends JFrame {
    public static final int DEFAULT_WIDTH = 640;
    public static final int DEFAULT_HEIGHT = 400;

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}
        new StartFrame();
    }

    public StartFrame() {
        setTitle("代码相似度比较系统");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("欢迎使用本系统", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setBounds(0, 90, DEFAULT_WIDTH, 60);
        add(title);

        JButton instructionBtn = new JButton("使用说明");
        instructionBtn.setBounds(160, 250, 140, 50);
        instructionBtn.addActionListener(e -> new InstructionFrame());
        add(instructionBtn);

        JButton startBtn = new JButton("开始使用");
        startBtn.setBounds(340, 250, 140, 50);
        startBtn.addActionListener(e -> { dispose(); new MainFrame(); });
        add(startBtn);

        beautify();
        setVisible(true);
    }

    private void beautify() {
        Color bg = new Color(242,245,249);
        Color accent = new Color(52,152,219);
        getContentPane().setBackground(bg);
        for (Component c : getContentPane().getComponents()) {
            if (c instanceof JButton) {
                JButton b = (JButton)c;
                b.setBackground(accent);
                b.setForeground(Color.WHITE);
                b.setFocusPainted(false);
                b.setBorder(BorderFactory.createEmptyBorder());
            }
            if (c instanceof JLabel) {
                c.setForeground(new Color(44,62,80));
            }
        }
    }
}
