package com.hazelcast.cloud;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.spi.impl.discovery.HazelcastCloudDiscovery;
import com.hazelcast.client.spi.properties.ClientProperty;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.SSLConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import java.util.Properties;
import java.io.File;
import java.util.Random;


/**
 *
 * This is boilerplate application that configures client to connect Hazelcast Cloud cluster.
 * After successful connection, it puts random entries into the map.
 *
 * See: https://docs.hazelcast.cloud/docs/java-client
 *
 */
public class ClientWithSsl {

    public static void main(String[] args) throws InterruptedException {
      ClassLoader classLoader = ClientWithSsl.class.getClassLoader();
      Properties props = new Properties();
      props.setProperty("javax.net.ssl.keyStore", classLoader.getResource("client.keystore").getPath());
      props.setProperty("javax.net.ssl.keyStorePassword", "YOUR_SSL_PASSWORD");
      props.setProperty("javax.net.ssl.trustStore", classLoader.getResource("client.truststore").getPath());
      props.setProperty("javax.net.ssl.trustStorePassword", "YOUR_SSL_PASSWORD");
      ClientConfig config = new ClientConfig();
      config.getNetworkConfig().setSSLConfig(new SSLConfig().setEnabled(true).setProperties(props));
      config.setGroupConfig(new GroupConfig("YOUR_CLUSTER_NAME", "YOUR_CLUSTER_PASSWORD"));
      config.setProperty("hazelcast.client.statistics.enabled","true");
      config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "YOUR_CLUSTER_DISCOVERY_TOKEN");
      config.setProperty(HazelcastCloudDiscovery.CLOUD_URL_BASE_PROPERTY.getName(), "YOUR_DISCOVERY_URL");
      HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
      IMap<String, String> map = client.getMap("map");
      map.put("key", "value");
      if(map.get("key").equals("value")) {
        System.out.println("Connection Successful!");
        System.out.println("Now, `map` will be filled with random entries.");
      }
      else {
        throw new RuntimeException("Connection failed, check your configuration.");
      }
      Random random = new Random();
      while (true) {
        int randomKey = (int) random.nextInt(100_000);
        map.put("key" + randomKey, "value" + randomKey);
        map.get("key" + random.nextInt(100_000));
        if(randomKey % 10 == 0 ) {
          System.out.println("map size:" + map.size());
        }
      }
    }

}
