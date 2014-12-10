package src.com.example.golfapp.gameSettings;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class UserProfileFragment extends BaseFragment {
    private ProgressDialog pdialog;
    private boolean success = false;
    String retVal;
    View view_container;

    private class GetProfile extends AsyncTask<String, String, String> {

        public GetProfile() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_loading_user_profile));
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result = null;
            String str = "";
            String token = readtoken();
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("http://zoogtech.com/golfapp/public/user/profile?access_token="+token.toString());
            try {
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
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

            return str;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                JSONObject info = null;
                try {
                    info = new JSONObject(retVal);
                    EditText fname = (EditText) view_container.findViewById(id.user_firstname);
                    EditText lname = (EditText) view_container.findViewById(id.user_lastname);
                    Spinner gender = (Spinner) view_container.findViewById(id.user_gender);
                    Spinner hc = (Spinner) view_container.findViewById(id.user_handicap);
                    EditText email = (EditText) view_container.findViewById(id.user_email);
                    EditText passw = (EditText) view_container.findViewById(id.user_pass);
                    fname.setText(info.getString("firstname"));
                    lname.setText(info.getString("lastname"));
                    email.setText(info.getString("email"));
                    passw.setText(info.getString("password"), TextView.BufferType.EDITABLE);
                    if(info.getString("gender").matches("Male") || info.getString("gender").matches("M")) {
                        gender.setSelection(0);
                    } else {
                        gender.setSelection(1);
                    }
                    hc.setSelection(Integer.parseInt(info.getString("handicap")) - 1);

                    /** Saving user email for comparison later */
                    final String TEMP_FILE_NAME = "golfapp_user_email.txt";
                    File tempFile;
                    /** Getting Cache Directory */
                    File cDir = getActivity().getCacheDir();
                    /** Getting a reference to temporary file, if created earlier */
                    tempFile = new File(cDir.getPath() + "/" + TEMP_FILE_NAME) ;
                    FileWriter writer=null;
                    try {
                        writer = new FileWriter(tempFile);

                        /** Saving the contents to the file*/
                        writer.write(info.getString("email"));

                        /** Closing the writer object */
                        writer.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    /** End save */

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Toast.makeText(getContext(), "Data ", Toast.LENGTH_LONG).show();
            } else {
//                Toast.makeText(getContext(), "Update failed. Please try again.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class UpdateCall extends AsyncTask<String, String, String> {

        public UpdateCall() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_updating_profile));
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String fname = strings[0];
            String lname = strings[1];
            String gender = strings[2];
            String handicap = strings[3];
            String email = strings[4];
            String pass = strings[5];
            byte[] result = null;
            String str = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httppost = new HttpPut("http://zoogtech.com/golfapp/public/user/profile");
            try {
                List<NameValuePair> json1 = new ArrayList<NameValuePair>(6);
                json1.add(new BasicNameValuePair("firstname", fname));
                json1.add(new BasicNameValuePair("lastname", lname));
                json1.add(new BasicNameValuePair("email", email));
                json1.add(new BasicNameValuePair("gender", gender));
                json1.add(new BasicNameValuePair("handicap", handicap));
                if(!pass.matches("")) {
                    json1.add(new BasicNameValuePair("password", pass));
                }

                httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
                httppost.setHeader("Authorization", readtoken());
                httppost.setEntity(new UrlEncodedFormEntity(json1));
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
                    System.out.println(json1.toString());
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
                Toast.makeText(getContext(), getResources().getString(R.string.jap_profile_updated), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.jap_profile_update_failed), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String readtoken() {
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
        return text.toString();
    }

    private String reademail() {
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
        return text.toString();
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_profile_user, container,
				false);
	}

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        new GetProfile().execute();
        Button update = (Button) view.findViewById(id.update_profile_btn);
        final EditText fname = (EditText) view.findViewById(id.user_firstname);
        final EditText lname = (EditText) view.findViewById(id.user_lastname);
        final Spinner gender = (Spinner) view.findViewById(id.user_gender);
        final Spinner hc = (Spinner) view.findViewById(id.user_handicap);
        final EditText email = (EditText) view.findViewById(id.user_email);
        final EditText pass = (EditText) view.findViewById(id.user_pass);
        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String fname_val = fname.getText().toString();
                String lname_val = lname.getText().toString();
                String email_val = email.getText().toString();
                String pass_val = pass.getText().toString();
                if(fname_val.matches("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.jap_enter_firstname), Toast.LENGTH_SHORT).show();
                    return;
                } else if(lname_val.matches("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.jap_enter_lastname), Toast.LENGTH_SHORT).show();
                    return;
                } else if(email_val.matches("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.jap_enter_email), Toast.LENGTH_SHORT).show();
                    return;
//                } else if(pass_val.matches("")) {
//                    Toast.makeText(getContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
//                    return;
                } else {
                    new UpdateCall().execute(fname_val, lname_val, gender.getSelectedItem().toString(), hc.getSelectedItem().toString(), email_val, pass_val);
                }

            }
        });
    }
}
