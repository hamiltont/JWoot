package edu.vt.ece4564.wootparser.fetcher;

/**
 * Used to deal with concurrency inside of {@link WootFetcherTask}
 * 
 * @author hamiltont
 * 
 */
public interface WootProgressPoster {

	/**
	 * Should only be called by the framework. Users *do not* need to call this!
	 * 
	 * @param instance
	 */
	public void setFetcherInstance(WootFetcherTask instance);

}
