package pl.edu.pg.eti.kask.perfum.klasy.model.function;

import lombok.SneakyThrows;
import pl.edu.pg.eti.kask.perfum.klasy.entity.Perfume;
import pl.edu.pg.eti.kask.perfum.klasy.model.PerfumeEditModel;
import pl.edu.pg.eti.kask.perfum.user.entity.User;

import java.io.Serializable;
import java.util.function.BiFunction;

/**
 * Returns new instance of {@link Perfume} based on provided value and updated with values from {@link PerfumeEditModel}.
 */
public class UpdatePerfumeWithModelFunction implements BiFunction<Perfume, PerfumeEditModel, Perfume>, Serializable {

    @Override
    @SneakyThrows
    public Perfume apply(Perfume entity, PerfumeEditModel request) {
        return Perfume.builder()
                .id(entity.getId())
                .name(request.getName())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .price(request.getPrice())
                .scentType(entity.getScentType())
                .brand(entity.getBrand())
                .user(User.builder()
                        .id(request.getUser().getId())
                        .build())
                .build();
    }
}