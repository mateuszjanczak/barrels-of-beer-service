package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTapLog;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@Service
public class DocumentService {

    private static final String[] HEADERS = {"Identyfikator operacji", "Numer kraniku", "Kod beczki", "Zawartość beczki", "Stan", "Ogólne zużycie", "Zużycie", "Data", "Typ operacji"};
    private static final CSVFormat FORMAT = CSVFormat.DEFAULT.withHeader(HEADERS);

    public ByteArrayInputStream load(final List<BarrelTapLog> barrelTapLogs) {
        return writeDataToCsv(barrelTapLogs);
    }

    private ByteArrayInputStream writeDataToCsv(final List<BarrelTapLog> barrelTapLogs) {
        try (final ByteArrayOutputStream stream = new ByteArrayOutputStream();
             final CSVPrinter printer = new CSVPrinter(new PrintWriter(stream), FORMAT)) {
            for (final BarrelTapLog barrelTapLog : barrelTapLogs) {
                final List<String> data = Arrays.asList(
                        barrelTapLog.getId(),
                        String.valueOf(barrelTapLog.getBarrelTapId()),
                        barrelTapLog.getBarrelName(),
                        barrelTapLog.getBarrelContent(),
                        String.valueOf(barrelTapLog.getCurrentLevel()),
                        String.valueOf(barrelTapLog.getTotalUsage()),
                        String.valueOf( barrelTapLog.getSingleUsage()),
                        String.valueOf(barrelTapLog.getDate()),
                        barrelTapLog.getLogType().name()
                );

                printer.printRecord(data);
            }
            printer.flush();
            return new ByteArrayInputStream(stream.toByteArray());
        } catch (final IOException e) {
            throw new RuntimeException("Csv writing error: " + e.getMessage());
        }
    }
}
