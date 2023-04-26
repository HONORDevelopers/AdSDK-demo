package com.hihonor.ads.group.sdk.base.net.core;


public class GrsConfig {
    private String bizName;
    private String grsKey;
    private String grsServerName;
    private ICountryProvider countryProvider;

    public GrsConfig(String bizName, String grsServerName, String grsKey, ICountryProvider countryProvider) {
        this.bizName = bizName;
        this.grsServerName = grsServerName;
        this.grsKey = grsKey;
        this.countryProvider = countryProvider;
    }

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

    public String getGrsKey() {
        return grsKey;
    }

    public void setGrsKey(String grsKey) {
        this.grsKey = grsKey;
    }

    public String getGrsServerName() {
        return grsServerName;
    }

    public void setGrsServerName(String grsServerName) {
        this.grsServerName = grsServerName;
    }

    public ICountryProvider getCountryProvider() {
        return countryProvider;
    }

    public void setCountryProvider(ICountryProvider countryProvider) {
        this.countryProvider = countryProvider;
    }
}
