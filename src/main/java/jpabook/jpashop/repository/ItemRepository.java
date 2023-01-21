package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) { //아이디가 없다는건 새로 생성했다는 객체
            em.persist(item);
        } else {            //아이디가 있으면 이미 DB에 등록된걸 어디서 가져옴
            em.merge(item); //like update라고 생각하기!
            //실제로 병합은 dirty checking과 다르게 (선택 속성이 아닌) 모든 속성이 변경된다.
            //따라서 실무에선 속성을 다 넣어주지 않으면 null로 업데이트될 가능성이 있어 위험하다!!
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() { //여러개를 가져오는 건 쿼리 작성
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
