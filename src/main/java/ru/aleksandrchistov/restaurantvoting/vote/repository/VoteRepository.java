package ru.aleksandrchistov.restaurantvoting.vote.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.aleksandrchistov.restaurantvoting.app.AuthUser;
import ru.aleksandrchistov.restaurantvoting.common.BaseRepository;
import ru.aleksandrchistov.restaurantvoting.vote.model.Vote;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v FROM Vote v WHERE v.userId = :userId AND v.createdAt = CURRENT_DATE")
    Vote findByUserIdAndDateNow(Integer userId);

    @Transactional
    default void prepareAndSave(Vote vote) {
        vote.setUserId(AuthUser.authId());
        save(vote);
    }
}
