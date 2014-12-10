package com.example.golfapp.scoreRegistration;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.OnClick;
import butterknife.OnItemSelected;
import src.com.example.golfapp.scoreRegistration.BetScoringFragment;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.R;
import com.example.golfapp.gameSettings.partyPlay.PartyPlayRegistrationFragment;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class ScoreRegistrationFragment extends BaseFragment {
    View view_container;
    private boolean success = false;
    private ProgressDialog pdialog;
    String party_json_string;
    String users_json_string;
    String retVal;
    ArrayList<Users> users_arraylist;
    TableLayout main_table;

    private class Holes {
        int id = 0;
        String hole = "";

        public Holes(int _id, String _hole) {
            id = _id;
            hole = _hole;
        }
        public String toString() {
            return hole;
        }

    }

    private class Users {
        String id = "";
        String handicap = "";
        String firstname = "";

        public Users(String _id, String _handicap, String _firstname) {
            id = _id;
            handicap = _handicap;
            firstname = _firstname;
        }

        public String toString() {
            return firstname;
        }
    }

    private class InitLists extends AsyncTask<String, String, String> {
        public InitLists() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_loaing_party_info));
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            success = false;

            byte[] party_byte = null;
            String party_string = "";
            byte[] users_byte = null;
            String users_string = "";

            File cDir = getActivity().getCacheDir();
            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt") ;
            String strLine="";
            StringBuilder golfapp_token = new StringBuilder();
            try {
                FileReader fReader = new FileReader(tempFile);
                BufferedReader bReader = new BufferedReader(fReader);
                while( (strLine=bReader.readLine()) != null  ){
                    golfapp_token.append(strLine);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            File cDir2 = getActivity().getCacheDir();
            File tempFile2 = new File(cDir2.getPath() + "/" + "party_play_number.txt") ;
            String strLine2="";
            StringBuilder party_play_number = new StringBuilder();
            try {
                FileReader fReader = new FileReader(tempFile2);
                BufferedReader bReader = new BufferedReader(fReader);
                while( (strLine2=bReader.readLine()) != null  ){
                    party_play_number.append(strLine2);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            HttpClient party_httpclient = new DefaultHttpClient();
            HttpGet party_httpget = new HttpGet("http://zoogtech.com/golfapp/public/score-registration/scores/"+party_play_number.toString()+"?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = party_httpclient.execute(party_httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    party_byte = EntityUtils.toByteArray(response.getEntity());
                    party_string = new String(party_byte, "UTF-8");
                    System.out.println(party_string);
                    party_json_string = party_string;
                    success = true;
                }else {
                    party_byte = EntityUtils.toByteArray(response.getEntity());
                    party_string = new String(party_byte, "UTF-8");
                    party_json_string = party_string;
                    success = false;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            HttpClient users_httpclient = new DefaultHttpClient();
            HttpGet users_httpget = new HttpGet("http://zoogtech.com/golfapp/public/user/all?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = users_httpclient.execute(users_httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    users_byte = EntityUtils.toByteArray(response.getEntity());
                    users_string = new String(users_byte, "UTF-8");
                    System.out.println(users_string);
                    users_json_string = users_string;
                    success = true;
                }else {
                    users_byte = EntityUtils.toByteArray(response.getEntity());
                    users_string = new String(users_byte, "UTF-8");
                    users_json_string = users_string;
                    success = false;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Done";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                JSONObject party_info = null;
                JSONArray users_info = null;
                try {
                    users_info = new JSONArray(users_json_string);
                    ArrayList<Users> users_list = new ArrayList<Users>();
                    for (int i = 0; i < users_info.length(); i++) {
                        JSONObject row = null;
                        try {
                            row = users_info.getJSONObject(i);
                            users_list.add(new Users(row.getString("id"), row.getString("handicap"), row.getString("firstname")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    party_info = new JSONObject(party_json_string);
                    JSONObject course_info = new JSONObject(party_info.getString("course"));
                    TextView name_tv = (TextView) view_container.findViewById(R.id.score_reg_party_name);
                    TextView course_tv = (TextView) view_container.findViewById(R.id.score_reg_course_name);
                    Spinner holes_sp = (Spinner) view_container.findViewById(R.id.score_reg_holes);
                    name_tv.setText(party_info.getString("name"));
                    course_tv.setText(course_info.getString("name"));
                    JSONArray holes_array = new JSONArray(course_info.getString("hole_items"));
                    ArrayList<Holes> holes_list = new ArrayList<Holes>();
                    for (int i = 0; i < holes_array.length(); i++) {
                        JSONObject row = null;
                        try {
                            row = holes_array.getJSONObject(i);
                            holes_list.add(new Holes(Integer.parseInt(row.getString("id")), (Integer.parseInt(row.getString("hole_number"))+1)+""));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, holes_list);
                    holes_sp.setAdapter(spinnerArrayAdapter);
                    JSONArray members_array = new JSONArray(party_info.getString("members"));
                    TableLayout main_table = (TableLayout) view_container.findViewById(R.id.score_reg_party_members);
                    for (int i = 0; i < members_array.length(); i++) {
                        JSONObject row = members_array.getJSONObject(i);
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        final View item = inflater.inflate(R.layout.score_reg_member_row, main_table, false);
                        TextView member_name_col = (TextView) item.findViewById(R.id.score_reg_member_name);
                        TextView handicap_col = (TextView) item.findViewById(R.id.score_reg_member_handicap);
                        for (Users u : users_list) {
                            if (u.id.matches(row.getString("member_id"))) {
                                member_name_col.setText(u.firstname);
                                member_name_col.setTag(row.getString("id"));
                                handicap_col.setText(u.handicap);
                            }
                        }
                        JSONArray score_info_array = new JSONArray(row.getString("scores"));
                        TextView course_col = (TextView) item.findViewById(R.id.score_reg_member_score);
                        if (score_info_array.length() != 0) {
                            for (int i2 = 0; i2 < score_info_array.length(); i2++) {
                                JSONObject hole_score = new JSONObject(score_info_array.getJSONObject(i2).toString());
                                Holes current_hole = (Holes) holes_sp.getSelectedItem();
                                if (current_hole.id == Integer.parseInt(hole_score.getString("hole_id"))) {
                                    course_col.setText(hole_score.getString("score"));
                                }
                            }
                        } else {
//                            course_col.setText("0");
                        }
                        if(i % 2 == 0) {
                            item.setBackgroundColor(Color.WHITE);
                        } else {
                            item.setBackgroundColor(Color.LTGRAY);
                        }

                        main_table.addView(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Toast.makeText(getActivity(), getResources().getString(R.string.jap_loading_complete), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ChangeHole extends AsyncTask<String, String, String> {
        public ChangeHole() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pdialog.setMessage(getResources().getString(R.string.jap_loaing_party_info));
//            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            success = false;

            byte[] party_byte = null;
            String party_string = "";

            File cDir = getActivity().getCacheDir();
            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt") ;
            String strLine="";
            StringBuilder golfapp_token = new StringBuilder();
            try {
                FileReader fReader = new FileReader(tempFile);
                BufferedReader bReader = new BufferedReader(fReader);
                while( (strLine=bReader.readLine()) != null  ){
                    golfapp_token.append(strLine);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            File cDir2 = getActivity().getCacheDir();
            File tempFile2 = new File(cDir2.getPath() + "/" + "party_play_number.txt") ;
            String strLine2="";
            StringBuilder party_play_number = new StringBuilder();
            try {
                FileReader fReader = new FileReader(tempFile2);
                BufferedReader bReader = new BufferedReader(fReader);
                while( (strLine2=bReader.readLine()) != null  ){
                    party_play_number.append(strLine2);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            HttpClient party_httpclient = new DefaultHttpClient();
            HttpGet party_httpget = new HttpGet("http://zoogtech.com/golfapp/public/score-registration/scores/"+party_play_number.toString()+"?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = party_httpclient.execute(party_httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    party_byte = EntityUtils.toByteArray(response.getEntity());
                    party_string = new String(party_byte, "UTF-8");
                    System.out.println(party_string);
                    party_json_string = party_string;
                    success = true;
                }else {
                    party_byte = EntityUtils.toByteArray(response.getEntity());
                    party_string = new String(party_byte, "UTF-8");
                    party_json_string = party_string;
                    success = false;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Done";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            if(pdialog != null && pdialog.isShowing()) {
//                pdialog.dismiss();
//            }
            if(success) {
                JSONObject party_info = null;
                JSONArray users_info = null;
                try {
                    users_info = new JSONArray(users_json_string);
                    ArrayList<Users> users_list = new ArrayList<Users>();
                    for (int i = 0; i < users_info.length(); i++) {
                        JSONObject row = null;
                        try {
                            row = users_info.getJSONObject(i);
                            users_list.add(new Users(row.getString("id"), row.getString("handicap"), row.getString("firstname")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    party_info = new JSONObject(party_json_string);
                    JSONObject course_info = new JSONObject(party_info.getString("course"));
                    Spinner holes_sp = (Spinner) view_container.findViewById(R.id.score_reg_holes);
                    JSONArray members_array = new JSONArray(party_info.getString("members"));
                    main_table.removeAllViews();
                    for (int i = 0; i < members_array.length(); i++) {
                        JSONObject row = members_array.getJSONObject(i);
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        final View item = inflater.inflate(R.layout.score_reg_member_row, main_table, false);
                        TextView member_name_col = (TextView) item.findViewById(R.id.score_reg_member_name);
                        TextView handicap_col = (TextView) item.findViewById(R.id.score_reg_member_handicap);
                        for (Users u : users_list) {
                            if (u.id.matches(row.getString("member_id"))) {
                                member_name_col.setText(u.firstname);
                                member_name_col.setTag(row.getString("id"));
                                handicap_col.setText(u.handicap);
                            }
                        }
                        JSONArray score_info_array = new JSONArray(row.getString("scores"));
                        TextView course_col = (TextView) item.findViewById(R.id.score_reg_member_score);
                        if (score_info_array.length() != 0) {
                            for (int i2 = 0; i2 < score_info_array.length(); i2++) {
                                JSONObject hole_score = new JSONObject(score_info_array.getJSONObject(i2).toString());
                                Holes current_hole = (Holes) holes_sp.getSelectedItem();
                                if (current_hole.id == Integer.parseInt(hole_score.getString("hole_id"))) {
                                    course_col.setText(hole_score.getString("score"));
                                }
                            }
                        } else {
//                            course_col.setText("0");
                        }
                        ImageButton add_score = (ImageButton) item.findViewById(R.id.add_score);
                        ImageButton sub_score = (ImageButton) item.findViewById(R.id.sub_score);
                        add_score.setOnClickListener(addScore);
                        sub_score.setOnClickListener(subScore);
                        if(i % 2 == 0) {
                            item.setBackgroundColor(Color.WHITE);
                        } else {
                            item.setBackgroundColor(Color.LTGRAY);
                        }

                        main_table.addView(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                Toast.makeText(getActivity(), getResources().getString(R.string.jap_loading_complete), Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class RegisterScore extends AsyncTask<String, String, String> {
        public RegisterScore() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_updating_party));
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result = null;
            String str = "";

            File cDir = getActivity().getCacheDir();
            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt") ;

            String strLine="";
            StringBuilder golfapp_token = new StringBuilder();

            try {
                FileReader fReader = new FileReader(tempFile);
                BufferedReader bReader = new BufferedReader(fReader);
                while( (strLine=bReader.readLine()) != null  ){
                    golfapp_token.append(strLine);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            File cDir2 = getActivity().getCacheDir();
            File tempFile2 = new File(cDir2.getPath() + "/" + "party_play_number.txt") ;
            String strLine2="";
            StringBuilder party_play_number = new StringBuilder();

            try {
                FileReader fReader = new FileReader(tempFile2);
                BufferedReader bReader = new BufferedReader(fReader);
                while( (strLine2=bReader.readLine()) != null  ){
                    party_play_number.append(strLine2);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httppost = new HttpPut("http://zoogtech.com/golfapp/public/score-registration/score/"+party_play_number.toString());

            try {
                Spinner holes_sp = (Spinner) view_container.findViewById(R.id.score_reg_holes);
                TableLayout tv = (TableLayout) view_container.findViewById(R.id.score_reg_party_members);
                Holes h1 = (Holes) holes_sp.getSelectedItem();
                List<NameValuePair> json = new ArrayList<NameValuePair>();
                json.add(new BasicNameValuePair("hole_id", h1.id + ""));
                for (int in = 0; in < tv.getChildCount(); in++) {
                    TableRow tr = (TableRow) tv.getChildAt(in);
                    TextView member = (TextView) tr.getChildAt(0);
                    TextView score = (TextView) tr.getChildAt(4);
                    String member_id = member.getTag().toString();
                    String member_score = score.getText().toString();
                    json.add(new BasicNameValuePair("scores["+in+"][party_member_id]", member_id));
                    json.add(new BasicNameValuePair("scores["+in+"][score]", member_score));
                }

                httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
                httppost.setHeader("Authorization", golfapp_token.toString());
                httppost.setEntity(new UrlEncodedFormEntity(json));

                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, "UTF-8");
                    System.out.println(str);
                    System.out.println(new UrlEncodedFormEntity(json));
                    System.out.println("Success!");
                    success = true;
                    retVal = str;
                }else {
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, "UTF-8");
                    System.out.println("Failed!");
                    System.out.println(str);
                    System.out.println(new UrlEncodedFormEntity(json).toString());
                    retVal = str;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Yeah";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                Toast.makeText(getActivity(), "Scores saved.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }


    @OnClick(R.id.bet_summary_btn)
	public void showBetSummary() {
		showFragmentAndAddToBackStack(new BetSummaryFragment());
	}
	
	@OnClick(R.id.bet_registration_btn)
	public void showBetRegistration() {
		showFragmentAndAddToBackStack(new BetScoringFragment());
	}
	
	@OnClick(R.id.record_score_btn)
	public void saveRecordScore(Button btn) {
		if (btn.getText().equals("Record Score")) {
            btn.setText("Save");
            Spinner sp = (Spinner) view_container.findViewById(R.id.score_reg_holes);
            sp.getSelectedView().setEnabled(false);
            sp.setEnabled(false);
            TableLayout mt = (TableLayout) view_container.findViewById(R.id.score_reg_party_members);
            for (int index = 0; index < mt.getChildCount(); index++) {
                TableRow tr = (TableRow) mt.getChildAt(index);
                TextView tv1 = (TextView) tr.getChildAt(4);
                if (tv1.getText().equals("")) {
                    tv1.setText("0");
                }
                ImageButton im1 = (ImageButton) tr.getChildAt(6);
                ImageButton im2 = (ImageButton) tr.getChildAt(5);
                im1.setVisibility(View.VISIBLE);
                im2.setVisibility(View.VISIBLE);
            }
        } else {
            new RegisterScore().execute();
            btn.setText("Record Score");
            Spinner sp = (Spinner) view_container.findViewById(R.id.score_reg_holes);
            sp.getSelectedView().setEnabled(true);
            sp.setEnabled(true);
            TableLayout mt = (TableLayout) view_container.findViewById(R.id.score_reg_party_members);
            for (int index = 0; index < mt.getChildCount(); index++) {
                TableRow tr = (TableRow) mt.getChildAt(index);
                TextView tv1 = (TextView) tr.getChildAt(4);
                if (tv1.getText().equals("")) {
                    tv1.setText("0");
                }
                ImageButton im1 = (ImageButton) tr.getChildAt(6);
                ImageButton im2 = (ImageButton) tr.getChildAt(5);
                im1.setVisibility(View.INVISIBLE);
                im2.setVisibility(View.INVISIBLE);
            }
        }
	}

    @OnItemSelected(R.id.score_reg_holes)
    public void changeHole() {
        new ChangeHole().execute();
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_score_registration_view_for_party_play,
				container, false);
	}

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        main_table = (TableLayout) view.findViewById(R.id.score_reg_party_members);
        new InitLists().execute();
    }

    private View.OnClickListener addScore = new View.OnClickListener() {
        public void onClick(View v) {
            TableRow tr = (TableRow) v.getParent();
            TextView score = (TextView) tr.findViewById(R.id.score_reg_member_score);
            int score_val = Integer.parseInt(score.getText().toString());
            score.setText((score_val+1)+"");
        }
    };

    private View.OnClickListener subScore = new View.OnClickListener() {
        public void onClick(View v) {
            TableRow tr = (TableRow) v.getParent();
            TextView score = (TextView) tr.findViewById(R.id.score_reg_member_score);
            int score_val = Integer.parseInt(score.getText().toString());
            score.setText((score_val-1)+"");
        }
    };
}
