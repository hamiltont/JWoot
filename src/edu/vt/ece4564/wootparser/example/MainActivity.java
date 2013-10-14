package edu.vt.ece4564.wootparser.example;

import com.example.tiny_android_project.R;

import edu.vt.ece4564.wootparser.WootApi;
import edu.vt.ece4564.wootparser.WootEvent;
import edu.vt.ece4564.wootparser.WootEventListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView myTextView_;
	private Button myButton_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myTextView_ = (TextView) findViewById(R.id.text);
		myButton_ = (Button) findViewById(R.id.button);

		myButton_.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				WootEventListener wel = new WootEventListener() {
					@Override
					public void onWootEvent(WootEvent event) {
						myTextView_.setText(myTextView_.getText() + "\n"
								+ event.getTitle());
					}
				};

				WootApi api = new WootApi("c87d461cadc342d882950184376a68f7");
				api.fetch(WootApi.WOOT_URL_NORM, wel);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
