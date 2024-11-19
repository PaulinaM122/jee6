package pl.edu.pg.eti.kask.perfum.configuration.singleton;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import pl.edu.pg.eti.kask.perfum.klasy.entity.Perfume;
import pl.edu.pg.eti.kask.perfum.klasy.entity.Brand;
import pl.edu.pg.eti.kask.perfum.klasy.entity.Perfume.ScentType;
import pl.edu.pg.eti.kask.perfum.klasy.service.PerfumeService;
import pl.edu.pg.eti.kask.perfum.klasy.service.BrandService;
import pl.edu.pg.eti.kask.perfum.user.entity.User;
import pl.edu.pg.eti.kask.perfum.user.entity.UserRoles;
import pl.edu.pg.eti.kask.perfum.user.service.UserService;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * EJB singleton can be forced to start automatically when application starts. Injects proxy to the services and fills
 * database with default content. When using persistence storage application instance should be initialized only during
 * first run in order to init database with starting data. Good place to create first default admin user.
 */
@Singleton
@Startup
@TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
@NoArgsConstructor
public class InitializedData {

    /**
     * Services for brands, perfumes, and users.
     */
    private BrandService brandService;
    private PerfumeService perfumeService;
    private UserService userService;

    @EJB
    public void setCharacterService(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    /**
     * @param userService user service
     */
    @EJB
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param brandService brand service
     */
    @EJB
    public void setProfessionService(BrandService brandService) {
        this.brandService = brandService;
    }

    /**
     * Initializes database with some example values. Should be called after creating this object. This object should be
     * created only once.
     */
    @PostConstruct
    @SneakyThrows
    private void init() {
        if (userService.find("admin").isEmpty()) {
            User admin = User.builder()
                    .id(UUID.fromString("c4804e0f-769e-4ab9-9ebe-0578fb4f00a6"))
                    .login("admin")
                    .name("System")
                    .surname("Admin")
                    .birthDate(LocalDate.of(1990, 10, 21))
                    .email("admin@simplerpg.example.com")
                    .password("adminadmin")
                    .roles(List.of(UserRoles.ADMIN, UserRoles.USER))
                    .build();

            User kevin = User.builder()
                    .id(UUID.fromString("81e1c2a9-7f57-439b-b53d-6db88b071e4e"))
                    .login("kevin")
                    .name("Kevin")
                    .surname("Pear")
                    .birthDate(LocalDate.of(2001, 1, 16))
                    .email("kevin@example.com")
                    .password("useruser")
                    .roles(List.of(UserRoles.USER))
                    .build();

            User alice = User.builder()
                    .id(UUID.fromString("ed6cfb2a-cad7-47dd-9b56-9d1e3c7a4197"))
                    .login("alice")
                    .name("Alice")
                    .surname("Grape")
                    .birthDate(LocalDate.of(2002, 3, 19))
                    .email("alice@example.com")
                    .password("useruser")
                    .roles(List.of(UserRoles.USER))
                    .build();

            userService.create(admin);
            userService.create(kevin);
            userService.create(alice);

            // Create some default brands
            Brand dior = Brand.builder()
                    .id(UUID.fromString("a1d6a00b-4d4f-4dbd-9b8b-08b4c8b68b6b"))
                    .name("Dior")
                    .country("France")
                    .description("A luxury French fashion house known for perfumes.")
                    .build();

            Brand chanel = Brand.builder()
                    .id(UUID.fromString("b7d9c9be-8dfd-426b-826f-4715f88c2f2e"))
                    .name("Chanel")
                    .country("France")
                    .description("Chanel is a high-end luxury brand famous for timeless elegance.")
                    .build();

            brandService.create(dior);
            brandService.create(chanel);

            // Create some default perfumes
            Perfume sauvage = Perfume.builder()
                    .id(UUID.fromString("ed50cbf7-67e8-4a1b-9e4d-627d6a587b36"))
                    .name("Sauvage")
                    .description("A strong, bold scent with hints of pepper and bergamot.")
                    .releaseDate(new Date())
                    .brand(dior)
                    .user(kevin)
                    .price(120.0)
                    .scentType(ScentType.SPICY)
                    .build();

            Perfume jadore = Perfume.builder()
                    .id(UUID.fromString("ed99cbf7-67e8-4a1b-9e4d-627d6a582a99"))
                    .name("J'adore Eau de Perfume")
                    .description("Infinissime, the sensual, powerful and enveloping Dior women's fragrance.")
                    .releaseDate(new Date())
                    .brand(dior)
                    .user(kevin)
                    .price(330.0)
                    .scentType(ScentType.FRESH)
                    .build();

            Perfume cocoMademoiselle = Perfume.builder()
                    .id(UUID.fromString("3fa7d874-8a33-47d1-8a65-558c50a64d56"))
                    .name("Coco Mademoiselle")
                    .description("A fresh and vibrant fragrance with floral notes.")
                    .releaseDate(new Date())
                    .brand(chanel)
                    .user(alice)
                    .price(150.0)
                    .scentType(ScentType.FLORAL)
                    .build();

            Perfume bleuDeChanel = Perfume.builder()
                    .id(UUID.fromString("3fb2d874-8a33-47d1-8a65-558c50a63a88"))
                    .name("Bleu De Chanel")
                    .description("Woody aromatic fragrance, which is identified by the combination of \"aromatic herbs\" and an \"opulent center and base.\"")
                    .releaseDate(new Date())
                    .brand(chanel)
                    .user(alice)
                    .price(740.0)
                    .scentType(ScentType.WOODY)
                    .build();

            perfumeService.create(sauvage);
            perfumeService.create(cocoMademoiselle);
            perfumeService.create(jadore);
            perfumeService.create(bleuDeChanel);
        }
    }

    /**
     * @param name name of the desired resource
     * @return array of bytes read from the resource
     */
    @SneakyThrows
    private byte[] getResourceAsByteArray(String name) {
        try (InputStream is = this.getClass().getResourceAsStream(name)) {
            if (is != null) {
                return is.readAllBytes();
            } else {
                throw new IllegalStateException("Unable to get resource %s".formatted(name));
            }
        }
    }

}