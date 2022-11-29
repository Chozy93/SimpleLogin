package com.example.simplelogin.repository;

import com.example.simplelogin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * packageName : com.example.simplelogin.repository
 * fileName : UserRepository
 * author : ds
 * date : 2022-11-29
 * description : 유저 인터페이스
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2022-11-29         ds          최초 생성
 */
// JpaRepository<모델명, 모델기본키타입>
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //    쿼리메소드
//    username(로그인id) 으로 조회하는 함수
    Optional<User> findByUsername(String username);

    //    username(로그인id) 있는지 검사하는 함수 ( 리턴값 : true/false )
    Boolean existsByUsername(String username);

    //    email 이 있는지 검사하는 함수 ( 리턴값 : true/false )
    Boolean existsByEmail(String email);
}





