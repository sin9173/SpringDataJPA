package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    //find...By
    //count...By -> long
    //exist...By -> boolean
    //delete...By, remove...By -> long
    //LIMIT : findFirst3, findFirst, findTop, findTop3
    List<Member> findHelloBy();

    List<Member> findTop3By();

    //Named Query
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    //로딩시점에 파싱됨
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //DTO 로 반환
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();
    
    
    @Query("select m from Member m where m.username in :names") //in 절을 만들어줌
    List<Member> findByNames(@Param("names") Collection<String> names);


    //
    List<Member> findListByUsername(String username); //컬렉션

    Member findMemberByUsername(String username); //단건

    Optional<Member> findOptionalByUsername(String username); //Optional
}



