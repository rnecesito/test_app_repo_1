package com.example.golfapp.gameSettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.OnClick;
import src.com.example.golfapp.gameSettings.UserProfileFragment;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.R;
import com.example.golfapp.gameSettings.competition.CompetitionSettingsFragment;
import com.example.golfapp.gameSettings.courseAdmin.CourseRegistrationFragment;
import com.example.golfapp.gameSettings.partyPlay.PartyPlayRegistrationFragment;

public class GameSettingsFragment extends BaseFragment {

	@OnClick(R.id.profile)
	public void showProfileRegistration() {
		showFragmentAndAddToBackStack(new UserProfileFragment());
	}

	@OnClick(R.id.court_admin)
	public void showCourtAdmin() {
		showFragmentAndAddToBackStack(new CourseRegistrationFragment());
	}
	
	@OnClick(R.id.competition)
	public void showCompetition() {
		showFragmentAndAddToBackStack(new CompetitionSettingsFragment());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_game_settings, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Button b = (Button) view.findViewById(R.id.party_play);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
						.replace(R.id.container, new PartyPlayRegistrationFragment())
						.addToBackStack(null).commit();
			}
		});
	}

}
