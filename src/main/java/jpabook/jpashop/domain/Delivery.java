package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery") //주인이 아닌 거울!
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) //EnumType.ORDINARY가 디폴트인데 절대 쓰지 말 것!! 중간에 다른거 끼면 골치아픔
    private DeliveryStatus status; //READY, COMP
}
