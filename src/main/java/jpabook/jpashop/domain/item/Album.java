package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("A") //상속 관계 매핑
@Getter @Setter
public class Album extends Item {

    private String artist;
    private String etc;
}
