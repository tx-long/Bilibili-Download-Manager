package cn.longtianxiang.app.bilibilidownloadmanager.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * Created by 龍天翔 on 2023/1/22 at 4:08 PM.
 */
public class SuccessTable {
    private JTable table;

    public SuccessTable(){
        createSuccessTable();
    }

    private void createSuccessTable() {
        table = new JTable();
        final String[] COLUMN_NAMES = {"序号", "封面", "标题", "UP", "BVID", "大小", "时间", "FE"};
        table.setModel(new DefaultTableModel(new Object[][] {}, COLUMN_NAMES) {
            final Class<?>[] COLUMN_TYPES = new Class<?>[] {
                    Integer.class, ImageIcon.class, String.class, String.class, String.class, String.class, String.class, Boolean.class
            };
            final boolean[] COLUMN_EDITABLE = new boolean[] {
                    false, true, false, false, false, false, false, false
            };
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return COLUMN_TYPES[columnIndex];
            }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return COLUMN_EDITABLE[columnIndex];
            }
        });
        {
            TableColumnModel cm = table.getColumnModel();
            cm.getColumn(0).setPreferredWidth(25);
            cm.getColumn(1).setPreferredWidth(72);
            cm.getColumn(1).setMaxWidth(72);
            cm.getColumn(2).setPreferredWidth(335);
            cm.getColumn(3).setPreferredWidth(100);
            cm.getColumn(4).setPreferredWidth(80);
            cm.getColumn(5).setPreferredWidth(50);
            cm.getColumn(6).setPreferredWidth(90);
            cm.getColumn(7).setPreferredWidth(5);
        }
        JTableHeader tableHeader = new JTableHeader();
        tableHeader.setColumnModel(table.getColumnModel());
        tableHeader.setPreferredSize(new Dimension(0,21));
        table.setTableHeader(tableHeader);
        table.setRowHeight(45);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
    }

    public JTable get(){
        return table;
    }

    public void loadData(){
        ((DefaultTableModel)table.getModel()).addRow(new Object[]{1, null, "bob up_ぼばっぷ_アイドル_S1H[4K_60P]『クレシェンド』さっぽろ雪まつり20200211",	"执迷终不悟", "BV1U3411d7Yk",	"678.22 MB", "2023-01-22 15:31", true});
        ((DefaultTableModel)table.getModel()).addRow(new Object[]{1, null, "bob up_ぼばっぷ_アイドル_S1H[4K_60P]『クレシェンド』さっぽろ雪まつり20200211",	"执迷终不悟", "BV1U3411d7Yk",	"678.22 MB", "2023-01-22 15:31", true});
        ((DefaultTableModel)table.getModel()).addRow(new Object[]{1, null, "bob up_ぼばっぷ_アイドル_S1H[4K_60P]『クレシェンド』さっぽろ雪まつり20200211",	"执迷终不悟", "BV1U3411d7Yk",	"678.22 MB", "2023-01-22 15:31", true});
        ((DefaultTableModel)table.getModel()).addRow(new Object[]{1, null, "bob up_ぼばっぷ_アイドル_S1H[4K_60P]『クレシェンド』さっぽろ雪まつり20200211",	"执迷终不悟", "BV1U3411d7Yk",	"678.22 MB", "2023-01-22 15:31", true});
        ((DefaultTableModel)table.getModel()).addRow(new Object[]{1, null, "bob up_ぼばっぷ_アイドル_S1H[4K_60P]『クレシェンド』さっぽろ雪まつり20200211",	"执迷终不悟", "BV1U3411d7Yk",	"678.22 MB", "2023-01-22 15:31", true});
    }
}
