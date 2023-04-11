package com.gds.challenge.repository;

import com.gds.challenge.entity.User;
import com.gds.challenge.utils.UserSortType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Users Repository with custom queries built with entity manager
 */
@Repository
public class CustomUsersRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> getUserResult(float minSalary,
                                    float maxSalary,
                                    int offset,
                                    Optional<Integer> limit,
                                    Optional<UserSortType> sortType) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> fromRoot = cq.from(User.class);
        cq.select(fromRoot);
        cq.where(cb.between(fromRoot.get("salary"), minSalary, maxSalary));

        sortType.ifPresent(type -> {
            switch (type) {
                case NAME -> {
                    cq.orderBy(cb.asc(fromRoot.get("name")));
                    break;
                }
                case SALARY -> {
                    cq.orderBy(cb.asc(fromRoot.get("salary")));
                    break;
                }
            }
        });

        TypedQuery<User> typedQuery = entityManager
                .createQuery(cq)
                .setFirstResult(offset);

        if (limit.isPresent()) {
            typedQuery = typedQuery.setMaxResults(limit.get());
        }

        return typedQuery.getResultList();
    }

}
