package edu.vt.ece4564.wootparser;

import android.util.Log;
import edu.vt.ece4564.wootparser.fetcher.PostOnlyWootEventListener;
import edu.vt.ece4564.wootparser.fetcher.WootFetcherTask;
import edu.vt.ece4564.wootparser.fetcher.WootFetcherTask.WootFetcherRequest;


/**
 * Convenience class to request the Woot.com v2 API, 
 * parse it, and notify a listener of new WootEvents
 * 
 * @author hamiltont
 * @contributor
 */
public class WootApi {

	private static final String WOOT_URL_BASE = "http://api.woot.com/2/events.json?site=";
	private static final String WOOT_API_KEY = "&key=";
	
	public static final String WOOT_URL_NORM = "www.woot.com";
	public static final String WOOT_URL_WINE = "wine.woot.com";
	public static final String WOOT_URL_TECH = "tech.woot.com";
	public static final String WOOT_URL_HOME = "home.woot.com";
	public static final String WOOT_URL_ACC = "accessories.woot.com";
	public static final String WOOT_URL_SPORT = "sport.woot.com";
	public static final String WOOT_URL_TOOLS = "tools.woot.com";
	
	private String apiKey_;
	
	public WootApi(String apiKey) {
		apiKey_ = apiKey;
	}
	
	/**
	 * Must be called from UI thread
	 * 
	 * @param url
	 * @param listener
	 */
	public void fetch(String url, WootEventListener listener) {
		String fullUrl = WOOT_URL_BASE + url + WOOT_API_KEY + apiKey_;
		Log.i("woot api", "Requesting " + fullUrl);
		
		WootFetcherTask task = new WootFetcherTask();
		WootFetcherRequest request = new WootFetcherRequest();
		request.url_ =fullUrl;
		request.listener_ = new PostOnlyWootEventListener(listener);
		task.execute(request);
	}
	
}
