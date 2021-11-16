package com.hazelcast.cloud;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.cloud.model.City;
import com.hazelcast.cloud.model.CityCompactSerializer;
import com.hazelcast.cloud.model.Country;
import com.hazelcast.cloud.model.CountryCompactSerializer;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.sql.SqlResult;
import com.hazelcast.sql.SqlService;

import static com.hazelcast.client.properties.ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN;
import static com.hazelcast.client.properties.ClientProperty.STATISTICS_ENABLED;

public class ClientWithSql {

    public static void main(String[] args) {
        ClientConfig config = new ClientConfig();
        config.setProperty(STATISTICS_ENABLED.getName(), "true");
        config.setProperty(HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "YOUR_CLUSTER_DISCOVERY_TOKEN");
        config.setProperty("hazelcast.client.cloud.url", "YOUR_DISCOVERY_URL");
        config.setClusterName("YOUR_CLUSTER_NAME");
        config.getSerializationConfig()
            .getCompactSerializationConfig()
            .setEnabled(true)
            .register(City.class, "city", new CityCompactSerializer())
            .register(Country.class, "country", new CountryCompactSerializer());
        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);

        createMappingForCountries(client);
        populateCountriesWithMap(client);

        populateCities(client);
        createMappingForCities(client);

        selectAllCountries(client);
        selectCitiesByCountry(client, "AU");

        System.out.println("Done");
    }

    private static void selectCitiesByCountry(HazelcastInstance client, String country) {
        String sql = "SELECT city, population FROM city where country=?";
        System.out.println("sql = " + sql);
        try (SqlResult result = client.getSql().execute(sql, country)) {
            result.forEach(row ->
                System.out.printf("city=%s, population=%s%n", row.getObject("city"), row.getObject("population"))
            );
        }
    }

    private static void selectAllCountries(HazelcastInstance client) {
        String sql = "SELECT c.country from country c";
        System.out.println("sql = " + sql);
        try (SqlResult result = client.getSql().execute(sql)) {
            result.forEach(row -> System.out.println("country=" + row.getObject("country")));
        }
    }

    private static void populateCountriesWithMap(HazelcastInstance client) {
        System.out.println("Populate Countries with map");
        IMap<String, Country> countries = client.getMap("country");
        countries.put("AU", Country.newCountry("AU", "Australia"));
        countries.put("EN", Country.newCountry("EN", "England"));
        countries.put("US", Country.newCountry("US", "United States"));
        countries.put("CZ", Country.newCountry("US", "Czech Republic"));
    }

    private static void populateCountriesWithSql(HazelcastInstance client) {
        System.out.println("Populate Countries with sql");
        SqlService sql = client.getSql();
        try (SqlResult result = sql.execute("INSERT INTO country (__key, isoCode, country) VALUES ('PL', 'PL', 'Poland')")) {
            if (result.updateCount() == 1) {
                System.out.println("Country has been added [isoCode='PL']");
            } else {
                System.out.println("Failed to add country via sql");
            }
        }
    }


    private static void createMappingForCountries(HazelcastInstance client) {
        String mappingSql = ""
            + "CREATE OR REPLACE MAPPING country("
            + " isoCode VARCHAR,"
            + " country VARCHAR)"
            + " TYPE IMap"
            + " OPTIONS ("
            + "     'keyFormat' = 'java',"
            + "     'keyJavaClass' = 'java.lang.String',"
            + "     'valueFormat' = 'compact',"
            + "     'valueCompactTypeName' = 'country'"
            + " )";

        try (SqlResult rs = client.getSql().execute(mappingSql)) {
            rs.updateCount();
            System.out.println("Mapping for countries has been created");
        }
    }

    private static void populateCities(HazelcastInstance client) {
        System.out.println("Populate cities");
        IMap<Integer, City> cities = client.getMap("city");
        cities.put(1, City.newCity("AU", "Canberra", 354644));
        cities.put(2, City.newCity("CZ", "Prague", 1227332));
        cities.put(3, City.newCity("EN", "London", 8174100));
        cities.put(4, City.newCity("US", "Washington, DC", 601723));
    }


    private static void createMappingForCities(HazelcastInstance client) {
        String mappingSql = ""
            + "CREATE OR REPLACE MAPPING city("
            + " country VARCHAR ,"
            + " city VARCHAR,"
            + " population INT)"
            + " TYPE IMap"
            + " OPTIONS ("
            + "     'keyFormat' = 'java',"
            + "     'keyJavaClass' = 'java.lang.Integer',"
            + "     'valueFormat' = 'compact',"
            + "     'valueCompactTypeName' = 'city'"
            + " )";

        try (SqlResult rs = client.getSql().execute(mappingSql)) {
            rs.updateCount();
            System.out.println("Mapping for cities has been created");
        }
    }

}
