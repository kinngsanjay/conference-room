package app.conferenceroom.facade.convertor;

import app.conferenceroom.facade.dto.MaintenanceTimeRange;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ListMapToMaintenanceTimeRangeConverter implements Converter<List<Map<String, String>>, List<MaintenanceTimeRange>> {
    @Override
    public List<MaintenanceTimeRange> convert(List<Map<String, String>> source) {
        return source.stream()
                .map(this::convertSingle)
                .collect(Collectors.toList());
    }

    private MaintenanceTimeRange convertSingle(Map<String, String> source) {
       return new MaintenanceTimeRange(
                LocalTime.parse(source.get("startTime")),
                LocalTime.parse(source.get("endTime")));
    }
}
