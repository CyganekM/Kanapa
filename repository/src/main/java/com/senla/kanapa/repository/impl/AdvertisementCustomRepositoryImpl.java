package com.senla.kanapa.repository.impl;

import com.senla.kanapa.entity.Advertisement;
import com.senla.kanapa.entity.Filter;
import com.senla.kanapa.repository.AdvertisementCustomRepository;
import com.senla.kanapa.repository.AdvertisementJpaRepository;
import com.senla.kanapa.repository.exception.OperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Component
@RequiredArgsConstructor
public class AdvertisementCustomRepositoryImpl implements AdvertisementCustomRepository {

    private final AdvertisementJpaRepository advertisementJpaRepository;

    private Specification<Advertisement> createSpecification(Filter<?> filter) throws OperationException {

        switch (filter.getQueryOperator()) {
            case EQUALS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(filter.getKey()), filter.getValue());
            case NOT_EQ:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(filter.getKey()), filter.getValue());
            case GREATER_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.gt(root.get(filter.getKey()), (Number) filter.getValue());
            case LESS_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lt(root.get(filter.getKey()), (Number) filter.getValue());
            case LIKE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(filter.getKey()), "%" + filter.getValue() + "%");
            case IN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.in(root.get(filter.getKey())).value(filter.getValues());
            default:
                throw new OperationException("Operation not supported yet");
        }
    }

    private Specification<Advertisement> getSpecificationFromFilters(List<Filter<?>> filterList) throws OperationException {
        Specification<Advertisement> specification = where(createSpecification(filterList.remove(0)));
        for (Filter<?> filter : filterList) {
            specification = specification.and(createSpecification(filter));
        }
        return specification;
    }

    @Override
    public List<Advertisement> getQueryResult(List<Filter<?>> filterList, Sort sort) throws OperationException {
        if (!filterList.isEmpty()) {
            Specification<Advertisement> advertisementSpecification = getSpecificationFromFilters(filterList);
            return advertisementJpaRepository.findAll(advertisementSpecification, sort);
        } else {
            return advertisementJpaRepository.findAll(sort);
        }
    }
}
