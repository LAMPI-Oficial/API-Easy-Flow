package br.com.ifce.easyflow.controller.dto.schedule;

import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponseDTO {

    private String shiftSchedule;
    private String day;
//    private int tableNumber;
    private Person person;

    public static ScheduleResponseDTO toResponseDTO(Schedule schedule) {
        return new ScheduleResponseDTO(schedule.getShiftSchedule(),
                schedule.getDay(),
//                schedule.getTableNumber(),
                schedule.getPerson());
    }
}
