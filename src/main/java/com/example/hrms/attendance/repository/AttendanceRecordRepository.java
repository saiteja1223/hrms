package com.example.hrms.attendance.repository;

import com.example.hrms.attendance.model.AttendanceRecord;
import com.example.hrms.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    Optional<AttendanceRecord> findByUserAndAttendanceDate(User user, LocalDate date);
    List<AttendanceRecord> findByUserAndAttendanceDateBetween(User user, LocalDate startDate, LocalDate endDate);
    // Efficiently finds all User IDs that have some kind of attendance record on a given date.
    @Query("SELECT ar.user.id FROM AttendanceRecord ar WHERE ar.attendanceDate = :date")
    Set<Long> findUserIdsWithAttendanceOnDate(@Param("date") LocalDate date);
}