package com.senla.kanapa.service.impl;

import com.senla.kanapa.entity.Advertisement;
import com.senla.kanapa.entity.Kanapic;
import com.senla.kanapa.entity.User;
import com.senla.kanapa.repository.AdvertisementJpaRepository;
import com.senla.kanapa.repository.KanapikJpaRepository;
import com.senla.kanapa.repository.UserJpaRepository;
import com.senla.kanapa.service.KanapicService;
import com.senla.kanapa.service.dto.request.KanapicCreditDto;
import com.senla.kanapa.service.dto.request.KanapicDebitDto;
import com.senla.kanapa.service.exception.AccountBalanceException;
import com.senla.kanapa.service.mapper.KanapicMapper;
import com.senla.kanapa.service.security.TokenExtractData;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log4j2
public class KanapicServiceImpl implements KanapicService {

    private final KanapikJpaRepository kanapikJpaRepository;

    private final UserJpaRepository userJpaRepository;

    private final AdvertisementJpaRepository advertisementJpaRepository;

    private final TokenExtractData tokenExtractData;

    @Override
    @Transactional
    @Secured("ROLE_ADMIN")
    public void creditKanapic(KanapicDebitDto kanapicDebitDto) {
        Kanapic kanapic = KanapicMapper.toKanapic(kanapicDebitDto, userJpaRepository);
        kanapikJpaRepository.save(kanapic);
        log.info("The administrator added {} kanapik to the user's account Id = {}", kanapic.getCredit(), kanapic.getUser().getUsername());
    }

    @Transactional
    @Override
    public void debitKanapic(KanapicCreditDto kanapicCreditDto, String token) throws AccountBalanceException {
        log.info("Start credit kanapic");
        User user = userJpaRepository.getReferenceById(tokenExtractData.extractUserIdFromToken(token));
        log.info("The beginning of debiting from the user's kanapic account Id = {}", user.getUsername());
        if (user.getKanapic() >= kanapicCreditDto.getCredit()) {
            user.setKanapic(user.getKanapic() - kanapicCreditDto.getCredit());
            LocalDateTime date = LocalDateTime.now();
            LocalDateTime dateBonus = date.plusDays(kanapicCreditDto.getCredit());
            Advertisement advertisement = advertisementJpaRepository.getReferenceById(kanapicCreditDto.getAdvertisementId());
            if (advertisement.getDateBonus() == null) {
                advertisement.setDateBonus(dateBonus);
            } else {
                advertisement.setDateBonus(advertisement.getDateBonus().plusDays(kanapicCreditDto.getCredit()));
            }
            Kanapic kanapic = Kanapic.builder()
                    .date(date)
                    .credit(kanapicCreditDto.getCredit())
                    .user(user)
                    .advertisement(advertisement)
                    .build();
            kanapikJpaRepository.save(kanapic);
            log.info("The kanapics were debited successfully from the user {} for the ad id = {}", kanapic.getUser().getUsername(), kanapic.getAdvertisement().getId());
        } else {
            log.error("Insufficient funds to be debited from the account {}", user.getUsername());
            throw new AccountBalanceException("You don't have enough kanapic");
        }
    }
}
