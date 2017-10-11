package org.hibernate.orm.model;

/**
 * @author Steve Ebersole
 */
public interface NonIdAttribute<J> extends PersistentAttribute<J>, StateArrayElementContributor<J> {
}
