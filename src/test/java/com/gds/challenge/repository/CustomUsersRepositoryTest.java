package com.gds.challenge.repository;

import com.gds.challenge.entity.User;
import com.gds.challenge.utils.UserSortType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CustomUsersRepositoryTest {

    private final float MIN_SALARY = 0.0f;
    private final float MAX_SALARY = 4000.0f;
    private final int OFFSET = 0;
    @InjectMocks
    CustomUsersRepository customUsersRepository;
    @Mock
    EntityManager entityManager;
    @Mock
    CriteriaBuilder criteriaBuilder;

    @Mock
    CriteriaQuery<User> criteriaQuery;
    @Mock
    Root<User> root;
    @Mock
    TypedQuery<User> typedQuery;

    @Mock
    Order order;

    @Mock
    Path path;

    @BeforeEach
    public void setUp() {
        when(root.get(anyString())).thenReturn(path);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(User.class)).thenReturn(criteriaQuery);
        when(criteriaBuilder.asc(any(Expression.class))).thenReturn(order);
        when(criteriaQuery.from(User.class)).thenReturn(root);
        when(entityManager.createQuery(ArgumentMatchers.any(CriteriaQuery.class))).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(anyInt())).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(new ArrayList<>());
    }

    @Test
    void getUserResult() {

        customUsersRepository.getUserResult(MIN_SALARY, MAX_SALARY, OFFSET, Optional.empty(), Optional.empty());

        verify(typedQuery, never()).setMaxResults(anyInt());
        verify(criteriaQuery, never()).orderBy(any(Order.class));

        verify(criteriaBuilder, times(1)).between(ArgumentMatchers.any(), eq(MIN_SALARY), eq(MAX_SALARY));
        verify(typedQuery, times(1)).setFirstResult(eq(OFFSET));
    }

    @Test
    void getUserResult_with_limit_and_sort() {
        final int limit = 10;

        customUsersRepository.getUserResult(MIN_SALARY, MAX_SALARY, OFFSET,
                Optional.of(limit),
                Optional.of(UserSortType.NAME));

        verify(typedQuery, times(1)).setMaxResults(eq(10));
        verify(criteriaQuery, times(1)).orderBy(any(Order.class));

        verify(criteriaBuilder, times(1)).between(ArgumentMatchers.any(), eq(MIN_SALARY), eq(MAX_SALARY));
        verify(typedQuery, times(1)).setFirstResult(eq(OFFSET));
    }
}