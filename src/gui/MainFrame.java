package gui;

import entity.CodeHashMap;
import entity.Node;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainFrame extends JFrame {
    private JTextField fileText1;
    private JTextField fileText2;
    private JTextArea statistics1;
    private JTextArea statistics2;
    private JTextArea conclusion;
    private File file1;
    private File file2;
    private final CodeHashMap map1 = new CodeHashMap();
    private final CodeHashMap map2 = new CodeHashMap();
    private final CodeHashMap identifierMap1 = new CodeHashMap("identifier");
    private final CodeHashMap identifierMap2 = new CodeHashMap("identifier");
    private boolean sortDescending = true; // 排序方向

    public MainFrame() {
        setTitle("代码相似度比较系统");
        setSize(820, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        JButton choose1 = new JButton("选择文件1");
        choose1.setBounds(20, 15, 110, 32);
        choose1.addActionListener(new FileAction(1));
        add(choose1);

        JButton choose2 = new JButton("选择文件2");
        choose2.setBounds(20, 55, 110, 32);
        choose2.addActionListener(new FileAction(2));
        add(choose2);

        fileText1 = new JTextField();
        fileText1.setBounds(140,15,420,32);
        fileText1.setEditable(false);
        add(fileText1);

        fileText2 = new JTextField();
        fileText2.setBounds(140,55,420,32);
        fileText2.setEditable(false);
        add(fileText2);

        JButton compare = new JButton("开始对比");
        compare.setBounds(580,15,210,72);
        compare.addActionListener(e -> doCompare());
        add(compare);

        statistics1 = new JTextArea();
        statistics1.setEditable(false);
        JScrollPane sp1 = new JScrollPane(statistics1);
        sp1.setBounds(20,110,370,250);
        add(sp1);

        statistics2 = new JTextArea();
        statistics2.setEditable(false);
        JScrollPane sp2 = new JScrollPane(statistics2);
        sp2.setBounds(410,110,370,250);
        add(sp2);

        conclusion = new JTextArea();
        conclusion.setEditable(false);
        conclusion.setBounds(20,370,760,110);
        add(conclusion);

        JButton exportStat = new JButton("导出统计");
        exportStat.setBounds(200,500,110,40);
        exportStat.addActionListener(e -> exportStatistics());
        add(exportStat);

        JButton exportSim = new JButton("导出相似度");
        exportSim.setBounds(330,500,130,40);
        exportSim.addActionListener(e -> exportSimilarity());
        add(exportSim);

        JButton sortToggle = new JButton("切换排序(当前:降序)");
        sortToggle.setBounds(480,500,160,40);
        sortToggle.addActionListener(e -> {
            sortDescending = !sortDescending;
            sortToggle.setText("切换排序(当前:" + (sortDescending?"降序":"升序") + ")");
            refreshStats();
        });
        add(sortToggle);

        JButton exit = new JButton("退出");
        exit.setBounds(660,500,100,40);
        exit.addActionListener(e -> System.exit(0));
        add(exit);

        beautify();
    }

    private void beautify() {
        Color bg = new Color(245,247,250);
        Color accent = new Color(52,152,219);
        getContentPane().setBackground(bg);
        for (Component c : getContentPane().getComponents()) {
            if (c instanceof JButton) {
                JButton b = (JButton)c;
                b.setBackground(accent);
                b.setForeground(Color.WHITE);
                b.setBorder(BorderFactory.createEmptyBorder());
                b.setFocusPainted(false);
            }
            if (c instanceof JTextArea) {
                JTextArea t = (JTextArea)c;
                t.setFont(new Font("Monospaced", Font.PLAIN, 13));
                t.setBorder(BorderFactory.createLineBorder(new Color(210,210,210)));
            }
            if (c instanceof JTextField) {
                JTextField tf = (JTextField)c;
                tf.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200,200,200)),
                        BorderFactory.createEmptyBorder(2,4,2,4)));
            }
        }
        conclusion.setBorder(BorderFactory.createTitledBorder("相似度结果"));
    }

    private void doCompare() {
        if (file1 == null || file2 == null) {
            JOptionPane.showMessageDialog(this, "请先选择两个文件", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double s1 = map1.compareCode(map2);
        double s2 = identifierMap1.compareIIdentifier(identifierMap2);
        conclusion.setText("关键字相似度值: " + s1 + "\n用户标识符相似度值: " + s2 + "\n" + makeConclusion(s1,s2));
    }

    private String makeConclusion(double s1, double s2) {
        if (s1 == 1 && s2 == 1) return "判定为同一文件";
        if (s1 == 1) return "关键字完全一致，高度相似";
        if (s1 > 0.5 || s2 > 0.1) return "整体高度相似";
        if (s1 > 0.2 || s2 > 0.05) return "部分结构相似";
        return "相似度较低";
    }

    private void exportStatistics() {
        if (file1 == null && file2 == null) {
            JOptionPane.showMessageDialog(this, "没有可导出的统计", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("statistics.txt"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter pw = new PrintWriter(chooser.getSelectedFile(), "UTF-8")) {
                pw.println("==== File1 关键字 ====");
                if (file1 != null) writeMap(pw, map1); else pw.println("(未选择)");
                pw.println();
                pw.println("==== File1 标识符 ====");
                if (file1 != null) writeMap(pw, identifierMap1); else pw.println("(未选择)");
                pw.println();
                pw.println("==== File2 关键字 ====");
                if (file2 != null) writeMap(pw, map2); else pw.println("(未选择)");
                pw.println();
                pw.println("==== File2 标识符 ====");
                if (file2 != null) writeMap(pw, identifierMap2); else pw.println("(未选择)");
                JOptionPane.showMessageDialog(this, "统计导出成功");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "导出失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportSimilarity() {
        if (file1 == null || file2 == null) {
            JOptionPane.showMessageDialog(this, "请选择两个文件", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("similarity.txt"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter pw = new PrintWriter(chooser.getSelectedFile(), "UTF-8")) {
                double s1 = map1.compareCode(map2);
                double s2 = identifierMap1.compareIIdentifier(identifierMap2);
                pw.println("文件1: " + file1.getName());
                pw.println("文件2: " + file2.getName());
                pw.println("关键字相似度: " + s1);
                pw.println("用户标识符相似度: " + s2);
                pw.println("结论: " + makeConclusion(s1,s2));
                JOptionPane.showMessageDialog(this, "相似度结果导出成功");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "导出失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void writeMap(PrintWriter pw, CodeHashMap map) {
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < map.getSt().length; i++) {
            for (Node x = map.getSt()[i].getHead(); x != null; x = x.getNext()) {
                if (x.getCount() != 0) nodes.add(x);
            }
        }
        nodes.sort(Comparator.comparingInt(Node::getCount));
        if (sortDescending) {
            for (int i = nodes.size() - 1; i >= 0; i--) pw.println(nodes.get(i).getKey() + " = " + nodes.get(i).getCount());
        } else {
            for (Node n : nodes) pw.println(n.getKey() + " = " + n.getCount());
        }
    }

    private void refreshStats() {
        if (file1 != null) fillArea(statistics1, map1, identifierMap1);
        if (file2 != null) fillArea(statistics2, map2, identifierMap2);
    }

    private class FileAction implements ActionListener {
        private final int index;
        FileAction(int idx){ this.index = idx; }
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("JAVA", "java"));
            if (chooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                if (index == 1) {
                    file1 = f;
                    map1.wordsStatistics(file1);
                    identifierMap1.identifierStatistics(file1);
                    fillArea(statistics1, map1, identifierMap1);
                    fileText1.setText(file1.getName());
                } else {
                    file2 = f;
                    map2.wordsStatistics(file2);
                    identifierMap2.identifierStatistics(file2);
                    fillArea(statistics2, map2, identifierMap2);
                    fileText2.setText(file2.getName());
                }
            }
        }
    }

    private void fillArea(JTextArea area, CodeHashMap kw, CodeHashMap id) {
        area.setText("关键字频率统计(排序:" + (sortDescending?"降序":"升序") + "):\n");
        List<Node> kwNodes = collect(kw);
        List<Node> idNodes = collect(id);
        appendNodes(area, kwNodes);
        area.append("\n-----------------------------\n用户标识符频率统计:\n");
        appendNodes(area, idNodes);
    }

    private List<Node> collect(CodeHashMap map) {
        List<Node> list = new ArrayList<>();
        for (int i = 0; i < map.getSt().length; i++) {
            for (Node x = map.getSt()[i].getHead(); x != null; x = x.getNext()) {
                if (x.getCount() != 0) list.add(x);
            }
        }
        list.sort(Comparator.comparingInt(Node::getCount));
        if (sortDescending) {
            List<Node> rev = new ArrayList<>();
            for (int i = list.size()-1; i>=0; i--) rev.add(list.get(i));
            return rev;
        }
        return list;
    }

    private void appendNodes(JTextArea area, List<Node> nodes) {
        for (Node n : nodes) area.append("  " + n.getKey() + " : " + n.getCount() + "\n");
    }
}
