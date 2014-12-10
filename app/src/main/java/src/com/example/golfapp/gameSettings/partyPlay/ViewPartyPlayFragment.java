package com.example.golfapp.gameSettings.partyPlay;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.R;
import com.example.golfapp.R.id;
import com.example.golfapp.R.layout;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ViewPartyPlayFragment extends BaseFragment {
    private ProgressDialog pdialog;
    private boolean success = false;
    String party_string;
    TableLayout main_table;

    private class PartyView extends AsyncTask<String, String, String> {

        public PartyView() {
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

            return result_string;
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
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = null;
                    try {
                        row = array.getJSONObject(i);
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        final View item = inflater.inflate(layout.party_play_row, main_table, false);
                        TextView course_name_col = (TextView) item.findViewById(R.id.party_name2);
                        course_name_col.setText(row.getString("name"));
                        TextView holes_col = (TextView) item.findViewById(R.id.party_date2);
                        holes_col.setText(row.getString("date"));
                        if(i % 2 == 0) {
                            item.setBackgroundColor(Color.WHITE);
                        } else {
                            item.setBackgroundColor(Color.LTGRAY);
                        }
                        ImageView edit_btn = (ImageView) item.findViewById(R.id.edit3);
                        edit_btn.setOnClickListener(listener);
                        edit_btn.setTag(row.getString("id"));
                        main_table.addView(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(getContext(), getResources().getString(R.string.jap_parties_loaded), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater
				.inflate(R.layout.fragment_view_party_play, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
        main_table = (TableLayout) view.findViewById(R.id.party_table);
        new PartyView().execute();
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
            final String TEMP_FILE_NAME = "party_play_number.txt";
            File tempFile;
            File cDir = getActivity().getCacheDir();
            tempFile = new File(cDir.getPath() + "/" + TEMP_FILE_NAME) ;
            FileWriter writer=null;
            try {
                writer = new FileWriter(tempFile);
                writer.write(v.getTag()+"");
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            /** End save */
			showFragmentAndAddToBackStack(new EditPartyPlayFragment());
		}
	};
}
