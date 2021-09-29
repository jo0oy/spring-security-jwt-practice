package com.example.springsecurityjwtpractice.controller;

import com.example.springsecurityjwtpractice.application.MemberService;
import com.example.springsecurityjwtpractice.dto.DefaultResponse;
import com.example.springsecurityjwtpractice.dto.MemberDto;
import com.example.springsecurityjwtpractice.dto.ResponseMessage;
import com.example.springsecurityjwtpractice.dto.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 회원가입, 로그인
     */
    @PostMapping("/api/v1/member/create")
    public ResponseEntity<?> createMember(@RequestBody MemberDto.SaveRequest requestDto) {
        Long savedId = memberService.save(requestDto.getUsername(), requestDto.getEmail()
                , requestDto.getPassword(), requestDto.getRole());

        Map<String, Long> result = new HashMap<>();
        result.put("회원가입 완료 id", savedId);

        return ResponseEntity.created(URI.create("/api/v1/member/create"))
                .body(DefaultResponse.res(StatusCode.CREATED, ResponseMessage.CREATED_MEMBER, result));
    }

    /**
     * "ROLE_USER" 권한만 접근 가능
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/api/v1/member/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok()
                .body(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_MEMBER,
                        memberService.findMemberById(id)));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/api/v1/member")
    public ResponseEntity<?> getMemberByUsername(@RequestParam(name = "username") String username) {
        return ResponseEntity.ok()
                .body(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_MEMBER,
                        memberService.findMemberByUsername(username)));
    }

    @GetMapping("/api/v1/member/members")
    public ResponseEntity<?> getMembers() {
        return ResponseEntity.ok()
                .body(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_MEMBERS,
                        memberService.findMembers()));
    }

    /**
     * "ROLE_ADMIN" 권한만 접근 가능
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/api/v1/admin/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok()
                .body(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_MEMBER,
                        memberService.findMemberById(id)));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/api/v1/admin/settings")
    public ResponseEntity<?> getSettings() {
        return ResponseEntity.ok()
                .body(DefaultResponse.res(StatusCode.OK, ResponseMessage.RES_SUCCESS,
                        "admin_settings"));
    }
}
