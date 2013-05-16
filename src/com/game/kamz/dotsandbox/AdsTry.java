package com.game.kamz.dotsandbox;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.app.Activity;
import android.os.Bundle;

public class AdsTry extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad);
		AdView ad=(AdView) findViewById(R.id.adView);
		ad.loadAd(new AdRequest());
	}

	
}
