package edu.vt.ece4564.wootparser;

import java.util.ArrayList;

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