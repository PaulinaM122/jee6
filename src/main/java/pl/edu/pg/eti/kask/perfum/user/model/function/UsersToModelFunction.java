package pl.edu.pg.eti.kask.perfum.user.model.function;

import pl.edu.pg.eti.kask.perfum.user.entity.User;
import pl.edu.pg.eti.kask.perfum.user.model.UsersModel;

import java.util.List;
import java.util.function.Function;

/**
 * Converts {@link List<User>} to {@link UsersModel}.
 */
public class UsersToModelFunction implements Function<List<User>, UsersModel> {

    @Override
    public UsersModel apply(List<User> entity) {
        return UsersModel.builder()
                .users(entity.stream()
                        .map(perfume -> UsersModel.User.builder()
                                .id(perfume.getId())
                                .login(perfume.getLogin())
                                .build())
                        .toList())
                .build();
    }

}

