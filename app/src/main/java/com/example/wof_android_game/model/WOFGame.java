package com.example.wof_android_game.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

enum DIFFICULTY_LEVEL {
    LOW,
    MEDIUM,
    HIGH
}


public class WOFGame {

    private int initial_turns = 0, totalAmountTries = 0, remainingTurns;
    private DIFFICULTY_LEVEL difficultyLevel;
    private String phrase;
    private Set<Character> guessedLetters;
    private List<Character> letters;

    private List<Character> lettersAlreadyPicked;
    private List<Character> loadedPhrase;
    private List<Character> phraseToGuess;
    private String sentence;


    public WOFGame(DIFFICULTY_LEVEL difficultyLevel, String phrase) {

        this.difficultyLevel = difficultyLevel;

        if (difficultyLevel == DIFFICULTY_LEVEL.LOW)
            this.initial_turns = 20;
        if (difficultyLevel == DIFFICULTY_LEVEL.MEDIUM)
            this.initial_turns = 15;
        if (difficultyLevel == DIFFICULTY_LEVEL.HIGH)
            this.initial_turns = 10;

        this.lettersAlreadyPicked = new ArrayList<>();
        this.loadedPhrase = new ArrayList<>();
        this.phraseToGuess = new ArrayList<>();
        this.sentence = getRandomPhrase();

        this.phrase = phrase;
        this.remainingTurns = this.initial_turns;
        this.setletters();
    }

    public int getUserChoice() {
        boolean validChoice = false;
        List<String> correct = Arrays.asList("1", "2", "3", "4");
        String choice = null;
        while (!validChoice) {
            System.out.println("\nMenu:");
            System.out.println("  1 - Guess a letter.");
            System.out.println("  2 - Solve the puzzle.");
            System.out.println("  3 - Quit the game.");
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextLine();
            if (correct.contains(choice)) {
                validChoice = true;
            } else {
                System.out.println(choice + " is an invalid choice.\n");
            }
        }
        return Integer.parseInt(choice);
    }

    public void mainDisplay() {
        String phrase = String.join("", (CharSequence) phraseToGuess);
        System.out.printf(
                "---------------- Current Game Status -----------------\n%54s\n\nAvailable Letters:  %s\n\n%n",
                phrase, String.join("", (CharSequence) this.letters));
    }

    public void processUserAction(int choice) {
        Scanner scanner = new Scanner(System.in);
        if (choice == 1) {
            System.out.print("Pick a letter: ");
            char letter = scanner.next().toLowerCase().charAt(0);
            guessLetter(letter);
        } else if (choice == 2) {
            System.out.print("  Guess: ");
            String guess = scanner.nextLine().toLowerCase();
            this.guessPhrase(guess);
        } else if (choice == 3) {
            System.out.println("Goodbye.!");
            System.exit(0);
        }
    }

    public void trackGuessedLetters(char letter) {
        int numLetters = 0;
        while (loadedPhrase.contains(letter)) {
            int index = loadedPhrase.indexOf(letter);
            this.phraseToGuess.set(index, Character.toUpperCase(letter));
            this.loadedPhrase.set(index, ' ');
            numLetters++;
        }
        this.lettersAlreadyPicked.add(Character.toUpperCase(letter));
        this.letters.set(letters.indexOf(Character.toUpperCase(letter)), ' ');
        if (numLetters == 1) {
            System.out.printf("There is %d %c.\n", numLetters, Character.toUpperCase(letter));
        } else if (numLetters > 1) {
            System.out.printf("There are %d %c's.\n", numLetters, Character.toUpperCase(letter));
        } else {
            System.out.printf("I'm sorry, there are no %c's.\n%n", Character.toUpperCase(letter));
        }
    }

    public void guessLetter(char letter) {
        while (!this.letters.contains(Character.toUpperCase(letter))) {
            if (letter != 1) {
                System.out.println(GameErrorCodes.ONLY_ONE_CHARACTER);
            } else if (this.lettersAlreadyPicked.contains(Character.toUpperCase(letter))) {
                System.out.println(GameErrorCodes.LETTER_ALREADY_GUESSED);
            } else {
                System.out.println(GameErrorCodes.INVALID_INPUT);
            }
            System.out.print("Pick a letter: ");
            Scanner scanner = new Scanner(System.in);
            letter = scanner.nextLine().charAt(0);
        }
        this.trackGuessedLetters(letter);
        this.subtractTurn();
    }

    public void guessPhrase(String guess) {
        System.out.println("Enter your solution.");
        System.out.printf("  Clues: %s%n", String.join("", (CharSequence) phraseToGuess));
        if (guess.equalsIgnoreCase(sentence)) {
            this.phraseToGuess.clear();
            for (char c : guess.toCharArray())
                this.phraseToGuess.add(c);
        } else {
            System.out.println("I'm sorry. Your guess is incorrect:");
            System.out.println(sentence.toUpperCase());
        }
        this.subtractTurn();
    }

    public boolean hasWon() {
        String checkSolution = String.join("", (CharSequence) phraseToGuess);
        if (checkSolution.equalsIgnoreCase(sentence)) {
            System.out.println(GameErrorCodes.HAVE_WON);
        }
        return this.sentence.equalsIgnoreCase(checkSolution);
    }

    public void hidePhrase() {
        for (char letter : this.sentence.toCharArray()) {
            this.loadedPhrase.add(Character.toLowerCase(letter));
            if (letter == '-' || letter == '&' || letter == '\'' || letter == ',') {
                this.phraseToGuess.add(letter);
            } else if (letter != ' ') {
                this.phraseToGuess.add('_');
            } else {
                this.phraseToGuess.add(' ');
            }
        }
    }

    public void playGame() {
        // greeting the user
        System.out.println("Welcome to GUESS THAT PHRASE!\n");
        boolean endGame;
        do {
            this.mainDisplay();
            int choice = getUserChoice();
            this.processUserAction(choice);
            endGame = this.hasWon();
        } while (!endGame);
    }


    private String getRandomPhrase() {
        return "";
    }

    public void resetValues() {
        this.remainingTurns = this.initial_turns;
        this.totalAmountTries = 0;
    }

    public void subtractTurn() {
        this.remainingTurns--;
    }

    private void setletters() {
        char c;

        for (c = 'A'; c <= 'Z'; ++c)
            this.letters.add(c);
    }

    /**
     * @description Getters and setters
     */

    public int getInitial_turns() {
        return initial_turns;
    }

    public void setInitial_turns(int initial_turns) {
        this.initial_turns = initial_turns;
    }

    public int getTotalAmountTries() {
        return totalAmountTries;
    }

    public void setTotalAmountTries(int totalAmountTries) {
        this.totalAmountTries = totalAmountTries;
    }

    public int getRemainingTurns() {
        return remainingTurns;
    }

    public void setRemainingTurns(int remainingTurns) {
        this.remainingTurns = remainingTurns;
    }

    public DIFFICULTY_LEVEL getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DIFFICULTY_LEVEL difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public Set<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public void setGuessedLetters(Set<Character> guessedLetters) {
        this.guessedLetters = guessedLetters;
    }

    public List<Character> getLetters() {
        return letters;
    }

    public void setLetters(List<Character> letters) {
        this.letters = letters;
    }

    public List<Character> getLettersAlreadyPicked() {
        return lettersAlreadyPicked;
    }

    public void setLettersAlreadyPicked(List<Character> lettersAlreadyPicked) {
        this.lettersAlreadyPicked = lettersAlreadyPicked;
    }

    public List<Character> getLoadedPhrase() {
        return loadedPhrase;
    }

    public void setLoadedPhrase(List<Character> loadedPhrase) {
        this.loadedPhrase = loadedPhrase;
    }

    public List<Character> getPhraseToGuess() {
        return phraseToGuess;
    }

    public void setPhraseToGuess(List<Character> phraseToGuess) {
        this.phraseToGuess = phraseToGuess;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
