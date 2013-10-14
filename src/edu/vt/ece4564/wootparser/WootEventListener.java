package edu.vt.ece4564.wootparser;

import edu.vt.ece4564.wootparser.fetcher.WootFetcherTask;

/**
 * Interface for being notified of {@link WootEvent}s as they are parsed
 * 
 * @author hamiltont
 * 
 */
public interface WootEventListener {

	/**
	 * Called every time a new {@link WootEvent} is generated. With all default
	 * options, this method will always be called on the UI thread. If you
	 * customize the arguments passed to {@link WootFetcherTask} this might no
	 * longer be called on the UI thread, but you probbaly don't need to do that
	 * anyway
	 * 
	 * @param event
	 */
	public void onWootEvent(WootEvent event);
}
