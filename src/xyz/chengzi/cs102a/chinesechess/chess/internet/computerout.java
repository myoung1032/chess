package xyz.chengzi.cs102a.chinesechess.chess.internet;

import xyz.chengzi.cs102a.chinesechess.Internetgame1;
import xyz.chengzi.cs102a.chinesechess.chess.ChessComponent;
import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardComponent;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class computerout extends Thread{
    PrintWriter writer;

    ServerSocket server;
    Socket socket;
    String[] moveto;
    ChessboardComponent board;
    ChessComponent[][] chessboard;
    public computerout(ChessboardComponent board, String[] move){
        this.board=board;
        this.chessboard=board.getChessboard();
        moveto=move;
        System.out.println("jiazai");
    }

    @Override
    public void run() {
        super.run();
        System.out.println(Internetgame1.jt.getText());
        System.out.println("对方"+Internetgame1.jt2.getText());
        try {
            socket=new Socket(Internetgame1.jt.getText(),Integer.parseInt(Internetgame1.jt2.getText()));
            System.out.println(Internetgame1.jt.getText());
            System.out.println("对方"+Internetgame1.jt2.getText());
            writer=new PrintWriter(socket.getOutputStream(),true);
            System.out.println(moveto[0]);
            writer.write(moveto[0]+"\n");
            writer.close();
            while (true){
                Thread.sleep(50);
                if (ChessboardComponent.eatover) {
                    ChessboardComponent.eatover=false;
                    break;}
            }
            for (int i=0;i<chessboard.length;i++){
                for (int j=0;j<chessboard[i].length;j++){
                    chessboard[i][j].canselect=false;
                }
            }
            //Thread.sleep(250);
            //socket.close();
            /*BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            moveto[0]=reader.readLine();
            in=Integer.parseInt(moveto[0]);
            x0=in/1000;
            y0=in/100-10*x0;
            x1=in/10-100*x0-10*y0;
            y1=in%10;
            board.swapChessComponents(chessboard[x0][y0],chessboard[x1][y1]);
            for (int i=0;i<chessboard.length;i++){
                for (int j=0;j<chessboard[i].length;j++){
                    chessboard[i][j].canselect=true;
                }
            }
            socket.close();*/
        } catch (IOException | InterruptedException e) {
            System.out.println("out false");
            e.printStackTrace();
        }

    }
}
