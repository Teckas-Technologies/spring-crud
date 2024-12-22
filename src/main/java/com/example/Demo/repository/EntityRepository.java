package com.example.Demo.repository;

import com.example.Demo.model.dao.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Repository class
@Repository
public interface EntityRepository extends JpaRepository<Entity,Long> {
    Page<Entity> findByEntityType(Entity.EntityType valueOf, PageRequest pageRequest);

    Page<Entity> findByNameContainingIgnoreCase(String name, PageRequest pageRequest);
}
