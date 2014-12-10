package com.example.golfapp;

import com.example.golfapp.gameSettings.GameCountingFragment;
import com.example.golfapp.gameSettings.GameHistoryFragment;
import com.example.golfapp.gameSettings.GameScoreRegistrationFragment;
import com.example.golfapp.gameSettings.GameSettingsFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class GameMenuFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main_menu, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Button logout = (Button) view.findViewById(R.id.logout_btn);
		logout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
						.replace(R.id.container, new LandingPageFragment()).commit();
			}
		});

		Button gameSettings = (Button) view.findViewById(R.id.game_settings);
		gameSettings.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
						.replace(R.id.container, new GameSettingsFragment())
						.addToBackStack(null).commit();
			}
		});

		Button gameCounting = (Button) view.findViewById(R.id.game_counting);
		gameCounting.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
						.replace(R.id.container, new GameCountingFragment())
						.addToBackStack(null).commit();
			}
		});

		Button history = (Button) view.findViewById(R.id.game_history);
		history.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
						.replace(R.id.container, new GameHistoryFragment())
						.addToBackStack(null).commit();
			}
		});

		Button score = (Button) view.findViewById(R.id.game_score);
		score.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
						.replace(R.id.container, new GameScoreRegistrationFragment())
						.addToBackStack(null).commit();
			}
		});
	}

}
