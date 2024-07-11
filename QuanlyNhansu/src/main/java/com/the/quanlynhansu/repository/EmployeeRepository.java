package com.the.quanlynhansu.repository;


import com.the.quanlynhansu.entity.DepartmentEntity;
import com.the.quanlynhansu.entity.EmployeeEntity;
import com.the.quanlynhansu.entity.PositionEntity;
import com.the.quanlynhansu.model.request.EmployeeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    @Query("SELECT e FROM EmployeeEntity e " +
            "LEFT JOIN e.user u " +
            "LEFT JOIN e.position p " +
            "LEFT JOIN e.department d " +
            "WHERE " +
            "(:#{#employeeRequest.userId} IS NULL OR u.id = :#{#employeeRequest.userId}) " +
            "AND (:#{#employeeRequest.positionId} IS NULL OR p.id = :#{#employeeRequest.positionId}) " +
            "AND (:#{#employeeRequest.departmentId} IS NULL OR d.id = :#{#employeeRequest.departmentId})")
    Page<EmployeeEntity> findFirstByEmployee(@Param("employeeRequest") EmployeeRequest employeeRequest, Pageable pageable);

    @Query(value = "SELECT e FROM EmployeeEntity e " +
            "LEFT JOIN e.user u " +
            "LEFT JOIN e.position p " +
            "LEFT JOIN e.department d " +
            "WHERE " +
            "(:#{#request.positionId} IS NULL OR p.id = :#{#request.positionId}) " +
            "AND (:#{#request.departmentId} IS NULL OR d.id = :#{#request.departmentId}) " +
            "AND (:#{#request.userId} IS NULL OR u.id = :#{#request.userId})")
    Optional<EmployeeEntity> employcreate(@Param("request") EmployeeRequest request);


}
