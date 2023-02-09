package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

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


    //페이징
    //@Query(value = "select m from Member m left join m.team t", countQuery = "select count(m.username) from Member m") //카운트 쿼리를 분리 가능
    Page<Member> findByAge(int age, Pageable pageable); //Page 타입으로 받을 경우 Total Count 를 추가로 조회
    
    Slice<Member> findSliceByAge(int age, Pageable pageable); //Slice 타입으로 받을 경우 다음 페이지의 여부를 확인 하기 위해 한개를 더 조회
    
    List<Member> findListByAge(int age, Pageable pageable); // 컬렉션 타입으로 받을 경우 해당되는 데이터만 조회


    // Modifying -> executeUpdate 수행 (bulk 수정 쿼리) 
    // 영속성 컨텍스트를 거치지 않음
    // clearAutomatically 를 true 로 줄 경우 자동으로 영속성 컨텍스트를 초기화
    @Modifying(clearAutomatically = true) 
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);


    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();


    //EntityGraph
    @Override
    @EntityGraph(attributePaths = {"team"}) //fetch join 을 적용해서 조회
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = ("team"))
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @EntityGraph("Member.all")
    List<Member> findNamedEntityGraphByUsername(@Param("username") String username);


    @QueryHints(value =
    @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);


    //Projection
    List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);


    //Native Query
    @Query(value = "select * from member where username=?", nativeQuery = true)
    Member findByNativeQuery(String username);


    @Query(value = "select m.member_id as id, m.username, t.name as teamName from member m left join team t",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findNativeProjection(Pageable pageable);
}



