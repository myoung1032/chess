package xyz.chengzi.cs102a.chinesechess.chess;

import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardPoint;

import javax.swing.*;
import java.awt.*;

public class CannonChessComponent extends ChessComponent{
    public Image red=new ImageIcon("redcan.png").getImage();
    public Image black=new ImageIcon("blackcan.png").getImage();

    public String toString(){
        if (getChessColor()==ChessColor.BLACK){
            return "N";
        }else return "n";
    }

    public CannonChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color) {
        super(chessboardPoint, location, color);
    }

    @Override
    public int evaluateValue() {
        /*int[][] redarray=new int[][]{
                {100,100,96,91,90,91,96,100,100},
                {98,98,96,92,89,92,96,98,98},
                {97,97,96,91,92,91,96,97,97},
                {96,99,99,98,100,98,99,99,96},
                {96,96,96,96,100,96,96,96,96},
                {95,96,99,96,100,96,99,96,95},
                {96,96,96,96,96,96,96,96,96},
                {97,96,100,99,101,99,100,96,97},
                {96,97,98,98,98,98,98,97,96},
                {96,96,97,99,99,99,97,96,96}
        };
        if (getChessColor()==ChessColor.RED){
            return redarray[getChessboardPoint().getX()][getChessboardPoint().getY()];
        } else {
            return redarray[9-getChessboardPoint().getX()][8-getChessboardPoint().getY()];
        }*/
        return (getChessColor() == ChessColor.RED ? 100 :-100);
    }

    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if (destination.getY()!=source.getY()){
            if (movecheckG(chessboard)) return false;
        }
        int blocknumber=0;
        if (source.getX() == destination.getX()) {
            int row = source.getX();
            for (int col = Math.min(source.getY(), destination.getY()) + 1;
                 col < Math.max(source.getY(), destination.getY()); col++){
                if (!(chessboard[row][col] instanceof EmptySlotComponent)){
                    blocknumber++;
                }
            }
            if (blocknumber==1&&(!(chessboard[destination.getX()][destination.getY()] instanceof EmptySlotComponent))){
                return true;
            }else if (blocknumber==0){
                if ((chessboard[destination.getX()][destination.getY()] instanceof EmptySlotComponent)){
                    return true;
                }
            }
        } else if (source.getY() == destination.getY()) {
            int col = source.getY();
            for (int row = Math.min(source.getX(), destination.getX()) + 1;
                 row < Math.max(source.getX(), destination.getX()); row++) {
                if (!(chessboard[row][col]instanceof EmptySlotComponent)){
                    blocknumber++;
                }
            }
            if (blocknumber==1&&(!(chessboard[destination.getX()][destination.getY()] instanceof EmptySlotComponent))){
                return true;
            } else if (blocknumber==0) {
                if ((chessboard[destination.getX()][destination.getY()] instanceof EmptySlotComponent)){
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
        if (isSelected()) { // Highlights the chess if selected.
            g.setColor(Color.RED);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }
}
