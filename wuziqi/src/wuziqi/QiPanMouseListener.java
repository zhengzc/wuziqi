/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wuziqi;

import javax.swing.*;
import java.awt.event.*;
/**定义一个监听器
 *这个是一个事件响应的适配器类 处理棋盘上放棋子的这一动作的处理
 * 这里处理要为按钮更换图片，并且要能够传捕获所点击的按钮的位置，这里的位置用它所在数组的下标来表示
 * @author ying
 */
public class QiPanMouseListener extends MouseAdapter{
    int index;//这个索引用记录点击时间的button在棋盘中的位置索引
    JButton button;//因为这里是要对点击后棋盘关键点的修改，所以这里声明一个button用来传递对象
    WuziqiClient clientSocket;//这里用这个对象来获得所建立的连接
    JButton buttonReady;//准备按钮
    WuziqiLogic wuziqiLogic;//这个是五子棋的内部逻辑！

    JButton[] buttonArray;//添加这个字段的目的是为了能够实现显示当前落下棋子的位置，当放下一个棋子的时候，这个棋子
    //当着重显示，同时清除上一个着重显示的棋子，这个字段的作用就在这里，清除上一个棋子，就需要用到这个棋盘信息数组

    ImageIcon imgBlack = new ImageIcon("imgs/everyBlack.jpg");//两个棋子 黑色和白色
    ImageIcon imgWhite = new ImageIcon("imgs/everyWhite.jpg");
    ImageIcon nowWhite = new ImageIcon("imgs/nowWhite.jpg");//两个棋子，用来标识刚刚放下的棋子的位置
    ImageIcon nowBlack = new ImageIcon("imgs/nowBlack.jpg");
    public QiPanMouseListener(int index,JButton button,WuziqiClient clientSocket,WuziqiLogic wuziqiLogic,JButton buttonReady,JButton[] buttonArray){
        this.index=index;
        this.button = button;//传递参数
        this.clientSocket = clientSocket;
        this.wuziqiLogic = wuziqiLogic;
        this.buttonReady = buttonReady;
        this.buttonArray = buttonArray;
    }
    //覆盖适配器中的部分方法
    @Override
    public void mousePressed(MouseEvent e){//鼠标单击事件 鼠标按下时调用
        System.out.println("ok 棋盘监听调用成功");//测试用例
        if (clientSocket.getIsTurn()) {//如果这个时候是轮到我了
            if (clientSocket.getChessColor() == 1) {//如果这时候我用的黑子
                button.setIcon(nowBlack);
                //为什么要替换黑子的的图片呢？因为知道对手上一次放下的棋子的位置是很重要的，我们对上一次棋手放下的
                //棋子的位置格外的标出， 所以当我们放下当前棋子的时候，要更新上一次格外标出的棋子的样子
                if (wuziqiLogic.getJustNowWhiteChess() != -1) {//得到上一次黑子的位置，并替换这个图片
                    buttonArray[wuziqiLogic.getJustNowWhiteChess()].setIcon(imgWhite);
                }
            } else {//不是黑子就是白子啦 呵呵
                button.setIcon(nowWhite);
                //为什么要替换黑子的的图片呢？因为知道对手上一次放下的棋子的位置是很重要的，我们对上一次棋手放下的
                //棋子的位置格外的标出， 所以当我们放下当前棋子的时候，要更新上一次格外标出的棋子的样子
                if (wuziqiLogic.getJustNowBlackChess() != -1) {//得到上一次黑子的位置，并替换这个图片
                    buttonArray[wuziqiLogic.getJustNowBlackChess()].setIcon(imgBlack);
                }
            }
            //button.setEnabled(false);//这个按钮不能再次点击了...
            //button.repaint();//重绘这个按钮
            //这里还要传递出一个信息，就是这个button的位置在哪里？如何传递出信息呢？写一个专门的通讯的类 在这里传递信息！行吗？
            String sendString = "$" + index;//要发送的序列 $开头表示是棋子信息
            System.out.println("发送出的信息是" + sendString);//测试
            clientSocket.sendMessage(sendString);//发送

            //发送成功之后我们把轮流字段设置为false
            clientSocket.setIsTurn(false);
            System.out.println("发送控制信息成功！turn字段设为false");//测试

            //放下一个棋子 我们来判断是否胜利了！
            if(clientSocket.getChessColor() == 1){//如果这时候我用的是黑色棋子
                if(wuziqiLogic.setBlackChess(index)){
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
            } else {//如果这时候我用的是白色棋子
                if(wuziqiLogic.setWhiteChess(index)){//如果这时候我用的是白色棋子
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
            }//判断完毕

            getIndex();//输出测试
        } else {
            System.out.println("现在还没轮到你哦！稍等一下");//测试！
        }
    }
    public int getIndex(){//得到这个按钮的索引位置 用来进行定位
        System.out.println("返回位置成功"+index);//测试返回是否成功 正确
        return index;
    }
}
