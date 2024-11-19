package pl.edu.pg.eti.kask.perfum.klasy.view;

import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.perfum.klasy.entity.Brand;
import pl.edu.pg.eti.kask.perfum.klasy.entity.Perfume;
import pl.edu.pg.eti.kask.perfum.klasy.model.BrandModel;
import pl.edu.pg.eti.kask.perfum.klasy.model.PerfumeModel;
import pl.edu.pg.eti.kask.perfum.klasy.service.BrandService;
import pl.edu.pg.eti.kask.perfum.component.ModelFunctionFactory;
import pl.edu.pg.eti.kask.perfum.klasy.service.PerfumeService;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * View bean for displaying details of a single brand.
 */
@ViewScoped
@Named
public class BrandView implements Serializable {

    private BrandService service;
    private final ModelFunctionFactory factory;
    private PerfumeService perfumeService;

    @Setter
    @Getter
    private UUID id;

    @Getter
    private BrandModel brand;

    @Getter
    private List<PerfumeModel> perfumes;

    @Inject
    public BrandView(ModelFunctionFactory factory) {
        this.factory = factory;
    }

    /**
     * @param perfumeService service for managing perfumes
     */
    @EJB
    public void setCharacterService(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    /**
     * @param brandService service for managing brand
     */
    @EJB
    public void setProfessionService(BrandService brandService) {
        this.service = brandService;
    }

    public void init() throws IOException {
        Optional<Brand> brandEntity = service.find(id);
        if (brandEntity.isPresent()) {
            this.brand = factory.brandToModel().apply(brandEntity.get());
            loadPerfumes(brandEntity.get().getId());
        } else {
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "Brand not found");
        }
    }

    private void loadPerfumes(UUID brandId) {
        Optional<List<Perfume>> perfumeEntitiesOpt = perfumeService.findAllByBrand(brandId);
        perfumes = perfumeEntitiesOpt.map(perfumesList -> perfumesList.stream()
                        .map(factory.perfumeToModel()::apply)
                        .collect(Collectors.toList()))
                .orElseGet(List::of);
    }

    public void deletePerfume(UUID perfumeId) {
        perfumeService.delete(perfumeId);
        loadPerfumes(brand.getId());
    }
}