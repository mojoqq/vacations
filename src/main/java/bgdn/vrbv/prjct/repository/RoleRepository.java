package bgdn.vrbv.prjct.repository;

import bgdn.vrbv.prjct.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Set<Role> getRoleById(Long id);

    Optional<Role> findByName(String name);
}
