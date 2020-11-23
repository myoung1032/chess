package xyz.chengzi.cs102a.chinesechess;

import javafx.stage.FileChooser;
import xyz.chengzi.cs102a.chinesechess.chess.*;
import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardComponent;
import xyz.chengzi.cs102a.chinesechess.chessboard.ChessboardPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ChessGameFrame extends JFrame {
    class renew implements Runnable{
        ChessboardComponent chessboardComponent;
        public renew(ChessboardComponent chessboardComponent){
            this.chessboardComponent=chessboardComponent;
        }
        @Override
        public void run() {
            chessboardComponent.repaint();
        }
    }
    ChessboardComponent chessboard;//= new ChessboardComponent(500, 500);
    public static boolean internet=false;
    public boolean isAIGame=false;
    Image background= new ImageIcon("background.jpg").getImage();
    JButton button = new JButton("新游戏");
    JButton button1 = new JButton("悔棋");
    JButton button2 = new JButton("导入");
    JButton button3 = new JButton("存盘");
    JButton button4 = new JButton("加载棋谱");
    JButton button5 = new JButton("结束并回放");
    JButton button6 = new JButton("排行榜");
    JButton button7 = new JButton("清空");
    JButton button8 = new JButton("主题");
    JButton button9 = new JButton("停止背景音乐");
    JCheckBox hasprocess=new JCheckBox("播放棋谱走棋过程");

    public ChessGameFrame() {
        chessboard= new ChessboardComponent(500, 500);
        setTitle("2019 CS102A Project Demo");
        setSize(700, 560);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        add(chessboard);

        button.addActionListener(e -> {
            String red=JOptionPane.showInputDialog("红方用户名");
            String black=JOptionPane.showInputDialog("黑方用户名");
            if (red==null||black==null){
                JOptionPane.showMessageDialog(this,"请输入用户名参与排名");
            }else if (red.trim().equals("")||black.trim().equals("")){
                JOptionPane.showMessageDialog(this,"请输入用户名参与排名");
            } else {
                chessboard.setPlayerRed(red);
                chessboard.setPlayerBlack(black);
                newboard(chessboard);
            }
        });
        button.setLocation(490, 50);
        button.setSize(80, 40);
        add(button);

        button1.addActionListener(e ->previousChessBoard(chessboard));
        button1.setLocation(490, 100);
        button1.setSize(80, 40);
        add(button1);

        button2.addActionListener(e -> {
            try {
                input(chessboard);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        button2.setLocation(490, 150);
        button2.setSize(80, 40);
        add(button2);

        button3.addActionListener(e -> store(chessboard));
        button3.setLocation(490, 200);
        button3.setSize(80, 40);
        add(button3);

        button4.addActionListener(e -> {
            JFileChooser fileChooser=new JFileChooser();
            int i=fileChooser.showOpenDialog(getContentPane());
            if (i==JFileChooser.APPROVE_OPTION){

                String str="abcde";
                try {
                    str=fileChooser.getSelectedFile().getName().split("chessmoveseq")[0];
                }catch (Exception E){E.printStackTrace();}
                //System.out.println(fileChooser.getSelectedFile().getParent()+"\\"+str+"chessboard");
                File chesssboardfile=new File(fileChooser.getSelectedFile().getParent()+"\\"+str+"chessboard");
                if (chesssboardfile.exists()){
                    processmove process=new processmove(chessboard,fileChooser.getSelectedFile(),true,chesssboardfile);
                    Thread t=new Thread(process);
                    t.setPriority(9);
                    t.start();
                }else {
                    processmove process=new processmove(chessboard,fileChooser.getSelectedFile(),false);
                    Thread t=new Thread(process);
                    t.setPriority(9);
                    t.start();
                }

                /*chessmoveseq(chessboard,fileChooser.getSelectedFile());*/
            }
        });
        button4.setLocation(490, 250);
        button4.setSize(160, 40);
        add(button4);

        button5.addActionListener(e -> {
            playBackInSteps play=new playBackInSteps(chessboard,0);
            Thread t=new Thread(play);
            t.start();
            this.repaint();
        });
        button5.setLocation(490, 350);
        button5.setSize(160, 40);
        add(button5);

        button6.addActionListener(e -> {
            JFrame ranks=new JFrame("皇城pk榜");
            ranks.setSize(400,200);
            ranks.setLayout(null);
            ranks.setLocationRelativeTo(null);
            ranks.setVisible(true);

            String[] colnames={"名次","用户","获胜局数"};
            String[][] info=new String[chessboard.getPlayers().size()][3];
            for (int i=0;i<chessboard.getPlayers().size();i++){
                info[i][0]="Rank"+ (i + 1);
                info[i][1]=chessboard.getPlayers().get(i);
                info[i][2]= String.valueOf(chessboard.getWinGames().get(i));
            }
            JTable rankList=new JTable(info,colnames);
            rankList.setSize(400,200);
            rankList.setVisible(true);
            ranks.add(rankList);
        });
        button6.setLocation(490, 400);
        button6.setSize(80, 40);
        add(button6);

        button7.addActionListener(e -> {
            chessboard.getPlayers().clear();
            chessboard.getWinGames().clear();
            JOptionPane.showMessageDialog(this,"清除成功");
        });
        button7.setLocation(580, 400);
        button7.setSize(80, 40);
        add(button7);

        button8.addActionListener(e -> {
            Object[] obj2 ={ "经典", "黑夜", "冰雪" };
            String theme = (String) JOptionPane.showInputDialog(null,"选择主题:\n", "主题",
                    JOptionPane.PLAIN_MESSAGE, new ImageIcon("icon.png"), obj2, "足球");
            if(theme!=null){
                chessboard.setTheme(theme);
                repaint();
            }

        });
        button8.setLocation(580, 200);
        button8.setSize(80, 40);
        add(button8);

        button9.addActionListener(e -> {
            chessboard.music.stop();
            JOptionPane.showMessageDialog(this,"停止成功");
        });
        button9.setLocation(500, 450);
        button9.setSize(160, 40);
        add(button9);

        hasprocess.setLocation(490,291);
        hasprocess.setSize(160,40);
        add(hasprocess);
    }

    public ChessGameFrame(String red,String black){
        this();
        chessboard.setPlayerRed(red);
        chessboard.setPlayerBlack(black);
    }

    public ChessGameFrame(boolean isAIGame,ChessColor AIColor){
        chessboard=new ChessboardComponent(500,500,true,AIColor);
        this.isAIGame=isAIGame;
        setTitle("2019 CS102A Project Demo");
        setSize(700, 560);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        add(chessboard);
        chessboard.repaint();


        button8.addActionListener(e -> {
            Object[] obj2 ={ "经典", "黑夜", "冰雪" };
            String theme = (String) JOptionPane.showInputDialog(null,"选择主题:\n", "主题",
                    JOptionPane.PLAIN_MESSAGE, new ImageIcon("icon.png"), obj2, "足球");
            if(theme!=null){
                chessboard.setTheme(theme);
                repaint();
            }

        });
        button8.setLocation(580, 200);
        button8.setSize(80, 40);
        add(button8);

        button9.addActionListener(e -> {
            chessboard.music.stop();
            JOptionPane.showMessageDialog(this,"停止成功");
        });
        button9.setLocation(500, 450);
        button9.setSize(160, 40);
        add(button9);

    }

    public void newboard(ChessboardComponent board){
        for (int i = 0; i < board.getChessboard().length; i++) {
            for (int j = 0; j < board.getChessboard()[i].length; j++) {
                board.putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), board.calculatePoint(i, j)));
            }
        }
        board.initTestBoard(2, 1, ChessColor.BLACK, CannonChessComponent.class);
        board.initTestBoard(2, 7, ChessColor.BLACK,CannonChessComponent.class);
        board.initTestBoard(7, 1, ChessColor.RED,CannonChessComponent.class);
        board.initTestBoard(7, 7, ChessColor.RED,CannonChessComponent.class);
        board.initTestBoard(0, 0, ChessColor.BLACK);
        board.initTestBoard(0, 8, ChessColor.BLACK);
        board.initTestBoard(9, 0, ChessColor.RED);
        board.initTestBoard(9, 8, ChessColor.RED);
        board.initTestBoard(0, 1, ChessColor.BLACK, HorseChessComponent.class);
        board.initTestBoard(0, 7, ChessColor.BLACK,HorseChessComponent.class);
        board.initTestBoard(9, 1, ChessColor.RED,HorseChessComponent.class);
        board.initTestBoard(9, 7, ChessColor.RED,HorseChessComponent.class);
        board.initTestBoard(0, 2, ChessColor.BLACK, ElephantChessComponent.class);
        board.initTestBoard(0, 6, ChessColor.BLACK,ElephantChessComponent.class);
        board.initTestBoard(9, 2, ChessColor.RED,ElephantChessComponent.class);
        board.initTestBoard(9, 6, ChessColor.RED,ElephantChessComponent.class);
        board.initTestBoard(0, 3, ChessColor.BLACK, AdvisorChessComponent.class);
        board.initTestBoard(0, 5, ChessColor.BLACK,AdvisorChessComponent.class);
        board.initTestBoard(9, 3, ChessColor.RED,AdvisorChessComponent.class);
        board.initTestBoard(9, 5, ChessColor.RED,AdvisorChessComponent.class);
        board.initTestBoard(0, 4, ChessColor.BLACK,GeneralChessComponent.class);
        board.initTestBoard(9, 4, ChessColor.RED,GeneralChessComponent.class);

        board.initTestBoard(6, 0, ChessColor.RED,SoldierChessComponent.class);
        board.initTestBoard(6, 2, ChessColor.RED,SoldierChessComponent.class);
        board.initTestBoard(6, 4, ChessColor.RED,SoldierChessComponent.class);
        board.initTestBoard(6, 6, ChessColor.RED,SoldierChessComponent.class);
        board.initTestBoard(6, 8, ChessColor.RED,SoldierChessComponent.class);

        board.initTestBoard(3, 0, ChessColor.BLACK,SoldierChessComponent.class);
        board.initTestBoard(3, 2, ChessColor.BLACK,SoldierChessComponent.class);
        board.initTestBoard(3, 4, ChessColor.BLACK,SoldierChessComponent.class);
        board.initTestBoard(3, 6, ChessColor.BLACK,SoldierChessComponent.class);
        board.initTestBoard(3, 8, ChessColor.BLACK,SoldierChessComponent.class);
        board.turn.setText("红方行棋");
        board.setCurrentColor(ChessColor.RED);
        board.repaint();
    }

    public void endGame(ChessboardComponent chessboard){
        for (int i = 0; i < chessboard.getChessboard().length; i++) {
            for (int j = 0; j < chessboard.getChessboard()[i].length; j++) {
                chessboard.getChessboard()[i][j].canselect=false;
            }
        }
    }

    public void store(ChessboardComponent chessboard){
        int i=1;
        File store=new File("store0.chessboard");
        while (store.exists()){
            store=new File("store"+i+".chessboard");
            i++;
        }
        System.out.println(store.getAbsolutePath());
        try {
            String s="";
            FileWriter writer = new FileWriter(store);
            if (chessboard.getCurrentColor()==ChessColor.BLACK){
                s="RED";
            }else {s="BLACK";}
            writer.write("@LAST_MOVER="+s+"\n");
            writer.write("@@\n\n");
            for (i = 0; i < 5; i++) {
                for (int j = 0; j < chessboard.getChessboard()[i].length; j++) {
                    writer.write(chessboard.getChessboard()[i][j].toString());
                }
                writer.write("\n");
            }
            writer.write("---------\n");
            for (i = 5; i < 10; i++) {
                for (int j = 0; j < chessboard.getChessboard()[i].length; j++) {
                    writer.write(chessboard.getChessboard()[i][j].toString());
                }
                writer.write("\n");
            }
            writer.write("\n");
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void input(ChessboardComponent chessboard) throws FileNotFoundException {
        //FileChooser chooser=new FileChooser();
        JFileChooser fileChooser=new JFileChooser();
        int m=fileChooser.showOpenDialog(getContentPane());
        if (m!=JFileChooser.APPROVE_OPTION){
            return;
        }
        Scanner input = new Scanner(fileChooser.getSelectedFile(), "UTF-8");

        String color;
        ArrayList<String> info=new ArrayList<>();
        while (input.hasNextLine()) {
            info.add(input.nextLine());
        }
        input.close();
        //get all the input as an arraylist

        String[] toRemove=new String[info.size()];
        for (int i=0;i<info.size();i++){
            for (int j=0;j<info.size();j++){
                if (info.get(i).startsWith("#")){
                    toRemove[j]=info.get(i);
                }
            }
        }
        for (int i=0;i<toRemove.length;i++){
            info.remove(toRemove[i]);
        }
        for (int i=0;i<info.size();i++){
            for (int j=0;j<info.get(i).length();j++){
                if (info.get(i).charAt(j)=='#'){
                    info.set(i,info.get(i).substring(0,j));
                }
            }
        }
        //delete notes with #

        if (!info.get(0).substring(0,1).equals("@")){
            JOptionPane.showMessageDialog(this, String.format("\"Metadata missing\"\nat %s",fileChooser.getSelectedFile()));
        }

        for (int i=0;i<info.size();i++){
            info.set(i,info.get(i).trim());
        }

        color=info.get(0);

        if (color.contains("BLACK")){
            chessboard.setCurrentColor(ChessColor.RED);
            chessboard.turn.setText("红方行棋");
        }else if(color.contains("RED")){
            chessboard.setCurrentColor(ChessColor.BLACK);
            chessboard.turn.setText("黑方行棋");

        }

        info.remove(0);
        info.remove(0);
        info.remove(0);
        if (info.get(info.size()-1).length()==0){
            info.remove(info.size()-1);
        }

        //get the infomation of chessboard

        if (info.size()!=11||!checkDimension(info)){
            JOptionPane.showMessageDialog(this, String.format("\"Invalid Dimension\"\nat %s",fileChooser.getSelectedFile()));
        }else if (!info.get(5).equals("---------")){
            JOptionPane.showMessageDialog(this, String.format("\"River Missing\"\nat %s",fileChooser.getSelectedFile()));
        }else if (!checkGeneral(info)){
            JOptionPane.showMessageDialog(this, String.format("\"Invalid Chess Amount\"\nat %s",fileChooser.getSelectedFile()));
        }else if (!checkChessNumber(info)){
            JOptionPane.showMessageDialog(this, String.format("\"Invalid Chess Amount\"\nat %s",fileChooser.getSelectedFile()));
        }else if (!checkspace(info)){
            JOptionPane.showMessageDialog(this, String.format("\"Space missing\"\nat %s",fileChooser.getSelectedFile()));
        }else{
            info.remove("---------");
            for (int i = 0; i < chessboard.getChessboard().length; i++) {
                for (int j = 0; j < chessboard.getChessboard()[i].length; j++) {
                    chessboard.putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), chessboard.calculatePoint(i, j)));
                }
            }

            for (int i=0;i<10;i++){
                for (int j=0;j<9;j++){
                    if (info.get(i).charAt(j)!='.'){
                        chessboard.initTestBoard(i,j,decideColor(info.get(i).charAt(j)),decideType(info.get(i).charAt(j)));
                    }
                }
            }
            chessboard.repaint();
            JOptionPane.showMessageDialog(this, "导入成功");
        }
    }

    public void input(ChessboardComponent chessboard,File file) throws FileNotFoundException {

        Scanner input = new Scanner(file, "UTF-8");



        String color;

        ArrayList<String> info=new ArrayList<>();

        while (input.hasNextLine()) {

            info.add(input.nextLine());

        }

        input.close();

        //get all the input as an arraylist



        String[] toRemove=new String[info.size()];

        for (int i=0;i<info.size();i++){

            for (int j=0;j<info.size();j++){

                if (info.get(i).startsWith("#")){

                    toRemove[j]=info.get(i);

                }

            }

        }

        for (int i=0;i<toRemove.length;i++){

            info.remove(toRemove[i]);

        }

        for (int i=0;i<info.size();i++){

            for (int j=0;j<info.get(i).length();j++){

                if (info.get(i).charAt(j)=='#'){

                    info.set(i,info.get(i).substring(0,j));

                }

            }

        }

        //delete notes with #



        if (!info.get(0).substring(0,1).equals("@")){

            JOptionPane.showMessageDialog(this, String.format("\"Metadata missing\"\nat %s",file));

        }



        for (int i=0;i<info.size();i++){

            info.set(i,info.get(i).trim());

        }



        color=info.get(0);



        if (color.contains("BLACK")){

            chessboard.setCurrentColor(ChessColor.RED);

            chessboard.turn.setText("红方行棋");

        }else if(color.contains("RED")){

            chessboard.setCurrentColor(ChessColor.BLACK);

            chessboard.turn.setText("黑方行棋");



        }



        info.remove(0);

        info.remove(0);

        info.remove(0);

        if (info.get(info.size()-1).length()==0){

            info.remove(info.size()-1);

        }



        //get the infomation of chessboard



        if (info.size()!=11||!checkDimension(info)){

            JOptionPane.showMessageDialog(this, String.format("\"Invalid Dimension\"\nat %s",file));

        }else if (!info.get(5).equals("---------")){

            JOptionPane.showMessageDialog(this, String.format("\"River Missing\"\nat %s",file));

        }else if (!checkGeneral(info)){

            JOptionPane.showMessageDialog(this, String.format("\"Invalid Chess Amount\"\nat %s",file));

        }else if (!checkChessNumber(info)){

            JOptionPane.showMessageDialog(this, String.format("\"Invalid Chess Amount\"\nat %s",file));

        }else if (!checkspace(info)){

            JOptionPane.showMessageDialog(this, String.format("\"Space missing\"\nat %s",file));

        }else{

            info.remove("---------");

            for (int i = 0; i < chessboard.getChessboard().length; i++) {

                for (int j = 0; j < chessboard.getChessboard()[i].length; j++) {

                    chessboard.putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), chessboard.calculatePoint(i, j)));

                }

            }


            for (int i=0;i<10;i++){

                for (int j=0;j<9;j++){

                    if (info.get(i).charAt(j)!='.'){

                        chessboard.initTestBoard(i,j,decideColor(info.get(i).charAt(j)),decideType(info.get(i).charAt(j)));

                    }

                }

            }

            chessboard.repaint();

        }

    }

    public boolean checkDimension(ArrayList<String> info){
        for (int i=0;i<11;i++){
            if (i==5){
                continue;
            }
            if (info.get(i).length()!=9){
                return false;
            }
        }
        return true;
    }

    public Class decideType(char c){
        Class type = null;
        if (c=='G'||c=='g'){
            type=GeneralChessComponent.class;
        }else if (c=='A'||c=='a'){
            type=AdvisorChessComponent.class;
        }else if (c=='E'||c=='e'){
            type=ElephantChessComponent.class;
        }else if (c=='H'||c=='h'){
            type=HorseChessComponent.class;
        }else if (c=='C'||c=='c'){
            type=ChariotChessComponent.class;
        }else if (c=='N'||c=='n'){
            type=CannonChessComponent.class;
        }else if (c=='S'||c=='s'){
            type=SoldierChessComponent.class;
        }
        return type;
    }

    public ChessColor decideColor(char c){
        if (Character.isUpperCase(c)){
            return ChessColor.BLACK;
        }
        return ChessColor.RED;
    }

    public boolean checkGeneral(ArrayList<String> info){
        if (info.get(5).equals("---------")){
            info.remove(5);
        }
        boolean blackCheck=false;
        boolean redCheck=false;
        for (int i=0;i<3;i++){
            for (int j=3;j<6;j++){
                if (info.get(i).charAt(j)=='G'){
                    blackCheck=true;
                }
            }
        }
        for (int i=7;i<10;i++){
            for (int j=3;j<6;j++){
                if (info.get(i).charAt(j)=='g'){
                    redCheck=true;
                }
            }
        }
        return blackCheck && redCheck;
    }

    public boolean checkChessNumber(ArrayList<String> info){
        int blackgen=0,redgen=0;
        int blackadv=0,redadv=0;
        int blackcan=0,redcan=0;
        int blackcar=0,redcar=0;
        int blackele=0,redele=0;
        int blackhor=0,redhor=0;
        int blacksol=0,redsol=0;
        for (int i=0;i<10;i++){
            for (int j=0;j<9;j++){
                switch (info.get(i).charAt(j)){
                    case 'C' :
                        blackcar++;
                        break;
                    case 'c' :
                        redcar++;
                        break;
                    case 'H' :
                        blackhor++;
                        break;
                    case 'h' :
                        redhor++;
                        break;
                    case 'E' :
                        blackele++;
                        break;
                    case 'e' :
                        redele++;
                        break;
                    case 'A' :
                        blackadv++;
                        break;
                    case 'a' :
                        redadv++;
                        break;
                    case 'N' :
                        blackcan++;
                        break;
                    case 'n' :
                        redcan++;
                        break;
                    case 'S' :
                        blacksol++;
                        break;
                    case 's' :
                        redsol++;
                        break;
                    case 'G' :
                        blackgen++;
                        break;
                    case 'g' :
                        redgen++;
                        break;
                }
            }
        }
        if (blackadv>2||redadv>2){
            return false;
        }else if (blackcan>2||redcan>2){
            return false;
        }else if (blackcar>2||redcar>2){
            return false;
        }else if (blackele>2||redele>2){
            return false;
        }else if (blackhor>2||redhor>2){
            return false;
        }else if (blacksol>5||redsol>5){
            return false;
        }else if (blackgen!=1||redgen!=1){
            return false;
        }
        return true;
    }

    public boolean checkspace(ArrayList<String> info){
        for (int i=0;i<10;i++){
            if (info.get(i).contains(" ")){
                return false;
            }
        }
        return true;
    }

    public boolean inboardx(int x){
        if (x>=1&&x<=10) return true;
        return false;
    }

    public boolean inboardy(int y){
        if (y>=1&&y<=9) return true;
        return false;
    }

    public int strtoint(String str){
        String s="";
        for (int i=0;i<str.length();i++){
            if (Character.isDigit(str.charAt(i))) s+=str.charAt(i);
        }
        return Integer.parseInt(s);
    }


    class processmove implements Runnable{
        File file,chessboardfile;
        boolean haschessborad;

        public processmove(ChessboardComponent board,File file,boolean haschessborad){
            this.file=file;
            this.haschessborad=haschessborad;
        }
        public processmove(ChessboardComponent board,File file,boolean haschessborad,File chessboardfile){
            this.file=file;
            this.haschessborad=haschessborad;
            this.chessboardfile=chessboardfile;
        }
        @Override
        public void run() {
            if (haschessborad){
                chessmoveseq(chessboard,file,haschessborad,chessboardfile);
            }else {
                chessmoveseq(chessboard,file,haschessborad);
            }
            ;
        }}

    public void chessmoveseq(ChessboardComponent board,File file,boolean haschessboard) {
        //renew r=new renew(chessboard);
        //Thread t=new Thread(r);
        try {
            System.out.println(hasprocess.isSelected());
            if (hasprocess.isSelected()){
                if (haschessboard){

                }
                int x0,y0,x1,y1;
                int store;
                ChessboardComponent oldboard=chessboard;
                FileReader fr=new FileReader(file);
                BufferedReader reader=new BufferedReader(fr);
                newboard(oldboard);
                ChessComponent[][] oldcomponent=chessboard.getChessboard();
                String str=reader.readLine();
                if (str.indexOf("@TOTAL_STEP")<0){
                    JOptionPane.showMessageDialog(this, "第一行格式不正确");
                    return;
                }
                int rows=strtoint(str.split("=",2)[1]);
                while ((!str.equals(""))&&(str.indexOf("#")!=0)){
                    str=reader.readLine();
                }
                for (int i=0;i<rows;i++){
                    str=reader.readLine();
                    if (str.contains("#")){
                        if (str.indexOf("#")==0){
                            i--;
                            continue;
                        }
                        store=strtoint(str.split("#",2)[0]);
                    }else store=strtoint(str);
                    x0=store/1000;
                    y0=(store%1000)/100;
                    x1=(store%100)/10;
                    y1=store%10;
                    if (inboardx(x0)&&inboardx(x1)&&inboardy(y0)&&inboardy(y1)){
                        if (oldboard.getCurrentColor()==ChessColor.RED){
                            if (oldcomponent[10-y0][9-x0] instanceof EmptySlotComponent){
                                JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                continue;
                            }
                            if (oldcomponent[10-y0][9-x0].getChessColor()==ChessColor.RED){
                                if (oldcomponent[10-y1][9-x1].getChessColor()!=ChessColor.RED){
                                    if (oldcomponent[10-y0][9-x0].canMoveTo(oldcomponent,oldcomponent[10-y1][9-x1].getChessboardPoint())){
                                        oldboard.swapChessComponents(oldcomponent[10-y0][9-x0],oldcomponent[10-y1][9-x1]);
                                        oldboard.swapColor();
                                        oldboard.repaint();
                                        Thread.sleep(250);
                                    }else {
                                        JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid Move Pattern");
                                        continue;
                                    }
                                }else {
                                    JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                    continue;}
                            }else {JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                continue;}
                        }else {
                            if (oldcomponent[y0-1][x0-1] instanceof EmptySlotComponent){
                                JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                continue;
                            }
                            if (oldcomponent[y0-1][x0-1].getChessColor()==ChessColor.BLACK){
                                if (oldcomponent[y1-1][x1-1].getChessColor()!=ChessColor.BLACK){
                                    if (oldcomponent[y0-1][x0-1].canMoveTo(oldcomponent,oldcomponent[y1-1][x1-1].getChessboardPoint())){
                                        oldboard.swapChessComponents(oldcomponent[y0-1][x0-1],oldcomponent[y1-1][x1-1]);
                                        oldboard.swapColor();
                                        oldboard.repaint();
                                        Thread.sleep(250);
                                    }else {
                                        JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid Move Pattern");
                                        continue;
                                    }
                                }else {
                                    JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                    continue;}
                            }else {JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                continue;}
                        }
                    }else{
                        JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Position Out Of Range");
                        continue;
                    }
                }
                JOptionPane.showMessageDialog(this, "加载完成！");
            } else { int x0,y0,x1,y1;
                ChessboardComponent.iscomputer=true;
                int store;
                ChessboardComponent oldboard=new ChessboardComponent(board.getWidth(),board.getHeight());
                FileReader fr=new FileReader(file);
                BufferedReader reader=new BufferedReader(fr);
                newboard(oldboard);
                ChessComponent[][] oldcomponent=oldboard.getChessboard();
                String str=reader.readLine();
                if (str.indexOf("@TOTAL_STEP")<0){
                    JOptionPane.showMessageDialog(this, "第一行格式不正确");
                    return;
                }
                int rows=strtoint(str.split("=",2)[1]);
                while ((!str.equals(""))&&(str.indexOf("#")!=0)){
                    str=reader.readLine();
                }
                for (int i=0;i<rows;i++){
                    str=reader.readLine();
                    if (str.contains("#")){
                        if (str.indexOf("#")==0){
                            i--;
                            continue;
                        }
                        store=strtoint(str.split("#",2)[0]);
                    }else store=strtoint(str);
                    x0=store/1000;
                    y0=(store%1000)/100;
                    x1=(store%100)/10;
                    y1=store%10;
                    if (inboardx(x0)&&inboardx(x1)&&inboardy(y0)&&inboardy(y1)){
                        if (oldboard.getCurrentColor()==ChessColor.RED){
                            if (oldcomponent[10-y0][9-x0] instanceof EmptySlotComponent){
                                JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                continue;
                            }
                            if (oldcomponent[10-y0][9-x0].getChessColor()==ChessColor.RED){
                                if (oldcomponent[10-y1][9-x1].getChessColor()!=ChessColor.RED){
                                    if (oldcomponent[10-y0][9-x0].canMoveTo(oldcomponent,oldcomponent[10-y1][9-x1].getChessboardPoint())){
                                        oldboard.swapChessComponents(oldcomponent[10-y0][9-x0],oldcomponent[10-y1][9-x1]);
                                        oldboard.swapColor();
                                    }else {
                                        JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid Move Pattern");
                                        continue;
                                    }
                                }else {
                                    JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                    continue;}
                            }else {JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                continue;}
                        }else {
                            if (oldcomponent[y0-1][x0-1] instanceof EmptySlotComponent){
                                JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                continue;
                            }
                            if (oldcomponent[y0-1][x0-1].getChessColor()==ChessColor.BLACK){
                                if (oldcomponent[y1-1][x1-1].getChessColor()!=ChessColor.BLACK){
                                    if (oldcomponent[y0-1][x0-1].canMoveTo(oldcomponent,oldcomponent[y1-1][x1-1].getChessboardPoint())){
                                        oldboard.swapChessComponents(oldcomponent[y0-1][x0-1],oldcomponent[y1-1][x1-1]);
                                        oldboard.swapColor();
                                    }else {
                                        JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid Move Pattern");
                                        continue;
                                    }
                                }else {
                                    JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                    continue;}
                            }else {JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                continue;}
                        }
                    }else{
                        JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Position Out Of Range");
                        continue;
                    }
                }
                for (int i = 0; i < board.getChessboard().length; i++) {
                    for (int j = 0; j < board.getChessboard()[i].length; j++) {
                        board.putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), board.calculatePoint(i, j)));
                    }
                }
                for (int i = 0; i < board.getChessboard().length; i++) {
                    for (int j = 0; j < board.getChessboard()[i].length; j++) {
                        if (oldcomponent[i][j] instanceof EmptySlotComponent) continue;
                        board.initTestBoard(i, j,oldcomponent[i][j].getChessColor(), oldcomponent[i][j].getClass());
                        board.getChessboard()[i][j].canselect=true;
                    }
                    board.repaint();

                }
                if (board.getCurrentColor()!=oldboard.getCurrentColor()) board.swapColor();
                if (board.getCurrentColor()==ChessColor.RED){
                    board.turn.setText("红方行棋");
                }else {
                    board.turn.setText("黑方行棋");
                }
                ChessComponent.listenerList.remove(1);
                JOptionPane.showMessageDialog(this, "加载完成！");
                ChessboardComponent.iscomputer=false;}
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void chessmoveseq(ChessboardComponent board,File file,boolean haschessboard,File chessboardfile) {
        //renew r=new renew(chessboard);
        //Thread t=new Thread(r);
        try {
            System.out.println(hasprocess.isSelected());
            if (hasprocess.isSelected()){
                if (haschessboard){
                    input(chessboard,chessboardfile);
                }
                int x0,y0,x1,y1;
                int store;
                ChessboardComponent oldboard=chessboard;
                FileReader fr=new FileReader(file);
                BufferedReader reader=new BufferedReader(fr);
                ChessComponent[][] oldcomponent=chessboard.getChessboard();
                String str=reader.readLine();
                if (str.indexOf("@TOTAL_STEP")<0){
                    JOptionPane.showMessageDialog(this, "第一行格式不正确");
                    return;
                }
                int rows=strtoint(str.split("=",2)[1]);
                while ((!str.equals(""))&&(str.indexOf("#")!=0)){
                    str=reader.readLine();
                }
                for (int i=0;i<rows;i++){
                    str=reader.readLine();
                    if (str.contains("#")){
                        if (str.indexOf("#")==0){
                            i--;
                            continue;
                        }
                        store=strtoint(str.split("#",2)[0]);
                    }else store=strtoint(str);
                    x0=store/1000;
                    y0=(store%1000)/100;
                    x1=(store%100)/10;
                    y1=store%10;
                    if (inboardx(x0)&&inboardx(x1)&&inboardy(y0)&&inboardy(y1)){
                        if (oldboard.getCurrentColor()==ChessColor.RED){
                            if (oldcomponent[10-y0][9-x0] instanceof EmptySlotComponent){
                                JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                continue;
                            }
                            if (oldcomponent[10-y0][9-x0].getChessColor()==ChessColor.RED){
                                if (oldcomponent[10-y1][9-x1].getChessColor()!=ChessColor.RED){
                                    if (oldcomponent[10-y0][9-x0].canMoveTo(oldcomponent,oldcomponent[10-y1][9-x1].getChessboardPoint())){
                                        oldboard.swapChessComponents(oldcomponent[10-y0][9-x0],oldcomponent[10-y1][9-x1]);
                                        oldboard.swapColor();
                                        oldboard.repaint();
                                        Thread.sleep(250);
                                    }else {
                                        JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid Move Pattern");
                                        continue;
                                    }
                                }else {
                                    JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                    continue;}
                            }else {JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                continue;}
                        }else {
                            if (oldcomponent[y0-1][x0-1] instanceof EmptySlotComponent){
                                JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                continue;
                            }
                            if (oldcomponent[y0-1][x0-1].getChessColor()==ChessColor.BLACK){
                                if (oldcomponent[y1-1][x1-1].getChessColor()!=ChessColor.BLACK){
                                    if (oldcomponent[y0-1][x0-1].canMoveTo(oldcomponent,oldcomponent[y1-1][x1-1].getChessboardPoint())){
                                        oldboard.swapChessComponents(oldcomponent[y0-1][x0-1],oldcomponent[y1-1][x1-1]);
                                        oldboard.swapColor();
                                        oldboard.repaint();
                                        Thread.sleep(250);
                                    }else {
                                        JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid Move Pattern");
                                        continue;
                                    }
                                }else {
                                    JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                    continue;}
                            }else {JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                continue;}
                        }
                    }else{
                        JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Position Out Of Range");
                        continue;
                    }
                }
                JOptionPane.showMessageDialog(this, "加载完成！");
            } else {
                int x0,y0,x1,y1;
                ChessboardComponent.iscomputer=true;
                int store;
                input(chessboard,chessboardfile);
                ChessboardComponent oldboard=chessboard;
                FileReader fr=new FileReader(file);
                BufferedReader reader=new BufferedReader(fr);
                ChessComponent[][] oldcomponent=oldboard.getChessboard();
                String str=reader.readLine();
                if (str.indexOf("@TOTAL_STEP")<0){
                    JOptionPane.showMessageDialog(this, "第一行格式不正确");
                    return;
                }
                int rows=strtoint(str.split("=",2)[1]);
                while ((!str.equals(""))&&(str.indexOf("#")!=0)){
                    str=reader.readLine();
                }
                for (int i=0;i<rows;i++){
                    str=reader.readLine();
                    if (str.contains("#")){
                        if (str.indexOf("#")==0){
                            i--;
                            continue;
                        }
                        store=strtoint(str.split("#",2)[0]);
                    }else store=strtoint(str);
                    x0=store/1000;
                    y0=(store%1000)/100;
                    x1=(store%100)/10;
                    y1=store%10;
                    if (inboardx(x0)&&inboardx(x1)&&inboardy(y0)&&inboardy(y1)){
                        if (oldboard.getCurrentColor()==ChessColor.RED){
                            if (oldcomponent[10-y0][9-x0] instanceof EmptySlotComponent){
                                JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                continue;
                            }
                            if (oldcomponent[10-y0][9-x0].getChessColor()==ChessColor.RED){
                                if (oldcomponent[10-y1][9-x1].getChessColor()!=ChessColor.RED){
                                    if (oldcomponent[10-y0][9-x0].canMoveTo(oldcomponent,oldcomponent[10-y1][9-x1].getChessboardPoint())){
                                        oldboard.swapChessComponents(oldcomponent[10-y0][9-x0],oldcomponent[10-y1][9-x1]);
                                        oldboard.swapColor();
                                    }else {
                                        JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid Move Pattern");
                                        continue;
                                    }
                                }else {
                                    JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                    continue;}
                            }else {JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                continue;}
                        }else {
                            if (oldcomponent[y0-1][x0-1] instanceof EmptySlotComponent){
                                JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                continue;
                            }
                            if (oldcomponent[y0-1][x0-1].getChessColor()==ChessColor.BLACK){
                                if (oldcomponent[y1-1][x1-1].getChessColor()!=ChessColor.BLACK){
                                    if (oldcomponent[y0-1][x0-1].canMoveTo(oldcomponent,oldcomponent[y1-1][x1-1].getChessboardPoint())){
                                        oldboard.swapChessComponents(oldcomponent[y0-1][x0-1],oldcomponent[y1-1][x1-1]);
                                        oldboard.swapColor();
                                    }else {
                                        JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid Move Pattern");
                                        continue;
                                    }
                                }else {
                                    JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                    continue;}
                            }else {JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Invalid To Position");
                                continue;}
                        }
                    }else{
                        JOptionPane.showMessageDialog(this, "从棋谱开始第"+(i+1)+"行，Position Out Of Range");
                        continue;
                    }
                }
                if (board.getCurrentColor()!=oldboard.getCurrentColor()) board.swapColor();
                if (board.getCurrentColor()==ChessColor.RED){
                    board.turn.setText("红方行棋");
                }else {
                    board.turn.setText("黑方行棋");
                }
                JOptionPane.showMessageDialog(this, "加载完成！");
                ChessboardComponent.iscomputer=false;}
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    class  playBackInSteps implements Runnable{
        ChessboardComponent board;
        int counter;
        public playBackInSteps(ChessboardComponent board,int counter){
            this.board=board;
            this.counter=counter;
        }
        @Override
        public void run() {
            for (int k=0;k<chessboard.getSavings().size();k++){
                for (int i = 0; i < board.getChessboard().length; i++) {
                    for (int j = 0; j < board.getChessboard()[i].length; j++) {
                        board.putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), board.calculatePoint(i, j)));
                    }
                }

                for (int i=0;i<10;i++){
                    for (int j=0;j<9;j++){
                        if (!(board.getSavings().get(counter)[i][j] instanceof EmptySlotComponent)){
                            board.initTestBoard(i, j, board.getSavings().get(counter)[i][j].getChessColor(),board.getSavings().get(counter)[i][j].getClass());
                        }
                    }
                }
                board.swapColor();
                board.repaint();
                endGame(chessboard);
                try {
                    Thread.sleep(500);
                }catch (Exception e){}

                counter++;
            }

        }
    }

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
        board.swapColor();
        board.repaint();

    }

    public void previousChessBoard(ChessboardComponent board){
        if (board.getSavings().size()==1){
            JOptionPane.showMessageDialog(this, "憨批到头了");
        }else {
            changeChessboard(board);
            board.getSavings().remove(board.getSavings().size()-1);
        }
    }

    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintBackground(g);
    }

    public void paintBackground(Graphics g){
        Image classic=new ImageIcon("background.jpg").getImage();
        g.drawImage(background,5,5,this);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame=new JFrame("中国象棋");
            frame.setSize(400,400);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLayout(null);
            frame.setLocationRelativeTo(null);
            JButton button1=new JButton("双人对战");
            button1.setSize(100,40);
            button1.setLocation(150,50);
            JButton button2=new JButton("导入棋局");
            button2.setSize(100,40);
            button2.setLocation(150,150);
            JButton button3=new JButton("导入棋谱");
            button3.setLocation(150,200);
            button3.setSize(100,40);
            JButton button4=new JButton("网络联机");
            button4.setLocation(150,250);
            button4.setSize(100,40);
            JButton button5=new JButton("人机对战");
            button5.setLocation(150,100);
            button5.setSize(100,40);
            frame.add(button1);
            frame.add(button2);
            frame.add(button3);
            frame.add(button4);
            frame.add(button5);

            button1.addActionListener(e -> {
                String red=JOptionPane.showInputDialog("红方用户名");
                String black=JOptionPane.showInputDialog("黑方用户名");
                //ChessGameFrame mainFrame = new ChessGameFrame(red,black);
                //mainFrame.setVisible(true);
                if (red==null||black==null){
                    JOptionPane.showMessageDialog(frame,"请输入用户名参与排名");
                }else if (red.trim().equals("")||black.trim().equals("")){
                    JOptionPane.showMessageDialog(frame,"请输入用户名参与排名");
                } else {
                    ChessGameFrame mainFrame = new ChessGameFrame(red,black);
                    mainFrame.setVisible(true);
                }
            });
            button2.addActionListener(e -> {
                ChessGameFrame mainFrame = new ChessGameFrame();
                mainFrame.setVisible(true);
                mainFrame.button2.doClick();
                frame.dispose();
            });
            button3.addActionListener(e -> {
                ChessGameFrame mainFrame = new ChessGameFrame();
                mainFrame.setVisible(true);
                mainFrame.button4.doClick();
                frame.dispose();
            });
            button4.addActionListener(e -> {
                Internetgame1 mainFrame = new Internetgame1();
                mainFrame.setVisible(true);
            });
            button5.addActionListener(e -> {
                ChessGameFrame mainFrame = new ChessGameFrame(true,ChessColor.BLACK);
                mainFrame.setVisible(true);
                frame.dispose();
            });

        });
    }
}