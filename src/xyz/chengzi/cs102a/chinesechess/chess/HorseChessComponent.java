package xyz.chengzi.cs102a.chinesechess.chess;

//import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardComponent;
import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardPoint;

import javax.swing.*;
import java.awt.*;

public class HorseChessComponent extends ChessComponent {
    public Image redhorse=new ImageIcon("redhorse.png").getImage();
    public Image blackhorse=new ImageIcon("blackhorse.png").getImage();

    public HorseChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color) {
        super(chessboardPoint, location, color);
    }

    @Override
    public int evaluateValue() {
        /*int[][] redarray=new int[][]{
                {90,90,90,96,90,96,90,90,90},
                {90,96,103,97,94,97,103,96,90},
                {92,98,99,103,99,103,99,98,92},
                {93,108,100,107,100,107,100,108,93},
                {90,100,99,103,104,103,99,100,90},
                {90,98,101,102,103,102,101,98,90},
                {92,94,98,95,98,95,98,94,92},
                {93,92,94,95,92,95,94,92,93},
                {85,90,92,93,78,93,92,90,85},
                {88,85,90,88,90,88,90,85,88}
        };
        if (getChessColor()==ChessColor.RED){
            return redarray[getChessboardPoint().getX()][getChessboardPoint().getY()];
        }else {return redarray[9-getChessboardPoint().getX()][8-getChessboardPoint().getY()];}*/
        return (getChessColor() == ChessColor.RED ? 100 :-100);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if (destination.getY()!=source.getY()){
            if (movecheckG(chessboard)) return false;
        }
        if (Math.abs(destination.getX()-source.getX())==1){
            if (destination.getY()-source.getY()==2){
                if (chessboard[source.getX()][source.getY()+1] instanceof EmptySlotComponent){
                    return true;
                }
            }
            if (destination.getY()-source.getY()==-2){
                if (chessboard[source.getX()][source.getY()-1] instanceof EmptySlotComponent){
                    return true;
                }
            }
        }
        if (Math.abs(destination.getY()-source.getY())==1){
            if (destination.getX()-source.getX()==2){
                if ((chessboard[source.getX()+1][source.getY()] instanceof EmptySlotComponent)){
                    return true;
                }
            }
            if (destination.getX()-source.getX()==-2){
                if ((chessboard[source.getX()-1][source.getY()] instanceof EmptySlotComponent)){
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
            g.drawImage(blackhorse,0,0,this);
        }else {
            g.drawImage(redhorse,0,0,this);
        }
        // FIXME: Use library to find the correct offset.
        if (isSelected()) { // Highlights the chess if selected.
            g.setColor(Color.RED);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }
}

