package edu.vt.ece4564.wootparser.fetcher;

import java.util.ArrayList;

import edu.vt.ece4564.wootparser.WootEvent;
import edu.vt.ece4564.wootparser.WootEventListener;
import edu.vt.ece4564.wootparser.fetcher.WootFetcherTask.WootFetcherRequest;

/**
 * Using this {@link WootEventListener} for {@link WootFetcherRequest#listener_}
 * will result in all {@link WootEvent}s being loaded into memory as they are
 * parsed, and then delivered in one complete dump to the final client. Note
 * that this is very memory intensive, and is probably not the class you want to
 * use.
 * 
 * Note that if full parsing of the woot.com response fails due to an
 * {@link OutOfMemoryError}, then this class will deliver all {@link WootEvent}s
 * that were parsed before memory was consumed. A smart user would then
 * immediately delete a few of these so they do not continue to experience
 * {@link OutOfMemoryError}s
 * 
 * @author hamiltont
 * 
 */
class StoreAndDeliverWootEventListener implements WootEventListener {

	ArrayList<WootEvent> events_ = new ArrayList<WootEvent>(60);
	WootEventListener listener_;

	/**
	 * @param listener
	 *            The {@link WootEventListener} that all {@link WootEvent}s will
	 *            be delivered to when parsing is complete. The
	 *            {@link WootEventListener#onWootEvent(WootEvent)} method will
	 *            be called once for each available {@link WootEvent}
	 */
	public StoreAndDeliverWootEventListener(WootEventListener listener) {
		listener_ = listener;
	}

	@Override
	public void onWootEvent(WootEvent event) {
		events_.add(event);
	}

	public ArrayList<WootEvent> getEvents() {
		return events_;
	}

	public WootEventListener getOriginalListener() {
		return listener_;
	}
}