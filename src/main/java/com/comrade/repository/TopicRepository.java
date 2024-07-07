package com.comrade.repository;

import com.comrade.entity.TopicEntity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.jpa.internal.util.LockOptionsHelper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;
public interface TopicRepository extends JpaRepository<TopicEntity,Long> {
    //LockMode.UPGRADE_SKIPLOCKED
    //LockOptions.SKIP_LOCKED = -2
    //LockOptions.SKIP_LOCKED
    //LockMode.
    //LockOptionsHelper.
//    @QueryHints({
//            @QueryHint(name = "javax.persistence.lock.timeout",
//                       value = "-2"
//                      )
//    })
    @QueryHints({
            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "-2")
    })
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<TopicEntity> findTop10ByStatusOrderByCreatedDateAsc(String status);

    @QueryHints({
            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "-2")
    })
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<TopicEntity> findTop100ByStatusOrderByCreatedDateAsc(String status);
}
