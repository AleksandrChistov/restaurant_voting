package ru.aleksandrchistov.restaurantvoting.vote.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.aleksandrchistov.restaurantvoting.common.BaseRepository;
import ru.aleksandrchistov.restaurantvoting.vote.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {
    List<Vote> findByUserIdAndCreatedAt(Integer userId, LocalDate createdAt);

    List<Vote> findAllByUserId(Integer userId);

    List<Vote> findAllByCreatedAt(LocalDate createdAt);

    @Transactional
    default Vote prepareAndSave(Vote vote, int authUserId) {
        vote.setUserId(authUserId);
        return save(vote);
    }
}
