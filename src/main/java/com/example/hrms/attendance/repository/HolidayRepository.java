package com.example.hrms.attendance.repository;

import com.example.hrms.attendance.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    boolean existsByOrganizationIdAndHolidayDate(Long organizationId, LocalDate date);

    @Query("SELECT h FROM Holiday h WHERE h.organizationId = :organizationId AND FUNCTION('YEAR', h.holidayDate) = :year")
    List<Holiday> findByOrganizationIdAndYear(Long organizationId, int year);

    // Finds all holidays that occur on a specific date (for all orgs).
    List<Holiday> findAllByHolidayDate(LocalDate date);
}