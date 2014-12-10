package com.example.golfapp;

import com.example.golfapp.gameSettings.ProfileRegistrationFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class LandingPageFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.login, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Button login = (Button) view.findViewById(R.id.login_btn);
		login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
						.replace(R.id.container, new LoginFragment()).commit();
			}
		});

		Button registration = (Button) view.findViewById(R.id.registration_btn);
		registration.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showFragmentAndAddToBackStack(new ProfileRegistrationFragment());
			}
		});

	}

}
