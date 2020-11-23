package xyz.chengzi.cs102a.chinesechess;

import xyz.chengzi.cs102a.chinesechess.chess.ChessColor;
import xyz.chengzi.cs102a.chinesechess.chess.ChessComponent;
import xyz.chengzi.cs102a.chinesechess.chess.EmptySlotComponent;
import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardComponent;
import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class AImode {
    public static ChessboardComponent copyboard;
    public String bestmove="";
    public ChessboardComponent board;
    public ChessColor AIcolor;
    public ChessColor othercolor;
    public ChessComponent[][] chessboard,copychessboard;
    private int searchdepth=2;
    private ArrayList<String>[] allMoves=new ArrayList[7];
    private int[] counter=new int[7];
    private int[] bestvalue=new int[7];
    private int value;
    private int bestlocation=0;
    int times=0;
    //the deepest search computer can do is 7
    //private ArrayList<String> allMoves=new ArrayList<>();
    //private ArrayList<Integer> allValues=new ArrayList<>();

    public AImode(ChessboardComponent board,ChessColor chessColor){
        copyboard=new ChessboardComponent(0,0);
        this.board=board;
        this.chessboard=board.getChessboard();
        AIcolor=chessColor;
        othercolor=(AIcolor==ChessColor.RED?ChessColor.BLACK:ChessColor.RED);
        copyboard.newboard();
        System.out.println(ChessComponent.listenerList.size());
        ChessComponent.unregisterListener(ChessComponent.listenerList.get(1));
        copychessboard=copyboard.getChessboard();
    }

    public int calculatesum(ChessboardComponent board){
        ChessComponent[][] chessboard=board.getChessboard();
        int sum=0;
        for (int i=0;i<chessboard.length;i++){
            for (int j=0;j<chessboard[i].length;j++){
                /*if (chessboard[i][j].getChessColor()==board.currentColor){
                    sum+=chessboard[i][j].evaluateValue();
                }else {
                    sum-=chessboard[i][j].evaluateValue();
                }*/
                sum+=chessboard[i][j].evaluateValue();
            }
        }
        //return (board.currentColor==ChessColor.RED ? sum : -sum);
        return sum;
    }
    //find out the current value for a certain color, RED is positive and BLACK is negative

    public void changeChessboard(ChessboardComponent board){
        for (int i = 0; i < board.getChessboard().length; i++) {
            for (int j = 0; j < board.getChessboard()[i].length; j++) {
                board.putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), board.calculatePoint(i, j)));
            }
        }

        for (int i=0;i<10;i++){
            for (int j=0;j<9;j++){
                if (!(board.getSavings().get(board.getSavings().size()-2)[i][j] instanceof EmptySlotComponent)){
                    board.initTestBoard(i, j, board.getSavings().get(board.getSavings().size()-2)[i][j].getChessColor(),board.getSavings().get(board.getSavings().size()-2)[i][j].getClass());
                }
            }
        }
        board.getSavings().remove(board.getSavings().size()-1);
        board.swapColor();
        board.repaint();
    }
    //for undo function in the copyboard

    public ArrayList<String> generateAllMoves(ChessboardComponent board, ChessColor color){
        ArrayList<String> moves=new ArrayList<>();
        ChessComponent[][] chessboard=board.getChessboard();
        int i,j,k,l;
        for (i=0;i<chessboard.length;i++){
            for (j=0;j<chessboard[i].length;j++){
                if (chessboard[i][j].getChessColor()==color){
                    for (k=0;k<chessboard.length;k++){
                        for (l=0;l<chessboard[k].length;l++){
                            if (chessboard[i][j].getChessColor()!=chessboard[k][l].getChessColor()
                                    &&chessboard[i][j].canMoveTo(chessboard,chessboard[k][l].getChessboardPoint())){
                                String walk=String.format("%d%d%d%d",i,j,k,l);
                                moves.add(walk);
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }
    //generate a list of all moves for a certain board and color, return a new ArrayList

    public int MiniMaxSearch(int depth) {
        ChessColor searchColor = copyboard.currentColor;

        if (depth == 0) {
            return calculatesum(copyboard);
        }

        bestvalue[searchdepth-depth] = (copyboard.currentColor == ChessColor.BLACK ? 10000 : -10000);
        allMoves[searchdepth - depth] = generateAllMoves(copyboard, copyboard.currentColor);

        for (counter[searchdepth-depth] = 0; counter[searchdepth-depth] < allMoves[searchdepth - depth].size(); counter[searchdepth-depth]++) {
            int i = Integer.parseInt(String.valueOf(allMoves[searchdepth - depth].get(counter[searchdepth-depth]).charAt(0)));
            int j = Integer.parseInt(String.valueOf(allMoves[searchdepth - depth].get(counter[searchdepth-depth]).charAt(1)));
            int k = Integer.parseInt(String.valueOf(allMoves[searchdepth - depth].get(counter[searchdepth-depth]).charAt(2)));
            int l = Integer.parseInt(String.valueOf(allMoves[searchdepth - depth].get(counter[searchdepth-depth]).charAt(3)));
            copyboard.swapChessComponents(copychessboard[i][j], copychessboard[k][l]);
            value = MiniMaxSearch(depth - 1);
            changeChessboard(copyboard);
            //System.out.println(times++);

            if (searchColor == ChessColor.BLACK) {
                if (value<bestvalue[searchdepth-depth]){
                    bestvalue[searchdepth-depth]=value;
                    bestlocation=counter[searchdepth-depth];
                }
            } else {
                if (value>bestvalue[searchdepth-depth]){
                    bestvalue[searchdepth-depth]=value;
                    bestlocation=counter[searchdepth-depth];
                }
            }
        }
        //Using the loop to find the bestvalue and the bestmove

        if (depth == searchdepth){
            bestmove = allMoves[0].get(bestlocation);
        }

        return bestvalue[searchdepth-depth];
    }
    //bestvalue is the best value for current player after depth search

    public void AIgo(int depth){
        MiniMaxSearch(depth);
        //use the minimax method to find the best move

        int i = Integer.parseInt(String.valueOf(bestmove.charAt(0)));
        int j = Integer.parseInt(String.valueOf(bestmove.charAt(1)));
        int k = Integer.parseInt(String.valueOf(bestmove.charAt(2)));
        int l = Integer.parseInt(String.valueOf(bestmove.charAt(3)));
        for (int counter = 0; counter < allMoves.length; counter++) {
            if (allMoves[counter] != null) {
                allMoves[counter].clear();
            }
        }
        copyboard.swapChessComponents(copychessboard[i][j], copychessboard[k][l]);
        board.swapChessComponentsman1(chessboard[i][j], chessboard[k][l]);
        //make a step according to the best step
    }

}




