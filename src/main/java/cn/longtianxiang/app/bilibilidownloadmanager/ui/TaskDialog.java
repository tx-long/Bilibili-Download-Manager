package cn.longtianxiang.app.bilibilidownloadmanager.ui;

import cn.longtianxiang.app.bilibilidownloadmanager.util.Utils;

import javax.swing.*;
import java.awt.*;

public class TaskDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton buttonPause;
    private JButton buttonHideDetail;
    private JTable table;
    private JProgressBar progressBar;
    private JLabel total;
    private JLabel done;
    private JLabel title;

    public TaskDialog() {
        setSize(650, 506);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setContentPane(contentPane);
    }

    public void display(Component location){
        SwingUtilities.invokeLater(() -> {
            setLocationRelativeTo(location);
            setVisible(true);
        });
    }

    public void conceal(){
        SwingUtilities.invokeLater(() -> {
            setVisible(false);
        });
    }

    public void showTitle(String s){
        SwingUtilities.invokeLater(() -> {
            title.setText(s);
        });
    }

    public void showTotal(long totalLength){
        String totalSizeStr = Utils.smartFormatDataSize(totalLength);
        SwingUtilities.invokeLater(() -> {
            total.setText(totalSizeStr);
        });
    }

    public void showDone(long totalLength, long totalDone){
        System.err.println("totalLength: " + totalLength + ", totalDone: " + totalDone);
        String doneStr = Utils.smartFormatDataSize(totalDone);
        String percentageStr = Utils.getPercentageStr(totalDone, totalLength, 2);
        SwingUtilities.invokeLater(() -> {
            done.setText(doneStr + " ( " + percentageStr + " )");
        });
    }

    public void showProgress(long totalLength, long totalDone){
        double rate = Utils.getRate(totalDone, totalLength);
        int value = (int) (rate * progressBar.getMaximum());
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(value);
        });
    }
}
