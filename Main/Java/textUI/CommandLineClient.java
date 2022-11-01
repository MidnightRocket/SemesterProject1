/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package worldOfZuul.Main.Java.textUI;

import worldOfZuul.Main.Java.Command;
import worldOfZuul.Main.Java.Actions;
import worldOfZuul.Main.Java.Game;

/**
 *
 * @author ancla
 */
public class CommandLineClient {

    private Parser parser;
    private Game game;

    public CommandLineClient() {
        game = new Game();
        parser = new Parser(game);
    }

    public void play() {
        printWelcome();

        boolean finished = false;
        while (!finished) {
            // parser takes user input and returns a Command object that constains command enum and command string.
            Command command = parser.getCommand();
            finished = processCommand(command); // finished is true, if "wantToQuit" becomes true.
        }

        System.out.println("Thank you for playing.   Goodbye.");
        // Game ends from here
    }

    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + Actions.HELP + "' if you need help.");
        System.out.println();
        System.out.println(game.getRoomDescription());
    }

    private void printHelp() {
        for(String str : game.getCommandDescriptions()) {
            System.out.println(str + " ");
        }
    }

    //Controller
    public boolean processCommand(Command command) {
        boolean wantToQuit = false;

        Actions commandWord = command.getCommandName(); // holds command enum

        if (commandWord == Actions.UNKNOWN) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        if (commandWord == Actions.HELP) {
            System.out.println("You are lost. You are alone. You wander");
            System.out.println("around at the university.");
            System.out.println();
            System.out.println("Your command words are:");
            printHelp();
        } else if (commandWord == Actions.GO) {
            if (game.goRoom(command)) {
                System.out.println(game.getRoomDescription());
            } else {
                System.out.println("Can't walk in that direction.");
            }
        } else if (commandWord == Actions.QUIT) {
            if (game.quit(command)) {
                wantToQuit = true;
            } else {
                System.out.println("Quit what?");
            }

        } else if (commandWord == Actions.INVENTORY) {
            System.out.println("Your inventory contains: ");
            System.out.println(game.getInventory());
        } else if (commandWord == Actions.PICKUP) {
            if(game.pickupItem(command)) {
                System.out.println("This item has been added to your inventory!");
            } else {
                System.out.println("That item does not exist in this room...");
            }
        } else if (commandWord == Actions.ACTIVITYS) {
            System.out.println("\nYou are still missing the following activitys:");
            System.out.println(game.getActivity());
        } else if (commandWord == Actions.IMLOST) {
            System.out.println(game.getRoomDescription());
        } else if (commandWord == Actions.POWER) {
            System.out.println("Your power is " +game.getPower());
        }

        return wantToQuit;
    }
}
