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

    //XXXToMany는 기본 FetchType이 Lazy
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id") //주인
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING) //EnumType.ORDINARY가 디폴트인데 절대 쓰지 말 것!! 중간에 다른거 끼면 골치아픔
    private OrderStatus status; //주문상태 [ORDER, CANCEL]을 뜻하는 enum

    //==연관관계 편의 메소드==// -> 컨트롤하는 쪽이 들고 있는게 좋음!
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
