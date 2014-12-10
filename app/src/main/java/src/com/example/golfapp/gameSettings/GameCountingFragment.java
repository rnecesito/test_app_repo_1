package com.example.golfapp.gameSettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.OnClick;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.R;
import com.example.golfapp.counting.BetScoreCountingFragment;
import com.example.golfapp.counting.CompetitionScoreCountingFragment;
import com.example.golfapp.counting.PartyPlayScoreCountingFragment;

public class GameCountingFragment extends BaseFragment {
	@OnClick(R.id.party_play)
	public void showPartyPlay() {
		showFragmentAndAddToBackStack(new PartyPlayScoreCountingFragment());
	}

	@OnClick(R.id.bet)
	public void showBet() {
		showFragmentAndAddToBackStack(new BetScoreCountingFragment());
	}

	@OnClick(R.id.competition)
	public void showCompetition() {
		showFragmentAndAddToBackStack(new CompetitionScoreCountingFragment());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_game_counting, container, false);
	}

}
