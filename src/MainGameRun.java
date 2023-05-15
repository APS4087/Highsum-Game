public class MainGameRun {

    private Dealer dealer;
    private Player player;
    private MainMenu app;

    public void run() {
        dealer.shuffle();
        app = new MainMenu();

    }

    public static void main(String[] args) {
        new MainGameRun().run();
    }

}
