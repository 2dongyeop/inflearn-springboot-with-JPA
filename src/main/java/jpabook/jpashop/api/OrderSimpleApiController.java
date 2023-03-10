package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * xxxToOne 관계 최적화
 * Order를 조회하고
 * Order->Member, Order->Delivery 조회
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    /**
     * 주문 조회 V1 : 엔티티 직접 노출
     * - Hibernate5Module 모듈을 등록해야 함 -> LAZY = null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("api/v1/simple-orders")
    public List<Order>  ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName();       //Lazy 강제 초기화
            order.getDelivery().getAddress();  //Lazy 강제 초기화
        }
        return all;
    }
}
