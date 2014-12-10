package com.example.golfapp.gameSettings.competition;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.R;

import butterknife.OnClick;
import src.com.example.golfapp.gameSettings.competition.ApproveOpenCompetitionFragment;
import src.com.example.golfapp.gameSettings.competition.JoinOpenCompetitionFragment;
import src.com.example.golfapp.gameSettings.competition.ViewOpenCompetitionsFragment;

public class OpenCompetitionFragment extends BaseFragment {

    @OnClick(R.id.create)
    public void createOpenCompetition() {
        showFragmentAndAddToBackStack(new CreateOpenCompetitionFragment());
    }

    @OnClick(R.id.view)
    public void viewOpenCompetition() {
        showFragmentAndAddToBackStack(new ViewOpenCompetitionsFragment());
    }

    @OnClick(R.id.join)
    public void joinOpenCompetition() {
        showFragmentAndAddToBackStack(new JoinOpenCompetitionFragment());
    }

    @OnClick(R.id.approve)
    public void approveOpenCompetition() {
        showFragmentAndAddToBackStack(new ApproveOpenCompetitionFragment());
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_open_competition, container, false);
	}

}
