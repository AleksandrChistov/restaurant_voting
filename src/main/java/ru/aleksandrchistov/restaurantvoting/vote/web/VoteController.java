package ru.aleksandrchistov.restaurantvoting.vote.web;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.aleksandrchistov.restaurantvoting.app.AuthUser;
import ru.aleksandrchistov.restaurantvoting.vote.model.Vote;
import ru.aleksandrchistov.restaurantvoting.vote.repository.VoteRepository;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.aleksandrchistov.restaurantvoting.common.validation.RestValidation.checkNew;
import static ru.aleksandrchistov.restaurantvoting.vote.validation.VoteRestValidation.assureItIsNotAfterEleven;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    private final Logger log = getLogger(VoteController.class);

    static final String REST_URL = "/api/votes";

    @Autowired
    private VoteRepository repository;

    @PostMapping(value = REST_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody Vote vote) {
        log.info("create: {}", vote);
        Vote voteFound = repository.findByUserIdAndDateNow(AuthUser.authId());
        if (voteFound == null) {
            checkNew(vote);
        } else {
            assureItIsNotAfterEleven();
            vote.setId(voteFound.getId());
        }
        repository.prepareAndSave(vote);
    }

    @GetMapping(REST_URL)
    public Vote findForToday() {
        return repository.findByUserIdAndDateNow(AuthUser.authId());
    }
}
