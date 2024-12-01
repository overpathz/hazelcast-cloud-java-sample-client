# Sample Java Client for Hazelcast Cloud Community with SSL

This repository provides a sample Java client application configured to securely connect to a Hazelcast Cloud Community cluster using SSL/TLS. It demonstrates how to set up and use the Hazelcast Java client with SSL to interact with a cloud-based Hazelcast cluster.

## Prerequisites

- **Java Development Kit (JDK) 17 or newer**: Ensure that JDK 17 or a more recent version is installed on your system.
- **Maven**: This project uses Maven for dependency management and build automation.

## Getting Started

1. **Create a Hazelcast Cloud Account**:

   If you don’t already have an account, visit [Hazelcast Cloud](https://cloud.hazelcast.com/) to create a free development test account. This will allow you to set up a cluster for testing purposes.

2. **Download the Preconfigured Sample Client**:

   You can download the preconfigured sample client directly or use `curl`:

   ```bash
   curl https://api.cloud.hazelcast.com/client_samples/download/<custom> -o hazelcast-cloud-<custom>-java-sample.zip
   ```

   Replace `<custom>` with your specific cluster identifier.

3. **Extract the Downloaded File**:

   Unzip the downloaded file:

   ```bash
   unzip hazelcast-cloud-<custom>-java-sample.zip
   cd hazelcast-cloud-<custom>-java-sample
   ```

4. **Run the Client with SSL**:

   For Unix-based systems:

   ```bash
   ./mvnw clean compile exec:java@client-with-ssl
   ```

   For Windows users:

   ```cmd
   .\mvnw.cmd clean compile exec:java@client-with-ssl
   ```

   Upon successful connection, the client will perform operations on the map named 'map' and output the current map size periodically.

5. **Verify the Connection**:

   After running the client, you can check the Hazelcast Cloud Dashboard to view your cluster’s characteristics and ensure the client is interacting with the cluster successfully.

## Additional Resources

For more comprehensive examples and configurations, consider exploring the [Hazelcast Code Samples](https://github.com/hazelcast/hazelcast-code-samples) repository. It offers a wide range of code samples demonstrating various features and use cases of Hazelcast.

## Contributing

Contributions are welcome! If you have suggestions or improvements, please open an issue or submit a pull request.

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](https://docs.hazelcast.com/hazelcast/latest/index.html#licenses-and-support) file for details.

By following this guide, you should be able to set up and run the sample Java client for Hazelcast Cloud Community with SSL successfully. For further assistance, refer to the official [Hazelcast documentation](https://docs.hazelcast.com/).
