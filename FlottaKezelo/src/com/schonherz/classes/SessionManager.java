package com.schonherz.classes;

import java.util.HashMap;

import com.schonherz.flottadroid.LoginActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	
	// Shared Preferences
    SharedPreferences pref;
     
    // Editor for Shared preferences
    Editor editor;
     
    // Context
    Context _context;
     
    // Shared pref mode
    int PRIVATE_MODE = 0;
     
    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";
     
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    
    public static final String KEY_USER_ID="id";
     
    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
     
    /**
     * Create login session
     * */
    public void createLoginSession(Long id){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        
        editor.putLong(KEY_USER_ID, id);
         
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
            Intent i = new Intent(_context, LoginActivity.class);
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
    
    
    public HashMap<String, Long> getUserID(){
        HashMap<String, Long> user = new HashMap<String, Long>();
        
        user.put(KEY_USER_ID, pref.getLong(KEY_USER_ID, 0));        
         
        // return user
        return user;
    }
     
    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
    	editor.remove(KEY_USER_ID);
    	editor.remove(IS_LOGIN);
    	editor.remove(PREF_NAME);
        editor.commit();
        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         
        // Starting Login Activity
        _context.startActivity(i);
    }
    
    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

}
