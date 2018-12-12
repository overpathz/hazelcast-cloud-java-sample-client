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

public class ClientWithSsl {

    public static void main(String[] args) {
      ClassLoader classLoader = ClientWithSsl.class.getClassLoader();
      Properties props = new Properties();
      props.setProperty("javax.net.ssl.keyStore", classLoader.getResource("client.keystore").getPath());
      props.setProperty("javax.net.ssl.keyStorePassword", "YOUR_SSL_PASSWORD");
      props.setProperty("javax.net.ssl.trustStore", classLoader.getResource("client.truststore").getPath());
      props.setProperty("javax.net.ssl.trustStorePassword", "YOUR_SSL_PASSWORD");
      ClientConfig config = new ClientConfig();
      config.getNetworkConfig().setSSLConfig(new SSLConfig().setEnabled(true).setProperties(props));
      config.getGroupConfig().setName("YOUR_CLUSTER_NAME").setPassword("YOUR_CLUSTER_PASSWORD");
      config.setProperty("hazelcast.client.statistics.enabled","true");
      config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "YOUR_CLUSTER_DISCOVERY_TOKEN");
      config.setProperty(HazelcastCloudDiscovery.CLOUD_URL_BASE_PROPERTY.getName(), "YOUR_DISCOVERY_URL");
      HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
      IMap<String, String> map = client.getMap("map");
      map.put("key", "value");
      System.out.println("Value: "+ map.get("key"));
    }

}
