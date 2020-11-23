package xyz.chengzi.cs102a.chinesechess.chess;

import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardPoint;

import javax.swing.*;
import java.awt.*;

public class AdvisorChessComponent extends ChessComponent{

    public Image red=new ImageIcon("redadv.png").getImage();
    public Image black=new ImageIcon("blackadv.png").getImage();

    public AdvisorChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color) {
        super(chessboardPoint, location, color);
    }
    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if (destination.getY()!=source.getY()){
            if (movecheckG(chessboard)) return false;
        }
        if (getChessColor()==ChessColor.BLACK){
            if (!(destination.getY()>=3&&destination.getY()<=5&&destination.getX()>=0&&destination.getX()<=2)) return false;
        }
        if (getChessColor()==ChessColor.RED){
            if (!(destination.getY()>=3&&destination.getY()<=5&&destination.getX()>=7&&destination.getX()<=9)) return false;
        }
        if(Math.abs(destination.getX()-source.getX())==1&&Math.abs(destination.getY()-source.getY())==1){
            return true;
        }
        return false;
    }

    @Override
    public int evaluateValue() {
        /*int[][] redarray=new int[][]{
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,20,0,20,0,0,0},
                {0,0,0,0,23,0,0,0,0},
                {0,0,0,20,0,20,0,0,0}
        };
        if (getChessColor()==ChessColor.RED){
            return redarray[getChessboardPoint().getX()][getChessboardPoint().getY()];
        }else {
            return redarray[9-getChessboardPoint().getX()][8-getChessboardPoint().getY()];
        }*/
        return (getChessColor() == ChessColor.RED ? 20 :-20);
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
