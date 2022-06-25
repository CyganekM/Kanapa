package com.senla.kanapa.repository.impl;

import com.senla.kanapa.entity.Advertisement;
import com.senla.kanapa.entity.Filter;
import com.senla.kanapa.entity.QueryOperator;
import com.senla.kanapa.repository.AdvertisementJpaRepository;
import com.senla.kanapa.repository.exception.OperationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AdvertisementCustomRepositoryImpl.class})
@ExtendWith(SpringExtension.class)
class AdvertisementCustomRepositoryImplTest {
    @Autowired
    private AdvertisementCustomRepositoryImpl advertisementCustomRepositoryImpl;

    @MockBean
    private AdvertisementJpaRepository advertisementJpaRepository;

    @Test
    void AdvertisementCustomRepositoryImpl_getQueryResult() throws OperationException {
        ArrayList<Advertisement> advertisementList = new ArrayList<>();
        when(this.advertisementJpaRepository.findAll((Sort) any())).thenReturn(advertisementList);
        ArrayList<Filter<?>> filterList = new ArrayList<>();
        Sort unsortedResult = Sort.unsorted();
        List<Advertisement> actualQueryResult = this.advertisementCustomRepositoryImpl.getQueryResult(filterList,
                unsortedResult);
        assertSame(advertisementList, actualQueryResult);
        assertTrue(actualQueryResult.isEmpty());
        verify(this.advertisementJpaRepository).findAll((Sort) any());
        assertTrue(unsortedResult.toList().isEmpty());
    }

    @Test
    void AdvertisementCustomRepositoryImpl_getQueryResult2() throws OperationException {
        ArrayList<Advertisement> advertisementList = new ArrayList<>();
        when(this.advertisementJpaRepository
                .findAll((org.springframework.data.jpa.domain.Specification<Advertisement>) any(), (Sort) any()))
                .thenReturn(advertisementList);
        when(this.advertisementJpaRepository.findAll((Sort) any())).thenReturn(new ArrayList<>());
        Filter filter = mock(Filter.class);
        when(filter.getQueryOperator()).thenReturn(QueryOperator.GREATER_THAN);

        ArrayList<Filter<?>> filterList = new ArrayList<>();
        filterList.add(filter);
        Sort unsortedResult = Sort.unsorted();
        List<Advertisement> actualQueryResult = this.advertisementCustomRepositoryImpl.getQueryResult(filterList,
                unsortedResult);
        assertSame(advertisementList, actualQueryResult);
        assertTrue(actualQueryResult.isEmpty());
        verify(this.advertisementJpaRepository)
                .findAll((org.springframework.data.jpa.domain.Specification<Advertisement>) any(), (Sort) any());
        verify(filter).getQueryOperator();
        assertTrue(filterList.isEmpty());
        assertTrue(unsortedResult.toList().isEmpty());
    }

    @Test
    void AdvertisementCustomRepositoryImpl_getQueryResult4() throws OperationException {
        ArrayList<Advertisement> advertisementList = new ArrayList<>();
        when(this.advertisementJpaRepository
                .findAll((org.springframework.data.jpa.domain.Specification<Advertisement>) any(), (Sort) any()))
                .thenReturn(advertisementList);
        when(this.advertisementJpaRepository.findAll((Sort) any())).thenReturn(new ArrayList<>());
        Filter filter = mock(Filter.class);
        when(filter.getQueryOperator()).thenReturn(QueryOperator.GREATER_THAN);
        Filter filter1 = mock(Filter.class);
        when(filter1.getQueryOperator()).thenReturn(QueryOperator.GREATER_THAN);

        ArrayList<Filter<?>> filterList = new ArrayList<>();
        filterList.add(filter1);
        filterList.add(filter);
        Sort unsortedResult = Sort.unsorted();
        List<Advertisement> actualQueryResult = this.advertisementCustomRepositoryImpl.getQueryResult(filterList,
                unsortedResult);
        assertSame(advertisementList, actualQueryResult);
        assertTrue(actualQueryResult.isEmpty());
        verify(this.advertisementJpaRepository)
                .findAll((org.springframework.data.jpa.domain.Specification<Advertisement>) any(), (Sort) any());
        verify(filter1).getQueryOperator();
        verify(filter).getQueryOperator();
        assertEquals(1, filterList.size());
        assertTrue(unsortedResult.toList().isEmpty());
    }
}

