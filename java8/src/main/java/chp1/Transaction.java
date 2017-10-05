package chp1;

import java.util.Currency;

/**
 * @author Andrea Boriero
 */
public class Transaction {
	private int price;
	private Currency currency;

	public Transaction(int price, Currency currency) {
		this.price = price;
		this.currency = currency;
	}

	public int getPrice() {
		return price;
	}

	public Currency getCurrency() {
		return currency;
	}
}
