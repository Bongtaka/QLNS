document.addEventListener('DOMContentLoaded', function() {
    const sessionSelect = document.getElementById('sessionSelect');
    const attendanceFormContainer = document.getElementById('attendanceFormContainer');
    const attendanceStats = document.getElementById('attendanceStats');

    // Kiểm tra sự tồn tại của phần tử trước khi thao tác
    if (!sessionSelect || !attendanceFormContainer || !attendanceStats) {
        console.error('Một hoặc nhiều phần tử không tồn tại.');
        return;
    }

    // Tải các phiên điểm danh để chọn
    function loadSessions() {
        fetch('http://localhost:3000/sessions')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                sessionSelect.innerHTML = '<option value="">-- Chọn phiên --</option>';
                data.forEach(session => {
                    const option = document.createElement('option');
                    option.value = session.id;
                    option.textContent = `${session.className} - ${new Date(session.dateTime).toLocaleString()}`;
                    sessionSelect.appendChild(option);
                });
            })
            .catch(error => console.error('Error loading sessions:', error));
    }

    // Tạo form điểm danh dưới dạng bảng
    function loadAttendanceForm(sessionId) {
        fetch(`http://localhost:3000/sessions/${sessionId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(session => {
                const students = session.students;
                const formHtml = `
                    <h3>Điểm danh cho lớp ${session.className} - ${new Date(session.dateTime).toLocaleString()}</h3>
                    <form id="attendanceForm">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Tên Học Sinh</th>
                                    <th>Ngày Sinh</th>
                                    <th>Trạng Thái</th>
                                    <th>Ghi Chú</th>
                                </tr>
                            </thead>
                            <tbody>
                                ${students.map(student => `
                                    <tr>
                                        <td>${student.name}</td>
                                        <td>${student.dateOfBirth}</td>
                                        <td>
                                            <select name="attendance_${student.id}" class="form-select">
                                                <option value="present">Đến</option>
                                                <option value="late">Đi muộn</option>
                                                <option value="excused">Nghỉ có phép</option>
                                                <option value="unexcused">Nghỉ không phép</option>
                                            </select>
                                        </td>
                                        <td>
                                            <input type="text" name="note_${student.id}" class="form-control" placeholder="Ghi chú (nếu có)">
                                        </td>
                                    </tr>
                                `).join('')}
                            </tbody>
                        </table>
                        <button type="submit" class="btn btn-primary">Lưu điểm danh</button>
                    </form>
                `;
                attendanceFormContainer.innerHTML = formHtml;

                const attendanceForm = document.getElementById('attendanceForm');
                if (attendanceForm) {
                    attendanceForm.addEventListener('submit', function(event) {
                        event.preventDefault();
                        const formData = new FormData(attendanceForm);
                        const attendanceData = students.map(student => ({
                            id: student.id,
                            status: formData.get(`attendance_${student.id}`),
                            note: formData.get(`note_${student.id}`)
                        }));

                        fetch(`http://localhost:3000/sessions/${sessionId}/attendance`, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                            body: JSON.stringify(attendanceData)
                        })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Network response was not ok');
                            }
                            updateAttendanceStats(sessionId); // Cập nhật thống kê sau khi lưu điểm danh
                        })
                        .catch(error => console.error('Error saving attendance:', error));
                    });
                }
            })
            .catch(error => console.error('Error loading session:', error));
    }

    // Cập nhật thống kê điểm danh
    function updateAttendanceStats(sessionId) {
        fetch(`http://localhost:3000/sessions/${sessionId}/attendance`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(attendance => {
                const stats = {
                    present: 0,
                    late: 0,
                    excused: 0,
                    unexcused: 0
                };

                attendance.forEach(record => {
                    stats[record.status]++;
                });

                attendanceStats.innerHTML = `
                    <h4>Thống kê điểm danh:</h4>
                    <p>Đến: ${stats.present}</p>
                    <p>Đi muộn: ${stats.late}</p>
                    <p>Nghỉ có phép: ${stats.excused}</p>
                    <p>Nghỉ không phép: ${stats.unexcused}</p>
                `;
            })
            .catch(error => console.error('Error loading attendance stats:', error));
    }

    // Xử lý sự kiện chọn phiên điểm danh
    sessionSelect.addEventListener('change', function() {
        const sessionId = this.value;
        if (sessionId) {
            loadAttendanceForm(sessionId);
            updateAttendanceStats(sessionId);
        } else {
            attendanceFormContainer.innerHTML = '';
            attendanceStats.innerHTML = '';
        }
    });

    // Tải các phiên điểm danh khi trang được nạp
    loadSessions();
});
