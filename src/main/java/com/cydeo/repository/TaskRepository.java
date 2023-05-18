package com.cydeo.repository;

import com.cydeo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task,Long> {
// needs to be joint table
    //JPQL, we need to count object "t" and this object belongs to which class
    @Query("SELECT COUNT(t) FROM Task t where t.project.projectCode= ?1 AND t.taskStatus <> 'Completed'")
 int totalNonCompletedTasks( String projectCode);

    //native Query        t.project_id -->foreignKey       p.id--> primaryKey
    @Query(value = "SELECT COUNT (*)" + "From tasks t JOIN projects p ON t.project_id = p.id"+
    "Where p.project_code = ?1 AND t.task_status= 'COMPLETE'",nativeQuery = true)
   int totalCompletedTasks(String projectCode);
}

