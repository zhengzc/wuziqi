/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wuziqi;

import javax.swing.*;
import java.awt.event.*;
/**
 *这个类用来实现 对连接按钮点击事件的捕获处理
 * 这个按钮主要的功能就是建立连接  这里暂且让它能够开始游戏 那么它就应该有一个控制棋盘的对象
 * 界面中其它的按钮都应该在这个点击事件发生时候注册，因为只有在连接建立成功之后其余按钮才是有意义的
 * @author ying
 */
public class ButtonConnMouseListener implements ActionListener{
    JTextField ipAddress;//ip地址 传递这两个对象得到数据
    JTextField port;//端口号
    JButton buttonConn;//连接按钮
    WuziqiClient clientSocket;//这个类里面应该有一个客户端连接
    WuziqiLogic wuziqiLogic;//内部逻辑
    ControlViewThread controlView;//控制棋盘的对象
    JButton[] buttonArray;//棋盘数组 这里给棋盘注册监听器
    JTextField dialogField;//聊天框！
    JButton dialogButton;//聊天发送按钮
    JTextArea dialogArea;//聊天框

    JButton buttonReady;//准备按钮

    public ButtonConnMouseListener(JTextField ipAddress,JTextField port,JButton buttonConn,WuziqiLogic wuziqiLogic,ControlViewThread controlView,JButton[] buttonArray,JTextField dialogField,JButton dialogButton,JTextArea dialogArea,JButton buttonReady){
        this.ipAddress = ipAddress;
        this.port = port;
        this.buttonConn = buttonConn;
        this.wuziqiLogic = wuziqiLogic;
        this.controlView = controlView;
        this.buttonArray = buttonArray;
        this.dialogField = dialogField;
        this.dialogButton = dialogButton;
        this.dialogArea = dialogArea;
        this.buttonReady = buttonReady;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        clientSocket = new WuziqiClient(ipAddress.getText(), Integer.valueOf(port.getText()));//得到客户端的连接
        if (clientSocket.isConnected()) {
            clientSocket.sendMessage("连接建立成功!");
            buttonConn.setEnabled(false);//连接建立成功后 设置为不可激活状态
            //String messageFormServer = clientSocket.getMessage();//获得服务端发来的信息
            //建立一个五子棋的内部逻辑
            wuziqiLogic = new WuziqiLogic();
            controlView.setWuziqiLogic(wuziqiLogic);//设置这个字段
            //开始为聊天框设置监听器
            //这里要为button 和 dialogArea都要注册监听器，来发送聊天信息，button就不用说了，dialogArea要注册成按回车键发送信息 恩恩..
            dialogButton.addMouseListener(new SendMessageButtonListener(clientSocket,dialogField,dialogArea));//先为按钮添加一个监听器吧！
            //这里将来要为dialogField设置监听器，主要是回车发送！
            dialogField.addKeyListener(new dialogFieldListener(clientSocket,dialogField,dialogArea));//为这个框框设置个ENTER键发送
            //为棋盘是指监听器
            //System.out.print("为棋盘设置监听器...");
            //for (int i = 0; i < 225; i++) {
            //    buttonArray[i].addMouseListener(new QiPanMouseListener(i, buttonArray[i], clientSocket,wuziqiLogic));//为每个按钮注册监听器
            //}
            //while (messageFormServer.charAt(0) != '$') {//这里用前面加$符号的字符串来表示棋子的变化 如果这个不是棋盘控制信息 而是聊天信息
            //    if ("36479543".equals(messageFormServer)) {//判断是否读取信息成功
            //        System.out.println("建立连接或读取信息失败！");
            //        break;
            //   }
            //    System.out.println("聊天信息调用成功！");//测试
            //    System.out.println("message form client:" + messageFormServer);
            //}

            //激活准备按钮
            buttonReady.setEnabled(true);//激活按钮
            ButtonReadyListener buttonReadyListener = new ButtonReadyListener(controlView,buttonArray,buttonReady);
            buttonReadyListener.setWuziqiServer(clientSocket);
            buttonReadyListener.setWuziqiLogic(wuziqiLogic);
            buttonReady.addActionListener(buttonReadyListener);

            System.out.println("从对方获得棋盘控制信息成功，开始对棋盘进行控制");//测试
            //controlView.Control(clientSocket);//对棋盘进行控制，开始对弈
            //更换为线程监听
            controlView.setWuziqiClient(clientSocket);//设置controlView的socket连接对象
            Thread clientThread = new Thread(controlView);//获得这个进程
            clientThread.start();//启动这个线程
        } else {//如果获取连接失败 那么就提示错误，重要的是如何实现重新连接？
            System.out.println("ButtonConnMouseListener连接建立失败！");//测试
            dialogArea.append("系统提示：\n连接已断开请重新开启服务器！\n");
            clientSocket.closeClient();//关闭连接
            buttonConn.setEnabled(true);//连接建立成功后 设置为可激活状态
        }
    }

    public WuziqiClient getWuziqiClient(){//返回这个类所建立的连接
            return clientSocket;
    }
}
