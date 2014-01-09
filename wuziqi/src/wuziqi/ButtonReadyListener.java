/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wuziqi;

import javax.swing.*;
import java.awt.event.*;

/**
 *这个类就是准备按钮的监听器了
 * 主要实现发送准备信息 并且初始化棋局
 * 这里对准备就绪的判断用一下方法来实现：在通信的类中我们添加一个areYouReady字段，这个字段用来确定对方是否准备就绪
 * 注意 是对方是否准备就绪，在这个里面的监听器中，我们查看这个字段，如果为真（对方准备就绪）那么就开始初始化棋局，
 * 点击事件发生后，我们先发送一个自己准备就绪的信息给对方，让对方知道我已经准备就绪了，再检查对方是否也已经准备就绪了
 * 如果对方准备就绪，那么我们就开始初始化这个棋盘，如果对方没有准备好，那么我们就建立一个监听线程，来监听areYouReady字段
 * 这个字段成功我们就初始化棋盘
 * @author ying
 */
public class ButtonReadyListener implements ActionListener{
    //WuziqiView wuziqiView;

    ControlViewThread controlView;//控制棋盘的对象
    WuziqiClient clientSocket;//这个类里面应该有一个服务端连接
    WuziqiLogic wuziqiLogic;//内部逻辑
    JButton[] buttonArray;//棋盘数组 这里给棋盘注册监听器
    JButton buttonReady;//准备按钮

    public ButtonReadyListener(ControlViewThread controlView,JButton[] buttonArray,JButton buttonReady) {
        //this.wuziqiView = wuziqiView;
        this.controlView = controlView;
        this.buttonArray = buttonArray;
        this.buttonReady = buttonReady;
    }

    //设置这个通讯字段
    public void setWuziqiServer(WuziqiClient clientSocket) {
        this.clientSocket = clientSocket;
        //this.controlView = controlView;
    }

    public void setWuziqiLogic(WuziqiLogic wuziqiLogic){
        this.wuziqiLogic = wuziqiLogic;
    }

    @Override
    public void actionPerformed(ActionEvent e) {//可以看得出来 这个事件调用之前我们必须先初始化WuziqiServer这个关键字段
        if (clientSocket.isConnected()) {//如果连接建立成功，那么我们来初始化棋局 开始对弈
            buttonReady.setEnabled(false);//设置这个按钮为不激活状态
            clientSocket.sendMessage("客户端开始准备！");
            System.out.println("客户端开始准备！");
            clientSocket.sendMessage("^300");//我们规定^300为用来识别准备就绪的字段！
            if (clientSocket.getAreYouReady()) {//如果对方已经准备成功！
                //初始化棋子颜色信息
                if(clientSocket.getChessColor() == 1){//如果上次用的棋子为黑色，那么我们这次用白色
                    clientSocket.setChessColor(-1);//我用白色棋子
                    clientSocket.setIsTurn(false);//现在不轮到我落棋
                    clientSocket.sendMessage("初始化棋局成功！请您先手！");//我既然这次用白色 说明对方为黑色，黑色先手
                }else{//如果不是黑色就是白色， 那这次我用黑色
                    clientSocket.setChessColor(1);
                    clientSocket.setIsTurn(true);//现在轮到我落棋
                }
                //初始化这个内部逻辑
                wuziqiLogic.initQiPan();
                controlView.setWuziqiLogic(wuziqiLogic);//设置这个字段System.out.println("开始为棋盘建立监听器.....");//测试

                //初始化棋盘并且为棋盘添加监听器
                //wuziqiView.initQiPan();
                ImageIcon img = new ImageIcon("imgs/every.jpg");//创建一个图片对象
                for (int i = 0; i < 225; i++) {
                    if (i > 0 && i < 14) {//设置最上面一行的图片
                        buttonArray[i].setIcon(new ImageIcon("imgs/every_top.jpg"));//初始化这些按钮，为按钮设置图片
                        //buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                        //buttonArray[i].setSize(41, 41);//设置按钮的大小
                    } else if (i % 15 == 0) {//设置左边一列的图片
                        if (i == 0) {//设置左上角
                            buttonArray[i].setIcon(new ImageIcon("imgs/every_topLeft.jpg"));//初始化这些按钮，为按钮设置图片
                            //buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                            //buttonArray[i].setSize(41, 41);//设置按钮的大小
                        } else if (i == 210) {//左下角
                            buttonArray[i].setIcon(new ImageIcon("imgs/every_bottomLeft.jpg"));//初始化这些按钮，为按钮设置图片
                            //buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                            //buttonArray[i].setSize(41, 41);//设置按钮的大小
                        } else {
                            buttonArray[i].setIcon(new ImageIcon("imgs/every_left.jpg"));//初始化这些按钮，为按钮设置图片
                            //buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                            //buttonArray[i].setSize(41, 41);//设置按钮的大小
                        }
                    } else if (i > 210 && i < 224) {//设置下边一行图片
                        buttonArray[i].setIcon(new ImageIcon("imgs/every_bottom.jpg"));//初始化这些按钮，为按钮设置图片
                        //buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                        //buttonArray[i].setSize(41, 41);//设置按钮的大小
                    } else if (i % 15 == 14) {//设置最右边一列图片
                        if (i == 14) {//设置右上角
                            buttonArray[i].setIcon(new ImageIcon("imgs/every_topRight.jpg"));//初始化这些按钮，为按钮设置图片
                            //buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                            //buttonArray[i].setSize(41, 41);//设置按钮的大小
                        } else if (i == 224) {//设置右下角
                            buttonArray[i].setIcon(new ImageIcon("imgs/every_bottomRight.jpg"));//初始化这些按钮，为按钮设置图片
                            //buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                            //buttonArray[i].setSize(41, 41);//设置按钮的大小
                        } else {
                            buttonArray[i].setIcon(new ImageIcon("imgs/every_right.jpg"));//初始化这些按钮，为按钮设置图片
                            //buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                            //buttonArray[i].setSize(41, 41);//设置按钮的大小
                        }
                    } else if (i == 112 || i == 48 || i == 56 || i == 176 || i == 168) {//设置棋盘中几个关键点的位置！
                        buttonArray[i].setIcon(new ImageIcon("imgs/every_center.jpg"));//初始化这些按钮，为按钮设置图片
                        //buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                        //buttonArray[i].setSize(41, 41);//设置按钮的大小
                    } else {//设置其余的
                        buttonArray[i].setIcon(img);//初始化这些按钮，为按钮设置图片
                        //buttonArray[i].setBorderPainted(false);//去掉按钮的边框
                        //buttonArray[i].setSize(41, 41);//设置按钮的大小
                    }
                    buttonArray[i].addMouseListener(new QiPanMouseListener(i, buttonArray[i], clientSocket, wuziqiLogic,buttonReady,buttonArray));//为每个按钮注册监听器
                }
                clientSocket.sendMessage("对弈开始，祝您好运！");
            } else{//如果没有成功，我们启动一个线程来监听这个准备字段
                Thread areYouReady = new Thread(new ButtonReadyThread(controlView,clientSocket,wuziqiLogic,buttonArray,buttonReady));
                areYouReady.start();//启动这个线程
            }

        } else {//如果连接建立不成功 那么我们提醒一下啦
            System.out.println("连接尚未建立，请建立连接后再准备");
        }
    }
}
