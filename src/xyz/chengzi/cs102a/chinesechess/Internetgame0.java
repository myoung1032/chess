package xyz.chengzi.cs102a.chinesechess;

import xyz.chengzi.cs102a.chinesechess.chess.ChessComponent;
import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardComponent;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/*class userout extends Thread{
    ServerSocket server;
    Socket socket;
    String[] move;
    public userout(ServerSocket server,String[] move){
        this.move=move;
        this.server=server;
    }
    @Override
    public void run() {
        super.run();
        try {
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(move[0]+"\n");
            writer.close();
            socket=server.accept();
            if (Internetgame1.otherip==null) Internetgame1.otherip=server.getInetAddress();
            System.out.println("已链接IP"+Internetgame1.otherip.getHostAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class userin extends Thread{
    ServerSocket server;
    Socket socket;
    String[] moveto;
    public userin(ServerSocket server,String[] moveto){
        this.server=server;
        this.moveto=moveto;
    }

    @Override
    public void run() {
        super.run();
        try {
            socket=new Socket(Internetgame1.otherip.getHostAddress(),2568);
            BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            moveto[0]=reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}*/
public class Internetgame0 extends JFrame {
    public static JTextField jt=new JTextField();
    public static InetAddress myip;
    public static InetAddress otherip;
    ChessboardComponent board = new ChessboardComponent(500, 500);
    //ChessComponent[][] chessboard=board.getChessboard();
    public Internetgame0(){
        jt.setSize(200,40);
        jt.setLocation(490,200);
        add(jt);
        add(board);
        setTitle("2019 CS102A Project Demo");
        setSize(700, 560);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        this.repaint();
        //ChessGameFrame.internet=true;
        try {
            otherip=myip=InetAddress.getLocalHost();
        }catch (Exception e){
            System.out.println("失败");
        }
        }
    }
