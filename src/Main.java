public class Main implements Runnable{

    GUI gui = new GUI();

    public static void main(String[] args) {
        new Thread(new Main()).start(); // a new thread will start when we start the game
    }

    @Override
    public void run() { // refresh game
        while(true){
            gui.repaint();
            if(!gui.resetter){
                gui.checkWinStatus();
            }
        }
    }
}


