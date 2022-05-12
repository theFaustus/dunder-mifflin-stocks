package inc.dundermifflin.stocks.organizationservice.repository;

import inc.dundermifflin.stocks.organizationservice.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String> {
}
