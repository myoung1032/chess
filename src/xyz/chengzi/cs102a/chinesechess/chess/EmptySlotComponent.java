package xyz.chengzi.cs102a.chinesechess.chess;

import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardPoint;

import java.awt.*;

public class EmptySlotComponent extends ChessComponent {
    @Override
    public int evaluateValue() {
        return 0;
    }

    public String toString(){
        return ".";
    }
    public EmptySlotComponent(ChessboardPoint chessboardPoint, Point location) {
        super(chessboardPoint, location, ChessColor.NONE);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        return false;
    }
}
