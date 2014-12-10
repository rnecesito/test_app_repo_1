package com.example.golfapp.counting;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.golfapp.R;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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
import java.util.ArrayList;

public class BetScoreCountingFragment extends Fragment {
    private ProgressDialog pdialog;
    private boolean success = false;
    String party_string;
    String score_json_string;
    String party_id;
    View view_container;
    ArrayList<Parties> party_list;

    private class Parties {
        int id = 0;
        String name = "";

        public Parties(int _id, String _name) {
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
            pdialog.setMessage(getResources().getString(R.string.jap_loading_parties));
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result_byte = null;
            String result_string = "";
            byte[] score_byte = null;
            String score_string = "";

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

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://zoogtech.com/golfapp/public/party-play?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = httpclient.execute(httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, "UTF-8");
                    party_string = result_string;
                    success = true;
                }else {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, "UTF-8");
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
        protected void onPostExecute(String result2) {
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                JSONArray array = null;
                try {
                    array = new JSONArray(party_string);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Spinner spinner = (Spinner) view_container.findViewById(R.id.score_count_party_spinner3);
                ArrayList<Parties> party_list = new ArrayList<Parties>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = null;
                    try {
                        row = array.getJSONObject(i);
                        party_list.add(new Parties(Integer.parseInt(row.getString("id")), row.getString("name")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, party_list);
                spinner.setAdapter(spinnerArrayAdapter);
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_info_loaded), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetScores extends AsyncTask<String, String, String> {

        public GetScores() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pdialog.setMessage(getResources().getString(R.string.jap_loading_parties));
//            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String pid = strings[0];
            byte[] score_byte = null;
            String score_string = "";

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

            HttpClient party_httpclient = new DefaultHttpClient();
            HttpGet party_httpget = new HttpGet("http://zoogtech.com/golfapp/public/counting/bet/"+pid+"?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = party_httpclient.execute(party_httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    score_byte = EntityUtils.toByteArray(response.getEntity());
                    score_string = new String(score_byte, "UTF-8");
                    System.out.println(score_string);
                    score_json_string = score_string;
                    success = true;
                }else {
                    score_byte = EntityUtils.toByteArray(response.getEntity());
                    score_string = new String(score_byte, "UTF-8");
                    System.out.println(score_string);
                    score_json_string = score_string;
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
        protected void onPostExecute(String result2) {
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                JSONArray array = null;
                try {
                    array = new JSONArray(score_json_string);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TableLayout main_table = (TableLayout) view_container.findViewById(R.id.score_count_party_members);
                main_table.removeAllViews();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = null;
                    try {
                        row = array.getJSONObject(i);
                        LayoutInflater inflater = LayoutInflater.from(getActivity());
                        final View item = inflater.inflate(R.layout.bet_counting_row, main_table, false);
                        TextView player_name_col = (TextView) item.findViewById(R.id.bet_row_name);
                        player_name_col.setText(row.getString("name"));
                        TextView gross_col = (TextView) item.findViewById(R.id.bet_row_operand);
                        gross_col .setText(row.getString("operand"));
                        TextView net_col = (TextView) item.findViewById(R.id.bet_row_score);
                        net_col.setText(row.getString("score"));
                        if(i % 2 == 0) {
                            item.setBackgroundColor(Color.WHITE);
                        } else {
                            item.setBackgroundColor(Color.LTGRAY);
                        }
                        main_table.addView(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_info_loaded), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bet_score_counting, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        new InitLists().execute();

        final Spinner sp = (Spinner) view.findViewById(R.id.score_count_party_spinner3);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view2, int i, long l) {
                Parties p = (Parties) adapterView.getSelectedItem();
                if (adapterView.getSelectedItem() != null) {
                    System.out.println(p.id+"");
                    new GetScores().execute(p.id+"");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
