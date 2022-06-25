package com.senla.kanapa.repository;

import com.senla.kanapa.entity.Advertisement;
import com.senla.kanapa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdvertisementJpaRepository extends JpaRepository<Advertisement, Long>,
        JpaSpecificationExecutor<Advertisement> {

    List<Advertisement> getByUserAndDateCloseIsNull(User user);

    List<Advertisement> getByDateCloseIsNullOrderByDateBonusDesc();

    List<Advertisement> getAdvertisementByDateBonusNotNull();

    @Query(value = "SELECT * FROM kanapa.advertisements a JOIN users u ON a.users_id = u.id  where a.date_close is null  order by a.date_bonus desc, u.rating desc", nativeQuery = true)
    List<Advertisement> getCurrentOrderByBonusAndRating();
}
