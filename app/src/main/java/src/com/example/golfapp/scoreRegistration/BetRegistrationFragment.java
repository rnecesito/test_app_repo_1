package com.example.golfapp.scoreRegistration;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textservice.TextInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.OnClick;

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
import java.util.ArrayList;
import java.util.List;

public class BetRegistrationFragment extends BaseFragment {
    View view_container;
    private boolean success = false;
    private ProgressDialog pdialog;
    String bettype_json_string;
    String party_json_string;
    String retVal;

    private class Bets {
        int id = 0;
        String description = "";
        String name = "";

        public Bets(int _id, String _description, String _name) {
            id = _id;
            description = _description;
            name = _name;
        }

        public String toString() {
            return name;
        }
    }

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
            byte[] bets_byte = null;
            String bets_string = "";

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
            HttpGet party_httpget = new HttpGet("http://zoogtech.com/golfapp/public/party-play/"+party_play_number.toString()+"?access_token="+golfapp_token.toString());

            HttpClient bets_httpclient = new DefaultHttpClient();
            HttpGet bets_httpget = new HttpGet("http://zoogtech.com/golfapp/public/bet-type?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = party_httpclient.execute(party_httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    party_byte = EntityUtils.toByteArray(response.getEntity());
                    party_string = new String(party_byte, "UTF-8");
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

            try {
                HttpResponse response = bets_httpclient.execute(bets_httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    bets_byte = EntityUtils.toByteArray(response.getEntity());
                    bets_string = new String(bets_byte, "UTF-8");
                    bettype_json_string = bets_string;
                    success = true;
                }else {
                    bets_byte = EntityUtils.toByteArray(response.getEntity());
                    bets_string = new String(bets_byte, "UTF-8");
                    bettype_json_string = bets_string;
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
//                Spinner spinner = (Spinner) view_container.findViewById(R.id.courses_spinner);
//                ArrayList<Courses> course_list = new ArrayList<Courses>();
                try {
                    party_info = new JSONObject(party_json_string);
                    JSONObject course_info = new JSONObject(party_info.getString("course"));
                    TextView name_tv = (TextView) view_container.findViewById(R.id.bet_reg_party_name);
                    TextView course_tv = (TextView) view_container.findViewById(R.id.bet_reg_course_name);
                    Spinner holes_sp = (Spinner) view_container.findViewById(R.id.bet_reg_holes);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray bets_array = null;
                try {
                    bets_array = new JSONArray(bettype_json_string);
                    ArrayList<Bets> bets_list = new ArrayList<Bets>();
                    bets_list.add(new Bets(-1, "", "None"));
                    for (int i = 0; i < bets_array.length(); i++) {
                        JSONObject row = null;
                        try {
                            row = bets_array.getJSONObject(i);
                            bets_list.add(new Bets(Integer.parseInt(row.getString("id")), row.getString("description"), row.getString("name")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, bets_list);
                    Spinner bet_type_1 = (Spinner) view_container.findViewById(R.id.bet_type_1);
                    Spinner bet_type_2 = (Spinner) view_container.findViewById(R.id.bet_type_2);
                    Spinner bet_type_3 = (Spinner) view_container.findViewById(R.id.bet_type_3);
                    bet_type_1.setAdapter(spinnerArrayAdapter);
                    bet_type_2.setAdapter(spinnerArrayAdapter);
                    bet_type_3.setAdapter(spinnerArrayAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Toast.makeText(getActivity(), getResources().getString(R.string.jap_loading_complete), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class RegisterBet extends AsyncTask<String, String, String> {
        public RegisterBet() {
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
            HttpPut httppost = new HttpPut("http://zoogtech.com/golfapp/public/bet-registration/register/"+party_play_number.toString());

            try {
                Spinner bet_type_1 = (Spinner) view_container.findViewById(R.id.bet_type_1);
                Spinner bet_type_2 = (Spinner) view_container.findViewById(R.id.bet_type_2);
                Spinner bet_type_3 = (Spinner) view_container.findViewById(R.id.bet_type_3);
                Bets b1 = (Bets) bet_type_1.getSelectedItem();
                Bets b2 = (Bets) bet_type_2.getSelectedItem();
                Bets b3 = (Bets) bet_type_3.getSelectedItem();
                Spinner holes_sp = (Spinner) view_container.findViewById(R.id.bet_reg_holes);
                Holes h1 = (Holes) holes_sp.getSelectedItem();
                List<NameValuePair> json = new ArrayList<NameValuePair>();
                json.add(new BasicNameValuePair("hole_id", h1.id+""));
                int index = 0;
                if (b1.id != -1) {
                    json.add(new BasicNameValuePair("bets["+index+"][bet_type_id]", b1.id+""));
                    json.add(new BasicNameValuePair("bets["+index+"][amount]", 0+""));
                    index += 1;
                }
                if (b2.id != -1) {
                    json.add(new BasicNameValuePair("bets["+index+"][bet_type_id]", b2.id+""));
                    json.add(new BasicNameValuePair("bets["+index+"][amount]", 0+""));
                    index += 1;
                }
                if (b3.id != -1) {
                    json.add(new BasicNameValuePair("bets["+index+"][bet_type_id]", b3.id+""));
                    json.add(new BasicNameValuePair("bets["+index+"][amount]", 0+""));
                    index += 1;
                }
//                json.add(new BasicNameValuePair("course_id", course));
//                for(int index = 0; index < competitor_list.getChildCount(); index++){
//                    TableRow row = (TableRow) competitor_list.getChildAt(index);
//                    RelativeLayout rl = (RelativeLayout) row.getChildAt(0);
//                    Spinner sp = (Spinner) rl.getChildAt(1);
//                    Players pl = (Players) sp.getSelectedItem();
//                    int pl_id = pl.id;
//                    json.add(new BasicNameValuePair("members["+index+"]", pl_id+""));
//                }

                httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
                httppost.setHeader("Authorization", golfapp_token.toString());
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
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_reg_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

	@OnClick(R.id.bet_register)
	public void register() {
        new RegisterBet().execute();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_bet_registration,
				container, false);
	}

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        new InitLists().execute();
    }
}
