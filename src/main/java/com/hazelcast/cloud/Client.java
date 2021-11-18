package com.hazelcast.cloud;

import java.util.Random;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.sql.SqlResult;
import com.hazelcast.sql.SqlRow;

import static com.hazelcast.client.properties.ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN;
import static com.hazelcast.client.properties.ClientProperty.STATISTICS_ENABLED;

/**
 * This is boilerplate application that configures client to connect Hazelcast Cloud cluster.
 * <p>
 * See: <a href="https://docs.cloud.hazelcast.com/docs/java-client">https://docs.cloud.hazelcast.com/docs/java-client</a>
 */
public class Client {

    public static void main(String[] args) {
        ClientConfig config = new ClientConfig();
        config.setProperty(STATISTICS_ENABLED.getName(), "true");
        config.setProperty(HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "YOUR_CLUSTER_DISCOVERY_TOKEN");
        config.setProperty("hazelcast.client.cloud.url", "YOUR_DISCOVERY_URL");
        config.setClusterName("YOUR_CLUSTER_NAME");
        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);

        System.out.println("Connection Successful!");

        // the 'mapExample' is an example with an infinite loop inside, so if you'd like to try other examples,
        // don't forget to comment out the following line
        mapExample(client);

        //sqlExample(client);
    }

    /**
     * This example shows how to work with Hazelcast maps.
     *
     * @param client - a {@link HazelcastInstance} client.
     */
    private static void mapExample(HazelcastInstance client) {
        System.out.println("Now the map named 'map' will be filled with random entries.");

        IMap<String, String> map = client.getMap("map");
        Random random = new Random();
        int iterationCounter = 0;
        while (true) {
            int randomKey = random.nextInt(100_000);
            map.put("key-" + randomKey, "value-" + randomKey);
            map.get("key-" + random.nextInt(100_000));
            if (++iterationCounter == 10) {
                iterationCounter = 0;
                System.out.println("Current map size: " + map.size());
            }
        }
    }

    /**
     * This example shows how to work with Hazelcast SQL queries.
     *
     * @param client - a {@link HazelcastInstance} client.
     */
    private static void sqlExample(HazelcastInstance client) {

        System.out.println("Creating a mapping...");
        // See: https://docs.hazelcast.com/hazelcast/5.0/sql/mapping-to-maps
        try (SqlResult ignored = client.getSql().execute(
            "CREATE MAPPING cities TYPE IMap OPTIONS (" +
                "'keyFormat' = 'java'," +
                "'keyJavaClass' = 'java.lang.String'," +
                "'valueFormat' = 'java'," +
                "'valueJavaClass' = 'java.lang.String')")) {
            System.out.println("The mapping has been created successfully.");
        }

        System.out.println("--------------------");
        System.out.println("Inserting data via SQL...");

        String insertQuery = "INSERT INTO cities VALUES" +
            "('Australia','Canberra')," +
            "('Croatia','Zagreb')," +
            "('Czech Republic','Prague')," +
            "('England','London')," +
            "('Turkey','Ankara')," +
            "('United States','Washington, DC');";
        try (SqlResult ignored = client.getSql().execute(insertQuery)) {
            System.out.println("The data has been inserted successfully.");
        }

        System.out.println("--------------------");
        System.out.println("Retrieving all the data via SQL...");
        try (SqlResult result = client.getSql().execute("SELECT * FROM cities")) {

            for (SqlRow row : result) {
                String country = row.getObject(0);
                String city = row.getObject(1);
                System.out.printf("%s - %s\n", country, city);
            }
        }

        System.out.println("--------------------");
        System.out.println("Retrieving a city name via SQL...");
        try (SqlResult result = client.getSql()
            .execute("SELECT __key, this FROM cities WHERE __key = ?", "United States")) {

            for (SqlRow row : result) {
                String country = row.getObject("__key");
                String city = row.getObject("this");
                System.out.printf("Country name: %s; City name: %s\n", country, city);
            }
        }
        System.out.println("--------------------");
        System.exit(0);
    }

}
