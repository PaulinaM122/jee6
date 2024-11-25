package pl.edu.pg.eti.kask.perfum.user.controller.rest;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.perfum.user.controller.api.UserController;
import pl.edu.pg.eti.kask.perfum.user.dto.GetUserResponse;
import pl.edu.pg.eti.kask.perfum.user.dto.GetUsersResponse;
import pl.edu.pg.eti.kask.perfum.user.dto.PutUserRequest;
import pl.edu.pg.eti.kask.perfum.user.entity.User;
import pl.edu.pg.eti.kask.perfum.user.service.UserService;
import pl.edu.pg.eti.kask.perfum.component.DtoFunctionFactory;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Log
@Path("/users")
public class UserRestController implements UserController {

    private final DtoFunctionFactory factory;
    private UserService service;

    @Inject
    public UserRestController(DtoFunctionFactory factory) {
        this.factory = factory;
    }

    @EJB
    public void setService(UserService service) {
        this.service = service;
    }

    /**
     * Tworzy lub aktualizuje użytkownika na podstawie danych.
     *
     * @param id      ID użytkownika.
     * @param request dane użytkownika do utworzenia.
     */
    @Override
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void createUser(@PathParam("id") UUID id, PutUserRequest request) {
        User user = factory.requestToUser().apply(id, request);
        service.create(user);
    }

    /**
     * Pobiera listę wszystkich użytkowników.
     *
     * @return lista użytkowników w formacie GetUsersResponse.
     */
    @Override
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GetUsersResponse getUsers() {
        List<User> users = service.findAll();

        return factory.usersToResponse().apply(users);
    }
}