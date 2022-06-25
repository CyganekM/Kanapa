package com.senla.kanapa.service.mapper;

import com.senla.kanapa.entity.Advertisement;
import com.senla.kanapa.service.dto.AdvertisementDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AdvertisementMapper {

    public AdvertisementDto toDto(Advertisement advertisement) {
        return AdvertisementDto.builder()
                .advertisementId(advertisement.getId())
                .name(advertisement.getName())
                .price(advertisement.getPrice())
                .dateStart(advertisement.getDateStart())
                .description(advertisement.getDescription())
                .categoryId(advertisement.getCategory().getId())
                .userId(advertisement.getUser().getId())
                .build();
    }

    public Advertisement toAdvertisement(AdvertisementDto advertisementDto) {
        return Advertisement.builder()
                .name(advertisementDto.getName())
                .price(advertisementDto.getPrice())
                .description(advertisementDto.getDescription())
                .build();
    }

    public void toAdvertisement(Advertisement advertisement, AdvertisementDto advertisementDto) {
        advertisement.setPrice(advertisementDto.getPrice());
        advertisement.setDescription(advertisementDto.getDescription());
        advertisement.setName(advertisementDto.getName());
    }
}
