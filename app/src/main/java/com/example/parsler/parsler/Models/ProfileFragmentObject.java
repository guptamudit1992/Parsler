package com.example.parsler.parsler.Models;

import android.content.Context;
import android.databinding.BaseObservable;

import com.example.parsler.parsler.Commons.StringConstants;
import com.example.parsler.parsler.Services.SharedPreferenceService;

public class ProfileFragmentObject extends BaseObservable {

    private String username;
    private String runnerPhone;
    private String rating;
    private String isOnDuty;

    public ProfileFragmentObject(Context context) {
        this.username = SharedPreferenceService.getValue(context, StringConstants.KEY_USERNAME);
        this.runnerPhone = SharedPreferenceService.getValue(context, StringConstants.KEY_RUNNER_MOBILE_NUMBER);
        this.rating = SharedPreferenceService.getValue(context, StringConstants.KEY_RATING);
        this.isOnDuty = SharedPreferenceService.getValue(context, StringConstants.KEY_IS_ONDUTY);
    }


    public String getUsername() {
        return this.username;
    }

    public String getRunnerPhone() {
        return this.runnerPhone;
    }

    public int getRating() {
        return Integer.valueOf(this.rating);
    }

    public boolean getIsOnDuty() {
        return Boolean.valueOf(this.isOnDuty);
    }
}
