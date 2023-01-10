package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY) //XXXToOne은 기본 FetchType이 EAGER이므로 직접 바꿔주기
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order") //XXXToMany는 기본 FetchType이 Lazy
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "delivery_id") //주인
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING) //EnumType.ORDINARY가 디폴트인데 절대 쓰지 말 것!! 중간에 다른거 끼면 골치아픔
    private OrderStatus status; //주문상태 [ORDER, CANCEL]을 뜻하는 enum
}
