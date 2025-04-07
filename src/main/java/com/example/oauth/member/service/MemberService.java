package com.example.oauth.member.service;

import com.example.oauth.member.domain.Member;
import com.example.oauth.member.dto.MemberCreateDto;
import com.example.oauth.member.dto.MemberLoginDto;
import com.example.oauth.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member create(MemberCreateDto memberCreateDto){
        Member member = Member.builder()
                .email(memberCreateDto.getEmail())
                .password(passwordEncoder.encode(memberCreateDto.getPassword()))
                .build();
        memberRepository.save(member);
        return member;
    }

    public Member login(MemberLoginDto memberLoginDto){
        Optional<Member> optMember = memberRepository.findByEmail(memberLoginDto.getEmail());
        
        log.info("login optMember {}", optMember);
        log.info("optMember.isPresent {}", optMember.isPresent());

        if(!optMember.isPresent()){
            throw new IllegalArgumentException("email이 존재하지 않습니다");
        }

        Member member = optMember.get();
        log.info("passwordEncoder.matches {}", passwordEncoder.matches(memberLoginDto.getPassword(), member.getPassword()));
        if(!passwordEncoder.matches(memberLoginDto.getPassword(), member.getPassword())){
            throw new IllegalArgumentException("password가 일치하지 않습니다.");
        }
        return member;
    }

}
