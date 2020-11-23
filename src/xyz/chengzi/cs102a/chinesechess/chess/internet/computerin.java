package xyz.chengzi.cs102a.chinesechess.chess.internet;

import xyz.chengzi.cs102a.chinesechess.Internetgame1;
import xyz.chengzi.cs102a.chinesechess.chess.ChessComponent;
import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardComponent;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class computerin extends Thread{
    int in,x0,y0,x1,y1;
    ServerSocket server;
    Socket socket;
    String move;
    ChessComponent[][] chessboard;
    ChessboardComponent board;
    public computerin(ServerSocket server, ChessboardComponent board){
        this.server=server;
        this.board=board;
        chessboard=board.getChessboard();
        for (int i=0;i<chessboard.length;i++){
            for (int j=0;j<chessboard[i].length;j++){
                chessboard[i][j].canselect=true;
            }
        }
        this.server=server;
    }
    @Override
    public void run() {
        super.run();
        try {
            BufferedReader reader;
            while (true){
            socket=server.accept();
            System.out.println("input");
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            move=reader.readLine();
            in=Integer.parseInt(move);
            x0=in/1000;
            y0=in/100-10*x0;
            x1=in/10-100*x0-10*y0;
            y1=in%10;
            board.swapChessComponentsman1(chessboard[x0][y0],chessboard[x1][y1]);
            for (int i=0;i<chessboard.length;i++){
                for (int j=0;j<chessboard[i].length;j++){
                    chessboard[i][j].canselect=true;
                }
            }
            socket.close();}
            /*BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(move[0]+"\n");
            writer.close();
            socket=server.accept();
            if (Internetgame1.otherip==null) Internetgame1.otherip=server.getInetAddress();
            System.out.println("已链接IP"+Internetgame1.otherip.getHostAddress()+"发送");*/
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("in false");
        }
    }
}
