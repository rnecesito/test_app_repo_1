package com.example.golfapp.gameSettings.partyPlay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.R;

import butterknife.OnClick;

public class PartyPlayRegistrationFragment extends BaseFragment {

	@OnClick(R.id.create_play)
	public void createPlay() {
		showFragmentAndAddToBackStack(new CreatePartyPlayFragment());
	}

	@OnClick(R.id.view_party_play)
	public void viewPartyPlay() {
		showFragmentAndAddToBackStack(new ViewPartyPlayFragment());
	}

	@OnClick(R.id.score_registration)
	public void viewScoreRegistration() {
		showFragmentAndAddToBackStack(new com.example.golfapp.scoreRegistration.ScoreRegViewPartyPlayFragment());
	}
	
	@OnClick(R.id.bet_registration)
	public void viewBetPartyPlay() {
		showFragmentAndAddToBackStack(new BetRegisterFragment());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_party_play_registration,
				container, false);
	}

}
