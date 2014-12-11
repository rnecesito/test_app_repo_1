package com.example.golfapp.gameSettings.partyPlay;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.R;
import com.example.golfapp.R.id;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditPartyPlayFragment extends BaseFragment {
    View view_container;
    private boolean success = false;
    private ProgressDialog pdialog;
    String course_json_string;
    String users_json_string;
    String party_json_string;
    TextView date_view;
    TableLayout competitor_list;
    ArrayList<Players> player_list;
    String retVal;

    private class Courses {
        int id = 0;
        String course = "";

        public Courses(int _id, String _course) {
            id = _id;
            course = _course;
        }

        public String toString() {
            return course;
        }
    }

    private class Players {
        int id = 0;
        String name = "";

        public Players(int _id, String _name) {
            id = _id;
            name = _name;
        }

        public String toString() {
            return name;
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
            byte[] course_result_byte = null;
            String course_result_string = "";

            byte[] result_2 = null;
            String result2_2 = "";

            byte[] result_3 = null;
            String result2_3 = "";

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

            HttpClient courses_httpclient = new DefaultHttpClient();
            HttpGet courses_httpget = new HttpGet("http://zoogtech.com/golfapp/public/course?access_token="+golfapp_token.toString());

            HttpClient users_httpclient = new DefaultHttpClient();
            HttpGet users_httpget = new HttpGet("http://zoogtech.com/golfapp/public/user/all?access_token="+golfapp_token.toString());

            HttpClient party_httpclient = new DefaultHttpClient();
            HttpGet party_httpget = new HttpGet("http://zoogtech.com/golfapp/public/party-play/"+party_play_number.toString()+"?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = courses_httpclient.execute(courses_httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    course_result_byte = EntityUtils.toByteArray(response.getEntity());
                    course_result_string = new String(course_result_byte, "UTF-8");
                    course_json_string = course_result_string;
                    success = true;
                }else {
                    course_result_byte = EntityUtils.toByteArray(response.getEntity());
                    course_result_string = new String(course_result_byte, "UTF-8");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                HttpResponse response = users_httpclient.execute(users_httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_2 = EntityUtils.toByteArray(response.getEntity());
                    result2_2 = new String(result_2, "UTF-8");
                    users_json_string = result2_2;
                    success = true;
                }else {
                    result_2 = EntityUtils.toByteArray(response.getEntity());
                    result2_2 = new String(result_2, "UTF-8");
                    success = false;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                HttpResponse response = party_httpclient.execute(party_httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_3 = EntityUtils.toByteArray(response.getEntity());
                    result2_3 = new String(result_3, "UTF-8");
                    party_json_string = result2_3;
                    success = true;
                }else {
                    result_3 = EntityUtils.toByteArray(response.getEntity());
                    result2_3 = new String(result_3, "UTF-8");
                    success = false;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return course_result_string;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                JSONArray array = null;
                try {
                    array = new JSONArray(course_json_string);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Spinner spinner = (Spinner) view_container.findViewById(R.id.courses_spinner);
                ArrayList<Courses> course_list = new ArrayList<Courses>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = null;
                    try {
                        row = array.getJSONObject(i);
                        course_list.add(new Courses(Integer.parseInt(row.getString("id")), row.getString("name")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                JSONArray array2 = null;
                try {
                    array2 = new JSONArray(users_json_string);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayList<Players> player_list_container = new ArrayList<Players>();
                for (int i = 0; i < array2.length(); i++) {
                    JSONObject row = null;
                    try {
                        row = array2.getJSONObject(i);
                        player_list_container.add(new Players(Integer.parseInt(row.getString("id")), row.getString("firstname")+" "+row.getString("lastname")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                player_list = player_list_container;
                ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, course_list);
                spinner.setAdapter(spinnerArrayAdapter);

                JSONObject spec_party = null;
                try {
                    spec_party = new JSONObject(party_json_string);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray array3 = null;
                try {
                    array3 = new JSONArray(spec_party.getString("members"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < array3.length(); i++) {
                    JSONObject row = null;
                    JSONObject row_in = null;
                    try {
                        row = array3.getJSONObject(i);
                        row_in = new JSONObject(row.getString("member"));
                        LayoutInflater inflater = LayoutInflater.from(getActivity());
                        View item = inflater.inflate(R.layout.row_player_handicap, competitor_list, false);
                        ImageView del_btn = (ImageView) item.findViewById(R.id.delete_competitor);
                        del_btn.setOnClickListener(btnListener);
                        Spinner clist = (Spinner) item.findViewById(R.id.comp_names);
                        ArrayAdapter spinnerArrayAdapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, player_list);
                        clist.setAdapter(spinnerArrayAdapter2);
                        int cnt = 0;
                        int index = 0;
                        for (Players p : player_list_container) {
                            cnt++;
                            if(p.id == Integer.parseInt(row_in.getString("id"))) {
                                index = cnt - 1;
                            }
                        }
                        clist.setSelection(index);
                        competitor_list.addView(item);
                        int cnt2 = 0;
                        int index2 = 0;
                        for (Courses c : course_list) {
                            cnt2++;
                            if(c.id == Integer.parseInt(spec_party.getString("course_id"))) {
                                index2 = cnt2 - 1;
                            }
                        }
                        spinner.setSelection(index2);
                        EditText et = (EditText) view_container.findViewById(R.id.party_name);
                        TextView tv = (TextView) view_container.findViewById(id.party_date);
                        et.setText(spec_party.getString("name"));
                        tv.setText(spec_party.getString("date"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                Toast.makeText(getActivity(), getResources().getString(R.string.jap_loading_complete), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UpdateParty extends AsyncTask<String, String, String> {
        StringBuilder text = new StringBuilder();
        public UpdateParty() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_updating_party));
            pdialog.show();

            File cDir = getActivity().getCacheDir();
            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt") ;

            String strLine="";

            try {
                FileReader fReader = new FileReader(tempFile);
                BufferedReader bReader = new BufferedReader(fReader);
                while( (strLine=bReader.readLine()) != null  ){
                    text.append(strLine);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String pname = strings[0];
            String date = strings[1];
            String course= strings[2];
            byte[] result = null;
            String str = "";

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
            HttpPut httppost = new HttpPut("http://zoogtech.com/golfapp/public/party-play/"+party_play_number.toString());

            try {
                List<NameValuePair> json = new ArrayList<NameValuePair>();
                json.add(new BasicNameValuePair("name", pname));
                json.add(new BasicNameValuePair("date", date));
                json.add(new BasicNameValuePair("course_id", course));
                for(int index = 0; index < competitor_list.getChildCount(); index++){
                    TableRow row = (TableRow) competitor_list.getChildAt(index);
                    RelativeLayout rl = (RelativeLayout) row.getChildAt(0);
                    Spinner sp = (Spinner) rl.getChildAt(1);
                    Players pl = (Players) sp.getSelectedItem();
                    int pl_id = pl.id;
                    json.add(new BasicNameValuePair("members["+index+"]", pl_id+""));
                }

                httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
                httppost.setHeader("Authorization", text.toString());
                httppost.setEntity(new UrlEncodedFormEntity(json));

                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, "UTF-8");
                    System.out.println(str);
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
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_party_updated), Toast.LENGTH_SHORT).show();
                showFragment(new ViewPartyPlayFragment());
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DeleteParty extends AsyncTask<String, String, String> {
        StringBuilder text = new StringBuilder();
        public DeleteParty() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_deleting_party));
            pdialog.show();
            /** Getting Cache Directory */
            File cDir = getActivity().getCacheDir();

            /** Getting a reference to temporary file, if created earlier */
            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt") ;

            String strLine="";

            /** Reading contents of the temporary file, if already exists */
            try {
                FileReader fReader = new FileReader(tempFile);
                BufferedReader bReader = new BufferedReader(fReader);

                /** Reading the contents of the file , line by line */
                while( (strLine=bReader.readLine()) != null  ){
                    text.append(strLine);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result = null;
            String str = "";

            /** Getting Cache Directory */
            File cDir2 = getActivity().getCacheDir();

            /** Getting a reference to temporary file, if created earlier */
            File tempFile2 = new File(cDir2.getPath() + "/" + "party_play_number.txt") ;

            String strLine2="";
            StringBuilder text2 = new StringBuilder();

            /** Reading contents of the temporary file, if already exists */
            try {
                FileReader fReader = new FileReader(tempFile2);
                BufferedReader bReader = new BufferedReader(fReader);

                /** Reading the contents of the file , line by line */
                while( (strLine2=bReader.readLine()) != null  ){
                    text2.append(strLine2);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpDelete httppost = new HttpDelete("http://zoogtech.com/golfapp/public/party-play/"+text2.toString());

            try {

                httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
                httppost.setHeader("Authorization", text.toString());

                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, "UTF-8");
                    System.out.println(str);
                    System.out.println("Success!");
                    success = true;
                    retVal = str;
                }else {
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, "UTF-8");
                    System.out.println("Failed!");
                    System.out.println(str);
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
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_party_deleted), Toast.LENGTH_SHORT).show();
                showFragment(new ViewPartyPlayFragment());
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater
				.inflate(R.layout.fragment_edit_party_play, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

        view_container = view;
        competitor_list = (TableLayout) view.findViewById(R.id.competitors);
        new InitLists().execute();

        Button b = (Button) view.findViewById(R.id.save_btn);
        Button b2 = (Button) view.findViewById(R.id.del_btn);
        date_view = (TextView) view.findViewById(R.id.party_date);
        ImageView iv = (ImageView) view.findViewById(R.id.set_party_date);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        new mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();
            }
        });

        ImageView add_player_btn = (ImageView) view.findViewById(R.id.add_player);
        add_player_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(competitor_list.getChildCount() < 3) {
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    View item = inflater.inflate(R.layout.row_player_handicap, competitor_list, false);
                    ImageView del_btn = (ImageView) item.findViewById(R.id.delete_competitor);
                    del_btn.setOnClickListener(btnListener);
                    Spinner clist = (Spinner) item.findViewById(R.id.comp_names);
                    ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, player_list);
                    clist.setAdapter(spinnerArrayAdapter);
                    competitor_list.addView(item);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.jap_upto_4_people), Toast.LENGTH_SHORT).show();
                }
            }
        });

        final EditText party_name = (EditText) view.findViewById(R.id.party_name);
        final Spinner course_name = (Spinner) view.findViewById(R.id.courses_spinner);
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String pn_val = party_name.getText().toString();
                Courses c = (Courses) course_name.getSelectedItem();
                int course_id = c.id;
                String p_date = date_view.getText().toString();
                if(pn_val.matches("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.jap_enter_party_name), Toast.LENGTH_SHORT).show();
                    return;
                } else if (p_date.matches("") || p_date.matches("0000-00-00") || p_date.matches("MM/dd/yy")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.jap_enter_valid_date), Toast.LENGTH_SHORT).show();
                } else {
                    new UpdateParty().execute(pn_val, p_date, course_id+"");
                }
            }
        });

        b2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                new DeleteParty().execute();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.jap_sure)).setPositiveButton(getResources().getString(R.string.jap_yes), dialogClickListener)
                        .setNegativeButton(getResources().getString(R.string.jap_no), dialogClickListener).show();
            }
        });
	}

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // getCalender();
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            date_view.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(new DecimalFormat("00").format(mYear)).append("-")
                    .append(new DecimalFormat("00").format(mMonth + 1)).append("-")
                    .append(new DecimalFormat("00").format(mDay)));
            System.out.println(date_view.getText().toString());

        }
    }


    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TableRow row = (TableRow) view.getParent().getParent();
            TableLayout parent = (TableLayout) row.getParent();
            parent.removeView(row);
        }
    };
}
