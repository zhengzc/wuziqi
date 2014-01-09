/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wuziqi;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
/**
 *这个类用来实现准备按钮线程，用来考察对方是否已经准备就绪
 * @author ying
 */
public class ButtonReadyThread implements Runnable{
    ControlViewThread controlView;//控制棋盘的对象
    WuziqiClient clientSocket;//这个类里面应该有一个服务端连接
    WuziqiLogic wuziqiLogic;//内部逻辑
    JButton[] buttonArray;//棋盘数组 这里给棋盘注册监听器
    JButton buttonReady;

    public ButtonReadyThread(ControlViewThread controlView,WuziqiClient clientSocket,WuziqiLogic wuziqiLogic,JButton[] buttonArray,JButton buttonReady) {
        this.controlView = controlView;
        this.clientSocket = clientSocket;
        this.wuziqiLogic = wuziqiLogic;
        this.buttonArray = buttonArray;
        this.buttonReady = buttonReady;
    }

    public void run(){
        clientSocket.sendMessage("客户端准备就绪！等待中....");
        while(!clientSocket.getAreYouReady()){//如果对方一直没用准备，那我们就每隔一秒来判断一次
            try {
                //如果对方一直没用准备，那我们就每隔一秒来判断一次
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ButtonReadyListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (clientSocket.getAreYouReady()) {//如果对方已经准备成功！
                //初始化棋子颜色信息
                if(clientSocket.getChessColor() == 1){//如果上次用的棋子为黑色，那么我们这次用白色
                    clientSocket.setChessColor(-1);//我用白色棋子
                    clientSocket.setIsTurn(false);//现在不轮到我落棋
                    clientSocket.sendMessage("初始化棋局成功!请您先手！");//我既然这次用白色 说明对方为黑色，黑色先手
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
        }
    }
}