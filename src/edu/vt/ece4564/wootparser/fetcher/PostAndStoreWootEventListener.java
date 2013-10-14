package edu.vt.ece4564.wootparser.fetcher;

import edu.vt.ece4564.wootparser.WootEvent;
import edu.vt.ece4564.wootparser.WootEventListener;
import edu.vt.ece4564.wootparser.fetcher.WootFetcherTask.WootFetcherRequest;

/**
 * Using this {@link WootEventListener} for {@link WootFetcherRequest#listener_}
 * will result in all {@link WootEvent}s being loaded into memory as they are
 * parsed, and then delivered in one complete dump to the final client. Note
 * that this is very memory intensive, and is probably not the class you want to
 * use. This class will *also* cause {@link WootEvent}s to be delivered
 * one-by-one as they are parsed. The end result is that the primary
 * {@link WootEventListener} will receive two copies of every {@link WootEvent},
 * which is only useful if you both want to incrementally update GUI and store
 * all of the results
 * 
 * @author hamiltont
 * 
 */
class PostAndStoreWootEventListener extends StoreAndDeliverWootEventListener
		implements WootProgressPoster {

	private WootFetcherTask taskInstance_;

	/**
	 * @param listener
	 *            The {@link WootEventListener} that all {@link WootEvent}s will
	 *            be delivered to. The
	 *            {@link WootEventListener#onWootEvent(WootEvent)} method will
	 *            be called once for each available {@link WootEvent}
	 */
	public PostAndStoreWootEventListener(WootEventListener l) {
		super(l);
	}

	/**
	 * Should only be called by the framework. Users *do not* need to call this!
	 * 
	 * @param instance
	 */
	public void setFetcherInstance(WootFetcherTask instance) {
		taskInstance_ = instance;
	}

	@Override
	public void onWootEvent(WootEvent event) {
		super.onWootEvent(event);
		taskInstance_.manualProgressUpdate(new WootEventAndListener(event,
				listener_));
	}
}