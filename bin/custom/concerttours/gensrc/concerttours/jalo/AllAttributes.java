/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Jun 7, 2023, 1:51:20 PM                     ---
 * ----------------------------------------------------------------
 */
package concerttours.jalo;

import concerttours.jalo.Genre;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type AllAttributes.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class AllAttributes extends GenericItem
{
	/** Qualifier of the <code>AllAttributes.songs</code> attribute **/
	public static final String SONGS = "songs";
	/** Qualifier of the <code>AllAttributes.genre</code> attribute **/
	public static final String GENRE = "genre";
	/** Qualifier of the <code>AllAttributes.concertType</code> attribute **/
	public static final String CONCERTTYPE = "concertType";
	/** Qualifier of the <code>AllAttributes.tickets</code> attribute **/
	public static final String TICKETS = "tickets";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(SONGS, AttributeMode.INITIAL);
		tmp.put(GENRE, AttributeMode.INITIAL);
		tmp.put(CONCERTTYPE, AttributeMode.INITIAL);
		tmp.put(TICKETS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AllAttributes.concertType</code> attribute.
	 * @return the concertType - type of concert
	 */
	public EnumerationValue getConcertType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, "concertType".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AllAttributes.concertType</code> attribute.
	 * @return the concertType - type of concert
	 */
	public EnumerationValue getConcertType()
	{
		return getConcertType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AllAttributes.concertType</code> attribute. 
	 * @param value the concertType - type of concert
	 */
	public void setConcertType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, "concertType".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AllAttributes.concertType</code> attribute. 
	 * @param value the concertType - type of concert
	 */
	public void setConcertType(final EnumerationValue value)
	{
		setConcertType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AllAttributes.genre</code> attribute.
	 * @return the genre - genre of songs
	 */
	public Genre getGenre(final SessionContext ctx)
	{
		return (Genre)getProperty( ctx, "genre".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AllAttributes.genre</code> attribute.
	 * @return the genre - genre of songs
	 */
	public Genre getGenre()
	{
		return getGenre( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AllAttributes.genre</code> attribute. 
	 * @param value the genre - genre of songs
	 */
	public void setGenre(final SessionContext ctx, final Genre value)
	{
		setProperty(ctx, "genre".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AllAttributes.genre</code> attribute. 
	 * @param value the genre - genre of songs
	 */
	public void setGenre(final Genre value)
	{
		setGenre( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AllAttributes.songs</code> attribute.
	 * @return the songs - list of songs
	 */
	public Collection<String> getSongs(final SessionContext ctx)
	{
		Collection<String> coll = (Collection<String>)getProperty( ctx, "songs".intern());
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AllAttributes.songs</code> attribute.
	 * @return the songs - list of songs
	 */
	public Collection<String> getSongs()
	{
		return getSongs( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AllAttributes.songs</code> attribute. 
	 * @param value the songs - list of songs
	 */
	public void setSongs(final SessionContext ctx, final Collection<String> value)
	{
		setProperty(ctx, "songs".intern(),value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AllAttributes.songs</code> attribute. 
	 * @param value the songs - list of songs
	 */
	public void setSongs(final Collection<String> value)
	{
		setSongs( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AllAttributes.tickets</code> attribute.
	 * @return the tickets - tickets
	 */
	public Map<String,String> getAllTickets(final SessionContext ctx)
	{
		Map<String,String> map = (Map<String,String>)getProperty( ctx, "tickets".intern());
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AllAttributes.tickets</code> attribute.
	 * @return the tickets - tickets
	 */
	public Map<String,String> getAllTickets()
	{
		return getAllTickets( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AllAttributes.tickets</code> attribute. 
	 * @param value the tickets - tickets
	 */
	public void setAllTickets(final SessionContext ctx, final Map<String,String> value)
	{
		setProperty(ctx, "tickets".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AllAttributes.tickets</code> attribute. 
	 * @param value the tickets - tickets
	 */
	public void setAllTickets(final Map<String,String> value)
	{
		setAllTickets( getSession().getSessionContext(), value );
	}
	
}
