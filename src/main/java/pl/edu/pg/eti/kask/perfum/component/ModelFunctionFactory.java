package pl.edu.pg.eti.kask.perfum.component;

import jakarta.enterprise.context.ApplicationScoped;
import pl.edu.pg.eti.kask.perfum.klasy.entity.Brand;
import pl.edu.pg.eti.kask.perfum.klasy.entity.Perfume;
import pl.edu.pg.eti.kask.perfum.klasy.model.*;
import pl.edu.pg.eti.kask.perfum.klasy.model.function.*;
import pl.edu.pg.eti.kask.perfum.user.entity.User;
import pl.edu.pg.eti.kask.perfum.user.model.UserModel;
import pl.edu.pg.eti.kask.perfum.user.model.UsersModel;
import pl.edu.pg.eti.kask.perfum.user.model.function.UserToModelFunction;
import pl.edu.pg.eti.kask.perfum.user.model.function.UsersToModelFunction;

import java.util.function.Function;

/**
 * Factory for creating {@link Function} implementations for converting between various objects used in different layers.
 * Instead of injecting multiple function objects, a single factory is injected.
 */
@ApplicationScoped
public class ModelFunctionFactory {

    /**
     * Returns a function to convert a single {@link Perfume} to {@link PerfumeModel}.
     *
     * @return new instance
     */
    public PerfumeToModelFunction perfumeToModel() {
        return new PerfumeToModelFunction();
    }

    /**
     * Returns a function to convert a list of {@link Perfume} to {@link PerfumesModel}.
     *
     * @return new instance
     */
    public PerfumesToModelFunction perfumesToModel() {
        return new PerfumesToModelFunction();
    }

    /**
     * Returns a function to convert a single {@link Perfume} to {@link PerfumeEditModel}.
     *
     * @return new instance
     */
    public PerfumeToEditModelFunction perfumeToEditModel() {
        return new PerfumeToEditModelFunction(userToModel());
    }

    /**
     * Returns a function to convert a single {@link PerfumeModel} to {@link Perfume}.
     *
     * @return new instance
     */
    public ModelToPerfumeFunction modelToPerfume() {
        return new ModelToPerfumeFunction();
    }

    /**
     * Returns a function to update a {@link Perfume}.
     *
     * @return UpdatePerfumeFunction instance
     */
    public UpdatePerfumeWithModelFunction updatePerfume() {
        return new UpdatePerfumeWithModelFunction();
    }

    /**
     * Returns a function to convert a single {@link Brand} to {@link BrandModel}.
     *
     * @return new instance
     */
    public BrandToModelFunction brandToModel() {
        return new BrandToModelFunction();
    }

    /**
     * Returns a function to convert a list of {@link Brand} to {@link BrandModel}.
     *
     * @return new instance
     */
    public BrandsToModelFunction brandsToModel() {
        return new BrandsToModelFunction();
    }

    /**
     * Returns a function to update a {@link Brand} using {@link BrandEditModel}.
     *
     * @return UpdateBrandFunction instance
     */
    public UpdateBrandWithModelFunction updateBrand() {
        return new UpdateBrandWithModelFunction();
    }

    /**
     * Returns a function to convert a single {@link Brand} to {@link BrandEditModel}.
     *
     * @return new instance
     */
    public BrandToEditModelFunction brandToEditModel() {
        return new BrandToEditModelFunction();
    }

    /**
     * Returns a function to convert a single {@link User} to {@link UserModel}.
     *
     * @return UserToModelFunction instance
     */
    public UserToModelFunction userToModel() {
        return new UserToModelFunction();
    }

    /**
     * Returns a function to convert a list {@link Perfume} to {@link UsersModel}.
     *
     * @return UserToModelFunction instance
     */
    public UsersToModelFunction usersToModel() {
        return new UsersToModelFunction();
    }


}
