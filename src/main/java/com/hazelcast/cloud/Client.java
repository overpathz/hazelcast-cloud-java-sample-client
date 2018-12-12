package com.hazelcast.cloud;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.spi.impl.discovery.HazelcastCloudDiscovery;
import com.hazelcast.client.spi.properties.ClientProperty;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.SSLConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class Client {

    public static void main(String[] args) {
        ClientConfig config = new ClientConfig();
        config.setGroupConfig(new GroupConfig("YOUR_CLUSTER_NAME", "YOUR_CLUSTER_PASSWORD"));
        config.setProperty("hazelcast.client.statistics.enabled","true");
        config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "YOUR_CLUSTER_DISCOVERY_TOKEN");
        config.setProperty(HazelcastCloudDiscovery.CLOUD_URL_BASE_PROPERTY.getName(), "YOUR_DISCOVERY_URL");
        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
        IMap<String, String> map = client.getMap("map");
        map.put("key", "value");
        System.out.println("Value:"+ map.get("key"));
    }

}
