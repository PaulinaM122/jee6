package pl.edu.pg.eti.kask.perfum.user.controller.api;

import pl.edu.pg.eti.kask.perfum.user.dto.GetUsersResponse;
import pl.edu.pg.eti.kask.perfum.user.dto.PutUserRequest;
import pl.edu.pg.eti.kask.perfum.user.entity.User;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/users")
public interface UserController {

    /**
     * Tworzy lub aktualizuje użytkownika.
     *
     * @param id      ID użytkownika.
     * @param request reprezentacja użytkownika do stworzenia/aktualizacji.
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void createUser(@PathParam("id") UUID id, PutUserRequest request);

    /**
     * Pobiera listę wszystkich użytkowników.
     *
     * @return reprezentacja wszystkich użytkowników.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    GetUsersResponse getUsers();
}