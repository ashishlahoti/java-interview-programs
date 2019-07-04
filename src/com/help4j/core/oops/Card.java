package com.help4j.core.oops;

public class Card {

	private enum Suit {
		SPADE(1), HEART(2), CLUB(3), DIAMOND(4);
		int value;
		private Suit(int v){
			this.value = v;
		}
		public int getValue() {
			return value;
		}
	}
	
	public Card(Suit s, int c){
		this.card = c;
		this.suit = s;
	}
	
	private int card;
	
	private Suit suit;
	
	public int getCard() {
		return card;
	}
	
	public Suit getSuit() {
		return suit;
	}
	
}
