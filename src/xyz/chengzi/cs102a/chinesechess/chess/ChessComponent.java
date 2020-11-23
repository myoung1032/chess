package xyz.chengzi.cs102a.chinesechess.chess;

import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardComponent;
import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardPoint;
import xyz.chengzi.cs102a.chinesechess.listener.ChessListener;

import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import javax.swing.*;
class swapmovement extends Thread{
    ChessComponent chess1,chess2;
    ChessboardComponent board;
    Point thispoint;
    public swapmovement(ChessComponent chess1,ChessComponent chess2,ChessboardComponent board){
        this.chess1=chess1;
        this.chess2=chess2;
        this.board=board;
    }

    @Override
    public void run() {
        super.run();
        ChessboardPoint chessboardPoint1 = new ChessboardPoint(chess1.getChessboardPoint().getX(),chess1.getChessboardPoint().getY()),
                chessboardPoint2 = new ChessboardPoint(chess2.getChessboardPoint().getX(),chess2.getChessboardPoint().getY());
        Point point1 = chess1.getLocation(), point2 = chess2.getLocation();
        thispoint=chess1.getLocation();
        double deltax,deltay,doublex,doubley;
        try {
            doublex=point1.x;
            doubley=point1.y;
            deltax=((double)(point2.x-point1.x))/250;
            deltay=((double)(point2.y-point1.y))/250;
            for (int i=1;i<=250;i++){
                thispoint=chess1.getLocation();
                doublex+=deltax;
                doubley+=deltay;
                thispoint.x=(int) doublex;
                thispoint.y=(int) doubley;
                chess1.setLocation(thispoint);
                Thread.sleep(1);
                board.repaint();
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("暂停失败");
        }
        chess2.setLocation(point1);
        chess2.setChessboardPoint(chessboardPoint1);
        chess1.setChessboardPoint(chessboardPoint2);
        chess1.setLocation(point2);
        ChessboardComponent.lastthreadfinish=true;
        System.out.println("lastthreadfinish=true");

    }
}


public abstract class ChessComponent extends JComponent {
    public final static Dimension CHESS_SIZE = new Dimension(40, 40);
    public final static Color CHESS_COLOR = new Color(254, 218, 164);

    public static List<ChessListener> listenerList = new ArrayList<>();
    public boolean othermove=false;
    private ChessboardPoint chessboardPoint;
    private ChessColor chessColor;
    private boolean selected;
    public boolean canselect=true;
    protected int value;

    protected ChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        setLocation(location);
        setSize(CHESS_SIZE);

        this.chessboardPoint = chessboardPoint;
        this.chessColor = chessColor;
        this.selected = false;
    }

    public String toString(){
        String s=getClass().getSimpleName().substring(0,1);
        if (this.chessColor==ChessColor.RED){
            s=s.toLowerCase();
        }
        return s;
    }

    public ChessboardPoint getChessboardPoint() {
        return chessboardPoint;
    }

    public void setChessboardPoint(ChessboardPoint chessboardPoint) {
        this.chessboardPoint = chessboardPoint;
    }

    public ChessColor getChessColor() {
        return chessColor;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getValue() {
        return value;
    }

    public void swapLocation(ChessComponent another,ChessboardComponent board) {
        swapmovement move=new swapmovement(this,another,board);
        move.start();
    }

    public void swapLocation(ChessComponent another) {
        ChessboardPoint chessboardPoint1 = getChessboardPoint(), chessboardPoint2 = another.getChessboardPoint();
        Point point1 = getLocation(), point2 = another.getLocation();
        setChessboardPoint(chessboardPoint2);
        setLocation(point2);
        another.setChessboardPoint(chessboardPoint1);
        another.setLocation(point1);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);

        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            for (ChessListener listener : listenerList) {
                listener.onClick(this);
            }
        }
    }

    public abstract boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination);

    public abstract int evaluateValue();

    public static boolean registerListener(ChessListener listener) {
        return listenerList.add(listener);
    }

    protected boolean movecheckG(ChessComponent[][] chessboard){
        int r1=chessboardPoint.getX(),r2=chessboardPoint.getX(),column=chessboardPoint.getY();
        do {
            r1++;
        } while (r1<=9&&chessboard[r1][column] instanceof EmptySlotComponent);
        if (r1==10) return false;
        do {
            r2--;
        }while (r2>=0&&chessboard[r2][column] instanceof EmptySlotComponent);
        if (r2==-1) return false;
        if (chessboard[r1][column] instanceof GeneralChessComponent&&chessboard[r2][column] instanceof GeneralChessComponent) return true;
        return false;
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if (othermove){
            g.setColor(Color.GREEN);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }

    public static boolean unregisterListener(ChessListener listener) {
        return listenerList.remove(listener);
    }
}
