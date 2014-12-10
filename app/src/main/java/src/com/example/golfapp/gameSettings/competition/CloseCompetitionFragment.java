package com.example.golfapp.gameSettings.competition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.R;

import butterknife.OnClick;
import src.com.example.golfapp.gameSettings.competition.ApproveOpenCompetitionFragment;
import src.com.example.golfapp.gameSettings.competition.CreateClosedCompetitionFragment;
import src.com.example.golfapp.gameSettings.competition.CreateClosedCompetitionGroupFragment;
import src.com.example.golfapp.gameSettings.competition.JoinClosedCompetitionFragment;
import src.com.example.golfapp.gameSettings.competition.JoinOpenCompetitionFragment;
import src.com.example.golfapp.gameSettings.competition.ViewClosedCompetitionGroupsFragment;
import src.com.example.golfapp.gameSettings.competition.ViewClosedCompetitionsFragment;
import src.com.example.golfapp.gameSettings.competition.ViewOpenCompetitionsFragment;

public class CloseCompetitionFragment extends BaseFragment {

    @OnClick(R.id.closed_create)
    public void createClosedCompetition() {
        showFragmentAndAddToBackStack(new CreateClosedCompetitionFragment());
    }

    @OnClick(R.id.closed_view)
    public void viewOpenCompetition() {
        showFragmentAndAddToBackStack(new ViewClosedCompetitionsFragment());
    }

    @OnClick(R.id.closed_join)
    public void joinOpenCompetition() {
        showFragmentAndAddToBackStack(new JoinClosedCompetitionFragment());
    }

    @OnClick(R.id.closed_create_group)
    public void createClosedGroup() {
        showFragmentAndAddToBackStack(new CreateClosedCompetitionGroupFragment());
    }

    @OnClick(R.id.closed_view_group)
    public void viewClosedGroup() {
        showFragmentAndAddToBackStack(new ViewClosedCompetitionGroupsFragment());
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_close_competition, container, false);
	}

}
