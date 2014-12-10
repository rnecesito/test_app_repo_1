package com.example.golfapp.gameSettings.courseAdmin;

import static com.example.golfapp.gameSettings.courseAdmin.CreateCourseFragment.EMPTY;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.R;
import com.example.golfapp.R.id;
import com.example.golfapp.R.layout;

import butterknife.OnClick;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CourseRegistrationFragment extends BaseFragment {

	@OnClick(R.id.create_courses)
	public void createCourse() {
		showFragmentAndAddToBackStack(new CreateCourseFragment());
	}

	@OnClick(R.id.view_courses)
	public void viewCourse() {
		showFragmentAndAddToBackStack(new ViewCourseFragment());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_course_registration, container,
				false);
	}
}
