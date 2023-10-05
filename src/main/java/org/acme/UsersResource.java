package org.acme;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.storage.UserEntity;
import org.acme.storage.UserRepository;

@Path("/users")
public class UsersResource {
    @Inject
    UserRepository userRepository;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Uni<String> generateUser(String username) {
        return userRepository.persist(new UserEntity(username)).map(user -> user.id.toString());
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    public Multi<String> getUsers() {
        return userRepository.streamAll().map(user -> user.username);
    }
}
