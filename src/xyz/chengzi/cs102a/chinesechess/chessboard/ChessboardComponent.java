package xyz.chengzi.cs102a.chinesechess.chessboard;

import xyz.chengzi.cs102a.chinesechess.AImode;
import xyz.chengzi.cs102a.chinesechess.ChessGameFrame;
import xyz.chengzi.cs102a.chinesechess.Internetgame1;
import xyz.chengzi.cs102a.chinesechess.chess.*;
import xyz.chengzi.cs102a.chinesechess.chess.internet.computerout;
import xyz.chengzi.cs102a.chinesechess.chess.internet.computerin;
import xyz.chengzi.cs102a.chinesechess.listener.ChessListener;
import xyz.chengzi.cs102a.chinesechess.listener.ChessboardChessListener;
import xyz.chengzi.cs102a.chinesechess.listener.MusicPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;

public class ChessboardComponent extends JComponent {
    public Thread music;
    public static boolean AI_OPEN=false;
    private ChessColor AIColor;
    public static boolean threadfinish=false;    public static boolean lastthreadfinish=false;
    public static boolean eatover=false;
    public static int internet;
    public static boolean iscomputer=false;
    public ChessListener chessListener = new ChessboardChessListener(this);
    protected ChessComponent[][] chessboard = new ChessComponent[10][9];
    protected ArrayList<ChessComponent[][]> savings=new ArrayList<>();
    public ChessColor currentColor = ChessColor.RED;
    public JLabel turn=new JLabel("红方行棋");
    public String theme="经典";
    computerin in;
    private String playerRed;
    private String playerBlack;
    private ArrayList<String> players=new ArrayList<>();
    private ArrayList<Integer> winGames=new ArrayList<>();
    public AImode aimode;

    public ChessboardComponent(int width, int height) {
        setLayout(null); // Use absolute layout.
        turn.setLocation(400,144);
        turn.setSize(200,200);
        add(turn);

        JLabel river1=new JLabel("楚河");
        river1.setLocation(145,144);
        river1.setSize(250,200);
        add(river1);

        JLabel river2=new JLabel("汉界");
        river2.setLocation(315,144);
        river2.setSize(250,200);
        add(river2);

        setSize(width, height);
        ChessComponent.registerListener(chessListener);
        newboard();
        if (ChessGameFrame.internet){try {
            ServerSocket server=new ServerSocket(Integer.parseInt(Internetgame1.jt1.getText()));
            //System.out.println(Internetgame1.jt1.getText());
            in=new computerin(server,this);
            in.start();
        } catch (IOException e) {
            e.printStackTrace();
        }}
        if (AI_OPEN&&width!=0){
            aimode=new AImode(this,ChessColor.BLACK);
            //System.out.println("AIopen");
        }
        try {
            MusicPlayer musicPlayer=new MusicPlayer("music.wav");
            music=new Thread(musicPlayer);
            music.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ChessboardComponent(int width, int height,boolean open,ChessColor color) {
        this.AIColor=ChessColor.BLACK;
        this.AI_OPEN=open;
        setLayout(null); // Use absolute layout.
        turn.setLocation(400,144);
        turn.setSize(200,200);
        add(turn);

        JLabel river1=new JLabel("楚河");
        river1.setLocation(145,144);
        river1.setSize(250,200);
        add(river1);

        JLabel river2=new JLabel("汉界");
        river2.setLocation(315,144);
        river2.setSize(250,200);
        add(river2);

        setSize(width, height);
        ChessComponent.registerListener(chessListener);
        newboard();
        if (ChessGameFrame.internet){try {
            ServerSocket server=new ServerSocket(Integer.parseInt(Internetgame1.jt1.getText()));
            //System.out.println(Internetgame1.jt1.getText());
            in=new computerin(server,this);
            in.start();
        } catch (IOException e) {
            e.printStackTrace();
        }}
        if (AI_OPEN&&width!=0){
            aimode=new AImode(this,ChessColor.BLACK);
            //System.out.println("AIopen1");
        }
        try {
            MusicPlayer musicPlayer=new MusicPlayer("music.wav");
            music=new Thread(musicPlayer);
            music.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ChessboardPoint getGeneralposition(ChessComponent[][] chessboard,ChessColor color){
        int i,j;
        for (i=0;i<chessboard.length;i++){
            for (j=0;j<chessboard[i].length;j++){
                if (chessboard[i][j] instanceof GeneralChessComponent&&chessboard[i][j].getChessColor()==color) return chessboard[i][j].getChessboardPoint();
            }
        }
        return chessboard[0][0].getChessboardPoint();
    }

    public void newboard(){
        for (int i = 0; i < chessboard.length; i++) {
            for (int j = 0; j < chessboard[i].length; j++) {
                putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j)));
            }
        }
        initTestBoard(0, 0, ChessColor.BLACK);
        initTestBoard(0, 8, ChessColor.BLACK);
        initTestBoard(9, 0, ChessColor.RED);
        initTestBoard(9, 8, ChessColor.RED);
        initTestBoard(2, 1, ChessColor.BLACK, CannonChessComponent.class);
        initTestBoard(2, 7, ChessColor.BLACK,CannonChessComponent.class);
        initTestBoard(7, 1, ChessColor.RED,CannonChessComponent.class);
        initTestBoard(7, 7, ChessColor.RED,CannonChessComponent.class);
        initTestBoard(0, 1, ChessColor.BLACK, HorseChessComponent.class);
        initTestBoard(0, 7, ChessColor.BLACK,HorseChessComponent.class);
        initTestBoard(9, 1, ChessColor.RED,HorseChessComponent.class);
        initTestBoard(9, 7, ChessColor.RED,HorseChessComponent.class);
        initTestBoard(0, 2, ChessColor.BLACK, ElephantChessComponent.class);
        initTestBoard(0, 6, ChessColor.BLACK,ElephantChessComponent.class);
        initTestBoard(9, 2, ChessColor.RED,ElephantChessComponent.class);
        initTestBoard(9, 6, ChessColor.RED,ElephantChessComponent.class);
        initTestBoard(0, 3, ChessColor.BLACK, AdvisorChessComponent.class);
        initTestBoard(0, 5, ChessColor.BLACK,AdvisorChessComponent.class);
        initTestBoard(9, 3, ChessColor.RED,AdvisorChessComponent.class);
        initTestBoard(9, 5, ChessColor.RED,AdvisorChessComponent.class);
        initTestBoard(0, 4, ChessColor.BLACK,GeneralChessComponent.class);
        initTestBoard(9, 4, ChessColor.RED,GeneralChessComponent.class);

        initTestBoard(6, 0, ChessColor.RED,SoldierChessComponent.class);
        initTestBoard(6, 2, ChessColor.RED,SoldierChessComponent.class);
        initTestBoard(6, 4, ChessColor.RED,SoldierChessComponent.class);
        initTestBoard(6, 6, ChessColor.RED,SoldierChessComponent.class);
        initTestBoard(6, 8, ChessColor.RED,SoldierChessComponent.class);

        initTestBoard(3, 0, ChessColor.BLACK,SoldierChessComponent.class);
        initTestBoard(3, 2, ChessColor.BLACK,SoldierChessComponent.class);
        initTestBoard(3, 4, ChessColor.BLACK,SoldierChessComponent.class);
        initTestBoard(3, 6, ChessColor.BLACK,SoldierChessComponent.class);
        initTestBoard(3, 8, ChessColor.BLACK,SoldierChessComponent.class);
        savings.add(makesaving(chessboard));
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setAIColor(ChessColor AIColor) {
        this.AIColor = AIColor;
    }

    public ChessColor getAIColor() {
        return AIColor;
    }

    public ChessComponent[][] getChessboard() {
        return chessboard;
    }

    public void setChessboard(ChessComponent[][] chessboard) {
        this.chessboard = chessboard;
    }

    public ChessColor getCurrentColor() {
        return currentColor;
    }

    public ArrayList<ChessComponent[][]> getSavings() {
        return savings;
    }

    public void setCurrentColor(ChessColor currentColor) {
        this.currentColor = currentColor;
    }

    public String getPlayerBlack() {
        return playerBlack;
    }

    public void setPlayerBlack(String playerBlack) {
        this.playerBlack = playerBlack;
        if (!(players.contains(playerBlack))){
            players.add(playerBlack);
            winGames.add(0);
        }
    }

    public String getPlayerRed() {
        return playerRed;
    }

    public void setPlayerRed(String playerRed) {
        this.playerRed = playerRed;
        if (!(players.contains(playerRed))){
            players.add(playerRed);
            winGames.add(0);
        }
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public ArrayList<Integer> getWinGames() {
        return winGames;
    }

    public void putChessOnBoard(ChessComponent chessComponent) {
        int row = chessComponent.getChessboardPoint().getX(), col = chessComponent.getChessboardPoint().getY();
        if (chessboard[row][col] != null) {
            remove(chessboard[row][col]);
        }
        chessboard[row][col] = chessComponent;
        add(chessboard[row][col] = chessComponent);
    }

    class  judg extends Thread{
        Thread t;
        public judg(Thread t){
            this.t=t;
        }

        @Override
        public void run() {
            super.run();
            while (true){
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!t.isAlive()){
                    eatover=false;
                    System.out.println("jugde over");
                    if (AI_OPEN){
                        if (ChessboardComponent.this.getCurrentColor()!=ChessColor.RED) ChessboardComponent.this.swapColor();
                        for (int i=0;i<10;i++){
                            for (int j=0;j<9;j++){
                                if (!aimode.copychessboard[i][j].toString().equals(chessboard[i][j].toString())){
                                    System.out.println("爆炸"+i+"+j");
                                    //System.out.println("open");
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    class AI extends Thread{
        AImode AI;
        ChessComponent c1,c2;
        public AI(ChessboardComponent board,ChessColor color,ChessComponent ch1,ChessComponent ch2,AImode AI){
            this.AI=AI;
            c1=ch1;
            c2=ch2;
        }

        @Override
        public void run(){
            while (true){
                try {
                    Thread.sleep(100);
                    if (eatover){
                        eatover=false;
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.AI.AIgo(2);
        }
    }

    class  swap extends Thread{
        ChessComponent chess1;
        ChessComponent chess2;
        public swap(ChessComponent chess1, ChessComponent chess2){
            this.chess1=chess1;
            this.chess2=chess2;
        }

        @Override
        public void run() {
            super.run();
            for (int i=0;i<chessboard.length;i++){
                for (int j=0;j<chessboard[i].length;j++){
                    chessboard[i][j].canselect=false;
                }
            }
            if (!(chess2 instanceof EmptySlotComponent)) {
                remove(chess2);
                add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation()));
            }
            chess1.swapLocation(chess2,ChessboardComponent.this);
            judge:while (true){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (lastthreadfinish){
                    lastthreadfinish=false;
                    break judge;
                }
            }
            System.out.println("break");
            repaint();
            int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
            chessboard[row1][col1] = chess1;
            int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
            chessboard[row2][col2] = chess2;

            if (!ChessboardComponent.this.canContinue()){
                if (ChessGameFrame.internet){
                    if (getCurrentColor()==ChessColor.RED){
                        JOptionPane.showMessageDialog(ChessboardComponent.this, "红方胜利");
                    }else {JOptionPane.showMessageDialog(ChessboardComponent.this, "黑方胜利");}
                    //updateRankList();
                }else if (AI_OPEN){
                    if (getCurrentColor()==ChessColor.RED){
                        JOptionPane.showMessageDialog(ChessboardComponent.this, "红方胜利");
                    }else {JOptionPane.showMessageDialog(ChessboardComponent.this, "黑方胜利");}
                    //updateRankList();
                }else{
                    decideWinner();
                    updateRankList();
                }
                for (int i = 0; i < chessboard.length; i++) {
                    for (int j = 0; j < chessboard[i].length; j++) {
                        chessboard[i][j].canselect=false;
                    }
                }
                return;

            }

            if (!iscomputer){
                judge:for (int i=0;i<chessboard.length;i++){
                    for (int j=0;j<chessboard[i].length;j++){
                        if (chessboard[i][j].canMoveTo(chessboard,getGeneralposition(chessboard,chessboard[i][j].getChessColor()==ChessColor.BLACK?ChessColor.RED:ChessColor.BLACK))){
                            JOptionPane.showMessageDialog(ChessboardComponent.this, "将军");
                            break judge;
                        }
                    }
                }}
            for (int i=0;i<chessboard.length;i++){
                for (int j=0;j<chessboard[i].length;j++){
                    chessboard[i][j].canselect=true;
                }
            }
            eatover=true;
            savings.add(makesaving(chessboard));
            ChessboardComponent.this.swapColor();
        }
    }

    public void swapChessComponentsman1(ChessComponent chess1, ChessComponent chess2){
        swap swap=new swap(chess1,chess2);
        judg judg=new judg(swap);
        swap.start();
        judg.start();
    }
    public void swapChessComponentsman(ChessComponent chess1, ChessComponent chess2){
        /*if (chess1.getChessColor()==ChessColor.RED){
            System.out.println((9-chess1.getChessboardPoint().getY())+" "+(10-chess1.getChessboardPoint().getX())+
                    (9-chess2.getChessboardPoint().getY())+" "+(10-chess2.getChessboardPoint().getX()));
        }else {System.out.println((chess1.getChessboardPoint().getY()+1)+" "+(chess1.getChessboardPoint().getX()+1)+
                (chess2.getChessboardPoint().getY()+1)+" "+(chess2.getChessboardPoint().getX()+1));}*/
        if (ChessGameFrame.internet){
            computerout out;
            try  {
                String[] ouput={Integer.toString(chess1.getChessboardPoint().getX())+ Integer.toString(chess1.getChessboardPoint().getY())+Integer.toString(chess2.getChessboardPoint().getX())+Integer.toString(chess2.getChessboardPoint().getY())};
                out=new computerout(this,ouput);
                out.start();
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("output失败");
            }
            swap swap=new swap(chess1,chess2);
            swap.start();
        }else{
            if (AI_OPEN){
                swap swap=new swap(chess1,chess2);
                swap.start();
                aimode.copyboard.swapChessComponents(aimode.copychessboard[chess1.getChessboardPoint().getX()][chess1.getChessboardPoint().getY()],
                        aimode.copychessboard[chess2.getChessboardPoint().getX()][chess2.getChessboardPoint().getY()]);
                AI ai=new AI(this,ChessColor.BLACK,chess1,chess2,aimode);
                ai.start();
                //aimode.AIgo(1);
            }else {swap swap=new swap(chess1,chess2);
                swap.start();}

        }

    }

    public void swapChessComponents(ChessComponent chess1, ChessComponent chess2) {
        if(AI_OPEN){
            //System.out.println("open");
            ChessComponent chess3=new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation());
            /*if (!(chess2 instanceof EmptySlotComponent)) {
                remove(chess2);
                add(chess3 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation()));
            }else {chess3 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation());}*/
            chess1.swapLocation(chess3);
            int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
            chessboard[row1][col1] = chess1;

            int row2 = chess3.getChessboardPoint().getX(), col2 = chess3.getChessboardPoint().getY();
            if (row1==row2&&col1==col2) {}else {
                chessboard[row2][col2] = chess3;}

        }else {if (!(chess2 instanceof EmptySlotComponent)) {
            remove(chess2);
            add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation())); }
            chess1.swapLocation(chess2);
            int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
            chessboard[row1][col1] = chess1;
            int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
            chessboard[row2][col2] = chess2;
            if (!this.canContinue()){
                //if (currentColor==ChessColor.BLACK) JOptionPane.showMessageDialog(this, "黑方胜");
                //else JOptionPane.showMessageDialog(this, "红方胜");
                decideWinner();
                updateRankList();
                for (int i = 0; i < chessboard.length; i++) {
                    for (int j = 0; j < chessboard[i].length; j++) {
                        chessboard[i][j].canselect=false;
                    }
                }
                return;
            }
            if (!iscomputer){
                judge:for (int i=0;i<chessboard.length;i++){
                    for (int j=0;j<chessboard[i].length;j++){
                        if (chessboard[i][j].canMoveTo(chessboard,getGeneralposition(chessboard,chessboard[i][j].getChessColor()==ChessColor.BLACK?ChessColor.RED:ChessColor.BLACK))){
                            JOptionPane.showMessageDialog(this, "将军");
                            break judge;
                        }
                    }
                }}




        }
        swapColor();
        savings.add(makesaving(chessboard));


    }

    public void swapColor() {
        currentColor = currentColor == ChessColor.BLACK ? ChessColor.RED : ChessColor.BLACK;
        if (currentColor== ChessColor.BLACK) turn.setText("黑方行棋");
        else turn.setText("红方行棋");

    }

    public void initTestBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new ChariotChessComponent(new ChessboardPoint(row, col),
                calculatePoint(row, col), color);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }

    public void initTestBoard(int row, int col, ChessColor color,Class <?extends ChessComponent> type) {
        try{
            Constructor constructor=type.getDeclaredConstructor(ChessboardPoint.class,Point.class,ChessColor.class);
            ChessComponent chessComponent = (ChessComponent) constructor.newInstance(new ChessboardPoint(row, col),
                    calculatePoint(row, col), color);
            chessComponent.setVisible(true);
            putChessOnBoard(chessComponent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintChessBoard(g);
        paintBoardLine(g, 0, 0, 9, 0);
        paintBoardLine(g, 0, 8, 9, 8);
        paintHalfBoard(g, 0);
        paintHalfBoard(g, 5);
        paintKingSquare(g, 1, 4);
        paintKingSquare(g, 8, 4);
    }

    private void paintHalfBoard(Graphics g, int startRow) {
        for (int row = startRow; row < startRow + 5; row++) {
            paintBoardLine(g, row, 0, row, 8);
        }
        for (int col = 0; col < 9; col++) {
            paintBoardLine(g, startRow, col, startRow + 4, col);
        }
    }

    private void paintKingSquare(Graphics g, int centerRow, int centerCol) {
        paintBoardLine(g, centerRow - 1, centerCol - 1, centerRow + 1, centerCol + 1);
        paintBoardLine(g, centerRow - 1, centerCol + 1, centerRow + 1, centerCol - 1);
    }

    private void paintBoardLine(Graphics g, int rowFrom, int colFrom, int rowTo, int colTo) {
        int offsetX = ChessComponent.CHESS_SIZE.width / 2, offsetY = ChessComponent.CHESS_SIZE.height / 2;
        Point p1 = calculatePoint(rowFrom, colFrom), p2 = calculatePoint(rowTo, colTo);
        g.drawLine(p1.x + offsetX, p1.y + offsetY, p2.x + offsetX, p2.y + offsetY);
    }

    public void paintChessBoard(Graphics g){
        Image classic=new ImageIcon("classic.jpg").getImage();
        Image dark=new ImageIcon("dark.jpg").getImage();
        Image ice=new ImageIcon("ice.jpg").getImage();
        if (theme.equals("经典")){
            g.drawImage(classic,5,5,this);
        }else if (theme.equals("黑夜")){
            g.drawImage(dark,5,5,this);
        }else if (theme.equals("冰雪")){
            g.drawImage(ice,5,5,this);
        }
    }

    public Point calculatePoint(int row, int col) {
        return new Point(col * getWidth() / 9, row * getHeight() / 10);
    }

    public boolean canContinue(){
        int generalCounter=0;
        for (int i=0;i<10;i++){
            for (int j=0;j<9;j++){
                if (this.chessboard[i][j] instanceof GeneralChessComponent) {
                    generalCounter++;
                }
            }
        }
        if (generalCounter==2){
            return true;
        }
        return false;
    }

    public void decideWinner(){
        /*if (AI_OPEN){
            if (currentColor.equals(ChessColor.BLACK)){
                JOptionPane.showMessageDialog(this, "红方胜");
            }
            else{
                JOptionPane.showMessageDialog(this, "黑方胜");
            }
        }else {*/
            if (currentColor.equals(ChessColor.BLACK)){
                JOptionPane.showMessageDialog(this, "黑方胜");
                winGames.set(players.indexOf(playerBlack),winGames.get(players.indexOf(playerBlack))+1);
            }
            else{
                JOptionPane.showMessageDialog(this, "红方胜");
                winGames.set(players.indexOf(playerRed),winGames.get(players.indexOf(playerRed))+1);
            }
        //}


    }

    public void updateRankList(){
        String tempstr;
        Integer tempint;
        for (int i=0;i<players.size();i++){
            for (int j=0;j<players.size()-1;j++){
                if (winGames.get(j)<winGames.get(j+1)){
                    tempint=winGames.get(j);
                    winGames.set(j,winGames.get(j+1));
                    winGames.set(j+1,tempint);
                    tempstr=players.get(j);
                    players.set(j,players.get(j+1));
                    players.set(j+1,tempstr);
                }
            }
        }
    }

    public ChessComponent[][] makesaving(ChessComponent[][] chessboard){
        ChessComponent[][] toSave = new ChessComponent[10][9];
        for (int i=0;i<chessboard.length;i++){
            toSave[i]=Arrays.copyOf(chessboard[i],chessboard[i].length);
        }
        return toSave;
    }

}