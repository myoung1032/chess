package xyz.chengzi.cs102a.chinesechess.listener;

import xyz.chengzi.cs102a.chinesechess.chess.ChessComponent;
import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardComponent;

public class ChessboardChessListener extends ChessListener {
    private ChessboardComponent chessboardComponent;
    private ChessComponent first;
    MusicPlayer movemusic=new MusicPlayer("move.wav",false);
    Thread thread=new Thread(movemusic);
    public ChessboardChessListener(ChessboardComponent chessboardComponent) {
        this.chessboardComponent = chessboardComponent;
    }

    @Override
    public void onClick(ChessComponent chessComponent) {
        ChessComponent[][] board=chessboardComponent.getChessboard();
        if (chessComponent.canselect){
            if (first == null) {
                if (handleFirst(chessComponent)) {
                    chessComponent.setSelected(true);
                    first = chessComponent;
                    for (int i=0;i<10;i++){
                        for (int j=0;j<9;j++){
                            if (chessComponent.canMoveTo(board,board[i][j].getChessboardPoint())&&chessComponent.getChessColor()!=board[i][j].getChessColor()){
                                board[i][j].othermove=true;
                            }
                        }
                    }
                    chessboardComponent.repaint();
                }
            } else {
                if (first == chessComponent) { // Double-click to unselect.
                    chessComponent.setSelected(false);
                    first = null;
                    for (int i=0;i<10;i++){
                        for (int j=0;j<9;j++){
                            if (chessComponent.canMoveTo(board,board[i][j].getChessboardPoint())){
                                board[i][j].othermove=false;
                            }
                        }
                    }
                    chessboardComponent.repaint();
                } else if (handleSecond(chessComponent)) {
                    Thread musicthread=new Thread(movemusic);
                    chessboardComponent.swapChessComponentsman(first, chessComponent);
                    musicthread.start();
                    //chessboardComponent.swapColor();
                    first.setSelected(false);
                    first = null;
                    for (int i=0;i<10;i++){
                        for (int j=0;j<9;j++){
                                board[i][j].othermove=false;
                        }
                    }
                    chessboardComponent.repaint();
                }
            }}
    }

    private boolean handleFirst(ChessComponent chessComponent) {
        return chessComponent.getChessColor() == chessboardComponent.getCurrentColor();
    }

    private boolean handleSecond(ChessComponent chessComponent) {
        return chessComponent.getChessColor() != chessboardComponent.getCurrentColor() &&
                first.canMoveTo(chessboardComponent.getChessboard(), chessComponent.getChessboardPoint());
    }
}

