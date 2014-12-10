package com.example.golfapp.gameSettings;

import com.example.golfapp.BaseFragment;
import com.example.golfapp.GameMenuFragment;
import com.example.golfapp.R;
import com.example.golfapp.R.id;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;

public class ProfileRegistrationFragment extends BaseFragment {
    private String retVal = "";
    private ProgressDialog pdialog;
    private boolean success = false;

    private class RegisterCall extends AsyncTask<String, String, String> {

        public RegisterCall() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_registering));
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
            HttpPost httppost = new HttpPost("http://zoogtech.com/golfapp/public/register");
            try {
                JSONObject json = new JSONObject();
                json.put("firstname", fname);
                json.put("lastname", lname);
                json.put("email", email);
                json.put("password", pass);
                json.put("gender", gender);
                json.put("handicap", Integer.parseInt(handicap));
                StringEntity se = new StringEntity(json.toString(), "UTF-8");

                httppost.setEntity(se);
                httppost.setHeader("Content-type", "application/json");

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
                    System.out.println(json.toString());
                    retVal = str;
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
                Toast.makeText(getContext(), getResources().getString(R.string.jap_reg_success), Toast.LENGTH_LONG).show();
                showFragmentAndAddToBackStack(new GameMenuFragment());
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.jap_reg_failed), Toast.LENGTH_LONG).show();
            }
        }
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_profile_registration, container,
				false);
	}

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button login = (Button) view.findViewById(id.register_btn);
        final EditText fname = (EditText) view.findViewById(id.firstname);
        final EditText lname = (EditText) view.findViewById(id.lastname);
        final Spinner gender = (Spinner) view.findViewById(id.gender);
        final Spinner hc = (Spinner) view.findViewById(id.handicap);
        final EditText email = (EditText) view.findViewById(id.email);
        final EditText pass = (EditText) view.findViewById(id.password);
        final EditText pass2 = (EditText) view.findViewById(id.password2);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String fname_val = fname.getText().toString();
                String lname_val = lname.getText().toString();
                String email_val = email.getText().toString();
                String pass_val = pass.getText().toString();
                String pass2_val = pass2.getText().toString();

                if(fname_val.matches("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.jap_enter_firstname), Toast.LENGTH_SHORT).show();
                    return;
                } else if(lname_val.matches("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.jap_enter_lastname), Toast.LENGTH_SHORT).show();
                    return;
                } else if(email_val.matches("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.jap_enter_email), Toast.LENGTH_SHORT).show();
                    return;
                } else if(pass_val.matches("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.jap_enter_pass), Toast.LENGTH_SHORT).show();
                    return;
                } else if(pass2_val.matches("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.jap_enter_pass_again), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!pass_val.matches(pass2_val)) {
                        Toast.makeText(getContext(), getResources().getString(R.string.jap_match_pass), Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        new RegisterCall().execute(fname_val, lname_val, gender.getSelectedItem().toString(), hc.getSelectedItem().toString(), email_val, pass_val);
                        System.out.println(retVal);
                        if(success) {

                        }
                    }
                }

            }
        });
    }
}
