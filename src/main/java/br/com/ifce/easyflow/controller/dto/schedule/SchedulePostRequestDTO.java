package br.com.ifce.easyflow.controller.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchedulePostRequestDTO {
    private String shiftSchedule;
    private String day;
    private int tableNumber;
    private Long personId;
}
