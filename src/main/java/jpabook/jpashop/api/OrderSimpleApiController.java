package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


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


    @GetMapping("/api/v2/simple-orders")
    public Result ordersV2() {
        //쿼리 1번 발생 -> 총 결과가 2개나옴 (회원, 배송)
        List<Order> orderList = orderRepository.findAllByString(new OrderSearch());

        //회원 2명 + 배송 2개 = 4개
        List<SimpleOrderDto> orderDtoList = orderList.stream()
                .map(order -> new SimpleOrderDto(order))
                .collect(Collectors.toList());

        //이게 N + 1문제 (== 1 + N) 

        //TODO : 반환 List를 여따 넣기
        return new Result(orderDtoList);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName(); //LAZY 초기화
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress(); //LAZY 초기화
        }
    }
}
