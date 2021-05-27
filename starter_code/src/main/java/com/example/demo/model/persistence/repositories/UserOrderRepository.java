package com.example.demo.model.persistence.repositories;

import com.example.demo.model.persistence.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {
}
