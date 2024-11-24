package pl.edu.pg.eti.kask.perfum.klasy.service;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJBAccessException;
import jakarta.security.enterprise.SecurityContext;
import pl.edu.pg.eti.kask.perfum.user.entity.User;
import pl.edu.pg.eti.kask.perfum.user.entity.UserRoles;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.perfum.klasy.entity.Perfume;
import pl.edu.pg.eti.kask.perfum.klasy.repository.api.PerfumeRepository;
import pl.edu.pg.eti.kask.perfum.klasy.repository.api.BrandRepository;
import pl.edu.pg.eti.kask.perfum.user.repository.api.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for all business actions regarding perfume entity.
 */
@LocalBean
@Stateless
@NoArgsConstructor(force = true)
public class PerfumeService {

    /**
     * Repository for perfume entity.
     */
    private final PerfumeRepository perfumeRepository;

    /**
     * Repository for brand entity.
     */
    private final BrandRepository brandRepository;

    /**
     * Repository for user entity.
     */
    private final UserRepository userRepository;

    /**
     * Security context
     */
    private final SecurityContext securityContext;

    /**
     * @param perfumeRepository  repository for perfume entity
     * @param brandRepository repository for brand entity
     * @param userRepository       repository for user entity
     * @param securityContext      security context
     */
    @Inject
    public PerfumeService(
            PerfumeRepository perfumeRepository,
            BrandRepository brandRepository,
            UserRepository userRepository,
            @SuppressWarnings("CdiInjectionPointsInspection") SecurityContext securityContext
    ) {
        this.perfumeRepository = perfumeRepository;
        this.brandRepository = brandRepository;
        this.userRepository = userRepository;
        this.securityContext = securityContext;
    }

    /**
     * Finds single perfume.
     *
     * @param id perfume's id
     * @return container with perfume
     */
    @RolesAllowed(UserRoles.USER)
    public Optional<Perfume> find(UUID id) {
        return perfumeRepository.find(id);
    }

    /**
     * @param id   character's id
     * @param user existing user
     * @return selected perfume for user
     */
    @RolesAllowed(UserRoles.USER)
    public Optional<Perfume> find(User user, UUID id) {
        return perfumeRepository.findByIdAndUser(id, user);
    }

    /**
     * @return selected perfume owned by the authenticated user
     */
    @RolesAllowed(UserRoles.USER)
    public Optional<Perfume> findForCallerPrincipal(UUID id) {
        if (securityContext.isCallerInRole(UserRoles.ADMIN)) {
            return find(id);
        }
        User user = userRepository.findByLogin(securityContext.getCallerPrincipal().getName())
                .orElseThrow(IllegalStateException::new);
        return find(user, id);
    }


    /**
     * @return all available perfumes
     */
    @RolesAllowed(UserRoles.USER)
    public List<Perfume> findAll() {
        return perfumeRepository.findAll();
    }

    /**
     * @param user existing user, perfume's owner
     * @return all available perfumes of the selected user
     */
    @RolesAllowed(UserRoles.USER)
    public List<Perfume> findAll(User user) {
        return perfumeRepository.findAllByUser(user);
    }

    /**
     * @return all available perfumes to th authenticated user
     */
    @RolesAllowed(UserRoles.USER)
    public List<Perfume> findAllForCallerPrincipal() {
        if (securityContext.isCallerInRole(UserRoles.ADMIN)) {
            return findAll();
        }
        User user = userRepository.findByLogin(securityContext.getCallerPrincipal().getName())
                .orElseThrow(IllegalStateException::new);
        return findAll(user);
    }

    /**
     * Creates new perfume.
     *
     * @param perfume new perfume
     */
    @RolesAllowed(UserRoles.ADMIN)
    public void create(Perfume perfume) {
        if (perfumeRepository.find(perfume.getId()).isPresent()) {
            throw new IllegalArgumentException("Perfume already exists.");
        }
        if (brandRepository.find(perfume.getBrand().getId()).isEmpty()) {
            throw new IllegalArgumentException("Brand does not exists.");
        }
        perfumeRepository.create(perfume);
        /* Both sides of relationship must be handled (if accessed) because of cache. */
        brandRepository.find(perfume.getBrand().getId())
                .ifPresent(brand -> brand.getPerfumes().add(perfume));
        //userRepository.find(perfume.getUser().getId())
        //        .ifPresent(user -> user.getPerfumes().add(perfume));
    }

    /**
     * Creates new perfume for current caller principal.
     *
     * @param perfume new perfume
     */
    @RolesAllowed(UserRoles.USER)
    public void createForCallerPrincipal(Perfume perfume) {
        User user = userRepository.findByLogin(securityContext.getCallerPrincipal().getName())
                .orElseThrow(IllegalStateException::new);

        perfume.setUser(user);
        create(perfume);
    }

    /**
     * Updates existing perfume.
     *
     * @param perfume perfume to be updated
     */
    @RolesAllowed(UserRoles.USER)
    public void update(Perfume perfume) {
        checkAdminRoleOrOwner(perfumeRepository.find(perfume.getId()));
        perfumeRepository.update(perfume);
    }

    /**
     * Deletes existing perfume.
     *
     * @param id existing perfume's id to be deleted
     */
    @RolesAllowed(UserRoles.USER)
    public void delete(UUID id) {
        checkAdminRoleOrOwner(perfumeRepository.find(id));
        perfumeRepository.delete(perfumeRepository.find(id).orElseThrow());
    }

    @RolesAllowed(UserRoles.USER)
    public Optional<List<Perfume>> findAllByBrand(UUID id) {
        return brandRepository.find(id)
                .map(perfumeRepository::findAllByBrand);
    }

    @RolesAllowed(UserRoles.USER)
    public Optional<List<Perfume>> findAllByUser(UUID id) {
        return userRepository.find(id)
                .map(perfumeRepository::findAllByUser);
    }

    /**
     * @param perfume perfume to be checked
     * @throws EJBAccessException when caller principal has no admin role and is not perfume's owner
     */
    private void checkAdminRoleOrOwner(Optional<Perfume> perfume) throws EJBAccessException {
        if (securityContext.isCallerInRole(UserRoles.ADMIN)) {
            return;
        }
        if (securityContext.isCallerInRole(UserRoles.USER)
                && perfume.isPresent()
                && perfume.get().getUser().getLogin().equals(securityContext.getCallerPrincipal().getName())) {
            return;
        }
        throw new EJBAccessException("Caller not authorized.");
    }

}