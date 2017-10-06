package chp7;

/**
 * @author Steve Ebersole
 */
public interface PersistentAttribute<J> extends StateArrayElementContributor<J> {
	String getAttributeInfo();
}
