package com.example.golfapp.gameSettings.partyPlay;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CreatePartyPlayFragment extends BaseFragment {
    View view_container;
    private boolean success = false;
    private ProgressDialog pdialog;
    String response2;
    String response2_2;
    String user_info_json_string;
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
            pdialog.setMessage(getResources().getString(R.string.jap_loading_courses_players));
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result = null;
            String result2 = "";

            byte[] result_2 = null;
            String result2_2 = "";

            byte[] user_info_result = null;
            String user_info_result_string = "";

            /** Getting Cache Directory */
            File cDir = getActivity().getCacheDir();

            /** Getting a reference to temporary file, if created earlier */
            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt") ;

            String strLine="";
            StringBuilder text = new StringBuilder();

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

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("http://zoogtech.com/golfapp/public/course?access_token="+text.toString());

            try {
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toByteArray(response.getEntity());
                    result2 = new String(result, "UTF-8");
                    System.out.println("Success!");
                    response2 = result2;
                    success = true;
                }else {
                    result = EntityUtils.toByteArray(response.getEntity());
                    result2 = new String(result, "UTF-8");
                    System.out.println("Failed!");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            HttpClient httpclient_2 = new DefaultHttpClient();
            HttpGet httppost_2 = new HttpGet("http://zoogtech.com/golfapp/public/user/all?access_token="+text.toString());

            try {
                HttpResponse response = httpclient_2.execute(httppost_2);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_2 = EntityUtils.toByteArray(response.getEntity());
                    result2_2 = new String(result_2, "UTF-8");
                    System.out.println(result2_2);
                    System.out.println("Success!");
                    response2_2 = result2_2;
                    success = true;
                }else {
                    result_2 = EntityUtils.toByteArray(response.getEntity());
                    result2_2 = new String(result_2, "UTF-8");
                    System.out.println(result2_2);
                    System.out.println("Failed!");
                    success = false;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            HttpClient httpclient_3 = new DefaultHttpClient();
            HttpGet httppost_3 = new HttpGet("http://zoogtech.com/golfapp/public/user/profile?access_token="+text.toString());

            try {
                HttpResponse response = httpclient_3.execute(httppost_3);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    user_info_result = EntityUtils.toByteArray(response.getEntity());
                    user_info_result_string = new String(user_info_result, "UTF-8");
                    System.out.println(user_info_result_string);
                    System.out.println("Success!");
                    user_info_json_string = user_info_result_string;
                    success = true;
                }else {
                    user_info_result = EntityUtils.toByteArray(response.getEntity());
                    user_info_result_string = new String(user_info_result, "UTF-8");
                    System.out.println(user_info_result_string);
                    System.out.println("Failed!");
                    success = false;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result2;
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
                    array = new JSONArray(response2);
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
                    array2 = new JSONArray(response2_2);
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

                int index = 0;
                JSONObject user_info_jo;
                try {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    View item = inflater.inflate(R.layout.row_player_handicap, competitor_list, false);
                    ImageView del_btn = (ImageView) item.findViewById(R.id.delete_competitor);
                    del_btn.setOnClickListener(btnListener);
                    del_btn.setVisibility(View.INVISIBLE);
                    Spinner clist = (Spinner) item.findViewById(R.id.comp_names);
                    ArrayAdapter spinnerArrayAdapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, player_list);
                    user_info_jo = new JSONObject(user_info_json_string);
                    clist.setAdapter(spinnerArrayAdapter2);
                    competitor_list.addView(item);
                    for (Players p : player_list) {
                        if (p.id == Integer.parseInt(user_info_jo.getString("id"))) {
                            int pos = spinnerArrayAdapter2.getPosition(p);
                            clist.setSelection(pos);
                            clist.setClickable(false);
                        }
                        index = index + 1;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getContext(), getResources().getString(R.string.jap_loading_complete), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class PartyCreate extends AsyncTask<String, String, String> {
        StringBuilder text = new StringBuilder();
        public PartyCreate() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_creating_party));
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
            String pname = strings[0];
            String date = strings[1];
            String course= strings[2];
            byte[] result = null;
            String str = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://zoogtech.com/golfapp/public/party-play");

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
                Toast.makeText(getContext(), getResources().getString(R.string.jap_party_created), Toast.LENGTH_SHORT).show();
                showFragmentAndAddToBackStack(new ViewPartyPlayFragment());
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_create_party_play, container, false);
	}

	@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
        view_container = view;
        competitor_list = (TableLayout) view.findViewById(R.id.competitors);
        new InitLists().execute();

		Button b = (Button) view.findViewById(R.id.save_btn);
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
                if(competitor_list.getChildCount() < 4) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    View item = inflater.inflate(R.layout.row_player_handicap, competitor_list, false);
                    ImageView del_btn = (ImageView) item.findViewById(R.id.delete_competitor);
                    del_btn.setOnClickListener(btnListener);
                    Spinner clist = (Spinner) item.findViewById(R.id.comp_names);
                    ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, player_list);
                    clist.setAdapter(spinnerArrayAdapter);
                    competitor_list.addView(item);
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.jap_upto_4_people), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), getResources().getString(R.string.jap_enter_party_name), Toast.LENGTH_SHORT).show();
                    return;
                } else if (p_date.matches("") || p_date.matches("0000-00-00") || p_date.matches("MM/dd/yy")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.jap_enter_valid_date), Toast.LENGTH_SHORT).show();
                } else {
                    new PartyCreate().execute(pn_val, p_date, course_id+"");
                }
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
