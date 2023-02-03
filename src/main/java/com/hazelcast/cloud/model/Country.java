package com.hazelcast.cloud.model;

public final class Country {

    private final String isoCode;

    private final String country;

    public Country(String isoCode, String country) {
        this.isoCode = isoCode;
        this.country = country;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getCountry() {
        return country;
    }

}
