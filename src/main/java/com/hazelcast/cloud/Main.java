package com.hazelcast.cloud;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.spi.impl.discovery.HazelcastCloudDiscovery;
import com.hazelcast.client.spi.properties.ClientProperty;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.SSLConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class Main {

    public static void main(String[] args) {
        ClientConfig config = new ClientConfig();
        config.setProperty("hazelcast.client.statistics.enabled","true");
        config.setGroupConfig(new GroupConfig("YOUR_CLUSTER_NAME", "YOUR_CLUSTER_PASSWORD"));
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "YOUR_CLUSTER_DISCOVERY_TOKEN");
        config.getNetworkConfig().setSSLConfig(new SSLConfig().setEnabled(true));
        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
        IMap<String, String> map = client.getMap("theGenius");
        map.put("vaccination", "Edward Jenner");
        map.put("alternating current motor", "Nikola Tesla");
        map.put("polonium and radium", "Marie Curie");
        System.out.println("You have "+ map.size() +" people in your genius map.");
    }

}
