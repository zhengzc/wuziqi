/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wuziqi;

import javax.swing.*;
import java.awt.event.*;
/**
 *这个类实现发送信息的线程 主要就是得到从聊天框里面输入的信息 发送出去
 * @author ying
 */
public class SendMessageButtonListener extends MouseAdapter{
        WuziqiClient clientSocket;//这里用这个对象来获得所建立的连接
        JTextField textField;//从这个对话框获得信息
        JTextArea textArea;//聊天框
        public SendMessageButtonListener(WuziqiClient clientSocket,JTextField textField,JTextArea textArea){
            this.clientSocket=clientSocket;
            this.textField=textField;
            this.textArea=textArea;
        }

    @Override
    public void mouseClicked(MouseEvent e) {
        String str = textField.getText();//获得信息
        System.out.println("调用聊天输入框发送按钮监听器成功 发送的信息是："+str+"\n");//测试
        clientSocket.sendMessage(str);//并发送
        textField.setText("");//发送完之后我们就把这个框框里面的东西给清空了 哇嘎嘎！
        System.out.println("发送信息成功！" + textField.getText());//测试
        textArea.append(str+"\n");//在聊天窗口显示这些信息
    }
}
