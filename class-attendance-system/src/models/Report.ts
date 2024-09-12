// src/models/Report.ts

import { IAttendanceSession } from './AttendanceSession';
import { IStudent } from './Student';

export interface IReport {
    className: string;
    dateTime: string;
    statistics: {
        Present: number;
        Late: number;
        Excused: number;
        Absent: number;
    };
    studentDetails: {
        name: string;
        dateOfBirth: string;
        status: string;
        note?: string;
    }[];
}

export class Report implements IReport {
    constructor(
        public className: string,
        public dateTime: string,
        public statistics: {
            Present: number;
            Late: number;
            Excused: number;
            Absent: number;
        },
        public studentDetails: {
            name: string;
            dateOfBirth: string;
            status: string;
            note?: string;
        }[]
    ) {}

    static generateReport(session: IAttendanceSession, students: IStudent[]): Report {
        const statistics = {
            Present: session.students.filter(s => s.status === 'Present').length,
            Late: session.students.filter(s => s.status === 'Late').length,
            Excused: session.students.filter(s => s.status === 'Excused').length,
            Absent: session.students.filter(s => s.status === 'Absent').length,
        };

        const studentDetails = session.students.map(record => {
            const student = students.find(stu => stu.id === record.studentId);
            return {
                name: student?.name || '',
                dateOfBirth: student?.dateOfBirth || '',
                status: record.status,
                note: record.note,
            };
        });

        return new Report(session.className, session.dateTime, statistics, studentDetails);
    }
}
