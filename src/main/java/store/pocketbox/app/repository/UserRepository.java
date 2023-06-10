package store.pocketbox.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import store.pocketbox.app.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    /*@Query(value = "SELECT distinct * FROM User u where u.randId>FLOOR((RAND()*100000000)) ORDER BY u.randId limit 5 ", nativeQuery = true)
    List<User>findRandUserList();*/


    /*@Query(value = "SELECT distinct * FROM User u join Interest i on i.id=:interestId where u.randId>FLOOR((RAND()*100000000)) ORDER BY u.randId ", nativeQuery = true)
    List<User>findRandUserByInterest(@Param("interestId") Integer interestId);*/

    /*@Query(value = "select * from User u ,Interest i,UserInterest ui where (u.id=ui.user_id and ui.interest_id=i.id) and i.id=:interestId and u.randId>FLOOR((RAND()*100000000)) ORDER BY u.randId limit 5", nativeQuery = true)
    List<User>findRandUserByInterest(@Param("interestId") Integer interestId);*/
}
