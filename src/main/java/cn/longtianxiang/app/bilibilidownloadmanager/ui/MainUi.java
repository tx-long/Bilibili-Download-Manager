package cn.longtianxiang.app.bilibilidownloadmanager.ui;

import cn.longtianxiang.app.bilibilidownloadmanager.task.Task;
import cn.longtianxiang.app.bilibilidownloadmanager.value.Constant;
import cn.longtianxiang.app.bilibilidownloadmanager.da.AppProperties;
import cn.longtianxiang.app.bilibilidownloadmanager.util.BvIdGetter;
import cn.zhxu.data.Mapper;
import cn.zhxu.okhttps.OkHttps;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.util.Objects;

/**
 * Created by 龍天翔 on 2023/1/21 at 5:26 PM.
 */
public class MainUi {

    private static class InstanceHolder{
        private static final MainUi INSTANCE = new MainUi();
    }
    public static MainUi getInstance(){
        return InstanceHolder.INSTANCE;
    }

    private static final int MAIN_UI_WIDTH = 1150;
    private static final int MAIN_UI_HEIGHT = 650;
    private enum WhichTable {HIGHEST, LOWER, FAILURE, QUEUE}
    private JFrame frame;
    private JPanel rootPane;
    private JTabbedPane tabbedPaneTask;
    private JScrollPane scrollPaneTable, scrollPaneTaskTree;
    private JMenuBar menuBar;
    private JMenu menuTask, menuAuto, menuPosition, menuAccount, menuWindow;
    private JMenuItem menuItemTaskNew, menuItemTaskBatch, menuItemAutoUp, menuItemAutoUpdateQuality,
            menuItemPositionEdit, menuItemPositionMerge, menuItemAccountImportSessionId, menuItemAccountClearSessionId, menuItemAccountInfo, menuItemWindowAlwaysTop;
    private JTree treeTask;
    private SuccessTable tableHighest;
    private SuccessTable tableLower;
    private FailureTable tableFailure;
    private QueueTable tableQueue;

    public MainUi(){
        createUiComponentsNotInForm();
        setListeners();
        switchTable(WhichTable.HIGHEST);
        tableHighest.loadData();
        tableLower.loadData();
        tableQueue.loadData();
    }

    public void show(){
        frame = new JFrame(Constant.APP_NAME);
        frame.setSize(MAIN_UI_WIDTH, MAIN_UI_HEIGHT);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(frame.getOwner());
        frame.setLocationByPlatform(true);
        frame.setContentPane(rootPane);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    // --------- BEHAVIORS ---------

    private void setListeners(){
        treeTask.addTreeSelectionListener(e -> {
            switch (Objects.requireNonNull(treeTask.getSelectionRows())[0]) {
                case 0 -> switchTable(WhichTable.HIGHEST);
                case 1 -> switchTable(WhichTable.LOWER);
                case 2 -> switchTable(WhichTable.QUEUE);
                case 3 -> switchTable(WhichTable.FAILURE);
            }
        });
        menuItemAccountImportSessionId.addActionListener(e -> {
            String sessionId = JOptionPane.showInputDialog(frame,
                    "高画质视频需要大会员才可下载，输入 Cookie 字段 SESSDATA 的值。", "导入 Session ID", JOptionPane.PLAIN_MESSAGE);
            if(sessionId != null && !sessionId.equals("")) {
                String cookieSessdata = "SESSDATA=" + sessionId + ";";
                AppProperties.set(AppProperties.Key.COOKIE_SESSDATA, cookieSessdata);
                new Thread(this::fetchAndShowLoginInfo).start();
            }
        });
        menuItemAccountClearSessionId.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(frame, "是否清除？", "确认", JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION) {
                AppProperties.set(AppProperties.Key.COOKIE_SESSDATA, "");
                new Thread(this::fetchAndShowLoginInfo).start();
            }
        });
        menuItemPositionEdit.addActionListener(e -> {
            new PositionDialog().display(frame);
        });
        menuItemTaskNew.addActionListener(e -> {
            String content = JOptionPane.showInputDialog(frame,
                    "输入内容来下载:\n   - AV号  - BV号  - 视频页URL", "新建", JOptionPane.PLAIN_MESSAGE);
            String bvId = BvIdGetter.getBvId(content);
            new Thread(() -> {
                try {
                    Task task = Task.create(bvId, false);
                    task.start(true);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }).start();
        });
    }

    private void switchTable(WhichTable whichTable){
        switch (whichTable){
            case HIGHEST:
                scrollPaneTable.setViewportView(tableHighest.get());
                if(!treeTask.getSelectionModel().isRowSelected(0)) {
                    treeTask.setSelectionRow(0);
                }
                break;
            case LOWER:
                scrollPaneTable.setViewportView(tableLower.get());
                if(!treeTask.getSelectionModel().isRowSelected(1)) {
                    treeTask.setSelectionRow(1);
                }
                break;
            case QUEUE:
                    scrollPaneTable.setViewportView(tableQueue.get());
                if(!treeTask.getSelectionModel().isRowSelected(2)) {
                    treeTask.setSelectionRow(2);
                }
                break;
            case FAILURE:
                scrollPaneTable.setViewportView(tableFailure.get());
                if(!treeTask.getSelectionModel().isRowSelected(3)) {
                    treeTask.setSelectionRow(3);
                }
                break;
        }
    }


    public void fetchAndShowLoginInfo(){
        String show;
        try {
            Mapper mapper = OkHttps.sync(Constant.Url.NAV)
                    .addHeader("Cookie", (String) AppProperties.get(AppProperties.Key.COOKIE_SESSDATA))
                    .get()
                    .getBody()
                    .toMapper();
            int code = mapper.getInt("code");
            if (code == 0) {
                Mapper data = mapper.getMapper("data");
                String uname = data.getString("uname");
                Mapper vipLabel = data.getMapper("vip_label");
                String vipText = vipLabel.getString("text");
                show = uname + " | " + vipText;
            } else if (code == -101) {
                show = "账号未登陆";
            }else {
                show = "code: " + code + ", message: " + mapper.getString("message");
            }
        } catch (RuntimeException e) {
            show = "网络异常";
        }
        String finalShow = show;
        SwingUtilities.invokeLater(() -> {
            menuItemAccountInfo.setText(finalShow);
            menuItemAccountInfo.setVisible(true);
        });
    }

    // --------- CREATE UI ---------

    private void createUiComponentsNotInForm() {
        createMenubar();
        createTables();
    }

    private void createTables() {
        tableHighest = new SuccessTable();
        tableLower = new SuccessTable();
        tableFailure = new FailureTable();
        tableQueue = new QueueTable();
    }

    private void createMenubar(){
        menuBar = new JMenuBar();
        menuTask = new JMenu("任务");
        menuAuto = new JMenu("自动");
        menuPosition = new JMenu("位置");
        menuAccount = new JMenu("账号");
        menuWindow = new JMenu("窗口");
        menuItemTaskNew = new JMenuItem("新建");
        menuItemTaskBatch = new JMenuItem("批量");
        menuItemAutoUp = new JMenuItem("UP");
        menuItemAutoUpdateQuality = new JMenuItem("升级画质");
        menuItemPositionEdit = new JMenuItem("编辑");
        menuItemPositionMerge = new JMenuItem("合并");
        menuItemAccountImportSessionId = new JMenuItem("导入 Session ID");
        menuItemAccountClearSessionId = new JMenuItem("清除 Session ID");
        menuItemAccountInfo = new JMenuItem("");
        menuItemWindowAlwaysTop = new JMenuItem("始终在前");
        menuTask.add(menuItemTaskNew);
        menuTask.add(menuItemTaskBatch);
        menuAuto.add(menuItemAutoUp);
        menuAuto.add(menuItemAutoUpdateQuality);
        menuPosition.add(menuItemPositionEdit);
        menuPosition.add(menuItemPositionMerge);
        menuAccount.add(menuItemAccountImportSessionId);
        menuAccount.add(menuItemAccountClearSessionId);
        menuAccount.add(menuItemAccountInfo);
        menuItemAccountInfo.setVisible(false);
        menuWindow.add(menuItemWindowAlwaysTop);
        menuBar.add(menuTask);
        menuBar.add(menuAuto);
        menuBar.add(menuPosition);
        menuBar.add(menuAccount);
        menuBar.add(menuWindow);
    }

    private void createUIComponents() {
        createScrollPanes();
        createTaskTree();
    }

    private void createScrollPanes() {
        scrollPaneTaskTree = new JScrollPane();
        scrollPaneTaskTree.setBorder(new CompoundBorder(new EmptyBorder(3, 0, 0, 0), scrollPaneTaskTree.getBorder()));
        scrollPaneTable = new JScrollPane();
        scrollPaneTable.setBorder(new CompoundBorder(new EmptyBorder(4, 0, 0, 0), scrollPaneTable.getBorder()));
    }

    private void createTaskTree() {
        var nodeRoot = new DefaultMutableTreeNode("任务");
        var nodeHighestQuality = new DefaultMutableTreeNode(" 最高画质");
        var nodeLowerQuality = new DefaultMutableTreeNode(" 更低画质");
        var nodeQueue = new DefaultMutableTreeNode(" 任务队列");
        var nodeFailure = new DefaultMutableTreeNode(" 失败");
        nodeRoot.add(nodeHighestQuality);
        nodeRoot.add(nodeLowerQuality);
        nodeRoot.add(nodeQueue);
        nodeRoot.add(nodeFailure);
        treeTask = new JTree(nodeRoot);
        treeTask.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }
}
