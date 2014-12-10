package src.com.example.golfapp.gameSettings.competition;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.R;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
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

public class ApproveOpenCompetitionFragment extends BaseFragment {
    private ProgressDialog pdialog;
    private boolean success = false;
    String competitors_json;
    TableLayout main_table;


    private class CompetitionView extends AsyncTask<String, String, String> {

        public CompetitionView() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pdialog.setMessage("Loading courses...");
//            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result_byte = null;
            String result_string = "";

            File cDir = getActivity().getCacheDir();
            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt") ;

            String strLine="";
            StringBuilder text = new StringBuilder();
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
            File cDir2 = getActivity().getCacheDir();
            File tempFile2 = new File(cDir2.getPath() + "/" + "competition_number.txt") ;

            String strLine2="";
            StringBuilder comp_number = new StringBuilder();
            try {
                FileReader fReader = new FileReader(tempFile2);
                BufferedReader bReader = new BufferedReader(fReader);
                while( (strLine2=bReader.readLine()) != null  ){
                    comp_number.append(strLine2);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://zoogtech.com/golfapp/public/open-competition/competitors?access_token="+text.toString());

            try {
                HttpResponse response = httpclient.execute(httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, "UTF-8");
                    System.out.println(result_string);
                    System.out.println("Success!");
                    competitors_json = result_string;
                    success = true;
                }else {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, "UTF-8");
                    System.out.println(result_string);
                    System.out.println("Failed!");
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
        protected void onPostExecute(String result) {
//            super.onPostExecute(result2);
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                JSONArray competitors_ja = null;
                try {
                    competitors_ja = new JSONArray(competitors_json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < competitors_ja.length(); i++) {
                    JSONObject competitor_row = null;
                    try {
                        competitor_row = competitors_ja.getJSONObject(i);
                        JSONObject open_comp_json = new JSONObject(competitor_row.getString("open_competition"));
                        JSONObject member_json = new JSONObject(competitor_row.getString("member"));
                        LayoutInflater inflater = LayoutInflater.from(getActivity());
                        final View item = inflater.inflate(R.layout.competition_row_approve, main_table, false);
                        TextView comp_name_col = (TextView) item.findViewById(R.id.approve_name);
                        comp_name_col.setText(open_comp_json.getString("name"));
                        TextView group_col = (TextView) item.findViewById(R.id.approve_group);
                        group_col.setText("Group " + competitor_row.getString("open_competition_group_id"));
                        TextView mem_col = (TextView) item.findViewById(R.id.approve_member);
                        mem_col.setText(member_json.getString("firstname"));
                        TextView approve_col = (TextView) item.findViewById(R.id.approve_status);
                        if (competitor_row.getString("approved") == "0") {
                            approve_col.setText("Pending");
                        } else {
                            approve_col.setText("Approved");
                        }
                        if(i % 2 == 0) {
                            item.setBackgroundColor(Color.WHITE);
                        } else {
                            item.setBackgroundColor(Color.LTGRAY);
                        }
                        ImageView view_btn = (ImageView) item.findViewById(R.id.approve_competition);
                        view_btn.setOnClickListener(approve_comp);
                        view_btn.setTag(competitor_row.getString("id"));
                        main_table.addView(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getContext(), "Courses loaded.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ApproveCompetition extends AsyncTask<String, String, String> {
        public ApproveCompetition() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            success = false;
            pdialog.setMessage("Joining competition...");
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String comp_id = strings[0];
            byte[] result_byte = null;
            String result_string = "";

            /** Getting Cache Directory */
            File cDir = getActivity().getCacheDir();

            /** Getting a reference to temporary file, if created earlier */
            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt") ;

            String strLine="";
            StringBuilder golfapp_token = new StringBuilder();

            /** Reading contents of the temporary file, if already exists */
            try {
                FileReader fReader = new FileReader(tempFile);
                BufferedReader bReader = new BufferedReader(fReader);

                /** Reading the contents of the file , line by line */
                while( (strLine=bReader.readLine()) != null  ){
                    golfapp_token.append(strLine);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            HttpClient httpclient_2 = new DefaultHttpClient();
            HttpPut httppost_2 = new HttpPut("http://zoogtech.com/golfapp/public/open-competition/approve/"+comp_id+"?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = httpclient_2.execute(httppost_2);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, "UTF-8");
                    System.out.println("Success!");
                    success = true;
                }else {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, "UTF-8");
                    System.out.println("Failed!");
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
//                Toast.makeText(getActivity(), "Loading Complete.", Toast.LENGTH_SHORT).show();
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Successful").setMessage("Join application approved.").setPositiveButton("Okay", dialogClickListener).show();
            } else {
                Toast.makeText(getActivity(), "Unable to approve. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_approve_open_competitions, container, false);
        main_table = (TableLayout) view.findViewById(R.id.competition_table);

        new CompetitionView().execute();

        return view;
	}

    private View.OnClickListener approve_comp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new ApproveCompetition().execute(view.getTag().toString());
        }
    };
}
