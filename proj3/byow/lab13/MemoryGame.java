package byow.lab13;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

import byow.Core.RandomUtils;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //Initialize random number generator
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        String letters = "";
        for (int i=0; i<n; i++) {
          int charIndex = RandomUtils.uniform(rand, 0, CHARACTERS.length);
          letters += CHARACTERS[charIndex];
        }
        return letters;
    }

    public void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        //Take the string and display it in the center of the screen
        Font letterFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(letterFont);
        StdDraw.text(this.width/2, this.height/2, s);

        //Display relevant game information at the top of the screen
        StdDraw.line(0, this.height-2, this.width, this.height-2);
        Font titleFont = new Font("Monaco", Font.PLAIN, 14);
        StdDraw.setFont(titleFont);
        if (gameOver) {
          StdDraw.text(this.width/2, this.height - 1, "Game Over! You made it to round: " + round);
        } else {
          StdDraw.textLeft(0.5, this.height - 1, "Round: " + round);
          StdDraw.textRight(this.width - 0.5, this.height - 1, ENCOURAGEMENT[round % ENCOURAGEMENT.length]);
          StdDraw.text(this.width/2, this.height - 1, playerTurn ? "Type!" : "Watch!");
        }

        StdDraw.show();
    }

    public void flashSequence(String letters) {
        //Display each character in letters, making sure to blank the screen between letters
        drawFrame("");
        StdDraw.pause(1000);
        for (int i=0; i<letters.length(); i++) {
          String s = letters.substring(i, i+1);
          drawFrame(s);
          StdDraw.pause(1000);
          drawFrame("");
          StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        //Read n letters of player input
        String letters = "";
        drawFrame(letters);
        for (int i=0; i<n; i++) {
          while (!StdDraw.hasNextKeyTyped()) {}
          letters += StdDraw.nextKeyTyped();
          drawFrame(letters);
        }
        StdDraw.pause(1000);
        return letters;
    }

    public void startGame() {
        //Set any relevant variables before the game starts
        round = 1;
        playerTurn = false;
        gameOver = false;
        //Establish Engine loop
        while (true) {
          String correctLetters = generateRandomString(round);
          playerTurn = false;
          flashSequence(correctLetters);
          playerTurn = true;
          String userLetters = solicitNCharsInput(round);
          if (correctLetters.equals(userLetters)) {
            round++;
          } else {
            break;
          }
        }
        gameOver = true;
        drawFrame("");
        StdDraw.pause(1000);
    }
}
