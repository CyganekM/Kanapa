package com.senla.kanapa.repository;

import com.senla.kanapa.entity.Advertisement;
import com.senla.kanapa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AdvertisementJpaRepository extends JpaRepository<Advertisement, Long>,
        JpaSpecificationExecutor<Advertisement> {

    List<Advertisement> getByUserAndDateCloseIsNull(User user);

    List<Advertisement> getAdvertisementByDateBonusNotNull();
}
