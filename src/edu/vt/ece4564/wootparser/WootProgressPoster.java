package edu.vt.ece4564.wootparser;

import edu.vt.ece4564.wootparser.fetcher.WootFetcherTask;

public interface WootProgressPoster {

	/**
	 * Should only be called by the framework. Users *do not* need to call this!
	 * 
	 * @param instance
	 */
	public void setFetcherInstance(WootFetcherTask instance);

}
