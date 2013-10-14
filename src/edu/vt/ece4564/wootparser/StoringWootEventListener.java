package edu.vt.ece4564.wootparser;

import java.util.ArrayList;

/**
 * Doesn't report events as they come in, simple absorbs them and builds a large
 * arraylist, which can later be queried with
 * {@link StoringWootEventListener#getEvents()}
 * 
 * @author hamiltont
 * 
 */
class StoringWootEventListener implements WootEventListener {

	ArrayList<WootEvent> events_ = new ArrayList<WootEvent>();

	@Override
	public void onWootEvent(WootEvent event) {
		events_.add(event);
	}

	public ArrayList<WootEvent> getEvents() {
		return events_;
	}
}