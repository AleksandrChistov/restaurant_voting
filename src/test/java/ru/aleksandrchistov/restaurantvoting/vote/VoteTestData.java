package ru.aleksandrchistov.restaurantvoting.vote;

import ru.aleksandrchistov.restaurantvoting.MatcherFactory;
import ru.aleksandrchistov.restaurantvoting.vote.model.Vote;

import static ru.aleksandrchistov.restaurantvoting.user.UserTestData.*;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "userId");

    public static final int USER_VOTE_ID = 1;
    public static final int ADMIN_VOTE_ID = 2;
    public static final Vote USER_VOTE = new Vote(USER_VOTE_ID, USER_ID, 2);
    public static final Vote ADMIN_VOTE = new Vote(ADMIN_VOTE_ID, ADMIN_ID, 1);

    public static Vote getNew() {
        return new Vote(null, GUEST_ID, 1);
    }

    public static Vote getUpdated() {
        return new Vote(USER_VOTE_ID, 1, 1);
    }
}
