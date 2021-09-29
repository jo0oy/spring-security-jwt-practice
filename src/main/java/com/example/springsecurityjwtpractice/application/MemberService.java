package com.example.springsecurityjwtpractice.application;

import com.example.springsecurityjwtpractice.domain.Member;
import com.example.springsecurityjwtpractice.domain.MemberRepository;
import com.example.springsecurityjwtpractice.domain.Role;
import com.example.springsecurityjwtpractice.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> {
                    log.error("Member Not Found in DB");
                    throw new UsernameNotFoundException("Member Not Found in DB");
                }
        );

        log.info("Load Member by username {}", username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRole().getValue()));

        return new User(member.getUsername(), member.getPassword(), authorities);
    }

    @Transactional
    public Long save(String username, String email, String password, Role role) {
        log.info("Saving new Member {} to database...", username);
        Member member = Member.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .build();

        return memberRepository.save(member).getId();
    }

    public MemberDto.Info findMemberById(Long id) {
        log.info("Finding Member By memberId : {}", id);
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Member Not Found in DB: memberId = {}", id);
                    throw new IllegalArgumentException("사용자를 찾을 수 없습니다. id = " + id);
                });
        return new MemberDto.Info(member);
    }

    public MemberDto.Info findMemberByEmail(String email) {
        log.info("Finding Member By email : {}", email);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Member Not Found in DB: email = {}", email);
                    throw new IllegalArgumentException("사용자를 찾을 수 없습니다. email = " + email);
                });
        return new MemberDto.Info(member);
    }

    public MemberDto.Info findMemberByUsername(String username) {
        log.info("Finding Member By username : {}", username);
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Member Not Found in DB: username = {}", username);
                    throw new IllegalArgumentException("사용자를 찾을 수 없습니다. username = " + username);
                });
        return new MemberDto.Info(member);
    }

    public MemberDto.ListResponse<MemberDto.SimpleInfo> findMembers() {
        log.info("Finding All Members...");
        return new MemberDto.ListResponse<>(memberRepository.findAll()
                .stream()
                .map(MemberDto.SimpleInfo::new)
                .collect(Collectors.toList()));
    }
}
