package com.hazelcast.cloud.model;

import java.io.IOException;

import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class CountryCompactSerializer implements CompactSerializer<Country> {

    @Override
    public Country read(CompactReader compactReader) throws IOException {
        return Country.newCountry(
            compactReader.readString("isoCode"),
            compactReader.readString("country")
        );
    }

    @Override
    public void write(CompactWriter compactWriter, Country country) throws IOException {
        compactWriter.writeString("isoCode", country.getIsoCode());
        compactWriter.writeString("country", country.getCountry());
    }
}
