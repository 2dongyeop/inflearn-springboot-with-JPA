package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm"; //이 html로 이동
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) { //@Valid -> @NotEmpty와 같은 기능을 검사

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member); //저장
        return "redirect:/"; //저장이 끝나면 홈으로 이동
    }

    @GetMapping("/members")
    public String list(Model model) {
        //사실 조회에도 아래처럼 Member 엔티티를 직접 사용하기보단
        //DTO를 사용하는게 좋음!
        //나중에 말하겠지만 API를 만들땐, 절대 엔티티를 외부로 반환하지 말것.
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
