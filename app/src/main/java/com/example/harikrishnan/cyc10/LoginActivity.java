package com.example.harikrishnan.cyc10;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static android.R.attr.data;
import static com.example.harikrishnan.cyc10.R.string.login;
public class LoginActivity extends AppCompatActivity {
    Context context=this;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = new SessionManagement(getApplicationContext());

        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
        }
    public void login(View view)
    {
        final EditText emailValidate = (EditText) findViewById(R.id.email);
        final EditText password =(EditText)findViewById(R.id.password);
        final EditText mobilenum=(EditText)findViewById(R.id.mobilenum);
        final EditText username=(EditText)findViewById(R.id.username);



        String email = emailValidate.getText().toString().trim();
        String pawd=password.getText().toString().trim();
        String uname=username.getText().toString();
        String mobilen=mobilenum.getText().toString();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        //emailValidate .addTextChangedListener(new TextWatcher() {
        //  public void afterTextChanged(Editable s) {

        if (email.matches(emailPattern)&& password.getText().toString().trim().length()!=0 ) {

            validateUserTask task = new validateUserTask(context);
            session.createLoginSession(uname, email);
            task.execute(new String[]{email, uname,pawd,mobilen});


        } else {
            Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();

        }
    }
    }
class validateUserTask extends AsyncTask<String, Void, String> {
    ProgressDialog pDialog;
    OkHttpClient client;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String credentials;
    private Context mcontext;
    String json2;
    public validateUserTask(Context context) {
        mcontext = context;
    }

    @Override
    protected void onPreExecute()

    {
        pDialog = ProgressDialog.show(mcontext, "", "Please Wait", false);
    }

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        JSONObject post_dict = new JSONObject();

        String res = null;
        try {
            post_dict.put("userName", params[0]);
            post_dict.put("emailID", params[1]);
            post_dict.put("password", params[2]);
            post_dict.put("mobileNum", params[3]);
            post_dict.put("currentToken", "sample");


        } catch (JSONException e) {
        }
        final String jsonString = post_dict.toString();
        client = new OkHttpClient();
        this.credentials = Credentials.basic("admin", "admin");
        try {
            RequestBody body = RequestBody.create(JSON, jsonString);
            Request request = new Request.Builder()
                    .url("http://cyc-blazemits.rhcloud.com/accountAuth/signup/")
                    .header("Authorization", credentials)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
             json2 = response.body().string();


        } catch (IOException e) {
        }
        return json2;

    }



    @Override
    protected void onPostExecute(String result) {
        String loginid="";
        pDialog.dismiss();
        try{
            JSONArray jsonarray = new JSONArray(result);

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
               loginid = jsonobject.getString("id");
            }
            //Snackbar.make(view,login, Snackbar.LENGTH_LONG).show();
        }
        catch (JSONException je){}
        mcontext.startActivity(new Intent(mcontext, MainActivity.class));
        if(!loginid.equals("")){
            //navigate to Main Menu
            mcontext.startActivity(new Intent(mcontext, MainActivity.class));
        }
        else{
            Toast toast=Toast.makeText(mcontext,"Succefully loginid",Toast.LENGTH_LONG);
            //txt_Error.setText("Sorry!! Incorrect Username or Password");
        }
    }//close onPostExecute
}
