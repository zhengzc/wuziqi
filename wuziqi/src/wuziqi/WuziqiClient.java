/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wuziqi;
import java.io.*;
import java.net.*;
/**
 *建造一个客户端的程序 这个类主要负责通讯
 * @author ying
 */
public class WuziqiClient {
    Boolean isTurn;//这个字段用来控制对弈双方的交替 如果为真，那么表明这时候是我放棋子 放完棋子之后设为假 服务端初始化为假
    //因为服务端最初是用白子，之后当一方发送棋盘控制信息（$开头的信息）后  设置自己isTurn为假 对方为真 这样来实现交替控制
    int chessColor;//这个字段我们来控制双方每局结束之后的交换棋局  这里应该有一个准备按钮，它来初始化棋盘信息，并且触发黑白子的交替
    // 1代表黑子  -1 代表白子
    Boolean areYouReady;//这个字段用来看对方是否准备好 true表示对方准备ok 默认是false

    Socket socketClient;//客户端的socket对象
    PrintWriter outClient;//客户端的输入输出流
    BufferedReader inClient;
    public WuziqiClient(String ipAddress,int port){//要连接主机的ip地址和端口号
        isTurn = true;//初始化 轮流信息和棋子颜色信息
        chessColor = 1;
        areYouReady = false;
        try{
            socketClient =new Socket(ipAddress,port);//发出连接请求
            //连接建立成功，通过Socket来获取连接上的输入/输出流
            outClient = new PrintWriter(socketClient.getOutputStream());
            inClient = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            System.out.println("连接建立成功");
        }catch(Exception e){
            System.out.println("连接建立失败"+e);
        }
    }

    public void sendMessage(String sendString){//发送信息的方法
        if (socketClient.isConnected()) {
            System.out.println("调用发送信息的方法");//测试
            //发送信息
            outClient.println(sendString);
            outClient.flush();
        } else {
            System.out.println("连接建立失败");
        }
    }

    public String getMessage(){//获取信息的方法
        String str;
        if (socketClient.isConnected()) {
            try {
                str = inClient.readLine();//从socket获取信息
                System.out.println("获取信息成功");//测试
                return str;
            } catch (Exception e) {
                System.out.println("读取信息失败" + e);
            }
            return "36479543";//读取失败就返回这个
        } else {
            System.out.println("连接建立失败");
            return "36479543";
        }
    }

    //设置isTurn字段
    public void setIsTurn(Boolean trueOrFalse){
        isTurn = trueOrFalse;
    }

    //获取isTurn字段
    public Boolean getIsTurn(){
        return isTurn;
    }

    //chessColor字段的set和get方法
    public void setChessColor(int whatColor){
        chessColor = whatColor;
    }

    public int getChessColor(){
        return chessColor;
    }

    //areYouReady 字段
    public void setAreYouReady(Boolean what){
        this.areYouReady = what;
    }

    public Boolean getAreYouReady(){
        return areYouReady;
    }
    
    public Boolean isConnected(){//测试这个连接是否建立成功
        if(socketClient.isConnected()){
            return true;
        }
        return false;
    }

    public void closeClient(){
        try{
            outClient.close();
            inClient.close();
            socketClient.close();
        }catch(Exception e){
            System.out.println("关闭输入输出流异常"+e);
        }
    }
}