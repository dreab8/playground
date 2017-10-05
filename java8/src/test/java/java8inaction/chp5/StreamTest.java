package java8inaction.chp5;

import java.util.List;

import chp5.Trader;
import chp5.Transaction;
import chp5.Transactions;

import org.junit.Test;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Andrea Boriero
 */
public class StreamTest {

	//	Find all transactions in the year 2011 and sort them by value (small to high).
	@Test
	public void findsTransactionsByYearSortedByValue() {
		List<Transaction> transactionList = Transactions.getTransactions();

		final List<Transaction> transactionsIn2011 = transactionList.stream()
				.filter( t -> t.getYear() == 2011 )
				.sorted( (t1, t2) -> ((Integer) t1.getValue()).compareTo( t2.getValue() ) )
				.collect( toList() );

		assertThat( transactionsIn2011.size(), is( 2 ) );
		assertThat( transactionsIn2011.get( 0 ).getValue(), is( 300 ) );
	}

	//	What are all the unique cities where the traders work?
	@Test
	public void selectUniqueCitiesWhereTheTraderWorks() {
		List<Transaction> transactionList = Transactions.getTransactions();

		final List<String> cities = transactionList.stream()
				.map( t -> t.getTrader().getCity() )
				.distinct()
				.collect( toList() );

		assertThat( cities.size(), is( 2 ) );
	}

	// 	Find all traders from Cambridge and sort them by name.
	@Test
	public void selectAllTradersFromCambridgeSortedByName() {
		List<Transaction> transactionList = Transactions.getTransactions();
		final List<Trader> cambridgeTreaders = transactionList.stream()
				.filter( t -> t.getTrader().getCity().equals( "Cambridge" ) )
				.map( t -> t.getTrader() )
				.distinct()
				.sorted( (t1, t2) -> t1.getName().compareTo( t2.getName() ) )
				.collect( toList() );

		assertThat( cambridgeTreaders.size(), is( 3 ) );
		assertThat( cambridgeTreaders.get( 0 ).getName(), is( "Alan" ) );
		assertThat( cambridgeTreaders.get( 1 ).getName(), is( "Brian" ) );
		assertThat( cambridgeTreaders.get( 2 ).getName(), is( "Raoul" ) );
	}

	//	Return a string of all traders’ names sorted alphabetically.
	@Test
	public void selectAllTradersNameSorted() {
		List<Transaction> transactionList = Transactions.getTransactions();
		final String names = transactionList.stream()
				.map( t -> t.getTrader().getName() )
				.distinct()
				.sorted()
				.reduce( "", (a, b) -> a + b );

		assertThat( names, is( "AlanBrianMarioRaoul" ) );
	}

	//	Are any traders based in Milan?
	@Test
	public void areAnyTradersFromMilan() {
		List<Transaction> transactionList = Transactions.getTransactions();
		final boolean isAnyFromMilan = transactionList.stream()
				.anyMatch( t -> t.getTrader().getCity().equals( "Milan" ) );

		assertThat( isAnyFromMilan, is( true ) );
	}

	//  Print all transactions’ values from the traders living in Cambridge.
	@Test
	public void allTransactionsFromCambridgeTrader() {
		List<Transaction> transactionList = Transactions.getTransactions();
		transactionList.stream().filter( t -> t.getTrader()
				.getCity()
				.equals( "Cambridge" ) )
				.map(Transaction::getValue)
				.forEach( v -> System.out.println(v) );


	}
}
