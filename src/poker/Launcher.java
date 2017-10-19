package poker;

import java.util.Scanner;

import poker.player.Player;

/**
 * Main game engine
 */
public class Launcher {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Starting Game....");
		
		// gather information needed to build a player and set game settings
		System.out.println("Setup Players and Bidding....");
		System.out.println("Enter your name.");
		String n = in.nextLine();
		System.out.println("Enter starting bankroll to the nearest dollar.");
		Integer p = in.nextInt();
		System.out.println("Enter bid amount per hand to the nearest dollar.");
		Integer b = in.nextInt();
		System.out.println("Enter the number of AI playing (1-3).");
		Integer ai = in.nextInt();
		
		Player[] player_a = new Player[ai]; // player array to hold ALL player objects
		player_a[0] = new Player(n, p, b); // create the human player, occupies 0 slot.
		String[] ai_names = { "Joan of Arc", "Jimmy the Tulip", "Bob the Builder" }; // default AI names
		// create the AI players
		while (ai < 0) {
			player_a[ai] = new Player(ai_names[ai-1], p, b); // create a new ai player
		}

		// start the game
		boolean run = true;
		while (run) {
			// check if human player is bankrupt
			if (player_a[0].getBankroll() <= 0) { // the game ends
				System.out.println("YOU LOOSE!");
				run = false;
			}
			else { // play the round
				System.out.println("Starting Round....");

				// create and setup a new deck
				Deck d = new Deck(); 
				d.shuffle();

				// deal out the starting 13 cards to each of the players
				for (int c = 0; c < 13; c++) {
					for (int h = 0; h < player_a.length; h++) {
						player_a[h].hand.getSubhand("pool")[c] = d.dealCard();
					}
				}
				
				run = false;
				// players all set their subhands and commit
				// compare the hands amongst the players
				// exchange winnings/losses
			}
		}
	}
}