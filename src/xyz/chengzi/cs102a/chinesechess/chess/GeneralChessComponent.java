package xyz.chengzi.cs102a.chinesechess.chess;

import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardPoint;

import javax.swing.*;
import java.awt.*;

public class GeneralChessComponent extends ChessComponent{
    public Image red=new ImageIcon("redGer.png").getImage();
    public Image black=new ImageIcon("blackGer.png").getImage();

    public GeneralChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color) {
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
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,0,1,1,1,0,0,0},
                {0,0,0,2,2,2,0,0,0},
                {0,0,0,11,15,11,0,0,0}};
        if (getChessColor()==ChessColor.RED){
            return redarray[getChessboardPoint().getX()][getChessboardPoint().getY()];
        }else {return redarray[9-getChessboardPoint().getX()][8-getChessboardPoint().getY()];}*/
        return (getChessColor() == ChessColor.RED ? 1000 :-1000);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if (destination.getY()!=source.getY()){
            if (movecheckG(chessboard)) return false;
        }
        int row,column;
        if (getChessColor()==ChessColor.BLACK) {row=Math.max(destination.getX(),source.getX()+1);
        }else {row=Math.min(destination.getX(),source.getX()-1);}
        column=destination.getY();

        if (getChessColor()==ChessColor.BLACK){
            if (!(destination.getY()>=3&&destination.getY()<=5&&destination.getX()>=0&&destination.getX()<=2)) return false;
        }
        if (getChessColor()==ChessColor.RED){
            if (!(destination.getY()>=3&&destination.getY()<=5&&destination.getX()>=7&&destination.getX()<=9)) return false;
        }

        if(Math.abs(destination.getX()-source.getX())==1&&destination.getY()-source.getY()==0){
            if (getChessColor()==ChessColor.RED){
                while (chessboard[row][column] instanceof EmptySlotComponent){
                    row--;
                    if (row==0) break;
                }
                if (row>=0&&row<=9){
                    if (!(chessboard[row][column] instanceof GeneralChessComponent))  return true;
                }}
            if (getChessColor()==ChessColor.BLACK){
                while (chessboard[row][column] instanceof EmptySlotComponent){
                    row++;
                    if (row==9) break;
                }
                if (row>=0&&row<=9){
                    if (!(chessboard[row][column] instanceof GeneralChessComponent))  return true;
                }}
        }
        if (Math.abs(destination.getY()-source.getY())==1&&destination.getX()-source.getX()==0){
            if (getChessColor()==ChessColor.BLACK){
                while (chessboard[row][column] instanceof EmptySlotComponent&&row<=9){
                    row++;
                    if (row==9) break;
                }
                if (row>=0&&row<=9){
                    if (!(chessboard[row][column] instanceof GeneralChessComponent))  return true;
                }}
            if (getChessColor()==ChessColor.RED){
                while (chessboard[row][column] instanceof EmptySlotComponent&&row>=0){
                    row--;
                    if (row==0) break;
                }
                if (row>=0&&row<=9){
                    if (!(chessboard[row][column] instanceof GeneralChessComponent))  return true;
                }}
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
