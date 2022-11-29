package com.example.simplelogin.controller;

import com.example.simplelogin.dto.MessageResponse;
import com.example.simplelogin.dto.request.LoginRequest;
import com.example.simplelogin.dto.request.SignupRequest;
import com.example.simplelogin.dto.response.JwtResponse;
import com.example.simplelogin.model.ERole;
import com.example.simplelogin.model.Role;
import com.example.simplelogin.model.User;
import com.example.simplelogin.repository.RoleRepository;
import com.example.simplelogin.repository.UserRepository;
import com.example.simplelogin.security.jwt.JwtUtils;
import com.example.simplelogin.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * packageName : com.example.simplelogin.controller
 * fileName : AuthController
 * author : Chozy93
 * date : 22-11-29(029)
 * description :
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 22-11-29(029)         Chozy93          최초 생성
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    //    인증/권한체크 처리를 위한 객체
    //    spring security 기본제공
    @Autowired
    AuthenticationManager authenticationManager;

    //    spring security 기본제공
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils; //웹토큰 유틸리티 객체(생성,유효성체크 등)

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    //    login 함수!
//    @vaild 서버 유효성 체크 어노테이션 (모델에 선언된 조건에 맞는 data만 걸러줌)
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//        인증 체크 시작
//        authentication 객체 : user == UserDetails 인증된 객체
//        authenticationManager.authenticate() 호출 실행 후
//        리턴값은 인증된 객체로 나옴 Authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
//        spring security : 인증된 객체 -> 홀더에 넣음(SecurityContextHolder)
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        토큰발행시작
        String jwt = jwtUtils.generateJwtToken(authentication);
//        인증된 객체에서 유저만 뽑아서 UserDetails에 저장
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

//        권한정보 추출
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        ));
    }

    //    signUp 함수
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
//        사용자가 DB에 존재하는지 체크
//        존재하면 예외처리 하고 Vue 메세지 전송
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
//        이메일 존재 체크
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already taken!"));
        }
//        신규사용자 생성
        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword())
        );

        Set<String> strRoles = signupRequest.getRole();

        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

    }

}
