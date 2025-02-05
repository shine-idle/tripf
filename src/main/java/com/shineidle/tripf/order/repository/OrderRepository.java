package com.shineidle.tripf.order.repository;

import com.shineidle.tripf.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * 주문 목록 조회(다건)
     *
     * @param userId 사용자 식별자
     * @return 해당 사용자가 주문한 모든 주문 목록
     */
    List<Order> findByUserId(Long userId);

    /**
     *주문 목록 조회(단건)
     *
     * @param id 주문 식별자
     * @param userId 사용자 식별자
     * @return 주문이 존재하면 {@Link Optional 반환, 없으면 빈 {@Link Optional} 반환
     */
    Optional<Order> findByIdAndUserId(Long id, Long userId);
}