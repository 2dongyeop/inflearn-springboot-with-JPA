package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * v1 : 요청 값으로 Member 엔티티를 직접 받는다.
     * 문제점 :
     * - 엔티티에 프레젠테이션 계층을 위한 로직이 추가됨.
     * - 엔티티에 API 검증을 위한 로직이 들어감 (@NotEmpty 등등)
     * - 실무에서는 회원 엔티티를 담기 위한 API가 다양하다.
     *   -> 한 엔티티에 여러 API를 위한 모든 요청 요구사항 로직을 담기는 어렵다.
     * - 엔티티가 변경되면 API 스펙이 변경됨
     *
     * 결론 :
     * - API 요청 스펙에 맞추어 별도의 DTO를 파라미터로 받는다.
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * v2 : 요청 값으로 Member 엔티티 대신에 별도의 DTO를 받는다.
     * 장점 :
     * - 엔티티와 프레젠테이션 계층을 위한 로직을 분리할 수 있다.
     * - 엔티티와 API 스펙을 명확하게 분리할 수 있다.
     * - 엔티티가 변해도 API 스펙이 변하지 않는다.
     *
     * 참고 : 실무에서는 엔티티를 API 스펙에 노출하면 안된다!
     */
    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.name);

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 회원 수정 API
     */
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.name);
        Member findMember = memberService.findOne(id);

        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
