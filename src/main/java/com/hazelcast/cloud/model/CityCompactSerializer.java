package com.hazelcast.cloud.model;

import java.io.IOException;

import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class CityCompactSerializer implements CompactSerializer<City> {

    @Override
    public City read(CompactReader compactReader) throws IOException {
        return City.newCity(
            compactReader.readString("country"),
            compactReader.readString("city"),
            compactReader.readInt("population")
        );
    }

    @Override
    public void write(CompactWriter compactWriter, City city) throws IOException {
        compactWriter.writeString("country", city.getCountry());
        compactWriter.writeString("city", city.getCity());
        compactWriter.writeInt("population", city.getPopulation());

    }
}
