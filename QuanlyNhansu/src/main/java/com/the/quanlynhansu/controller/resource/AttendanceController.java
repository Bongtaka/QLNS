package com.the.quanlynhansu.controller.resource;

import com.the.quanlynhansu.model.dto.AttendanceDTO;

import com.the.quanlynhansu.service.AttendanceSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceSevice attendanceService;

    @PostMapping("/checkin")
    public ResponseEntity<AttendanceDTO> checkIn(@RequestBody AttendanceDTO attendanceDTO) {
        AttendanceDTO savedAttendance = attendanceService.checkIn(attendanceDTO);
        return ResponseEntity.ok(savedAttendance);
    }

    @PostMapping("/checkout/{id}")
    public ResponseEntity<AttendanceDTO> checkOut(@PathVariable Long id) {
        AttendanceDTO savedAttendance = attendanceService.checkOut(id);
        return ResponseEntity.ok(savedAttendance);
    }

    @GetMapping("/{employeeId}/{month}")
    public ResponseEntity<?> getMonthlyAttendance(@PathVariable Long employeeId, @PathVariable int month) {
        // Lấy danh sách chấm công hàng tháng của nhân viên bằng cách gọi phương thức từ AttendanceService
        List<AttendanceDTO> attendanceDTOList = attendanceService.getMonthlyAttendance(employeeId, month);

        // Tính tổng số giờ làm việc trong tháng bằng cách lấy khoảng thời gian giữa check-in và check-out của mỗi bản ghi và tính tổng
        long totalHours = attendanceDTOList.stream().mapToLong(a -> Duration.between(a.getCheckInTime(), a.getCheckOutTime()).toHours()).sum();

        // Tính tổng số giờ làm thêm trong tháng bằng cách lọc các bản ghi có làm thêm giờ (isOvertime) và tính số giờ làm thêm (trừ đi 8 giờ)
        long overtimeHours = attendanceDTOList.stream().filter(AttendanceDTO::isOvertime).mapToLong(a -> Duration.between(a.getCheckInTime(), a.getCheckOutTime()).toHours() - 8).sum();

        // Trả về kết quả dưới dạng chuỗi JSON chứa tổng số giờ làm việc, số giờ làm thêm và số ngày làm việc
        return ResponseEntity.ok(
                "Total hours: " + totalHours + ", Overtime hours: " + overtimeHours + ", Days worked: " + attendanceDTOList.size()
        );
    }

}
