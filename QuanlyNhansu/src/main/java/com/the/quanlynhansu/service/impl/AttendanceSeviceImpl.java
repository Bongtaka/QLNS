package com.the.quanlynhansu.service.impl;

import com.the.quanlynhansu.entity.AttendanceEntity;
import com.the.quanlynhansu.entity.EmployeeEntity;
import com.the.quanlynhansu.model.dto.AttendanceDTO;
import com.the.quanlynhansu.repository.AttendanceRepository;
import com.the.quanlynhansu.repository.EmployeeRepository;
import com.the.quanlynhansu.service.AttendanceSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceSeviceImpl implements AttendanceSevice {

    // Inject AttendanceRepository để tương tác với cơ sở dữ liệu
    @Autowired
    private AttendanceRepository attendanceRepository;

    // Inject EmployeeRepository để tương tác với dữ liệu nhân viên
    @Autowired
    private EmployeeRepository employeeRepository;

    // Phương thức để nhân viên check-in
    @Override
    public AttendanceDTO checkIn(AttendanceDTO attendanceDTO) {
        // Tìm kiếm nhân viên bằng ID
        EmployeeEntity entity = employeeRepository.findById(attendanceDTO.getEmployeeId()).orElse(null);

        // Nếu không tìm thấy nhân viên, ném ra ngoại lệ IllegalArgumentException
        if (entity == null) {
            throw new IllegalArgumentException("Không tìm thấy nhân viên");
        }

        // Tạo một bản ghi chấm công mới và thiết lập các thuộc tính của nó
        AttendanceEntity attendance = new AttendanceEntity();
        attendance.setEmployee(entity);
        attendance.setWorkday(attendanceDTO.getWorkday());
        attendance.setCheckInTime(LocalDateTime.now());

        // Lưu bản ghi chấm công vào cơ sở dữ liệu
        AttendanceEntity savedAttendance = attendanceRepository.save(attendance);

        // Chuyển đổi thực thể chấm công đã lưu thành DTO và trả về
        return convertToDTO(savedAttendance);
    }

    // Phương thức để nhân viên check-out
    @Override
    public AttendanceDTO checkOut(Long id) {
        // Tìm kiếm bản ghi chấm công bằng ID
        AttendanceEntity attendance = attendanceRepository.findById(id).orElse(null);

        // Nếu không tìm thấy bản ghi, ném ra ngoại lệ IllegalArgumentException
        if (attendance == null) {
            throw new IllegalArgumentException("Không tìm thấy bản ghi chấm công");
        }

        // Thiết lập thời gian check-out thành thời gian hiện tại
        attendance.setCheckOutTime(LocalDateTime.now());

        // Tính toán khoảng thời gian giữa check-in và check-out
        Duration duration = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime());
        long hoursWorked = duration.toHours();

        // Đánh dấu làm thêm giờ nếu thời gian làm việc vượt quá 8 giờ
        attendance.setOvertime(hoursWorked > 8);

        // Lưu bản ghi chấm công đã cập nhật vào cơ sở dữ liệu
        AttendanceEntity savedAttendance = attendanceRepository.save(attendance);

        // Chuyển đổi thực thể chấm công đã lưu thành DTO và trả về
        return convertToDTO(savedAttendance);
    }

    // Phương thức để lấy dữ liệu chấm công hàng tháng của nhân viên
    @Override
    public List<AttendanceDTO> getMonthlyAttendance(Long employeeId, int month) {
        // Xác định ngày bắt đầu và kết thúc cho tháng đã cho
        LocalDateTime start = LocalDateTime.now().withMonth(month).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = start.plusMonths(1);

        // Tìm kiếm các bản ghi chấm công của nhân viên trong khoảng thời gian đó
        List<AttendanceEntity> attendanceList = attendanceRepository.findByEmployeeIdAndCheckInTimeBetween(employeeId, start, end);

        // Chuyển đổi danh sách các thực thể chấm công thành danh sách DTO và trả về
        return attendanceList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Phương thức chuyển đổi AttendanceEntity thành AttendanceDTO
    private AttendanceDTO convertToDTO(AttendanceEntity attendance) {
        // Tạo một DTO mới và thiết lập các thuộc tính của nó từ thực thể
        AttendanceDTO dto = new AttendanceDTO();
        dto.setId(attendance.getId());
        dto.setWorkday(attendance.getWorkday());
        dto.setCheckInTime(attendance.getCheckInTime());
        dto.setCheckOutTime(attendance.getCheckOutTime());
        dto.setOvertime(attendance.isOvertime());
        dto.setEmployeeId(attendance.getEmployee().getId());
        return dto;
    }
}
