package pl.edu.pg.eti.kask.perfum.klasy.model.function;

import pl.edu.pg.eti.kask.perfum.klasy.entity.Perfume;
import pl.edu.pg.eti.kask.perfum.klasy.model.PerfumeEditModel;
import pl.edu.pg.eti.kask.perfum.user.model.function.UserToModelFunction;
import java.io.Serializable;
import java.util.function.Function;

/**
 * Converts {@link Perfume} to {@link PerfumeEditModel}.
 */
public class PerfumeToEditModelFunction implements Function<Perfume, PerfumeEditModel>, Serializable {

    /**
     * Converts {@link pl.edu.pg.eti.kask.perfum.user.entity.User} to {@link pl.edu.pg.eti.kask.perfum.user.model.UserModel}.
     */
    private final UserToModelFunction userToModelFunction;

    /**
     * @param userToModelFunction converts {@link pl.edu.pg.eti.kask.perfum.user.entity.User} to
     *                            {@link pl.edu.pg.eti.kask.perfum.user.model.UserModel}
     */
    public PerfumeToEditModelFunction(UserToModelFunction userToModelFunction) {
        this.userToModelFunction = userToModelFunction;
    }

    @Override
    public PerfumeEditModel apply(Perfume entity) {
        return PerfumeEditModel.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .releaseDate(entity.getReleaseDate())
                .price(entity.getPrice())
                .user(userToModelFunction.apply(entity.getUser()))
                .build();
    }
}
