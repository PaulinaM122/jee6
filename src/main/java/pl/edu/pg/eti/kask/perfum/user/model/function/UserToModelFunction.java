package pl.edu.pg.eti.kask.perfum.user.model.function;

import pl.edu.pg.eti.kask.perfum.klasy.entity.Perfume;
import pl.edu.pg.eti.kask.perfum.klasy.model.PerfumeModel;
import pl.edu.pg.eti.kask.perfum.user.entity.User;
import pl.edu.pg.eti.kask.perfum.user.model.UserModel;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Converts {@link Perfume} to {@link PerfumeModel}.
 */
public class UserToModelFunction implements Function<User, UserModel>, Serializable {

    @Override
    public UserModel apply(User entity) {
        return UserModel.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .build();
    }

}
