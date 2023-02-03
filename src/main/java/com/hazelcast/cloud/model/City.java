package com.hazelcast.cloud.model;

public final class City {

    private final String country;

    private final String city;

    private final int population;

    public City(String country, String city, int population) {
        this.country = country;
        this.city = city;
        this.population = population;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public int getPopulation() {
        return population;
    }

}
