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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void saveAdvertisement(AdvertisementDto advertisementDto, String token) {
        User user = userJpaRepository.getReferenceById(tokenExtractData.extractUserIdFromToken(token));
        log.info("The user {} started adding the ad", user.getUsername());
        Advertisement advertisement = AdvertisementMapper.toAdvertisement(advertisementDto);
        advertisement.setCategory(categoryJpaRepository.getReferenceById(advertisementDto.getCategoryId()));
        advertisement.setUser(user);
        advertisement.setDateStart(LocalDateTime.now());
        advertisementJpaRepository.save(advertisement);
        log.info("The user {} added an ad {}.", user.getUsername(), advertisement.getId());
    }

    @Override
    @Transactional
    public void editAdvertisement(Long advertisementId, AdvertisementDto advertisementDto, String token) throws TokenCompareException {
        Long userId = tokenExtractData.extractUserIdFromToken(token);
        log.info("The user {} is editing the ad.", userId);
        Advertisement advertisement = advertisementJpaRepository.getReferenceById(advertisementId);

        if (advertisement.getUser().getId().equals(userId)) {
            AdvertisementMapper.toAdvertisement(advertisement, advertisementDto);
            advertisementJpaRepository.save(advertisement);
            log.info("The operation of editing the ad {} by the user {} - was successful", advertisement.getId(), userId);
        } else {
            throw new TokenCompareException("You id = " + userId + " don't have access to edit this advertisement id = " + advertisement.getId());
        }
    }

    @Override
    @Transactional
    public void closeAdvertisement(Long advertisementId, String token) throws TokenCompareException {
        Long userId = tokenExtractData.extractUserIdFromToken(token);
        log.info("The user {} is closing the ad.", userId);
        Advertisement advertisement = advertisementJpaRepository.getReferenceById(advertisementId);
        if (advertisement.getUser().getId().equals(userId)) {
            advertisement.setDateClose(LocalDateTime.now());
            advertisementJpaRepository.save(advertisement);
            log.info("The operation of editing the ad {} by the user {} - was successful", advertisement.getId(), userId);
        } else {
            throw new TokenCompareException("You Id = " + userId + " don't have access to delete this comment advertisement Id = " + advertisement.getId());
        }
    }

    @Override
    @Transactional
    public List<AdvertisementDto> getAdvertisementByUser(Long userId) {
        User user = userJpaRepository.getReferenceById(userId);
        List<Advertisement> advertisementList = advertisementJpaRepository.getByUserAndDateCloseIsNull(user);
        return advertisementList.stream().map(AdvertisementMapper::toDto).collect(Collectors.toList());
    }

    //    @Scheduled(cron = "${AdvertisementServiceImpl.cronExpression}")
    @Transactional
    void scheduleCleanAdvertisementBonusDate() {
        log.info("Start of cleaning BonusDate");
        LocalDateTime dateTime = LocalDateTime.now();
        List<Advertisement> advertisementList = advertisementJpaRepository.getAdvertisementByDateBonusNotNull();
        advertisementList.stream().forEach(advertisement -> cleanBonusDate(advertisement, dateTime));
        log.info("End of cleaning BonusDate");
    }

    private void cleanBonusDate(Advertisement advertisement, LocalDateTime dateTime) {
        if (advertisement.getDateBonus() != null && advertisement.getDateBonus().isBefore(dateTime)) {
            advertisement.setDateBonus(null);
            advertisementJpaRepository.save(advertisement);
        }
    }

    @Transactional
    @Override
    public List<AdvertisementDto> getAdvertisementByFilter(List<FilterDto> filterDtoList, String fieldSort, Direction sort) throws OperationException {
        log.info("Start of ad selection");
        String[] fieldsSort = fieldSort.split(",");
        List<Filter<?>> filterList = filterDtoList.stream().map(filterDto -> FilterMapper.toFilter(filterDto, categoryJpaRepository)).collect(Collectors.toList());
        return advertisementCustomRepository.getQueryResult(filterList, Sort.by(sort, fieldsSort)).stream()
                .map(AdvertisementMapper::toDto).collect(Collectors.toList());
    }
}
