package com.example.golfapp.gameSettings.partyPlay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.OnClick;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.R;
import com.example.golfapp.scoreRegistration.ScoreRegistrationFragment;

public class ViewBetPartyPlayFragment extends BaseFragment {
	
	@OnClick({R.id.edit1,R.id.edit2,R.id.edit3})
	public void showView() {
		showFragmentAndAddToBackStack(new ScoreRegistrationFragment());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_score_registration_party_play,
				container, false);
	}

}
