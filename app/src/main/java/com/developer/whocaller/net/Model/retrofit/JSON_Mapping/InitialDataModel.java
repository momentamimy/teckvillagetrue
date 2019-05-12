package com.developer.whocaller.net.Model.retrofit.JSON_Mapping;

import java.util.List;

/**
 * Created by khaled-pc on 4/23/2019.
 */

public class InitialDataModel {
    private List<ItemsResultInitail> tags ;
    private int spamLimit;

    public List<ItemsResultInitail> getTags() {
        return tags;
    }

    public void setTags(List<ItemsResultInitail> tags) {
        this.tags = tags;
    }

    public int getSpamLimit() {
        return spamLimit;
    }

    public void setSpamLimit(int spamLimit) {
        this.spamLimit = spamLimit;
    }
}
