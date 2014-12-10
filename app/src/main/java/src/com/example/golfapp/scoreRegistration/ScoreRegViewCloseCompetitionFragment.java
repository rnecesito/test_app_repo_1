package com.example.golfapp.scoreRegistration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.OnClick;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.R;

public class ScoreRegViewCloseCompetitionFragment extends BaseFragment {
	@OnClick({ R.id.edit1, R.id.edit2, R.id.edit3 })
	public void showView() {
		showFragmentAndAddToBackStack(new ScoreRegDetailCloseCompetitionFragment());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(
				R.layout.fragment_score_registration_close_competition, container,
				false);
	}

}
