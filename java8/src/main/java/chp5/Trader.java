package chp5;

/**
 * @author Andrea Boriero
 */
public class Trader {
	private final String name;
	private final String city;

	public Trader(String n, String c) {
		this.name = n;
		this.city = c;
	}

	public String getName() {
		return this.name;
	}

	public String getCity() {
		return this.city;
	}

	public String toString() {
		return "Trader:" + this.name + " in " + this.city;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( !(o instanceof Trader) ) {
			return false;
		}

		Trader trader = (Trader) o;

		if ( !getName().equals( trader.getName() ) ) {
			return false;
		}
		return getCity().equals( trader.getCity() );

	}

	@Override
	public int hashCode() {
		int result = getName().hashCode();
		result = 31 * result + getCity().hashCode();
		return result;
	}
}
