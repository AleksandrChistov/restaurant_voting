package ru.aleksandrchistov.restaurantvoting.vote;

import ru.aleksandrchistov.restaurantvoting.MatcherFactory;
import ru.aleksandrchistov.restaurantvoting.vote.model.Vote;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);

    public static final int VOTE_ID = 1;

    public static Vote getUpdated() {
        return new Vote(VOTE_ID, 1, 1);
    }
}
