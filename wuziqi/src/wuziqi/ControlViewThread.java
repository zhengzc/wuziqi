/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wuziqi;

import javax.swing.*;

/**
 *这是一个控制棋盘的程序，有控制棋盘和文本区域变化的方法
 * 通过分析得到的信息来控制窗口
 * @author ying
 */
public class ControlViewThread implements Runnable {
    //他的成员变量里面应该有它要控制的对象
    //String serverMessage;//得到的信息 用字符串来表示

    JButton[] button;//对按钮（棋盘）的控制
    JButton buttonConn;
    JTextArea textArea;//对文本区域的控制
    JButton buttonReady;//准备按钮
    WuziqiClient clientSocket;//一个连接对象
    WuziqiLogic wuziqiLogic;//内部逻辑处理
    ImageIcon imgBlack = new ImageIcon("imgs/everyBlack.jpg");//两个棋子 黑色和白色
    ImageIcon imgWhite = new ImageIcon("imgs/everyWhite.jpg");
    ImageIcon nowWhite = new ImageIcon("imgs/nowWhite.jpg");//两个棋子，用来标识刚刚放下的棋子的位置
    ImageIcon nowBlack = new ImageIcon("imgs/nowBlack.jpg");

    //public ControlViewThread(String serverMessage,JButton[] button,JTextArea textArea){//构造方法来传递这些参数
    public ControlViewThread(JButton[] button,JButton buttonConn, JTextArea textArea,JButton buttonReady) {//构造方法来传递这些参数
        //this.serverMessage=serverMessage;
        this.button = button;
        this.buttonConn = buttonConn;
        this.textArea = textArea;
        this.buttonReady = buttonReady;
    }

    public void Control(WuziqiClient clientSocket) {
        String messageFromServer = clientSocket.getMessage();
        while (messageFromServer.charAt(0) != '$') {//这里用前面加$符号的字符串来表示棋子的变化 如果这个不是棋盘控制信息 而是聊天信息
            if ("36479543".equals(messageFromServer)) {//判断是否读取信息成功
                System.out.println("建立连接或读取信息失败！");
                break;
            }
            System.out.println("聊天信息调用成功！");//测试
            System.out.println("message form client:" + messageFromServer);//测试
            //这里还少一个把聊天信息写入到文本区域的方法
            messageFromServer = clientSocket.getMessage();

        } //判断如果不是棋子信息
        if (messageFromServer.charAt(0) == '$') {
            System.out.println("棋子控制信息" + messageFromServer);//测试
            int buttonMessage = Integer.valueOf(messageFromServer.substring(1));//去掉前面的$符号，得到一个新的数据，这个数据就是发生改变的棋子的位置信息
            System.out.println("对方的棋子位置是" + buttonMessage);//测试
            button[buttonMessage].setIcon(imgWhite);//假设服务器用的是白子,把白子放上去
        }
    }

    public void setWuziqiClient(WuziqiClient clientSocket) {//这个方法用来设置这个链接
        this.clientSocket = clientSocket;
        System.out.println("为发送线程设置连接成功！");//测试
    }

    public void setWuziqiLogic(WuziqiLogic wuziqiLogic) {//这个方法用来设置五子棋内部逻辑
        this.wuziqiLogic = wuziqiLogic;
    }

    public void run() {
        System.out.println("线程启动成功！");
        String messageFromServer = clientSocket.getMessage();
        while (true) {//线程内是死循环 一直监听这个链接
            if ("36479543".equals(messageFromServer)) {//判断是否读取信息成功
                System.out.println("建立连接或读取信息失败！");
                textArea.append("系统提示：\n连接已断开请重新开启服务器！\n");
                clientSocket.closeClient();//关闭连接
                buttonConn.setEnabled(true);//连接建立成功后 设置为可激活状态
                break;
            }
            if (messageFromServer.charAt(0) == '$') {
                System.out.println("棋子控制信息" + messageFromServer);//测试
                int buttonMessage = Integer.valueOf(messageFromServer.substring(1));//去掉前面的$符号，得到一个新的数据，这个数据就是发生改变的棋子的位置信息
                System.out.println("对方的棋子位置是" + buttonMessage);//测试
                //设置棋子颜色！
                if (clientSocket.getChessColor() == 1) {//如果这时候我用的是黑色棋子 那么对方用的就是白色棋子
                    button[buttonMessage].setIcon(nowWhite);//设置棋子的颜色
                    //为什么要替换黑子的的图片呢？因为知道对手上一次放下的棋子的位置是很重要的，我们对上一次棋手放下的
                    //棋子的位置格外的标出， 所以当我们放下当前棋子的时候，要更新上一次格外标出的棋子的样子
                    if (wuziqiLogic.getJustNowBlackChess() != -1) {//得到上一次黑子的位置，并替换这个图片
                        button[wuziqiLogic.getJustNowBlackChess()].setIcon(imgBlack);
                    }
                } else {
                    button[buttonMessage].setIcon(nowBlack);
                    if (wuziqiLogic.getJustNowWhiteChess() != -1) {//得到上一次白子的位置，并替换这个图片
                        button[wuziqiLogic.getJustNowWhiteChess()].setIcon(imgWhite);
                    }
                }

                clientSocket.setIsTurn(true);//设置这时轮到自己放下棋子了！

                //得到对方的棋子位置，判断对方是否胜利
                if (clientSocket.getChessColor() == 1) {//如果这时候我用的是黑色棋子 那么对方用的就是白色棋子
                    if (wuziqiLogic.setWhiteChess(buttonMessage)) {
                        System.out.println("白子胜利");
                        //胜利之后应该还有如何终止这棋局？
                        //用一下方法来实现终止这个棋局，我们把isTurn字段设置为false，双方都不能放下棋子，并且激活准备按钮
                        //弹出胜利信息的提示
                        clientSocket.setIsTurn(false);
                        buttonReady.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "白子胜利！");//这句先注释掉
                        //原因  这里要换一个弹出框，用JDialog这个类，不阻塞线程，上面这个会阻塞线程导致错误
                        clientSocket.sendMessage("白方胜利！");
                    }
                } else {//如果这时候我用的是白色棋子 那对方用的就是黑色棋子
                    if (wuziqiLogic.setBlackChess(buttonMessage)) {//如果这时候我用的是白色棋子
                        System.out.println("黑子胜利");
                        //胜利之后应该还有如何终止这棋局？
                        //用一下方法来实现终止这个棋局，我们把isTurn字段设置为false，双方都不能放下棋子，并且激活准备按钮
                        //弹出胜利信息的提示
                        clientSocket.setIsTurn(false);
                        buttonReady.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "黑子胜利！");//这句先注释掉
                        //原因  这里要换一个弹出框，用JDialog这个类，不阻塞线程，上面这个会阻塞线程导致错误
                        clientSocket.sendMessage("黑方胜利！");
                    }
                }//判断完毕

            } else if (messageFromServer.charAt(0) == '^') {//我们假设"^"开头的字段都是按钮等的控制信息
                int Ready = Integer.valueOf(messageFromServer.substring(1));
                if (Ready == 300) {
                    clientSocket.setAreYouReady(true);//如果为准备信息 那么我们就设置这个字段为true
                }
            } else {
                System.out.println("聊天信息调用成功！");//测试
                textArea.append("message form server:\n  " + messageFromServer + "\n");
                textArea.setCaretPosition(textArea.getText().length());//设置光标的位置 这里设置光标位置来让滚动条自动跟随
                //这里还少一个把聊天信息写入到文本区域的方法
            }
            messageFromServer = clientSocket.getMessage();
        }
    }
}
