package edu.vt.ece4564.wootparser;

public class WootEventAndListener {
	public final WootEvent event_;
	public final WootEventListener listener_;
	
	public WootEventAndListener(WootEvent e, WootEventListener l) {
		event_ = e;
		listener_ = l;
	}
}