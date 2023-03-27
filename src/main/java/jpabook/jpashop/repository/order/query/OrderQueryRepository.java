package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;


    /*
    * 컬렉션은 별도로 조회
    * Query 횟수 : 루트 1번, 컬렉션 N번
    * 단건 조회에서 많이 사용하는 방식
    */
    public List<OrderQueryDto> findOrderQueryDtos() {

        List<OrderQueryDto> result = findOrders();

        //루프를 돌면서 컬렉션 추가!
        result.forEach(orderQueryDto -> {

            List<OrderItemQueryDto> orderItems = findOrderItems(orderQueryDto.getOrderId());
            orderQueryDto.setOrderItems(orderItems);
        });
        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {

        return em.createQuery("select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                " from OrderItem oi" +
                " join oi.item i" +
                " where oi.order.id = : orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() {

        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    public List<OrderQueryDto> finAllByDto_optimization() {

        //루트 조회(toOne 코드를 모두 한번에 조회)
        List<OrderQueryDto> result = findOrders();

        //orderItem 컬렉션을 MAP 한방에 조회
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(result));

        //루프를 돌면서 컬렉션 추가(추가 쿼리 실행x)
        result.forEach(orderQueryDto -> orderQueryDto.setOrderItems(orderItemMap.get(orderQueryDto.getOrderId())));
        return result;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery("select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id in : orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        return orderItemMap;
    }

    private static List<Long> toOrderIds(List<OrderQueryDto> result) {
        List<Long> orderIds = result.stream()
                .map(orderQueryDto -> orderQueryDto.getOrderId())
                .collect(Collectors.toList());
        return orderIds;
    }
}
