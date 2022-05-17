package inc.dundermifflin.stocks.licensingservice.repository;

import inc.dundermifflin.stocks.licensingservice.model.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRedisRepository extends CrudRepository<Organization, String> {
}
