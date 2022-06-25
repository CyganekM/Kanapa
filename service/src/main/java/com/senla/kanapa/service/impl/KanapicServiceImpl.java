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
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KanapicServiceImpl implements KanapicService {

    private final KanapikJpaRepository kanapikJpaRepository;

    private final UserJpaRepository userJpaRepository;

    private final AdvertisementJpaRepository advertisementJpaRepository;

    private final TokenExtractData tokenExtractData;

    @Override
    @Secured("ROLE_ADMIN")
    public void addKanapicDebit(KanapicDebitDto kanapicDebitDto) {
        Kanapic kanapic = KanapicMapper.getKanapicDebitDtoToKanapic(kanapicDebitDto, userJpaRepository);
        kanapikJpaRepository.save(kanapic);
    }

    @Override
    public void addKanapicCredit(KanapicCreditDto kanapicCreditDto, String token) throws AccountBalanceException {
        User user = userJpaRepository.getReferenceById(tokenExtractData.extractUserIdFromToken(token));
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
        } else {
            throw new AccountBalanceException("You don't have enough kanapic");
        }
    }
}
