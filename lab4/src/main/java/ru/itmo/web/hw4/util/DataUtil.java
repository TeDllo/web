package ru.itmo.web.hw4.util;

import ru.itmo.web.hw4.model.Color;
import ru.itmo.web.hw4.model.Post;
import ru.itmo.web.hw4.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DataUtil {
    private static final List<User> USERS = Arrays.asList(
            new User(1, "MikeMirzayanov", "Mike Mirzayanov", Color.RED),
            new User(6, "pashka", "Pavel Mavrin", Color.BLUE),
            new User(9, "geranazavr555", "Georgiy Nazarov", Color.GREEN),
            new User(11, "tourist", "Gennady Korotkevich", Color.GREEN)
    );

    private static final List<Post> POSTS = Arrays.asList(
            new Post(1, "Educational Codeforces Round 138 [рейтинговый для Div. 2]", PostContainer.post1, 1),
            new Post(2, "Codeforces Round #828 (Div. 3)", PostContainer.post2, 6),
            new Post(3, "Codeforces Global Round 23", PostContainer.post3, 9)
    );

    public static void addData(HttpServletRequest request, Map<String, Object> data) {
        data.put("users", USERS);
        data.put("posts", POSTS);

        for (User user : USERS) {
            if (Objects.equals(request.getSession().getAttribute("logged_user_id"), user.getId())) {
                data.put("user", user);
            }
        }
    }
}
