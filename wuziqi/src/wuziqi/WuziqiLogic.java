/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wuziqi;

import java.util.*;
/**
 * 这个类用来实现五子棋的内部逻辑，打算用二维表模拟一个棋局，通过这个棋局来判断输赢
 * 这个类应该在双方都处于准备状态的时候建立 （连接状态？），就是说棋局开始的时候。应该在点击准备按钮的时候被重新初始化
 * 另外至少还要维护两个队列，栈？数组？ 用来维护黑白双方交替所放下的棋子的位置，就是所走的步！
 * 这个里面应该有一个初始化的棋局的一个方法，用来重置这个棋局，以及这两个队列
 * 对于应该实现悔棋，保存棋局等操作暂不考虑
 * 另外如何实现这个内部逻辑的棋局和外面棋局同步？ 要好好想一想....
 * 还有就是如何实现对弈双方交替控制？ 内部实现和界面的实现如何同步....
 * 解决交替控制：大致思路就是在server和client双方的棋盘点击监听的类中（QiPanMouseListener）设置一个控制字段（isTurn）
 * 如果这个字段为true，那么我们就允许这个点击事件发生，并且发送信息，否则不允许其发生
 * 并且规定双方在收到关于棋子放下的信息的时候 都能够将这个字段设置为ture（意思就是你放完了，该我了 hoho~~）
 * 要规定发送聊天的信息的时候不能出现$开头的字符串  因为这个符号不能用 不能用就是不能用， $开头的字符串是代表棋子信息，所以不能用
 * 今天就这么多了...还要做作业...
 * 关于这其中的控制字段，上面说到在棋盘监听器类中添加控制字段，这些控制字段是应该棋局开始的时候初始化的，并且是通过分析收发
 * 的信息来改变这个字段，那么我们用同样的方法来实现黑子白子的交替，添加一个关键字段，来表示这局用的是黑子还是白子  而这个信息也是
 * 在棋局开始的时候我们默认使客户端为黑色棋子 每局双方换子
 * 一下是关于准备这个按钮的逻辑：连接建立成功后，当一方点击准备按钮，发送$isOK询问给对方，这时对方准备按钮应该返回$yes
 * 双方都来初始化自己的棋局，双方都基于一下规则来进行初始化，如果我收到了$isOK询问，那么我用的是黑子（初始化字段）并且这时候
 * 是轮到我来放置棋子（初始化字段isTurn 注意，谁先手是由黑子决定的) 如果这个连接不断开 每准备一次，我就轮流让双方使用黑白子
 *
 * 对上面的控制字段的设置位置提出改变， 既然我是通过分析信息而初始化控制字段，那我为什么不把控制字段设在client和server里面，
 * 我们所有的通讯都要经过socket来发送 都要用到send 和 get方法
 * @author ying
 */
public class WuziqiLogic {
    Vector<Integer> playerWhite;//玩家双发要使用的存储数组
    Vector<Integer> playerBlack;
    //int whitePointer;//白子和黑子存储数组的指针  使用Vector的时候不用使用指针了
    //int blackPointer;

    //这两个代表期盼中某点的位置信息，x坐标 和 y坐标   其实就是qiPan数组的下标！
    //这个点代表了此时棋盘中某一点的位置信息，有时候我们很需要一个关键点
    //当我们要用到某个关键点的时候，我们要先设置这个点的坐标 就是调用setPoint方法
    int x;
    int y;
    
    int[][] qiPan = new int[17][17];//维护棋盘格局的二维数组 1代表黑子 -1代表白子 0代表空白位置 2代表边界

    public WuziqiLogic(){
        initQiPan();//初始化棋盘
    }

    //初始化棋盘的方法
    public void initQiPan(){
        x = 1;//我们先把这个关键点设置在第一行第一列 
        y = 1;
        //whitePointer = 0;//初始化指针
        //blackPointer = 0;
        playerWhite = new Vector(30);//默认为30长度的数组吧，这个是可变长的数组
        playerWhite.add(-1);
        playerBlack = new Vector(30);
        playerBlack.add(-1);
        //开始初始化棋盘
        for(int i=0;i<=16;i++){//初始化整个棋盘 为什么是17 而不是15呢？ 我想为棋盘加上边界！
            for(int j=0;j<=16;j++){
                if(i==0 || i==16 || j==0 || j==16){//设置边界
                    qiPan[i][j] = 2;
                }else{//初始化棋盘
                    qiPan[i][j] = 0;
                }
            }
        }
    }

    //放下一白色棋子个棋子，放入对应的数组，记录入棋盘  并判断是否胜利
    public Boolean setWhiteChess(int where){//参数代表是棋子的位置
        setPoint(where);//设置这个坐标
        //记录下白色棋子所走的位置
        qiPan[x][y] = -1;//设置白子的位置
        playerWhite.add(where);//记录这个位置
        if(isWin()){//如果这个时候白子胜利
            return true;
        }else{
            return false;
        }
    }

    //放下一个黑色棋子，放入对应的数组，记录入棋盘  并判断是否胜利
    public Boolean setBlackChess(int where) {//参数代表是棋子的位置
        setPoint(where);//设置这个坐标
        //记录下黑色棋子所走的位置
        qiPan[x][y] = 1;//设置黑子的位置
        playerBlack.add(where);//记录这个位置
        if (isWin()) {//如果这个时候黑子胜利
            return true;
        } else {
            return false;
        }
    }

    //判断位置棋子是否胜利
    public  Boolean isWin(){//没有参数？ 默认的就是这个类中的关键点喽！我们可以先调用设置这个关键点的方法来改变它  他会根据位置得到这个位置棋子的颜色 并判断是否胜利
        //setPoint(where);//设置某点的位置
        int isFive = 1;//这个变量来累积是否出现五连星 因为这个点一给出 至少已经有一个棋子了
        int color = getColor();//得到这个位置的棋子颜色 1或-1
        //判断四个方向上是否已形成五连星！如果形成返回ture 否则 false
        //我们按照从12点钟方向开始，先上后下 顺时针判断
        int i,j;

        //判断 “|” 方向上
        for(i=x-1,j=y;i>0;i--){//判断上半部分
            if(qiPan[i][j] == color){//如果颜色相同
                isFive++;//递增
                if(isFive == 5){//如果这时候出现五连星
                    return true;//返回胜利信息
                }
            }else{//若颜色不同
                break;//上半部分搜索完毕
            }
        }
        for (i = x + 1, j = y; i < 16; i++) {//判断下半部分
            if (qiPan[i][j] == color) {//如果颜色相同
                isFive++;//递增
                if (isFive == 5) {//如果这时候出现五连星
                    return true;//返回胜利信息
                }
            } else {//若颜色不同
                break;//下半部分搜索完毕
            }
        } // “|” 方向上搜索完毕！

        //判断“/”方向上
        isFive = 1;//初始化这个变量
        for(i=x-1,j=y+1;i>0&&j<16;i--,j++){//判断上半部分
            if (qiPan[i][j] == color) {//如果颜色相同
                isFive++;//递增
                if (isFive == 5) {//如果这时候出现五连星
                    return true;//返回胜利信息
                }
            } else {//若颜色不同
                break;//上半部分搜索完毕
            }
        }
        for(i=x+1,j=y-1;i<16&&j>0;i++,j--){//判断下半部分
            if (qiPan[i][j] == color) {//如果颜色相同
                isFive++;//递增
                if (isFive == 5) {//如果这时候出现五连星
                    return true;//返回胜利信息
                }
            } else {//若颜色不同
                break;//下半部分搜索完毕
            }
        }//“/”方向上搜索完毕

        //判断“—”方向上
        isFive = 1;//初始化变量
        for(i=x,j=y-1;j>0;j--){//判断左半部分
            if (qiPan[i][j] == color) {//如果颜色相同
                isFive++;//递增
                if (isFive == 5) {//如果这时候出现五连星
                    return true;//返回胜利信息
                }
            } else {//若颜色不同
                break;//左半部分搜索完毕
            }
        }
        for(i=x,j=y+1;j<16;j++){//判断右半部分
            if (qiPan[i][j] == color) {//如果颜色相同
                isFive++;//递增
                if (isFive == 5) {//如果这时候出现五连星
                    return true;//返回胜利信息
                }
            } else {//若颜色不同
                break;//右半部分搜索完毕
            }
        }//“—”方向上搜索完毕！
        
        //判断“\”方向上
        isFive = 1;//初始化变量
        for(i=x-1,j=y-1;x>0&&y>0;i--,j--){//判断上半部分
            if (qiPan[i][j] == color) {//如果颜色相同
                isFive++;//递增
                if (isFive == 5) {//如果这时候出现五连星
                    return true;//返回胜利信息
                }
            } else {//若颜色不同
                break;//上半部分搜索完毕
            }
        }
        for(i=x+1,j=y+1;x<16&&y<16;i++,j++){
            if (qiPan[i][j] == color) {//如果颜色相同
                isFive++;//递增
                if (isFive == 5) {//如果这时候出现五连星
                    return true;//返回胜利信息
                }
            } else {//若颜色不同
                break;//下半部分搜索完毕
            }
        }//“\”方向上搜索完毕

        //所有方向搜索完毕 没发现五连星
        return false;//返回没有胜利的信息
    }

    //这个方法用来设置棋盘中某点位置，因外外部的点信息是用一个整数来表示，而内部逻辑是一个坐标来表示
    //这个方法根据外部的点信息来设置内部坐标信息
    //当我们调用设置棋子位置的时候，我们应该先调用这个方法来设定点的坐标
    public void setPoint(int where){
        System.out.println("调用setPoint方法");//测试
        x = where/15 + 1;//加1  是因为我们的逻辑上的qipan是从1开始的， 0行是边界 下面列计算同理
        y = where%15 + 1;
    }

    //这个方法来返回某点（位置）的棋子颜色 -1 白色  1黑色 0没有棋子 2 棋盘边界
    public int getColor(){
        return qiPan[x][y];//返回这个坐标所代表的位置
    }

    //得到刚才放下的黑色棋子的位置
    public int getJustNowBlackChess(){
        return playerBlack.lastElement();
    }
    //得到刚才放下的白色棋子的位置
    public int getJustNowWhiteChess(){
        return playerWhite.lastElement();
    }
}
