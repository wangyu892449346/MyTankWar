package GameScreen;

import Tank.Monster;
import Tank.Recorder;
import UIElement.CommonWall;
import Util.Audio;
import Util.CommonUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener {

    private boolean ifStart = false;
    // 定义组件
    private GamePanel gamepanel = null;
    private GameStartPanel gamestartpanel;

    // 构造函数
    public MainFrame() {
        setMenuBarAttr();
        // 实例化开始面板
        gamestartpanel = new GameStartPanel();
        gamestartpanel.setBackground(Color.BLACK);
        resetGamePanel(GameStatus.ShowStartScreen);
        setMainFrameAttr();
    }

    /**
     * 设置MenuBar的属性
     */
    private void setMenuBarAttr() {
        // 菜单条
        MenuBar mb = new MenuBar();
        String[] menuString = new String[]{"游戏开始", "游戏说明", "游戏帮助", "游戏作者"};
        Menu[] menus = new Menu[menuString.length];

        // 菜单条上的菜单
        for (int i = 0; i < menuString.length; ++i) {
            menus[i] = new Menu(menuString[i]);
            // 设置字体
            menus[i].setFont(new Font("微软雅黑", Font.BOLD, 18));
        }

        // 开始游戏的子菜单
        String[] childMenusString = new String[]{
                "开始 游戏", "继续游戏", "保存游戏",
                "级别  1", "级别  2", "级别  3",
                "直接退出游戏", "存盘退出游戏"
        };
        String[] childMenuCommand = new String[]{
                "start", "continue", "save",
                "level1", "level2", "level3",
                "exit", "saveBeforeExit"
        };
        MenuItem[] childMenus = new MenuItem[childMenusString.length];
        int[] separatorIndex = new int[]{3, 6};
        // 创建游戏开始菜单下的子菜单并添加到Menu
        createMenuItem(childMenusString, childMenuCommand, childMenus, separatorIndex, menus, 0);

        // 游戏说明的子菜单
        String[] childMenuString2 = new String[]{
                "游戏开发说明", "开始游戏说明", "继续游戏说明", "级别  1", "级别  2", "级别  3"
        };

        String[] childMenuCommand2 = new String[]{
                "instruction", "instruction1", "ContinueIntroduction",
                "instruction2", "instruction3", "instruction4"
        };
        MenuItem[] childMenus2 = new MenuItem[childMenuString2.length];
        int[] separatorIndex2 = new int[]{3};
        // 创建游戏开始菜单下的子菜单并添加到Menu
        createMenuItem(childMenuString2, childMenuCommand2, childMenus2, separatorIndex2, menus, 1);

        // 游戏帮助下的子菜单
        MenuItem menuHelp = new MenuItem("游戏帮助");
        menuHelp.setFont(new Font("微软雅黑", Font.BOLD, 15));
        menuHelp.addActionListener(this);
        menuHelp.setActionCommand("help");
        menus[2].add(menuHelp);

        // 游戏作者下的子菜单
        MenuItem menuAuthor = new MenuItem("游戏作者");
        menuAuthor.setFont(new Font("微软雅黑", Font.BOLD, 15));
        menuAuthor.addActionListener(this);
        menuAuthor.setActionCommand("author");
        // 游戏作者菜单栏
        menus[3].add(menuAuthor);

        // 添加到MenuBar
        for (Menu menu : menus) {
            mb.add(menu);
        }

        // 添加到菜单条上
        this.setMenuBar(mb);
    }

    /**
     * 设置MainFrame的属性
     */
    private void setMainFrameAttr() {
        // 界面的设置
        this.setIconImage((new ImageIcon(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("images/wangyu.jpg")))).getImage());
        // 设置软件名字
        this.setTitle("MyTankWar-无聊版");
        // 禁止用户改变窗体大小
        this.setResizable(false);
        // 设置窗体的尺寸
        this.setSize(CommonUtil.screenWidth, CommonUtil.screenHeight + 30);
        // 设置窗体出现的位置
        this.setLocation(250, 6);
        // 退出时清除占用的内存
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // 可视化
        this.setVisible(true);
    }

    /**
     * 创建Menu的子菜单
     *
     * @param childMenuString  子菜单标签
     * @param childMenuCommand 子菜单的事件标签
     * @param childMenus       子菜单的MenuItem
     * @param separatorIndex   分割线的索引地址
     * @param menus            主菜单
     * @param menuIndex        主菜单的索引地址
     */
    private void createMenuItem(String[] childMenuString, String[] childMenuCommand,
                                MenuItem[] childMenus, int[] separatorIndex,
                                Menu[] menus, int menuIndex) {
        for (int i = 0; i < childMenuString.length; ++i) {
            childMenus[i] = new MenuItem(childMenuString[i]);
            // 设置字体
            childMenus[i].setFont(new Font("微软雅黑", Font.BOLD, 15));
            // 添加点击事件
            childMenus[i].addActionListener(this);
            // 添加事件标签
            childMenus[i].setActionCommand(childMenuCommand[i]);
            // 添加MenuItem到Menu
            for (int index : separatorIndex) {
                if (index == i) {
                    menus[menuIndex].addSeparator();
                }
            }
            menus[menuIndex].add(childMenus[i]);
        }
    }

    /**
     * 事件监听
     */
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "instruction":
                showIntroduceDialog("个人制作", "的仿制版坦克大战游戏！", "游戏说明");
                break;
            case "author":
                showIntroduceDialog("计算机科学与技术1155班", "1号的无聊的坦克大战", "游戏作者");
                break;
            case "help":
                showIntroduceDialog("用→ ← ↑ ↓控制方向，空格键开火，回车键开始！", ")", "游戏帮助");
                break;
            case "start": {
                Monster.getInstance().clearMonsters();
                setGameLevel(0);

                gamepanel.setBackground(Color.BLACK);
                GamePanel.continueBtn = true;
                CommonUtil.getInstance().startSingleThread(gamepanel);
                // 启动声音
                new Audio("bgm.wav").start();
                resetGamePanel(GameStatus.StartGame);
                this.setVisible(true);
                break;
            }
            case "level1":
                Change2Level(1, "是否使用级别1模式");
                break;
            case "level2":
                Change2Level(2, "是否使用级别2模式");
                break;
            case "level3":
                Change2Level(3, "是否使用级别3模式");
                break;
            case "instruction2":
                showIntroduceDialog("级别1：", "改变怪物连发子弹数为：3，怪物数为：8", "游戏帮助");
                break;
            case "instruction3":
                showIntroduceDialog("级别2：", "改变怪物连发子弹数为：4，怪物数为：9", "游戏帮助");
                break;
            case "instruction4":
                showIntroduceDialog("级别3：", "改变怪物连发子弹数为：5，怪物数为：10", "游戏帮助");
                break;
            case "exit": {
                Object[] options = {"不玩了！", "继续玩！"};
                int response = showChoiceStyleDialog(options, "退出游戏?");
                if (response == 0) {
                    // 正常退出程序
                    System.exit(0);
                }
                break;
            }
            case "saveBeforeExit": {
                Object[] options = {"保存并退出", "继续玩游戏"};
                int response = showChoiceStyleDialog(options, "存盘并退出游戏？");
                if (response == 0) {
                    // 保存我的坦克的信息和怪物的信息
                    new Recorder().save();
                    // 正常退出程序
                    System.exit(0);
                }
                break;
            }
            case "save":
                // 保存我的坦克的信息和怪物的信息
                CommonUtil.getInstance().startSingleThread(new Recorder());
                break;
            case "continue": {
                if (GamePanel.continueBtn) {
                    Object[] options = {"恢复游戏", "继续游戏"};
                    int response = showChoiceStyleDialog(options,
                            "恢复保存的游戏并放弃本次游戏？");
                    if (response == 0) {
                        this.remove(gamepanel);
                        this.removeKeyListener(gamepanel);
                        clearMonsterAndCWalls();
                    }
                }

                // 初始化界面
                this.gamepanel = new GamePanel(GamePanelStatus.Continue);
                resetGamePanel(GameStatus.StartGame);
                // 启动线程
                CommonUtil.getInstance().startSingleThread(gamepanel);
                this.setVisible(true);
                break;
            }
            case "ContinueIntroduction":
                showIntroduceDialog("继续游戏", "恢复游戏为保存的状态", "游戏帮助");
                break;
        }
    }

    /**
     * 切换Level
     *
     * @param level   等级
     * @param message 弹出的Dialog的信息
     */
    private void Change2Level(int level, String message) {
        int response = showDialogMessage(message);
        if (response == 0) {
            switch (level) {
                case 1:
                    setGamePanelAttr(8, 3, 1);
                    break;
                case 2:
                    setGamePanelAttr(9, 4, 2);
                    break;
                case 3:
                    setGamePanelAttr(10, 5, 3);
                    break;
                default:
                    break;
            }
            clearMonsterAndCWalls();
        }
    }

    private int showDialogMessage(String message) {
        Object[] options = {"确定", "取消"};
        return JOptionPane.showOptionDialog(this, message, "",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                options, options[0]);
    }

    public void run() {
        while (true) {
            this.start();
        }
    }

    private void start() {
        // 判断游戏的输赢
        if (GamePanel.buttonWin || GamePanel.buttonFail) {
            if (GamePanel.buttonWin) {
                Object[] options = {"确定", "取消"};
                if (showChoiceDialog(options, "是否继续闯关") == 0) {
                    clearMonsterAndCWalls();
                    // 设置游戏的段位
                    setGameLevel(GamePanel.level + 1);
                }
                GamePanel.buttonWin = false;
            }
            if (GamePanel.buttonFail) {
                Object[] options = {"确定", "取消"};
                int response = showChoiceDialog(options, "是否继续游戏");
                if (response == 0) {
                    clearMonsterAndCWalls();
                    setGameLevel(GamePanel.level);
                }
                GamePanel.buttonFail = false;
            }
            try {
                Thread.sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!this.ifStart) {
            try {
                // 开始游戏
                if (GameStartPanel.startGameBtn) {
                    Monster.getInstance().clearMonsters();
                    // 设置游戏的段位
                    setGameLevel(0);

                    gamepanel.setBackground(Color.BLACK);
                    GamePanel.continueBtn = true;

                    CommonUtil.getInstance().startSingleThread(gamepanel);

                    // 启动声音
                    new Audio("bgm.wav").start();
                    resetGamePanel(GameStatus.StartGame);
                    this.setVisible(true);
                    this.ifStart = true;
                }
                // 继续游戏
                if (GameStartPanel.continueBtn) {
                    // 删除旧的面板
                    this.remove(gamestartpanel);
                    this.removeKeyListener(gamestartpanel);
                    if (GamePanel.continueBtn) {
                        Object[] options = {"恢复游戏", "继续游戏"};
                        int response = showChoiceStyleDialog(options,
                                "恢复保存的游戏并放弃本次游戏?");
                        if (response == 0) {
                            this.remove(gamepanel);
                            this.removeKeyListener(gamepanel);
                            clearMonsterAndCWalls();
                        }
                    }
                    // 初始化界面
                    gamepanel = new GamePanel(GamePanelStatus.Continue);
                    // 启动线程
                    CommonUtil.getInstance().startSingleThread(gamepanel);
                    this.add(gamepanel);
                    this.addKeyListener(gamepanel);
                    this.setVisible(true);
                    this.ifStart = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置当前游戏的属性
     *
     * @param level 需要设置的等级
     */
    private void setGameLevel(int level) {
        switch (level) {
            case 0:
                setGamePanelAttr(6, 2, 0);
                break;
            case 1:
                setGamePanelAttr(8, 3, 1);
                break;
            case 2:
                setGamePanelAttr(9, 4, 2);
                break;
            case 3:
                setGamePanelAttr(10, 10, 3);
                break;
            default:

                break;
        }
    }

    private void setGamePanelAttr(int monsterSize, int bulletSize, int level) {
        Monster.monsterSize = monsterSize;
        Monster.bulletOneTime = bulletSize;
        GamePanel.level = level;
        gamepanel = new GamePanel(GamePanelStatus.StartGame);
    }

    private int showChoiceDialog(Object[] options, String message) {
        return JOptionPane.showOptionDialog(this,
                message, "", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options,
                options[0]);
    }

    private int showChoiceStyleDialog(Object[] options, String message) {
        return JOptionPane.showOptionDialog(this,
                "<html><h2><font color='red'>是否</font>" + message + "</h2></html>",
                "", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options,
                options[0]);
    }


    private void showIntroduceDialog(String redString, String content, String title) {
        JOptionPane
                .showMessageDialog(
                        null,
                        new JLabel("<html><h2><font color='red'>" + redString +
                                "：</font>" + content + "</h2></html>"),
                        title, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * 清除原先储存在vector中的数据，防止多余的数据影响恢复体验
     */
    private void clearMonsterAndCWalls() {
        Monster.getInstance().clearMonsters();
        CommonWall.getInstance().initBWall();
    }

    /**
     * 重置游戏状态
     *
     * @param gameStatus 游戏状态，StartGame为开始游戏，ShowStartScreen显示开始面板
     */
    private void resetGamePanel(GameStatus gameStatus) {
        switch (gameStatus) {
            case StartGame:
                if (gamestartpanel != null) {
                    this.remove(gamestartpanel);
                    this.removeKeyListener(gamestartpanel);
                }
                this.add(gamepanel);
                this.addKeyListener(gamepanel);
                break;
            case ShowStartScreen:
                if (gamepanel != null) {
                    this.remove(gamepanel);
                    this.removeKeyListener(gamepanel);
                }
                this.add(gamestartpanel);
                this.addKeyListener(gamestartpanel);
                break;
            default:
                break;
        }
    }
}
