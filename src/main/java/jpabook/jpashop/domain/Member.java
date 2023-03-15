package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name; //Unique로 설정해보기

    @Embedded //내장된
    private Address address;

    /**
     * 하나의 회원은 여러 오더를 가짐
     * 아래의 orders는 Order 테이블에 있는 member 필더와 연관관계를 가짐.
     * 얜 주인이 아닌, 따라오는 거울 역할
     */
    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
