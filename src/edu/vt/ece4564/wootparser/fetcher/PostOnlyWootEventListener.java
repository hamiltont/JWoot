package edu.vt.ece4564.wootparser.fetcher;

import edu.vt.ece4564.wootparser.WootEvent;
import edu.vt.ece4564.wootparser.WootEventAndListener;
import edu.vt.ece4564.wootparser.WootEventListener;
import edu.vt.ece4564.wootparser.WootProgressPoster;

/**
 * the Woot.com API can return a ton of data (e.g. 1-2MB). Most of this data can
 * easily be tossed if you don't want it, so I actually recommend an approach
 * such as nulling any variables you don't need, like so:
 * 
 * <pre>
 * final ArrayList&lt;WootEvent&gt; events = new ArrayList&lt;WootEvent&gt;();
 * new WootEventListener() {
 * 	&#064;Override
 * 	public void onWootEvent(WootEvent event) {
 * 		// All I care about is the title, so let's remove
 * 		// extra fields before we store the event
 * 		event.setID(null);
 * 		event.setType(null);
 * 		event.setStartDate(null);
 * 		event.setSite(null);
 * 		event.setEndDate(null);
 * 		events.add(event);
 * 	}
 * };
 * </pre>
 * 
 * This is a bit of a pain, but it's code that you write once and forget, and it
 * avoids your application completely dying because of {@link OutOfMemoryError}
 * s. All calls to your {@link WootEventListener#onWootEvent(WootEvent)} occur
 * on the UI thread.
 */
public class PostOnlyWootEventListener implements WootEventListener,
		WootProgressPoster {

	private WootEventListener listener_;
	private WootFetcherTask taskInstance_;

	/**
	 * @param listener
	 *            The {@link WootEventListener} that all {@link WootEvent}s will
	 *            be delivered to as parsing compeltes. The
	 *            {@link WootEventListener#onWootEvent(WootEvent)} method will
	 *            be called once for each available {@link WootEvent}
	 */
	public PostOnlyWootEventListener(WootEventListener l) {
		listener_ = l;
	}

	@Override
	public void onWootEvent(WootEvent event) {
		taskInstance_.manualProgressUpdate(new WootEventAndListener(event,
				listener_));
	}

	public WootEventListener getOriginalListener() {
		return listener_;
	}

	@Override
	public void setFetcherInstance(WootFetcherTask instance) {
		taskInstance_ = instance;
	}

}
