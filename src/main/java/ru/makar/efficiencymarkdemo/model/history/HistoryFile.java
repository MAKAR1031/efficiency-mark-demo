package ru.makar.efficiencymarkdemo.model.history;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class HistoryFile {
    private final String path;

    public HistoryData read(int year) throws IOException {
        Path historyPath = Paths.get(path);
        ByteBuffer bytes = ByteBuffer.wrap(Files.readAllBytes(historyPath));
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        HistoryHeader header = new HistoryHeader();
        byte[] copyRightBytes = new byte[64];
        byte[] symbolBytes = new byte[12];
        header.setVersion(bytes.getInt());
        bytes.get(copyRightBytes);
        bytes.get(symbolBytes);
        header.setCopyright(new String(copyRightBytes));
        header.setSymbol(new String(symbolBytes));
        header.setPeriod(bytes.getInt());
        header.setDigits(bytes.getInt());
        header.setCreateTime(bytes.getInt());
        header.setLastSynchronizeTime(bytes.getInt());

        bytes.position(bytes.position() + 52);
        List<HistoryBar> bars = new LinkedList<>();
        ZoneId zone = ZoneId.systemDefault();
        while (bytes.hasRemaining()) {
            HistoryBar bar = new HistoryBar();
            LocalDateTime openTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(bytes.getLong() * 1000), zone).plusHours(-3);
            if (openTime.getYear() < year) {
                bytes.position(bytes.position() + 52);
                continue;
            }
            bar.setOpenTime(openTime);
            bar.setOpen(bytes.getDouble());
            bar.setHigh(bytes.getDouble());
            bar.setLow(bytes.getDouble());
            bar.setClose(bytes.getDouble());
            bar.setVolume(bytes.getLong());
            bar.setSpread(bytes.getInt());
            bar.setRealVolume(bytes.getLong());
            bars.add(bar);
        }
        return new HistoryData(header, bars);
    }
}
