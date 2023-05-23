import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.awt.*;

public class GUI extends JFrame{

    public int start = 0;
    public boolean resetter = false;
    public boolean flagger = false;
    Date startDate = new Date();
    Date endDate;

    int temp = 2; // spacing between the grids

    int neighs = 0;

    String winmsg = "";
    public int tempx = 0;
    public int tempy = 0;

    public int smilex = 605;
    public int smiley = 5;

    public int smileycx = smilex + 35;
    public int smileycy = smiley + 35;

    public int flaggerx = 450;
    public int flaggery = 5;

    public int flaggerCenterx = flaggerx + 35;
    public int flaggerCentery = flaggery + 35;

    public int timex = 1130;
    public int timey = 5;

    public int winmsgx = 730;
    public int winmsgy = -50;

    public int sec = 0;
    public boolean happy = true;

    public boolean win = false;

    public boolean lose = false;

    Random rand = new Random();

    int[][] mines = new int[16][9]; // 0 if no mine, 1 if yes mine
    int[][] neighbours = new int[16][9]; // number of mines around them
    boolean[][] revealed = new boolean[16][9];
    public boolean[][] flagged = new boolean[16][9];
    public GUI(){ // constructor
        this.setTitle("Minesweeper");
        this.setSize(1280, 829); // ideally 1280x800, but top window has buffer
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // code will terminate when red button is checked
        this.setVisible(true);
        this.setResizable(false); // user cannot resize the window

        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 9; j++){
                if(rand.nextInt(5) < 1){
                    mines[i][j] = 1;
                }
                else{
                    mines[i][j] = 0;
                }
                revealed[i][j] = false;
            }
        }

        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 9; j++){
                neighs = 0;
                for(int m = 0; m < 16; m++){
                    for(int n = 0; n < 9; n++){
                        if(!(m==i && n==j)){
                            if(isN(i,j,m,n)){
                                neighs++;
                            }
                        }
                    }
                    neighbours[i][j] = neighs;
                }
            }
        }

        Board board = new Board();
        this.setContentPane(board);

        Move move = new Move();
        this.addMouseMotionListener(move);

        Click click = new Click();
        this.addMouseListener(click);
    }

    public class Board extends JPanel{
        public void paintComponent(Graphics g){ // where we can create graphics
            g.setColor(Color.darkGray);
            g.fillRect(0,0,1280,800);

            for(int i = 0; i < 16; i++){
                for(int j = 0; j < 9; j++){
                    g.setColor(Color.gray); // creating the grid
                    if(neighbours[i][j] == 0 && start == 0){
                        g.setColor(Color.yellow);
                        start++;
                    }

                    if(revealed[i][j]){
                        g.setColor(Color.white);
                        if(mines[i][j] == 1){
                            g.setColor(Color.red);
                        }
                    }

                    if(i == 6 && j == 9){
                        g.setColor(Color.yellow);
                    }
                    else if(tempx >= temp+i*80 && tempx < i*80+80-2*temp && tempy >= temp + j*80+80+26 && tempy < temp + j*80+80+80+26-2*temp){
                        g.setColor(Color.lightGray);
                    }
                    g.fillRect(temp + i*80, temp + j*80+80,80-2*temp,80-2*temp);
                    if(revealed[i][j]){ // printing numbers & number colors
                        g.setColor(Color.black);
                        if(mines[i][j] == 0){
                            if(neighbours[i][j]>0){
                                if(neighbours[i][j] == 1){
                                    g.setColor(Color.blue);
                                }
                                else if(neighbours[i][j] == 2){
                                    g.setColor(Color.green);
                                }
                                else if(neighbours[i][j] == 3){
                                    g.setColor(Color.red);
                                }
                                else if(neighbours[i][j] == 4){
                                    g.setColor(new Color(0,0,128));
                                }
                                else if(neighbours[i][j] == 5){
                                    g.setColor(new Color(178,34,34));
                                }
                                else if(neighbours[i][j] == 6){
                                    g.setColor(new Color(72,209,204));
                                }
                                else if(neighbours[i][j] == 8){
                                    g.setColor(Color.darkGray);
                                }
                                g.setFont(new Font("Mono", Font.BOLD, 40));
                                g.drawString(Integer.toString(neighbours[i][j]),i*80+27,j*80+135);
                            }
                        }
                        else{ // bomb graphics
                            g.fillRect(i*80+30, j*80+100,20,40);
                            g.fillRect(i*80+20, j*80+110,40,20);
                            g.fillRect(i*80+25, j*80+105,30,30);
                            g.fillRect(i*80+38, j*80+95, 4, 50);
                            g.fillRect(i*80+15, j*80+80+38, 50, 4);
                        }
                    }

                    // flags painting

                    if(flagged[i][j]){
                        g.setColor(Color.BLACK);
                        g.fillRect(i*80+36, j*80+80+17, 7, 40);
                        g.fillRect(i*80+24, j*80+80+52, 30, 7);
                        g.setColor(Color.red);
                        g.fillRect(i*80+17, j*80+80+17, 20, 15);
                        g.setColor(Color.black);
                    }
                }
            }
            // smiley face!!
            g.setColor(Color.yellow);
            g.fillOval(smilex, smiley, 70, 70);
            g.setColor(Color.black);
            g.fillOval(smilex+15,smiley+20,10,10);
            g.fillOval(smilex+45,smiley+20,10,10);
            if(happy){
                g.fillRect(smilex+20,smiley+50,30,5);
                g.fillRect(smilex+18,smiley+45,5,5);
                g.fillRect(smilex+47,smiley+45,5,5);
            }
            else{
                g.fillRect(smilex+20,smiley+45,30,5);
                g.fillRect(smilex+18,smiley+50,5,5);
                g.fillRect(smilex+47,smiley+50,5,5);
            }

            // flag button painting

            g.setColor(Color.BLACK);
            g.fillRect(flaggerx+32, flaggery+15, 7, 40);
            g.fillRect(flaggerx+20, flaggery+50, 30, 7);
            g.setColor(Color.red);
            g.fillRect(flaggerx+13, flaggery+15, 20, 15);
            g.setColor(Color.black);

            if(flagger){
                g.setColor(Color.red);
            }

            g.drawOval(flaggerx, flaggery, 70, 70);
            g.drawOval(flaggerx+1, flaggery+1, 68,68);
            g.drawOval(flaggerx+2, flaggery+2, 66, 66);

            // timer painting
            g.setColor(Color.black);
            g.fillRect(timex, timey, 145, 70 );
            if(lose == false && win == false){
                sec = (int) (new Date().getTime()-startDate.getTime()) / 1000 ;
            }
            if(sec>999){ //999 is max number it will go to
                sec = 999;
            }
            g.setColor(Color.WHITE);
            if(win){
                g.setColor(Color.green);
            }
            else if(lose){
                g.setColor(Color.red);
            }
            g.setFont(new Font("Mono", Font.PLAIN, 75));
            if(sec<10){
                g.drawString("00"+Integer.toString(sec), timex, timey+65);
            }
            else if(sec<100){
                g.drawString("0"+Integer.toString(sec), timex, timey+65);
            }
            else{
                g.drawString(Integer.toString(sec), timex, timey+65);
            }

            // win message painting

            if(win){
                g.setColor(Color.green);
                winmsg = "YOU WIN!";
            }
            else if(lose){
                g.setColor(Color.red);
                winmsg = "YOU LOSE!";
            }

            if(win || lose){
                winmsgy = -50 + (int) (new Date().getTime() - endDate.getTime()) / 10;
                if(winmsgy>65){
                    winmsgy = 65; // so that it doesnt keep going down lol
                }
                g.setFont(new Font("Mono",Font.PLAIN, 70));
                g.drawString(winmsg, winmsgx, winmsgy);
            }

        }
    }

    public class Move implements MouseMotionListener{

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            tempx = e.getX();
            tempy = e.getY(); // return coordinates of the mouse
        }
    }

    public class Click implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

            tempx = e.getX();
            tempy = e.getY();

            if (inBoxX() != -1 && inBoxY() != -1){
                if(flagger && !revealed[inBoxX()][inBoxY()]){
                    if(!flagged[inBoxX()][inBoxY()]){
                        flagged[inBoxX()][inBoxY()] = true;
                    }
                    else{
                        flagged[inBoxX()][inBoxY()] = false;
                    }
                }
                else{
                    if(!flagged[inBoxX()][inBoxY()]){
                        revealed[inBoxX()][inBoxY()] = true;
                        if (neighbours[inBoxX()][inBoxY()] == 0 && mines[inBoxX()][inBoxY()] == 0) {
                            for (int m = inBoxX() - 1; m < inBoxX() + 2; m++) {
                                for (int n = inBoxY() - 1; n < inBoxY() + 2; n++) {
                                    revealed[m][n] = true;
                                    if (neighbours[m][n] == 0 && mines[m][n] == 0) {
                                        for (int q = m - 1; q < m + 2; q++) {
                                            for (int p = n - 1; p < n + 2; p++) {
                                                revealed[q][p] = true;
                                                if (neighbours[q][p] == 0 && mines[q][p] == 0) {
                                                    for (int a = q - 1; a < q + 2; a++) {
                                                        for (int b = p - 1; b < p + 2; b++) {
                                                            revealed[a][b] = true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }

            if(inSmiley()){
                reset();
            }

            if(inFlagger()){
                if(!flagger){
                    flagger = true;
                }else{
                    flagger = false;
                }

            }

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public void checkWinStatus(){

        if(!lose){
            for(int i = 0; i < 16; i++){
                for(int j = 0; j < 9; j++){
                    if(revealed[i][j] && mines[i][j] == 1){
                        lose = true;
                        happy = false;
                        endDate = new Date();
                    }
                }
            }
        }


        if(totalBoxesRevealed() >= 144 - totalMines() && !win){
            win = true;
            endDate = new Date();
        }
    }

    public int totalMines(){ // count the number of mines in the board
        int sum = 0;
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 9; j++){
                if(mines[i][j] == 1){
                    sum++;
                }
            }
        }
        return sum;
    }

    public int totalBoxesRevealed(){
        int sum = 0;
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 9; j++){
                if(revealed[i][j]){
                        sum++;
                }
            }
        }
        return sum;
    }

    public void reset(){ // resets everything
        flagger = false;
        resetter = true;
        startDate = new Date();
        happy = true;
        win = false;
        lose = false;
        winmsgy = -50;
        winmsg = "";

        for(int i = 0; i < 16; i++){ // below resets the board
            for(int j = 0; j < 9; j++){
                if(rand.nextInt(5) < 1){
                    mines[i][j] = 1;
                }
                else{
                    mines[i][j] = 0;
                }
                revealed[i][j] = false;
                flagged[i][j] = false;
            }
        }

        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 9; j++){
                neighs = 0;
                for(int m = 0; m < 16; m++){
                    for(int n = 0; n < 9; n++){
                        if(!(m==i && n==j)){
                            if(isN(i,j,m,n)){
                                neighs++;
                            }
                        }
                    }
                    neighbours[i][j] = neighs;
                }
            }
        }

        resetter = false;

    }

    // helper methods below

    public boolean inSmiley(){
        int dif = (int) Math.sqrt(Math.abs(tempx - smileycx)*Math.abs(tempx - smileycx) + Math.abs(tempy - smileycy)*Math.abs(tempy - smileycy));  // apply distance between two points formula
        if(dif<35){
            return true; // it means that it is within the radius of 35 of the smiley
        }
        return false;
    }

    public boolean inFlagger(){
        int dif = (int) Math.sqrt(Math.abs(tempx - flaggerCenterx)*Math.abs(tempx - flaggerCenterx) + Math.abs(tempy - flaggerCentery)*Math.abs(tempy - flaggerCentery));  // apply distance between two points formula
        if(dif<35){
            return true; // it means that it is within the radius of 35 of the smiley
        }
        return false;
    }

    public int inBoxX(){
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 9; j++){
                if(tempx >= temp+i*80 && tempx < i*80+80-2*temp && tempy >= temp + j*80+80+26 && tempy < temp + j*80+80+80+26-2*temp){
                    return i;
                }
            }
        }
        return -1;
    }

    public int inBoxY(){
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 9; j++){
                if(tempx >= temp+i*80 && tempx < i*80+80-2*temp && tempy >= temp + j*80+80+26 && tempy < temp + j*80+80+80+26-2*temp){
                    return j;
                }
            }
        }
        return -1;
    }

    public boolean isN(int mX, int mY, int cX, int cY){ // whether two boxes are neighbors
        if(mX - cX < 2 && mX - cX > -2 && mY - cY < 2 && mY - cY > -2 && mines[cX][cY] == 1){
            return true;
        }
        return false;
    }

}
