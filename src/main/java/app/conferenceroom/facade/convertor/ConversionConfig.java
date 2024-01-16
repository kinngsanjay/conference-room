package app.conferenceroom.facade.convertor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConversionConfig {
    @Bean
    public ListMapToMaintenanceTimeRangeConverter listMapToMaintenanceTimeRangeConverter() {
        return new ListMapToMaintenanceTimeRangeConverter();
    }
}
