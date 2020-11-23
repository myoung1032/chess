package xyz.chengzi.cs102a.chinesechess;

import xyz.chengzi.cs102a.chinesechess.chess.ChessComponent;
import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Internetgame1 extends JFrame{
    public static InetAddress myip;
    public static InetAddress otherip;
    public static JTextField jt=new JTextField();
    public static JTextField jt1=new JTextField();
    public static JTextField jt2=new JTextField();
    public static JButton jb=new JButton("确定");
    ChessboardComponent board ;
    ChessComponent[][] chessboard;
    public Internetgame1(){
        JLabel jp=new JLabel("对方IP:");
        jp.setLocation(490,155);
        jp.setSize(80,40);
        add(jp);
        jt.setSize(100,40);
        jt.setLocation(490,200);
        add(jt);
        JLabel jp1=new JLabel("己方端口");
        jp1.setLocation(490,240);
        jp1.setSize(80,40);
        jt1.setSize(100,40);
        jt1.setLocation(490,280);
        add(jt1);
        add(jp1);
        JLabel jp2=new JLabel("对方端口");
        jp2.setLocation(490,320);
        jp2.setSize(80,40);
        jt2.setSize(100,40);
        jt2.setLocation(490,360);
        add(jt2);
        add(jp2);
        jb.setSize(80,40);
        jb.setLocation(490,400);
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board= new ChessboardComponent(500, 500);
                chessboard=board.getChessboard();
                add(board);
                repaint();
            }
        });
        add(jb);
        setTitle("2019 CS102A Project Demo");
        setSize(700, 560);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        ChessGameFrame.internet=true;
        this.repaint();
        try {
            otherip=myip=InetAddress.getLocalHost();
            //ServerSocket server=new ServerSocket(2578);
            //if (server.isBound()){
                System.out.println("绑定成功");
            //}
            //server.close();
        }catch (Exception e){
            System.out.println("失败");
        }
    }
}
