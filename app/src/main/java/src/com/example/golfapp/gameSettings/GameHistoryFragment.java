package com.example.golfapp.gameSettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.OnClick;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.R;
import com.example.golfapp.history.CompetitionHistoryFragment;
import com.example.golfapp.history.ViewPartyPlayHistoryFragment;

public class GameHistoryFragment extends BaseFragment {
	@OnClick(R.id.party_play)
	public void showPartyPlayHistory() {
		showFragmentAndAddToBackStack(new ViewPartyPlayHistoryFragment());
	}

	@OnClick(R.id.competition)
	public void showCompetitionHistory() {
		showFragmentAndAddToBackStack(new CompetitionHistoryFragment());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_game_history, container, false);
	}

}
