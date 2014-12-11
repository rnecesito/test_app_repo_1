package src.com.example.golfapp.scoreRegistration;

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

import butterknife.OnClick;
import butterknife.OnItemSelected;

public class BetScoringFragment extends BaseFragment {
    View view_container;
    private boolean success = false;
    private ProgressDialog pdialog;
    String party_json_string;
    String users_json_string;
    String bets_json_string;
    String bettype_json_string;
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

    private class BetTypes {
        int id = 0;
        String name = "";
        String description = "";

        public BetTypes(int _id, String _description, String _name) {
            id = _id;
            name = _name;
            description = _description;
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

            byte[] party_byte = null;
            String party_string = "";
            byte[] users_byte = null;
            String users_string = "";
            byte[] bets_byte = null;
            String bets_string = "";

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

            HttpClient bets_httpclient = new DefaultHttpClient();
            HttpGet bets_httpget = new HttpGet("http://zoogtech.com/golfapp/public/bet-type?access_token="+golfapp_token.toString());
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
                JSONArray users_info = null;
                JSONArray bets_array = null;
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

                    bets_array = new JSONArray(bettype_json_string);
                    ArrayList<BetTypes> bets_list = new ArrayList<BetTypes>();
                    bets_list.add(new BetTypes(-1, "", "None"));
                    for (int i = 0; i < bets_array.length(); i++) {
                        JSONObject row = null;
                        try {
                            row = bets_array.getJSONObject(i);
                            bets_list.add(new BetTypes(Integer.parseInt(row.getString("id")), row.getString("description"), row.getString("name")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
//                    ArrayAdapter spinnerArrayAdapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, bets_list);
//                    Spinner bet_type = (Spinner) view_container.findViewById(R.id.score_reg_bet_type);
//                    bet_type.setAdapter(spinnerArrayAdapter2);

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
            pdialog.setMessage(getResources().getString(R.string.jap_loading_bet_types));
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

            HttpClient bets_httpclient = new DefaultHttpClient();
            HttpGet bets_httpget = new HttpGet("http://zoogtech.com/golfapp/public/bet-registration/bets/"+party_play_number.toString()+"?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = bets_httpclient.execute(bets_httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    bets_byte = EntityUtils.toByteArray(response.getEntity());
                    bets_string = new String(bets_byte, "UTF-8");
                    bets_json_string = bets_string;
                    success = true;
                }else {
                    bets_byte = EntityUtils.toByteArray(response.getEntity());
                    bets_string = new String(bets_byte, "UTF-8");
                    bets_json_string = bets_string;
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
                ArrayList<BetTypes> bets_list = new ArrayList<BetTypes>();
                JSONObject bets_info = null;
                Spinner spinner2 = (Spinner) view_container.findViewById(R.id.score_reg_bet_type);
                Spinner spinner = (Spinner) view_container.findViewById(R.id.score_reg_holes);
                Holes h = (Holes) spinner.getSelectedItem();
                try {
                    bets_info = new JSONObject(bets_json_string);
                    JSONObject course_info = new JSONObject(bets_info.getString("course"));
                    JSONArray holes = new JSONArray(course_info.getString("hole_items"));
                    for (int index = 0; index < holes.length(); index++) {
                        JSONObject bet_row = holes.getJSONObject(index);
                        if (bet_row.getString("id").matches(String.valueOf(h.id))) {
                            JSONArray bets_array2 = new JSONArray(bet_row.getString("bets"));
                            for (int index2 = 0; index2 < bets_array2.length(); index2++) {
                                JSONObject bet_info = bets_array2.getJSONObject(index2);
                                JSONObject bet_type_info = new JSONObject(bet_info.getString("bet_type"));
                                bets_list.add(new BetTypes(Integer.parseInt(bet_type_info.getString("id")), bet_type_info.getString("description"), bet_type_info.getString("name")));
                            }
                        }
                    }
                    ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, bets_list);
                    spinner2.setAdapter(spinnerArrayAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_loading_complete), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ChangeBetType extends AsyncTask<String, String, String> {
        public ChangeBetType() {
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

            HttpClient bets_httpclient = new DefaultHttpClient();
            HttpGet bets_httpget = new HttpGet("http://zoogtech.com/golfapp/public/score-registration/bets/"+party_play_number.toString()+"?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = bets_httpclient.execute(bets_httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    bets_byte = EntityUtils.toByteArray(response.getEntity());
                    bets_string = new String(bets_byte, "UTF-8");
                    bets_json_string = bets_string;
                    success = true;
                }else {
                    bets_byte = EntityUtils.toByteArray(response.getEntity());
                    bets_string = new String(bets_byte, "UTF-8");
                    bets_json_string = bets_string;
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
                main_table.removeAllViews();
                JSONObject party_info = null;
                JSONArray users_info = null;
                JSONObject bets_info = null;
                ArrayList<Users> users_list = new ArrayList<Users>();
                Spinner bet_type_spinner = (Spinner) view_container.findViewById(R.id.score_reg_bet_type);
                BetTypes current_bet_type = (BetTypes) bet_type_spinner.getSelectedItem();
                try {
                    bets_info = new JSONObject(bets_json_string);
                    JSONArray members_ja = new JSONArray(bets_info.getString("members"));
                    for (int index = 0; index < members_ja.length(); index++) {
                        JSONObject member_info = members_ja.getJSONObject(index);
                        JSONObject member_info_names = new JSONObject(member_info.getString("member"));
                        users_list.add(new Users(member_info.getString("party_play_id"), member_info_names.getString("handicap"), member_info_names.getString("firstname")));
                    }
                    int i = 0;
                    for (Users u : users_list) {
                        JSONArray bet_scores = new JSONArray(bets_info.getString("bet_scores"));
                        if (bet_scores.length() > 0) {
                            for (int index = 0; index < bet_scores.length(); index++) {
                                JSONObject bet_score_info = bet_scores.getJSONObject(index);
                                JSONObject party_play_bet = new JSONObject(bet_score_info.getString("party_play_bet"));
                                if ((current_bet_type.id == Integer.parseInt(party_play_bet.getString("bet_type_id"))) & u.id.matches(party_play_bet.getString("party_play_id"))) {
                                    LayoutInflater inflater = LayoutInflater.from(getContext());
                                    final View item = inflater.inflate(R.layout.score_reg_member_row, main_table, false);
                                    TextView member_name_col = (TextView) item.findViewById(R.id.score_reg_member_name);
                                    TextView handicap_col = (TextView) item.findViewById(R.id.score_reg_member_handicap);
                                    TextView score_col = (TextView) item.findViewById(R.id.score_reg_member_score);
                                    member_name_col.setText(u.firstname);
                                    member_name_col.setTag(u.id);
                                    handicap_col.setText(u.handicap);
                                    score_col.setText(bet_score_info.getString("score"));
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
                                    i = i + 1;
                                }
                            }
                        } else {
                            LayoutInflater inflater = LayoutInflater.from(getContext());
                            final View item = inflater.inflate(R.layout.score_reg_member_row, main_table, false);
                            TextView member_name_col = (TextView) item.findViewById(R.id.score_reg_member_name);
                            TextView handicap_col = (TextView) item.findViewById(R.id.score_reg_member_handicap);
                            TextView score_col = (TextView) item.findViewById(R.id.score_reg_member_score);
                            member_name_col.setText(u.firstname);
                            member_name_col.setTag(u.id);
                            handicap_col.setText(u.handicap);
                            score_col.setText("");
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
                            i = i + 1;
                        }
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
            HttpPut httppost = new HttpPut("http://zoogtech.com/golfapp/public/score-registration/bet");

            try {
                Spinner holes_sp = (Spinner) view_container.findViewById(R.id.score_reg_holes);
                Spinner bet_sp = (Spinner) view_container.findViewById(R.id.score_reg_bet_type);
                TableLayout tv = (TableLayout) view_container.findViewById(R.id.score_reg_party_members_table);
                Holes h1 = (Holes) holes_sp.getSelectedItem();
                BetTypes b1 = (BetTypes) bet_sp.getSelectedItem();
                List<NameValuePair> json = new ArrayList<NameValuePair>();
                json.add(new BasicNameValuePair("hole_id", h1.id+""));
                json.add(new BasicNameValuePair("bet_type_id", b1.id+""));
                json.add(new BasicNameValuePair("party_play_id", party_play_number.toString()));
                for (int index = 0; index < tv.getChildCount(); index++) {
                    TableRow tr = (TableRow) tv.getChildAt(index);
                    TextView member = (TextView) tr.getChildAt(0);
                    TextView score = (TextView) tr.getChildAt(4);
                    String member_id = member.getTag().toString();
                    String member_score = score.getText().toString();
                    json.add(new BasicNameValuePair("scores["+index+"][party_member_id]", member_id));
                    json.add(new BasicNameValuePair("scores["+index+"][score]", member_score));
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
	
	@OnClick(R.id.record_score_btn)
	public void saveRecordScore(Button btn) {
        if (btn.getText().equals(getResources().getString(R.string.jap_record_score))) {
            btn.setText(getResources().getString(R.string.jap_save));
            Spinner sp = (Spinner) view_container.findViewById(R.id.score_reg_holes);
            Spinner sp2 = (Spinner) view_container.findViewById(R.id.score_reg_bet_type);
            sp.getSelectedView().setEnabled(false);
            sp.setEnabled(false);
            sp2.getSelectedView().setEnabled(false);
            sp2.setEnabled(false);
            TableLayout mt = (TableLayout) view_container.findViewById(R.id.score_reg_party_members_table);
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
            btn.setText(getResources().getString(R.string.jap_record_score));
            Spinner sp = (Spinner) view_container.findViewById(R.id.score_reg_holes);
            Spinner sp2 = (Spinner) view_container.findViewById(R.id.score_reg_bet_type);
            sp.getSelectedView().setEnabled(true);
            sp.setEnabled(true);
            sp2.getSelectedView().setEnabled(true);
            sp2.setEnabled(true);
            TableLayout mt = (TableLayout) view_container.findViewById(R.id.score_reg_party_members_table);
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
    public void changeHole(Spinner spinner) {
        new ChangeHole().execute();
    }

    @OnItemSelected(R.id.score_reg_bet_type)
    public void changeBet() {
        new ChangeBetType().execute();
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_bet_scoring_view_for_party_play,
				container, false);
	}

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        main_table = (TableLayout) view.findViewById(R.id.score_reg_party_members_table);
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
