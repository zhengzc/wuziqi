/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wuziqi;
import java.awt.event.*;
import javax.swing.*;
/**
 *监听器类  实现聊天文本区域回车发送信息！
 * @author ying
 */
public class dialogFieldListener extends KeyAdapter {
    WuziqiClient clientSocket;//这里用这个对象来获得所建立的连接
    JTextField textField;//聊天输入框
    JTextArea textArea;//对话框
    public dialogFieldListener(WuziqiClient clientSocket,JTextField textField,JTextArea textArea){
        this.clientSocket = clientSocket;
        this.textField = textField;
        this.textArea = textArea;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        //不行就注释掉 哇嘎嘎！//e.setKeyCode(KeyEvent.VK_ENTER);//哇嘎嘎 这样就可以设置这个事件的值了  可是貌似这样应该不行，因为KeyEvent事件应该已经传递进来了，这是再设置按键是不是已经晚了？
        //有一种解决办法就是if判断 所以事件都接受 不过只有是enter键的时候才触发！
        //以上方法果然不行 所有键盘按键都接受了 还是用我们伟大的if语句吧
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            String str = textField.getText();//获得信息
            if (!str.isEmpty() || str.charAt(0) != '&' || str.charAt(0) != '^') {//如果这里面没有这几个敏感字符
                System.out.println("调用聊天输入框ENTER键监听器成功，发送的信息是：" + str);//测试
                clientSocket.sendMessage(str);//并发送
                textField.setText("");//发送完之后我们就把这个框框里面的东西给清空了 哇嘎嘎！
                System.out.println("发送信息成功！" + textField.getText());//测试
                textArea.append("menssage form me:\n  " + str + "\n");//在聊天窗口显示这些信息
            }else {
                textArea.append("请勿输入非法字符：不能发送空信息，不能发送$ ^ 符号开头的字符！");
                textField.setText("");//发送完之后我们就把这个框框里面的东西给清空了 哇嘎嘎！
            }
        } else {
            //什么也不做！
        }
    }
}
