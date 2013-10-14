package edu.vt.ece4564.wootparser.fetcher;

import edu.vt.ece4564.wootparser.WootEvent;
import edu.vt.ece4564.wootparser.WootEventListener;

/**
 * Convenience data class to hold an event and a listener. Used inside of
 * {@link WootFetcherTask} to avoid using member variables across two threads
 * 
 * @author hamiltont
 * 
 */
public class WootEventAndListener {
	public final WootEvent event_;
	public final WootEventListener listener_;

	public WootEventAndListener(WootEvent e, WootEventListener l) {
		event_ = e;
		listener_ = l;
	}
}