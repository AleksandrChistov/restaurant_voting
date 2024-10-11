package ru.aleksandrchistov.restaurantvoting.vote.web;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.aleksandrchistov.restaurantvoting.app.AuthUser;
import ru.aleksandrchistov.restaurantvoting.common.error.IllegalRequestDataException;
import ru.aleksandrchistov.restaurantvoting.vote.model.Vote;
import ru.aleksandrchistov.restaurantvoting.vote.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.aleksandrchistov.restaurantvoting.common.validation.DateTimeValidator.checkItIsAfter;
import static ru.aleksandrchistov.restaurantvoting.common.validation.RestValidation.*;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    private final Logger log = getLogger(VoteController.class);
    static final int STOP_VOTE_HOURS = 11;
    static final String REST_URL = "/api/votes";
    static final String ADMIN_REST_URL = "/api/admin/votes";

    @Autowired
    private VoteRepository repository;

    @PostMapping(value = REST_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Vote createWithLocation(@Valid @RequestBody Vote vote, @AuthenticationPrincipal AuthUser authUser) {
        log.info("createWithLocation: {}", vote);
        checkNew(vote);
        return repository.prepareAndSave(vote, authUser.id());
    }

    @PutMapping(value = REST_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Vote vote, @PathVariable int id, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update: {} with id={}", vote, id);
        assureIdConsistent(vote, id);
        Vote fromDb = repository.getExisted(id);
        if (fromDb.getUserId() != authUser.id()) {
            throw new IllegalRequestDataException("You do not own this vote");
        }
        if (checkItIsAfter(LocalTime.of(STOP_VOTE_HOURS, 0))) {
            throw new IllegalRequestDataException("It's too late, vote can't be changed");
        }
        repository.prepareAndSave(vote, authUser.id());
    }

    @GetMapping(REST_URL)
    public List<Vote> findAllByAuthUserId(@AuthenticationPrincipal AuthUser authUser) {
        log.info("findAllByAuthUserId");
        return repository.findAllByUserId(authUser.id());
    }

    @GetMapping(REST_URL + "/today")
    public List<Vote> findByAuthUserIdAndDateNow(@AuthenticationPrincipal AuthUser authUser) {
        log.info("findByAuthUserIdAndDateNow");
        return repository.findByUserIdAndCreatedAt(authUser.id(), LocalDate.now());
    }

    @GetMapping(ADMIN_REST_URL)
    public List<Vote> findAllByUserIdAndCreatedAt(@Nullable @RequestParam Integer userId, @Nullable @RequestParam LocalDate createdAt) {
        log.info("findAllByUserIdAndCreatedAt: with userId={} and createdAt={}", userId, createdAt);
        if (userId != null && createdAt != null) {
            return repository.findByUserIdAndCreatedAt(userId, createdAt);
        } else if (userId != null) {
            return repository.findAllByUserId(userId);
        } else if (createdAt != null) {
            return repository.findAllByCreatedAt(createdAt);
        }
        return repository.findAll();
    }
}
