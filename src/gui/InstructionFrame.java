package gui;

import javax.swing.*;
import java.awt.*;


/**
 * 使用说明窗口
 */
public class InstructionFrame extends JFrame {
    public static final int DEFAULT_WIDTH = 420;
    public static final int DEFAULT_HEIGHT = 420;
    public static final String INSTRUCTION = "本程序用于对两个 Java 源文件进行关键字与用户标识符频次统计，并计算相似度结果。\n" +
            "步骤：\n1. 返回主界面选择两个 .java 文件\n2. 查看各自统计结果\n3. 点击开始比较，查看相似度与结论\n\n提示：结果仅供快速参考。";
    private JButton returnButton;
    private JTextArea instructionText;

    public InstructionFrame() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        setTitle("使用说明");

        instructionText = new JTextArea();
        instructionText.setBounds(15, 15, 380, 300);
        instructionText.setEditable(false);
        instructionText.setFont(new Font("SansSerif", Font.PLAIN, 14));
        instructionText.setLineWrap(true);
        instructionText.setWrapStyleWord(true);
        instructionText.setText(INSTRUCTION);
        add(instructionText);

        returnButton = new JButton("关闭");
        returnButton.setBounds(160, 330, 100, 40);
        returnButton.addActionListener(e -> dispose());
        add(returnButton);

        beautify();
        setVisible(true);
    }

    private void beautify()
    {
        Color bg = new Color(250,250,252);
        Color accent = new Color(52,152,219);
        getContentPane().setBackground(bg);
        instructionText.setBackground(Color.WHITE);
        instructionText.setForeground(new Color(60,60,60));
        instructionText.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210,210,210)),
                BorderFactory.createEmptyBorder(8,10,8,10)));
        returnButton.setBackground(accent);
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusPainted(false);
        returnButton.setBorder(BorderFactory.createEmptyBorder());
    }
}