package sg.ic.asteroidmonitor.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedDateRangeReq {
    @NotNull(message = "Start date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
