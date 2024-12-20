package pl.edu.pg.eti.kask.perfum.klasy.service;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.perfum.klasy.entity.Brand;
import pl.edu.pg.eti.kask.perfum.klasy.repository.api.BrandRepository;
import pl.edu.pg.eti.kask.perfum.user.entity.UserRoles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for all business actions regarding perfume's brand entity.
 */
@LocalBean
@Stateless
@NoArgsConstructor(force = true)
@Log
public class BrandService {

    /**
     * Repository for brand entity.
     */
    private final BrandRepository repository;

    /**
     * @param repository repository for brand entity
     */
    @Inject
    public BrandService(BrandRepository repository) {
        this.repository = repository;
    }

    /**
     * @param id brand's id
     * @return container with brand entity
     */
    public Optional<Brand> find(UUID id) {
        Optional<Brand> brand = repository.find(id);
        /* Until lazy loaded list of perfumes is not accessed it is not in cache, so it does not need bo te cared of. */
//        brand.ifPresent(value -> log.info("Number of brands: %d".formatted(value.getPerfumes().size())));
        return brand;
    }

    /**
     * @return all available brands
     */
    @PermitAll
    public List<Brand> findAll() {
        return repository.findAll();
    }

    /**
     * Stores new brand in the data store.
     *
     * @param brand new bran to be saved
     */
    @RolesAllowed(UserRoles.ADMIN)
    public void create(Brand brand) {
        repository.create(brand);
    }

    /**
     * Stores new brand in the data store.
     *
     * @param brand update brand to be saved
     */
    @RolesAllowed(UserRoles.ADMIN)
    public void update(Brand brand) {
        repository.update(brand);
    }

    /**
     * Deletes brand.
     *
     * @param id id of brand to be deleted
     */
    @RolesAllowed(UserRoles.ADMIN)
    public void delete(UUID id) {
        repository.delete(repository.find(id).orElseThrow());
    }

}