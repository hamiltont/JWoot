package edu.vt.ece4564.wootparser.fetcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.os.AsyncTask;
import android.util.Log;
import edu.vt.ece4564.wootparser.WootEvent;
import edu.vt.ece4564.wootparser.WootEventListener;
import edu.vt.ece4564.wootparser.WootEventParser;
import edu.vt.ece4564.wootparser.fetcher.WootFetcherTask.WootFetcherRequest;
import edu.vt.ece4564.wootparser.fetcher.WootFetcherTask.WootFetcherResponse;

/**
 * Used to start a new thread, download the woot.com data, instruct
 * {@link WootEventParser} to parse that data. Uses {@link WootEventListener}s,
 * specifically ones that implement {@link WootProgressPoster}, to ensure that
 * all calls to {@link WootEventListener#onWootEvent(WootEvent)} occur on the ui
 * thread
 * 
 * @author hamiltont
 * 
 */
public class WootFetcherTask
		extends
		AsyncTask<WootFetcherRequest, WootEventAndListener, WootFetcherResponse> {

	/**
	 * Used to pass parameters into each instance of {@link WootFetcherTask}.
	 * Using this avoids concurrency issues associated with storing member
	 * variables in an AsyncTask. You can use any {@link WootEventListener} you
	 * like to get updates, but you probably want to use one of the defaults
	 * provided in {@link edu.vt.ece4564.wootparser.fetcher} as they
	 * automatically synchronize with the user interface thread for you
	 * 
	 * @author hamiltont
	 * 
	 */
	public static class WootFetcherRequest {
		public WootEventListener listener_;
		public String url_;
	}

	public class WootFetcherResponse {
		public WootEventListener listener_;
		public ArrayList<WootEvent> events_;
	}

	protected void manualProgressUpdate(WootEventAndListener wel) {
		this.publishProgress(wel);
	}

	@Override
	protected WootFetcherResponse doInBackground(WootFetcherRequest... requests) {
		WootEventListener listener = requests[0].listener_;
		WootFetcherResponse response = new WootFetcherResponse();
		response.events_ = new ArrayList<WootEvent>();
		response.listener_ = listener;

		if (listener instanceof WootProgressPoster)
			((WootProgressPoster) listener).setFetcherInstance(this);

		WootEventParser wootParser = new WootEventParser(requests[0].listener_);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(requests[0].url_);

		HttpResponse execute;
		try {
			execute = client.execute(httpGet);
			JSONParser parser = new JSONParser();
			Long time = System.currentTimeMillis();

			parser.parse(new BufferedReader(new InputStreamReader(execute
					.getEntity().getContent())), wootParser);
			Long time2 = System.currentTimeMillis();
			Log.i("TIME", "" + (time2 - time) + "ms");

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}

		if (listener instanceof StoreAndDeliverWootEventListener) {
			// We will deliver the events. If a custom WEL was provided then
			// all events have already been delivered from the background
			// thread
			response.events_ = ((StoreAndDeliverWootEventListener) listener)
					.getEvents();
		}

		return response;
	}

	@Override
	protected void onProgressUpdate(WootEventAndListener... events) {
		for (WootEventAndListener wel : events)
			wel.listener_.onWootEvent(wel.event_);
	}

	@Override
	protected void onPostExecute(WootFetcherResponse result) {
		super.onPostExecute(result);

		for (WootEvent we : result.events_)
			result.listener_.onWootEvent(we);
	}

}
