// src/models/AttendanceSession.ts

import { Student } from './Student';

export interface IAttendanceRecord {
    studentId: string;
    status: 'Present' | 'Late' | 'Excused' | 'Absent';
    note?: string;
}

export interface IAttendanceSession {
    id: string;
    className: string;
    dateTime: string;
    students: IAttendanceRecord[];
}

export class AttendanceSession implements IAttendanceSession {
    constructor(
        public id: string,
        public className: string,
        public dateTime: string,
        public students: IAttendanceRecord[]
    ) {}

    addRecord(record: IAttendanceRecord) {
        this.students.push(record);
    }

    removeRecord(studentId: string) {
        this.students = this.students.filter(record => record.studentId !== studentId);
    }
}
