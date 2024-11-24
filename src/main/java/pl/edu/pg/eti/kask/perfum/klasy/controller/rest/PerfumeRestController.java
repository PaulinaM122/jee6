package pl.edu.pg.eti.kask.perfum.klasy.controller.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBAccessException;
import jakarta.ejb.EJBException;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.perfum.klasy.controller.api.PerfumeController;
import pl.edu.pg.eti.kask.perfum.klasy.dto.GetPerfumeResponse;
import pl.edu.pg.eti.kask.perfum.klasy.dto.GetPerfumesResponse;
import pl.edu.pg.eti.kask.perfum.klasy.dto.PatchPerfumeRequest;
import pl.edu.pg.eti.kask.perfum.klasy.dto.PutPerfumeRequest;
import pl.edu.pg.eti.kask.perfum.klasy.entity.Brand;
import pl.edu.pg.eti.kask.perfum.klasy.entity.Perfume;
import pl.edu.pg.eti.kask.perfum.klasy.service.PerfumeService;
import pl.edu.pg.eti.kask.perfum.component.DtoFunctionFactory;
import java.util.UUID;
import java.util.logging.Level;
import pl.edu.pg.eti.kask.perfum.user.entity.UserRoles;

/**
 * Simple framework agnostic implementation of controller.
 */
@Path("")
@Log
public class PerfumeRestController implements PerfumeController {

    /**
     * Perfume service.
     */
    private PerfumeService service;

    /**
     * Factory producing functions for conversion between DTO and entities.
     */
    private final DtoFunctionFactory factory;

    /**
     * Allows to create {@link UriBuilder} based on current request.
     */
    private final UriInfo uriInfo;

    /**
     * Current HTTP Servlet response.
     */
    private HttpServletResponse response;

    @Context
    public void setResponse(HttpServletResponse response) {
        //ATM in this implementation only HttpServletRequest can be injected with CDI so JAX-RS injection is used.
        this.response = response;
    }

    /**
     * @param factory factory producing functions for conversion between DTO and entities
     * @param uriInfo allows to create {@link UriBuilder} based on current request
     */
    @Inject
    public PerfumeRestController(
            DtoFunctionFactory factory,
            @SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo
    ) {
        this.factory = factory;
        this.uriInfo = uriInfo;
    }

    /**
     * @param service perfume service
     */
    @EJB
    public void setService(PerfumeService service) {
        this.service = service;
    }

    @Override
    public GetPerfumesResponse getPerfumes() {
        return factory.perfumesToResponse().apply(service.findAll());
    }

    @Override
    public GetPerfumesResponse getBrandPerfumes(UUID id) {
        return service.findAllByBrand(id)
                .map(factory.perfumesToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public GetPerfumesResponse getUserPerfumes(UUID id) {
        return service.findAllByUser(id)
                .map(factory.perfumesToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public GetPerfumeResponse getPerfume(UUID id) {
        return service.find(id)
                .map(factory.perfumeToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    @SneakyThrows
    public void putPerfume(UUID id, PutPerfumeRequest request) {
        try {
            service.createForCallerPrincipal(factory.requestToPerfume().apply(id, request));
            response.setHeader("Location", uriInfo.getBaseUriBuilder()
                    .path(PerfumeController.class, "getPerfume")
                    .build(id)
                    .toString());
            throw new WebApplicationException(Response.Status.CREATED);
        } catch (IllegalArgumentException ex) {
            if (ex.getCause() instanceof IllegalArgumentException) {
                log.log(Level.WARNING, ex.getMessage(), ex);
                throw new BadRequestException(ex);
            }
            throw ex;
        }
    }

    @Override
    public void patchPerfume(UUID id, PatchPerfumeRequest request) {
        service.find(id).ifPresentOrElse(
                entity -> {
                    try {
                        service.update(factory.updatePerfume().apply(entity, request));
                    } catch (EJBAccessException ex) {
                        log.log(Level.WARNING, ex.getMessage(), ex);
                        throw new ForbiddenException(ex.getMessage());
                    }
                },
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public void deletePerfume(UUID id) {
        service.find(id).ifPresentOrElse(
                entity -> service.delete(id),
                () -> { throw new NotFoundException(); }
        );
    }

    @Override
    public void createPerfume(UUID brandId, UUID id, PutPerfumeRequest request) {
        if (request == null) {
            throw new BadRequestException("Request cannot be null.");
        }

        Perfume perfume = factory.requestToPerfume().apply(id, request);

        Brand brand = Brand.builder()
                .id(brandId)
                .build();

        perfume.setBrand(brand);

        if (perfume.getScentType() == null) {
            throw new BadRequestException("Scent Type cannot be null.");
        }

        service.create(perfume);
    }
    /*@Override
    public void patchPerfume(UUID id, PatchPerfumeRequest request) {
        if (request == null) {
            throw new BadRequestException("Request cannot be null.");
        }

        Perfume perfume = service.find(id).orElseThrow(NotFoundException::new);

        if (request.getName() != null) {
            perfume.setName(request.getName());
        }
        if (request.getPrice() != null) {
            perfume.setPrice(request.getPrice());
        }
        if (request.getScentType() != null) {
            perfume.setScentType(Perfume.ScentType.valueOf(request.getScentType()));
        }
        if (request.getDescription() != null) {
            perfume.setDescription(request.getDescription());
        }
        if (request.getReleaseDate() != null) {
            perfume.setReleaseDate(request.getReleaseDate());
        }
        if (request.getReleaseDate() != null) {
            perfume.setReleaseDate(request.getReleaseDate());
        }

        service.update(perfume);
    }*/

}