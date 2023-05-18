
import com.sun.tools.javac.Main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


public class Admin {

    private static final String ADMIN_PASSWORD_FILE_PATH = "admin.txt";
    private static final String PLAYERS_FILE_PATH = "players.bin";
    private final Scanner scan = new Scanner(System.in);
    private MainMenu mainMenu;

    // hashing password
    public String passwordHash(String base)
    {
        String message="";

        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            message = hexString.toString();

        } catch(Exception ex){
            throw new RuntimeException(ex);
        }

        return message;
    }

    // Used to write player list into file
    public void writePlayersToFile(List<Player> players, String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            for (Player player : players) {
                out.writeObject(player);
            }
            out.flush();
            //System.out.println("Players written to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing players to file: " + e.getMessage());
        }
    }

    // return true false on admin password
    public boolean checkAdminLogin(String password){
        String hashedPW="";
        // getting hashed admin password
        try {
            File file = new File(ADMIN_PASSWORD_FILE_PATH);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()){
                hashedPW = reader.nextLine();
            }
            reader.close();
        }catch (IOException e){
            System.out.println("Error reading admin password file: "+ e.getMessage());
            return false;
        }
        // hashing the password
        String hashedEnteredPW = passwordHash(password);
        return hashedEnteredPW.equals(hashedPW);
    }


    // Admin menu
    public void adminMenu(){
        String input ="";
        while (!input.equals("8")) {
            System.out.println("\nAdmin Menu");
            System.out.println("1. Create a player");
            System.out.println("2. Delete a player");
            System.out.println("3. View all players");
            System.out.println("4. Issue more chips to a player");
            System.out.println("5. Reset player’s password");
            System.out.println("6. Change administrator’s password");
            System.out.println("7. View all player's password");
            System.out.println("8. Logout and go back to Main Menu");
            System.out.println();
            System.out.print("Please select an option: ");
            input = scan.nextLine();
            try {
                int option = Integer.parseInt(input);
                // enhanced switch case
                switch (option) {
                    case 1 -> createPlayer();
                    case 2 -> deletePlayer();
                    case 3 -> viewAllPlayer();
                    case 4 -> issueChipsToPlayer();
                    case 5 -> resetPlayerPW();
                    case 6 -> changeAdminPW();
                    case 7 -> viewAllPlayerPasswords();
                    case 8 -> new MainMenu();

                    default -> System.out.println("Invalid input. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    // Create player
    public void createPlayer(){
        System.out.print("Enter the username for new player: ");
        String loginName = scan.nextLine();
        System.out.print("Enter the password for new player: ");
        String password = scan.nextLine();

        // Hashing password
        String hashedPW = passwordHash(password);
        int chips = 0; //default chips given

        // Creating player obj
        List<Player> players = Player.getAllPlayers();
        players.add(new Player(loginName,hashedPW,chips));

        System.out.println("Player \""+loginName+"\" has been created....");
        // Saving player obj to bin file
        writePlayersToFile(players,PLAYERS_FILE_PATH);
    }

    // Delete player
    public void deletePlayer(){

        // Loading the players into the list from players.bin
        List<Player> players = Player.getAllPlayers();
        System.out.println("------------Available players to delete---------");
        viewAllPlayer();
        System.out.println();
        System.out.print("Enter the username of the player you wants to delete: ");
        String playerToDelete = scan.nextLine();

        Iterator<Player> iterator = players.iterator();
        boolean playerFound = false;
        while (iterator.hasNext()) {
            Player player = iterator.next();
            String loginName = player.getLoginName();
            if (playerToDelete.equals(loginName)) { // Making it case sensitive
                System.out.println("Player available to delete....");
                System.out.print("Do you want to delete player \"" + loginName + "\" [Y/N]: ");
                String wantToDelete = scan.nextLine();
                if (wantToDelete.equalsIgnoreCase("y")) {
                    iterator.remove();          // Remove the player using the iterator
                    System.out.println("Player " + "\"" + loginName + "\" has been removed.");
                } else if (wantToDelete.equalsIgnoreCase("n")) {
                    System.out.println("Going back to main menu....");
                } else {
                    System.out.println("Invalid input. Going back to main menu....");
                }
                playerFound = true;
                break;                // Exiting the loop once the player is removed
            }
        }
        if (!playerFound){
            System.out.println("Could not find player. Going back to main menu....");
        }

        // Saving player obj to bin file
        writePlayersToFile(players,PLAYERS_FILE_PATH);
    }

    // View all players
    public void viewAllPlayer() {

        List<Player> players = Player.getAllPlayers();

        System.out.println("+----------------------+----------------+");
        System.out.println("|      Login Name      |      Chips     |");
        System.out.println("+----------------------+----------------+");

        for (Player player : players) {
            String loginName = player.getLoginName();
            int chips = player.getChips();
            System.out.printf("|  %-19s|  %12d  |\n", loginName, chips);
        }
        System.out.println("+----------------------+----------------+");
    }

    // To view player and their password
    public void viewAllPlayerPasswords() {

        List<Player> players = Player.getAllPlayers();

        System.out.println("+----------------------+----------------+");
        System.out.println("|      Login Name      |   Password     |");
        System.out.println("+----------------------+----------------+");

        for (Player player : players) {
            String loginName = player.getLoginName();
            String password = player.getHashedPassword();
            System.out.printf("|  %-19s|  %12s  |\n", loginName, password);
        }
        System.out.println("+----------------------+----------------+");
    }
    // Issue more chips to player
    public void issueChipsToPlayer(){
        List<Player> players = Player.getAllPlayers();
        System.out.println("------------Available players to issue chips---------");
        viewAllPlayer();
        System.out.println();
        System.out.print("Enter the username of the player you wants to issue chips: ");
        String playerToIssueChips = scan.nextLine();


        Iterator<Player> iterator = players.iterator();
        boolean playerFound = false;
        while (iterator.hasNext()) {
            Player player = iterator.next();
            String loginName = player.getLoginName();
            int chipsPlayerHave = player.getChips();
            if (playerToIssueChips.equals(loginName)) { // Making it case-sensitive
                System.out.println("Player available to issue chips....");
                int chipsToIssue;
                do {
                    try {
                        System.out.print("Enter the amount of chips to issue: ");
                        chipsToIssue = scan.nextInt();
                        scan.nextLine();
                        if (chipsToIssue < 0) {
                            System.out.println("Chips amount must be >= 0. Please try again.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid integer.");
                        scan.nextLine(); // clear the scanner buffer
                        chipsToIssue = -1; // set to an invalid value to trigger another iteration of the loop
                    }
                } while (chipsToIssue < 0);
                chipsPlayerHave += chipsToIssue;
                player.setChips(chipsPlayerHave);
                System.out.println(chipsToIssue + " chips have been added to player \"" + player.getLoginName() + "\"");
                playerFound = true;
                break;
            }
        }
        if (!playerFound){
            System.out.println("Could not find player. Going back to main menu....");
        }
        // Saving player obj to bin file
        writePlayersToFile(players,PLAYERS_FILE_PATH);

    }

    // Reset Player's password
    public void resetPlayerPW(){
        List<Player> players = Player.getAllPlayers();
        System.out.println("------------Available players to reset password---------");
        viewAllPlayer();
        System.out.println();
        System.out.print("Enter the username of the player you want to reset password: ");
        String playerToResetPassword = scan.nextLine();

        boolean playerFound = false;
        Iterator<Player> iterator = players.iterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            String loginName = player.getLoginName();
            if (playerToResetPassword.equals(loginName)) { // Making it case-sensitive
                System.out.print("Do you want to reset player \"" + loginName + "\" password [Y/N]: ");
                String wantToReset = scan.nextLine();
                if (wantToReset.equalsIgnoreCase("y")) {
                    System.out.print("Enter the new password to reset: ");
                    String updatedPW = scan.nextLine();
                    player.setHashedPassword(passwordHash(updatedPW));   // hashing and saving the hashed password
                    System.out.println("Player " + "\"" + loginName + "\" password has been updated.");
                } else if (wantToReset.equalsIgnoreCase("n")) {
                    System.out.println("Going back to main menu....");
                } else {
                    System.out.println("Invalid input. Going back to main menu....");
                }
                playerFound = true;
                break;
            }
        }
        if(!playerFound){
            System.out.println("Could not find player. Going back to main menu.....");
        }
        // Saving player obj to bin file
        writePlayersToFile(players,PLAYERS_FILE_PATH);
    }

    // Change admin password
    public void changeAdminPW(){
        System.out.print("Enter admin current password to access this function: ");
        String enteredCurrentPW = scan.nextLine();

        if(checkAdminLogin(enteredCurrentPW)){
            System.out.println("Given access to change admin password.");
            System.out.print("Enter new admin password: ");
            String newPW = scan.nextLine();
            System.out.print("Retype the new password: ");  // Asking user to retype to ensure correctness
            String retypePW = scan.nextLine();

            if(newPW.equals(retypePW)) {
                String hashedPW = "";
                // getting hashed admin password
                try {
                    File file = new File(ADMIN_PASSWORD_FILE_PATH);
                    Scanner reader = new Scanner(file);
                    while (reader.hasNextLine()) {
                        hashedPW = reader.nextLine();
                    }
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Error reading admin password file: " + e.getMessage());

                }
                // changing the admin password in the txt file
                hashedPW = passwordHash(newPW);
                System.out.println("ADMIN PASSWORD CHANGED.");

                // Writing back to txt file

                try {
                    PrintWriter pw = new PrintWriter(ADMIN_PASSWORD_FILE_PATH);
                    pw.write(hashedPW);
                    pw.close();
                } catch (FileNotFoundException e) {
                    System.out.println("Unable to write to file:" + e.getMessage());
                }
            }else {
                System.out.println("The passwords do not match. Please try again.");
                System.out.println();
                changeAdminPW();   // Using recursion
            }

        }else {
            System.out.println("Incorrect admin password. Going back to main menu.....");
        }
    }


    // Logout
    public void logout(){
        System.out.println("logging out....");
    }



}
