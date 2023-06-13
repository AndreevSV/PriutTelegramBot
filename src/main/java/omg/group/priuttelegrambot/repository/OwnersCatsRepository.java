package omg.group.priuttelegrambot.repository;


import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnersCatsRepository extends JpaRepository<OwnerCat, Long> {

}
