package com.example.golfapp.gameSettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.OnClick;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.R;
import com.example.golfapp.scoreRegistration.ScoreRegViewCloseCompetitionFragment;
import com.example.golfapp.scoreRegistration.ScoreRegViewOpenCompetitionFragment;
import com.example.golfapp.scoreRegistration.ScoreRegViewPartyPlayFragment;

public class GameScoreRegistrationFragment extends BaseFragment {
	
	@OnClick(R.id.party_play)
	public void showPartyPlay()	 {
		showFragmentAndAddToBackStack(new ScoreRegViewPartyPlayFragment());
	}
	
	@OnClick(R.id.open_competition)
	public void showOpenCompetition() {
		showFragmentAndAddToBackStack(new ScoreRegViewOpenCompetitionFragment());
	}
	
	@OnClick(R.id.closed_competition) 
	public void showCloseCompetition() {
		showFragmentAndAddToBackStack(new ScoreRegViewCloseCompetitionFragment());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_game_score_registration,
				container, false);
	}

}
