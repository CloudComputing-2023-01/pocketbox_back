package store.pocketbox.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.pocketbox.app.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
