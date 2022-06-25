package com.senla.kanapa.service.mapper;

import com.senla.kanapa.entity.Kanapic;
import com.senla.kanapa.entity.User;
import com.senla.kanapa.repository.UserJpaRepository;
import com.senla.kanapa.service.dto.request.KanapicDebitDto;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class KanapicMapper {

    public Kanapic getKanapicDebitDtoToKanapic(KanapicDebitDto kanapicDto, UserJpaRepository userJpaRepository) {
        User user = userJpaRepository.getReferenceById(kanapicDto.getUserId());
        user.setKanapic(user.getKanapic() + kanapicDto.getDebit());
        return Kanapic.builder()
                .date(LocalDateTime.now())
                .debit(kanapicDto.getDebit())
                .user(user)
                .build();
    }
}
