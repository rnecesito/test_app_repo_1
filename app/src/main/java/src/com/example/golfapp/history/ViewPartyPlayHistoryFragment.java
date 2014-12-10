package com.example.golfapp.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.R;

public class ViewPartyPlayHistoryFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_view_party_play_history,
				container, false);
	}

}
