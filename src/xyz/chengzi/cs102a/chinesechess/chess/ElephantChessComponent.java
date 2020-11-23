package xyz.chengzi.cs102a.chinesechess.chess;

import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardPoint;

import javax.swing.*;
import java.awt.*;

public class ElephantChessComponent extends ChessComponent{
    public Image red=new ImageIcon("redele.png").getImage();
    public Image black=new ImageIcon("blackele.png").getImage();

    public ElephantChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color) {
        super(chessboardPoint, location, color);
    }

    @Override
    public int evaluateValue() {
        /*int[][] redarray=new int[][]{
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,20,0,0,0,20,0,0},
                {0,0,0,0,0,0,0,0,0},
                {18,0,0,0,23,0,0,0,18},
                {0,0,0,0,0,0,0,0,0},
                {0,0,20,0,0,0,20,0,0}};
        if (getChessColor()==ChessColor.RED){
            return redarray[getChessboardPoint().getX()][getChessboardPoint().getY()];
        }else {return redarray[9-getChessboardPoint().getX()][8-getChessboardPoint().getY()];}*/
        return (getChessColor() == ChessColor.RED ? 20 :-20);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if (destination.getY()!=source.getY()){
            if (movecheckG(chessboard)) return false;
        }
        if (getChessColor()==ChessColor.BLACK){
            if (destination.getX()>=5) return false;
        }
        if (getChessColor()==ChessColor.RED){
            if (destination.getX()<=4) return false;
        }
        if (destination.getX()-source.getX()==2){
            if (destination.getY()-source.getY()==2){
                if (chessboard[source.getX()+1][source.getY()+1] instanceof EmptySlotComponent){
                    return true;
                }
            }
            if (destination.getY()-source.getY()==-2){
                if (chessboard[source.getX()+1][source.getY()-1] instanceof EmptySlotComponent){
                    return true;
                }
            }
        }
        if (destination.getX()-source.getX()==-2){
            if (destination.getY()-source.getY()==2){
                if (chessboard[source.getX()-1][source.getY()+1] instanceof EmptySlotComponent){
                    return true;
                }
            }
            if (destination.getY()-source.getY()==-2){
                if (chessboard[source.getX()-1][source.getY()-1] instanceof EmptySlotComponent){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getChessColor()==ChessColor.BLACK){
            g.drawImage(black,0,0,this);
        }else{
            g.drawImage(red,0,0,this);
        }
        // FIXME: Use library to find the correct offset.
        if (isSelected()) { // Highlights the chess if selected.
            g.setColor(Color.RED);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }
}
