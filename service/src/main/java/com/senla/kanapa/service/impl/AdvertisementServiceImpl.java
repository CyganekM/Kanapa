package com.senla.kanapa.service.impl;

import com.senla.kanapa.entity.Advertisement;
import com.senla.kanapa.entity.Filter;
import com.senla.kanapa.entity.User;
import com.senla.kanapa.repository.AdvertisementCustomRepository;
import com.senla.kanapa.repository.AdvertisementJpaRepository;
import com.senla.kanapa.repository.CategoryJpaRepository;
import com.senla.kanapa.repository.UserJpaRepository;
import com.senla.kanapa.repository.exception.OperationException;
import com.senla.kanapa.service.AdvertisementService;
import com.senla.kanapa.service.dto.AdvertisementDto;
import com.senla.kanapa.service.dto.request.FilterDto;
import com.senla.kanapa.service.exception.TokenCompareException;
import com.senla.kanapa.service.mapper.AdvertisementMapper;
import com.senla.kanapa.service.mapper.FilterMapper;
import com.senla.kanapa.service.security.TokenExtractData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {
    private final AdvertisementJpaRepository advertisementJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final AdvertisementCustomRepository advertisementCustomRepository;
    private final TokenExtractData tokenExtractData;

    @Override
    public void saveAdvertisement(AdvertisementDto advertisementDto, String token) {
        Advertisement advertisement = AdvertisementMapper.toAdvertisement(advertisementDto);
        advertisement.setCategory(categoryJpaRepository.getReferenceById(advertisementDto.getCategoryId()));
        advertisement.setUser(userJpaRepository.getReferenceById(tokenExtractData.extractUserIdFromToken(token)));
        advertisement.setDateStart(LocalDateTime.now());
        advertisementJpaRepository.save(advertisement);
    }

    @Override
    public void editAdvertisement(Long advertisementId, AdvertisementDto advertisementDto, String token) throws TokenCompareException {
        Advertisement advertisement = advertisementJpaRepository.getReferenceById(advertisementId);
        if (advertisement.getUser().getId().equals(tokenExtractData.extractUserIdFromToken(token))) {
            AdvertisementMapper.toAdvertisement(advertisement, advertisementDto);
            advertisementJpaRepository.save(advertisement);
        } else {
            throw new TokenCompareException("You don't have access to delete this comment advertisement");
        }
    }

    @Override
    public void closeAdvertisement(Long advertisementId, String token) throws TokenCompareException {
        Advertisement advertisement = advertisementJpaRepository.getReferenceById(advertisementId);
        if (advertisement.getUser().getId().equals(tokenExtractData.extractUserIdFromToken(token))) {
            advertisement.setDateClose(LocalDateTime.now());
            advertisementJpaRepository.save(advertisement);
        } else {
            throw new TokenCompareException("You don't have access to delete this comment advertisement");
        }
    }

    @Override
    public List<AdvertisementDto> getAdvertisementByUser(Long userId) {
        User user = userJpaRepository.getReferenceById(userId);
        List<Advertisement> advertisementList = advertisementJpaRepository.getByUserAndDateCloseIsNull(user);
        return advertisementList.stream().map(AdvertisementMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AdvertisementDto> getAdvertisementOrderByRating() {
        List<Advertisement> advertisementList = advertisementJpaRepository.getByDateCloseIsNullOrderByDateBonusDesc();
        return advertisementList.stream().map(AdvertisementMapper::toDto).collect(Collectors.toList());
    }

    //    @Scheduled(cron = "${AdvertisementServiceImpl.cronExpression}")
    private void scheduleCleanAdvertisementBonusDate() {
        LocalDateTime dateTime = LocalDateTime.now();
        List<Advertisement> advertisementList = advertisementJpaRepository.getAdvertisementByDateBonusNotNull();
        advertisementList.stream().forEach(advertisement -> cleanBonusDate(advertisement, dateTime));
    }

    private void cleanBonusDate(Advertisement advertisement, LocalDateTime dateTime) {
        if (advertisement.getDateBonus() != null && advertisement.getDateBonus().isBefore(dateTime)) {
            advertisement.setDateBonus(null);
            advertisementJpaRepository.save(advertisement);
        }
    }

    @Override
    public List<AdvertisementDto> getAdvertisementByFilter(List<FilterDto> filterDtoList, String fieldSort, Direction sort) throws OperationException {
        String[] fieldsSort = fieldSort.split(",");
        List<Filter<?>> filterList = filterDtoList.stream().map(filterDto -> FilterMapper.toFilter(filterDto, categoryJpaRepository)).collect(Collectors.toList());
        return advertisementCustomRepository.getQueryResult(filterList, Sort.by(sort, fieldsSort)).stream()
                .map(AdvertisementMapper::toDto).collect(Collectors.toList());
    }
}
