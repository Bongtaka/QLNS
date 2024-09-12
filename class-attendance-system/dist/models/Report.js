"use strict";
// src/models/Report.ts
Object.defineProperty(exports, "__esModule", { value: true });
exports.Report = void 0;
class Report {
    constructor(className, dateTime, statistics, studentDetails) {
        this.className = className;
        this.dateTime = dateTime;
        this.statistics = statistics;
        this.studentDetails = studentDetails;
    }
    static generateReport(session, students) {
        const statistics = {
            Present: session.students.filter(s => s.status === 'Present').length,
            Late: session.students.filter(s => s.status === 'Late').length,
            Excused: session.students.filter(s => s.status === 'Excused').length,
            Absent: session.students.filter(s => s.status === 'Absent').length,
        };
        const studentDetails = session.students.map(record => {
            const student = students.find(stu => stu.id === record.studentId);
            return {
                name: (student === null || student === void 0 ? void 0 : student.name) || '',
                dateOfBirth: (student === null || student === void 0 ? void 0 : student.dateOfBirth) || '',
                status: record.status,
                note: record.note,
            };
        });
        return new Report(session.className, session.dateTime, statistics, studentDetails);
    }
}
exports.Report = Report;
