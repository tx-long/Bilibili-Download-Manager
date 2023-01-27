package cn.longtianxiang.app.bilibilidownloadmanager.ui;

import cn.longtianxiang.app.bilibilidownloadmanager.da.AppProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class PositionDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton buttonUse;
    private JButton buttonAdd;
    private JButton buttonRemove;
    private JList<String> list;
    private Vector<String> listData;
    private String[] positionList = new String[]{AppProperties.Key.POSITION_1,
            AppProperties.Key.POSITION_2, AppProperties.Key.POSITION_3,
            AppProperties.Key.POSITION_4, AppProperties.Key.POSITION_5,
            AppProperties.Key.POSITION_6, AppProperties.Key.POSITION_7,
            AppProperties.Key.POSITION_8, AppProperties.Key.POSITION_9,
            AppProperties.Key.POSITION_10};

    public PositionDialog() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        loadListData();
        list.setListData(listData);

        buttonAdd.addActionListener(e -> {
            if(listData.size() >= 10){
                JOptionPane.showMessageDialog(this, "位置最多添加 10 个", "信息", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                if(listData.contains(path)){
                    JOptionPane.showMessageDialog(this, "位置已经被添加", "信息", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                listData.add(listData.size(), path);
                list.setListData(listData);
            }
        });
        buttonRemove.addActionListener(e -> {
            if(listData.size() <= 1){
                JOptionPane.showMessageDialog(this, "位置至少添加 1 个", "信息", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String selectedValue = list.getSelectedValue();
            if(selectedValue != null) {
                listData.remove(selectedValue);
                list.setListData(listData);
            }
        });
        buttonUse.addActionListener(e -> {
            String selectedValue = list.getSelectedValue();
            if(selectedValue != null) {
                listData.remove(selectedValue);
                listData.add(0, selectedValue);
                list.setListData(listData);
            }
        });
    }

    private void loadListData() {
        listData = new Vector<>();
        for (String s : positionList) {
            String path = (String) AppProperties.get(s);
            if(path != null && !path.equals("")) {
                listData.add(path);
            }
        }
    }

    private void saveToProperties(){
        for (int i = 0; i < positionList.length; i++) {
            if(i < listData.size()) {
                String path = listData.get(i);
                AppProperties.set(positionList[i], path);
            }else {
                AppProperties.set(positionList[i], "");
            }
        }
    }

    public void display(Component location){
        setSize(460, 300);
        setLocationRelativeTo(location);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    private void onOK() {
        saveToProperties();
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
