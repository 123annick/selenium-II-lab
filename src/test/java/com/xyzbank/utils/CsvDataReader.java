package com.xyzbank.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Reads CSV files from src/test/resources and converts them to TestNG data-provider rows.
 * The first line is the header; every following line becomes one test invocation.
 */
public final class CsvDataReader {

    private CsvDataReader() {
    }

    public static Object[][] read(String resourcePath) {
        return read(resourcePath, row -> true);
    }

    /** Reads only the rows accepted by the filter (e.g. only "should_pass" rows). */
    public static Object[][] read(String resourcePath, Predicate<CSVRecord> filter) {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setTrim(true)
                .setIgnoreEmptyLines(true)
                .setCommentMarker('#')
                .build();

        try (InputStream in = CsvDataReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalArgumentException("Test data file not found: " + resourcePath);
            }
            try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
                 CSVParser parser = format.parse(reader)) {

                List<Object[]> rows = new ArrayList<>();
                for (CSVRecord record : parser) {
                    if (filter.test(record)) {
                        rows.add(record.toList().toArray());
                    }
                }
                return rows.toArray(new Object[0][]);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + resourcePath, e);
        }
    }
}
