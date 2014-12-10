package com.example.golfapp.gameSettings.competition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.OnClick;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.R;

public class CompetitionSettingsFragment extends BaseFragment {
	@OnClick(R.id.close_competition)
	public void showPartyPlayHistory() {
		showFragmentAndAddToBackStack(new CloseCompetitionFragment());
	}

	@OnClick(R.id.open_competition)
	public void showCompetitionHistory() {
		showFragmentAndAddToBackStack(new OpenCompetitionFragment());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_competition_settings, container,
				false);
	}

}
