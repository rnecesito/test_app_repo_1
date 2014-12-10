package com.example.golfapp.scoreRegistration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.OnClick;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.R;
import com.example.golfapp.gameSettings.GameScoreRegistrationFragment;

public class ScoreRegDetailCloseCompetitionFragment extends BaseFragment {

	@OnClick(R.id.record_score_btn)
	public void saveRecordScore(Button btn) {
		if (btn.getText().equals("Record Score"))
			btn.setText("Save");
		else
			showFragmentAndAddToBackStack(new GameScoreRegistrationFragment());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(
				R.layout.fragment_score_registration_detail_close_competition,
				container, false);
	}
}
