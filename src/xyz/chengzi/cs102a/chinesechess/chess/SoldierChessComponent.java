package xyz.chengzi.cs102a.chinesechess.chess;

import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardPoint;

import javax.swing.*;
import java.awt.*;

public class SoldierChessComponent extends ChessComponent{
    public Image redsoilder=new ImageIcon("redsoilder.png").getImage();
    public Image blacksoilder=new ImageIcon("blacksoilder.png").getImage();


    public SoldierChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color) {
        super(chessboardPoint, location, color);
    }

    @Override
    public int evaluateValue() {
        /*int[][] redarray;
        redarray= new int[][]{
                {9, 9, 9, 11, 13, 11, 9, 9, 9},
                {19, 24, 34, 42, 44, 42, 34, 24, 19},
                {19, 24, 32, 37, 37, 37, 32, 24, 19},
                {19, 23, 27, 29, 30, 29, 27, 23, 19},
                {14, 18, 20, 27, 29, 27, 20, 18, 14},
                {7, 0, 13, 0, 16, 0, 13, 0, 7},
                {7, 0, 7, 0, 15, 0, 7, 0, 7},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}};
        if (getChessColor()==ChessColor.RED){
            return redarray[getChessboardPoint().getX()][getChessboardPoint().getY()];
        }else {return redarray[9-getChessboardPoint().getX()][8-getChessboardPoint().getY()];}*/
        return (getChessColor() == ChessColor.RED ? 30 :-30);
    }

    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if (destination.getY()!=source.getY()){
            if (movecheckG(chessboard)) return false;
        }
        if (this.getChessColor()==ChessColor.BLACK){
            if (source.getX()<5){
                if (destination.getX()==source.getX()+1&&destination.getY()==source.getY()){
                    return true;
                }
            }else{
                if (destination.getX()==source.getX()+1&&destination.getY()==source.getY()
                ||destination.getY()==source.getY()+1&&destination.getX()==source.getX()
                ||destination.getY()==source.getY()-1&&destination.getX()==source.getX()){
                    return true;
                }
            }
        }else {
            if (source.getX()>=5){
                if (destination.getX()==source.getX()-1&&destination.getY()==source.getY()){
                    return true;
                }
            }else{
                if (destination.getX()==source.getX()-1&&destination.getY()==source.getY()
                        ||destination.getY()==source.getY()+1&&destination.getX()==source.getX()
                        ||destination.getY()==source.getY()-1&&destination.getX()==source.getX()){
                    return true;
                }
            }
        }
        return false;

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.getChessColor()==ChessColor.BLACK){
            g.drawImage(blacksoilder,0,0,this);
        }else {
            g.drawImage(redsoilder,0,0,this);
        }
        if (isSelected()) { // Highlights the chess if selected.
            g.setColor(Color.RED);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }
}
