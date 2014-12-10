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
import org.apache.http.client.methods.HttpPost;
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

public class JoinOpenCompetitionFragment extends BaseFragment {
    private String retVal = null;
    private ProgressDialog pdialog;
    private boolean success = false;
    String response2;
    TableLayout main_table;

    private class CompetitionView extends AsyncTask<String, String, String> {

        public CompetitionView() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage("Loading competitions...");
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result_byte = null;
            String result_string = "";
            success = false;

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
            HttpGet httppost = new HttpGet("http://zoogtech.com/golfapp/public/open-competition?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, "UTF-8");
                    System.out.println(result_string);
                    System.out.println("Success!");
                    response2 = result_string;
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
        protected void onPostExecute(String result2) {
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                JSONArray array = null;
                try {
                    array = new JSONArray(result2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = null;
                    try {
                        row = array.getJSONObject(i);
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        final View item = inflater.inflate(R.layout.competition_row_join, main_table, false);
                        TextView course_name_col = (TextView) item.findViewById(R.id.join_name);
                        course_name_col.setText(row.getString("name"));
                        TextView date_col = (TextView) item.findViewById(R.id.join_date);
                        date_col.setText(row.getString("date"));
                        String deadline = row.getString("registration_deadline");
                        deadline = deadline.substring(0, Math.min(deadline.length(), 10));
                        TextView deadline_col = (TextView) item.findViewById(R.id.join_deadline);
                        deadline_col.setText(deadline);
                        if(i % 2 == 0) {
                            item.setBackgroundColor(Color.WHITE);
                        } else {
                            item.setBackgroundColor(Color.LTGRAY);
                        }
                        ImageView view_btn = (ImageView) item.findViewById(R.id.join_competition);
                        view_btn.setOnClickListener(join_comp);
                        view_btn.setTag(row.getString("id"));
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

    private class JoinCompetition extends AsyncTask<String, String, String> {
        public JoinCompetition() {
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
            byte[] result = null;
            String result2 = "";

            byte[] result_2 = null;
            String result2_2 = "";

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

            /** Getting Cache Directory */
            File cDir2 = getActivity().getCacheDir();

            /** Getting a reference to temporary file, if created earlier */
            File tempFile2 = new File(cDir2.getPath() + "/" + "competition_number.txt") ;

            String strLine2="";
            StringBuilder comp_number = new StringBuilder();

            /** Reading contents of the temporary file, if already exists */
            try {
                FileReader fReader = new FileReader(tempFile2);
                BufferedReader bReader = new BufferedReader(fReader);

                /** Reading the contents of the file , line by line */
                while( (strLine2=bReader.readLine()) != null  ){
                    comp_number.append(strLine2);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://zoogtech.com/golfapp/public/open-competition/"+comp_number.toString()+"/join");

            try {
                httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
                httppost.setHeader("Authorization", golfapp_token.toString());
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_2 = EntityUtils.toByteArray(response.getEntity());
                    result2_2 = new String(result_2, "UTF-8");
                    System.out.println("Success!");
                    success = true;
                }else {
                    result_2 = EntityUtils.toByteArray(response.getEntity());
                    result2_2 = new String(result_2, "UTF-8");
                    System.out.println("Failed!");
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
                builder.setTitle("Join Successful").setMessage("Please wait for join approval.").setPositiveButton("Okay", dialogClickListener).show();
            } else {
                Toast.makeText(getActivity(), "Unable to join. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_open_competition, container, false);
        main_table = (TableLayout) view.findViewById(R.id.competition_table);

        new CompetitionView().execute();

        return view;
	}

    private View.OnClickListener join_comp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new JoinCompetition().execute();
        }
    };
}
