package java8inaction.chp1;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import chp1.Transaction;

import org.junit.Before;
import org.junit.Test;

import static java.util.stream.Collectors.groupingBy;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Andrea Boriero
 */
public class StreamsTest {

	private List<Transaction> transactions = new ArrayList<>();
	private static final Currency GBP = Currency.getInstance( Locale.UK );
	private static final Currency USD = Currency.getInstance( Locale.US );
	private static final Currency EUR = Currency.getInstance( Locale.ITALY );

	@Before
	public void setUp() {
		transactions.add( new Transaction( 999, GBP ) );
		transactions.add( new Transaction( 1001, GBP ) );
		transactions.add( new Transaction( 1003, EUR ) );
		transactions.add( new Transaction( 1004, EUR ) );
		transactions.add( new Transaction( 900, USD ) );
	}

	@Test
	public void testMapProcessingNoStreams() {
		Map<Currency, List<Transaction>> transactionsByCurrencies = new HashMap<>();
		for ( Transaction transaction : transactions ) {
			if ( transaction.getPrice() > 1000 ) {
				List<Transaction> transactionsForCurrency = transactionsByCurrencies.get( transaction.getCurrency() );
				if ( transactionsForCurrency == null ) {
					transactionsForCurrency = new ArrayList<>();
					transactionsByCurrencies.put( transaction.getCurrency(), transactionsForCurrency );
				}
				transactionsForCurrency.add( transaction );
			}
		}
		assertThat( transactionsByCurrencies.get( GBP ).size(), is( 1 ) );
		assertThat( transactionsByCurrencies.get( USD ), is( nullValue() ) );
		assertThat( transactionsByCurrencies.get( EUR ).size(), is( 2 ) );
	}

	@Test
	public void testMapProcessingWithStreams() {
		Map<Currency, List<Transaction>> transactionsByCurrencies = transactions.stream()
				.filter( (Transaction t) -> t.getPrice() > 1000 )
				.collect( groupingBy( Transaction::getCurrency ) );
		assertThat( transactionsByCurrencies.get( GBP ).size(), is( 1 ) );
		assertThat( transactionsByCurrencies.get( USD ), is( nullValue() ) );
		assertThat( transactionsByCurrencies.get( EUR ).size(), is( 2 ) );
	}
}
