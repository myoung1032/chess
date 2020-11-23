package xyz.chengzi.cs102a.chinesechess.chess;

import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardPoint;

import javax.swing.*;
import java.awt.*;

public class ChariotChessComponent extends ChessComponent {
    public Image red=new ImageIcon("redchi.png").getImage();
    public Image black=new ImageIcon("blackchi.png").getImage();


    public ChariotChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color) {
        super(chessboardPoint, location, color);
    }

    @Override
    public int evaluateValue() {
        /*int[][] redarray=new int[][]{
                {206,208,207,213,214,213,207,208,206},
                {206,212,209,216,233,216,209,212,206},
                {206,208,207,214,216,214,207,208,206},
                {206,213,213,216,216,216,213,213,206},
                {208,211,211,214,215,214,211,211,208},
                {208,212,212,214,215,214,212,212,208},
                {204,209,204,212,214,212,204,209,204},
                {198,208,204,212,212,212,204,208,198},
                {200,208,206,212,200,212,206,208,200},
                {194,206,204,212,200,212,204,206,194}
        };
        if (getChessColor()==ChessColor.RED){
            return redarray[getChessboardPoint().getX()][getChessboardPoint().getY()];
        }else {return redarray[9-getChessboardPoint().getX()][8-getChessboardPoint().getY()];}*/
        return (getChessColor() == ChessColor.RED ? 200 :-200);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if (destination.getY()!=source.getY()){
            if (movecheckG(chessboard)) return false;
        }
        if (source.getX() == destination.getX()) {
            int row = source.getX();
            for (int col = Math.min(source.getY(), destination.getY()) + 1;
                 col < Math.max(source.getY(), destination.getY()); col++) {
                if (!(chessboard[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else if (source.getY() == destination.getY()) {
            int col = source.getY();
            for (int row = Math.min(source.getX(), destination.getX()) + 1;
                 row < Math.max(source.getX(), destination.getX()); row++) {
                if (!(chessboard[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else { // Not on the same row or the same column.
            return false;
        }
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getChessColor()==ChessColor.BLACK){
            g.drawImage(black,0,0,this);
        }else{
            g.drawImage(red,0,0,this);
        }
        if (isSelected()) { // Highlights the chess if selected.
            g.setColor(Color.RED);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }
}
