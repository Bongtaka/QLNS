package com.the.quanlynhansu.repository;

import com.the.quanlynhansu.entity.DepartmentEntity;
import com.the.quanlynhansu.model.request.DepartmentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
    @Query("SELECT d FROM DepartmentEntity d " +
            "LEFT JOIN d.employees e " +
            "WHERE (:#{#departmentRequest.idEmployee} IS NULL OR e.id = :#{#departmentRequest.idEmployee})")
    Page<DepartmentEntity> findFirstByDeparment(@Param("departmentRequest") DepartmentRequest departmentRequest, Pageable pageable);

}
