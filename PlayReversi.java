import java.util.Scanner;

/**
 * PlayReversi: a class that allows the user to play Reversi matches against the computer in the terminal
 * @author Grace Bocek
 */
public class PlayReversi {
    public static void main(String[] args) {
        Board.resetGameCount();
        Scanner scan = new Scanner(System.in);
        String playAgain = "y";
        while (playAgain.equals("y")) {
            Reversi reversi = new Reversi();
            reversi.play();
            System.out.println();
            System.out.print("Play again? (y/n) ");
            playAgain = scan.next();
            scan.nextLine();
            while (!playAgain.equals("n") && !playAgain.equals("y")) {
                System.out.print("Play again? (y/n) ");
                playAgain = scan.next();
                scan.nextLine();
            }
        }
    }
}