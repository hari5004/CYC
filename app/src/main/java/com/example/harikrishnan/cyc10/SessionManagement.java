package com.example.harikrishnan.cyc10;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

/**
 * Created by harikrishnan on 03/01/17.
 */

public class SessionManagement {
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        Context _context;
        int PRIVATE_MODE = 0;
        private static final String PREF_NAME = "Logindetails";
        private static final String IS_LOGIN = "IsLoggedIn";
        public static final String KEY_NAME = "name";
        public static final String KEY_EMAIL = "email";
        public SessionManagement(Context context){
            this._context = context;
            pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        }


        public void createLoginSession(String name, String email){
            // Storing login value as TRUE
            editor.putBoolean(IS_LOGIN, true);

            // Storing name in pref
            editor.putString(KEY_NAME, name);

            // Storing email in pref
            editor.putString(KEY_EMAIL, email);

            // commit changes
            editor.commit();
        }

        /**
         * Check login method wil check user login status
         * If false it will redirect user to login page
         * Else won't do anything
         * */
        public void checkLogin(){
            // Check login status
            if(!this.isLoggedIn()){
                // user is not logged in redirect him to Login Activity
                Intent i = new Intent(_context, MainLoginActivity.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Staring Login Activity
                _context.startActivity(i);
            }

        }



        /**
         * Get stored session data
         * */
        public HashMap<String, String> getUserDetails(){
            HashMap<String, String> user = new HashMap<String, String>();
            // user name
            user.put(KEY_NAME, pref.getString(KEY_NAME, null));

            // user email id
            user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

            // return user
            return user;
        }

        /**
         * Clear session details
         * */
        public void logoutUser(){
            // Clearing all data from Shared Preferences
            editor.clear();
            editor.commit();

            // After logout redirect user to Loing Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }


        public boolean isLoggedIn(){
            return pref.getBoolean(IS_LOGIN, false);
        }

}
