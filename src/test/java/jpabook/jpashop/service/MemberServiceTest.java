package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given -- 조건
        Member member = new Member();
        member.setName("kim");

        //when -- 동작
        Long savedId = memberService.join(member);

        //then -- 검증
        assertEquals(member, memberRepository.findById(savedId));
    }

    @Test(expected = IllegalStateException.class) //이 예외를 기대하는 테스트
    public void 중복_회원_예외() throws Exception {
        //given -- 조건
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when -- 동작
        memberService.join(member1);
        memberService.join(member2); //예외가 발생해야함 - 같은 이름

        //then -- 검증
        //fail -> 여기에 도달하면 안된다.
        fail("예외가 발생해야 한다.");
    }
}