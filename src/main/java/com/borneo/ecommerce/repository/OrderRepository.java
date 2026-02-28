package com.borneo.ecommerce.repository;

import com.borneo.ecommerce.model.Order;
import com.borneo.ecommerce.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByUserOrderByOrderDateDesc(User user);

  Optional<Order> findByIdAndUser(Long id, User user);
}
