/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wuziqi;
import javax.swing.*;
import java.awt.*;
/**
 *这个类用来实现界面的构建，监听器的注册，至于事件响应在其它类中实现
 * @author ying
 */
public class WuziqiView {
    //定义成员变量
    JFrame frame;//主窗体
    JPanel qiPan;//构建棋盘的面板
    JButton[] buttonArray;//棋盘中的每一个关键点
    JTextArea dialogArea;//会话窗口
    JTextField dialogField;
    JButton dialogButton;
    JTextField hostAddress;//这个标签输入要连接的主机的地址
    JTextField hostPort;//主机端口号
    JButton buttonConn;//这个按钮用来确定地址输入无误并连接主机
    JButton buttonReady;//准备按钮

    WuziqiLogic wuziqiLogic;//包含一个内部逻辑
    public WuziqiView(){
        //创建组件
        frame = new JFrame("五子棋客户端");//创建主窗体 顶层容器
        frame.setLayout(new BorderLayout());//为主窗体frame设置布局管理器

        //center位置的组件
        qiPan = new JPanel();//创建一个面板 这个面板来存放棋盘
        GridLayout gridLayout = new GridLayout(15,15);//创建一个GridLayout布局管理器 维护一个15*15的网格
        gridLayout.setHgap(0);//将组件的水平间距和垂直间距设置为零
        gridLayout.setVgap(0);
        qiPan.setLayout(gridLayout);//把gridLayout作为qiPan的布局管理器
        qiPan.setSize(615, 615);//设置棋盘的大小

        ImageIcon img = new ImageIcon("imgs/every.jpg");//创建一个图片对象
        buttonArray = new JButton[225];//创建一个按钮数组15*15个，来构成这个棋盘
        for (int i = 0; i < 225; i++) {
            if (i > 0 && i < 14) {//设置最上面一行的图片
                buttonArray[i] = new JButton(new ImageIcon("imgs/every_top.jpg"));//初始化这些按钮，为按钮设置图片
                buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                buttonArray[i].setSize(41, 41);//设置按钮的大小
                qiPan.add(buttonArray[i]);//添加这些按钮到qiPan中
            } else if (i % 15 == 0) {//设置左边一列的图片
                if (i == 0) {//设置左上角
                    buttonArray[i] = new JButton(new ImageIcon("imgs/every_topLeft.jpg"));//初始化这些按钮，为按钮设置图片
                    buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                    buttonArray[i].setSize(41, 41);//设置按钮的大小
                    qiPan.add(buttonArray[i]);//添加这些按钮到qiPan中
                } else if (i == 210) {//左下角
                    buttonArray[i] = new JButton(new ImageIcon("imgs/every_bottomLeft.jpg"));//初始化这些按钮，为按钮设置图片
                    buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                    buttonArray[i].setSize(41, 41);//设置按钮的大小
                    qiPan.add(buttonArray[i]);//添加这些按钮到qiPan中
                } else {
                    buttonArray[i] = new JButton(new ImageIcon("imgs/every_left.jpg"));//初始化这些按钮，为按钮设置图片
                    buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                    buttonArray[i].setSize(41, 41);//设置按钮的大小
                    qiPan.add(buttonArray[i]);//添加这些按钮到qiPan中
                }
            } else if (i > 210 && i < 224) {//设置下边一行图片
                buttonArray[i] = new JButton(new ImageIcon("imgs/every_bottom.jpg"));//初始化这些按钮，为按钮设置图片
                buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                buttonArray[i].setSize(41, 41);//设置按钮的大小
                qiPan.add(buttonArray[i]);//添加这些按钮到qiPan中
            } else if (i % 15 == 14) {//设置最右边一列图片
                if (i == 14) {//设置右上角
                    buttonArray[i] = new JButton(new ImageIcon("imgs/every_topRight.jpg"));//初始化这些按钮，为按钮设置图片
                    buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                    buttonArray[i].setSize(41, 41);//设置按钮的大小
                    qiPan.add(buttonArray[i]);//添加这些按钮到qiPan中
                } else if (i == 224) {//设置右下角
                    buttonArray[i] = new JButton(new ImageIcon("imgs/every_bottomRight.jpg"));//初始化这些按钮，为按钮设置图片
                    buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                    buttonArray[i].setSize(41, 41);//设置按钮的大小
                    qiPan.add(buttonArray[i]);//添加这些按钮到qiPan中
                } else {
                    buttonArray[i] = new JButton(new ImageIcon("imgs/every_right.jpg"));//初始化这些按钮，为按钮设置图片
                    buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                    buttonArray[i].setSize(41, 41);//设置按钮的大小
                    qiPan.add(buttonArray[i]);//添加这些按钮到qiPan中
                }
            } else if (i == 112 || i == 48 || i == 56 || i == 176 || i == 168) {//设置棋盘中几个关键点的位置！
                buttonArray[i] = new JButton(new ImageIcon("imgs/every_center.jpg"));//初始化这些按钮，为按钮设置图片
                buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                buttonArray[i].setSize(41, 41);//设置按钮的大小
                qiPan.add(buttonArray[i]);//添加这些按钮到qiPan中
            } else {//设置其余的
                buttonArray[i] = new JButton(img);//初始化这些按钮，为按钮设置图片
                buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                buttonArray[i].setSize(41, 41);//设置按钮的大小
                qiPan.add(buttonArray[i]);//添加这些按钮到qiPan中
            }
            //buttonArray[i].addMouseListener(new QiPanMouseListener(i,buttonArray[i]));//为每个按钮注册监听器
        }

        //east位置组件
        JPanel eastPanel = new JPanel(new BorderLayout());//存放组件的面板 用一行一列的布局管理器 竖着排列
        JPanel eastPanelOne = new JPanel();//这个面板使用默认布局管理器
        dialogArea = new JTextArea("欢迎使用五子棋游戏\n"); //实例化组件
        dialogArea.setEditable(false);//设置为不可编辑
        dialogArea.setLineWrap(true);//设置自动换行
        JScrollPane dialogScrollPane = new JScrollPane(dialogArea);//这个是为聊天框添加的滚动面板
        dialogField = new JTextField("请输入聊天信息",15);
        dialogButton = new JButton("发送");
        //这里要为button 和 dialogArea都要注册监听器，来发送聊天信息，button就不用说了，dialogArea要注册成按回车键发送信息 恩恩..
        //dialogButton.addMouseListener(new SendMessageButtonListener(buttonConnMouseListener.getWuziqiClient(),dialogField,dialogArea));//先为按钮添加一个监听器吧！
        //这里出现了一个问题，这样设置监听器是不行的，因为在初始化的时候并没有建立连接，所以这个链接一直是空，这个按钮的监听器应该在conn按钮的方法中注册，因为只有连接成功后才能开始通讯
        eastPanelOne.add(dialogField);//添加组件到面板
        eastPanelOne.add(dialogButton);
        eastPanel.add(new JLabel("聊天框"),BorderLayout.NORTH);
        eastPanel.add(dialogScrollPane,BorderLayout.CENTER);
        eastPanel.add(eastPanelOne,BorderLayout.SOUTH);

        //north位置的组件
        JPanel panelNorth = new JPanel();//这个是向north位置放的面板
        buttonConn = new JButton("连接");//labelOne
        buttonReady = new JButton("准备");//准备按钮
        buttonReady.setEnabled(false);//初始化的时候禁用此按钮
        hostAddress = new JTextField("115.61.xxx.xxx",8);
        hostPort = new JTextField("端口号",4);
        //为buttonConn添加监听器
        ButtonConnMouseListener buttonConnMouseListener = new ButtonConnMouseListener(hostAddress,hostPort,buttonConn,wuziqiLogic,new ControlViewThread(buttonArray,buttonConn,dialogArea,buttonReady),buttonArray,dialogField,dialogButton,dialogArea,buttonReady);
        buttonConn.addActionListener(buttonConnMouseListener);
        panelNorth.add(new JLabel("请输入要连接的主机地址:"));
        panelNorth.add(hostAddress);//添加这两个组件到面板中
        panelNorth.add(new JLabel("请输入端口号："));
        panelNorth.add(hostPort);
        panelNorth.add(buttonConn);
        panelNorth.add(buttonReady);

        frame.add(qiPan,BorderLayout.CENTER);//添加qiPan面板到frame中
        frame.add(panelNorth,BorderLayout.NORTH);//添加面板到主窗体
        frame.add(eastPanel,BorderLayout.EAST);

        frame.setSize(800,600);//设置窗口的大小
        frame.setResizable(false);//固定窗口大小
        frame.setVisible(true);//窗口设置为可见
    }
}
