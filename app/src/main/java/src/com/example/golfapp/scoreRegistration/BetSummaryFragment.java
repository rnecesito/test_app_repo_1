package com.example.golfapp.scoreRegistration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.R;

public class BetSummaryFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_bet_summary,
				container, false);
	}
}
