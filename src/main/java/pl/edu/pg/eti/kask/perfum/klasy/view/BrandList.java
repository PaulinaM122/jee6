package pl.edu.pg.eti.kask.perfum.klasy.view;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import pl.edu.pg.eti.kask.perfum.klasy.model.BrandModel;
import pl.edu.pg.eti.kask.perfum.klasy.service.BrandService;
import pl.edu.pg.eti.kask.perfum.component.ModelFunctionFactory;

import java.util.List;
import java.util.UUID;

/**
 * View bean for rendering list of brands.
 */
@RequestScoped
@Named
public class BrandList {

    private BrandService service;
    private final ModelFunctionFactory factory;

    private List<BrandModel> brands;

    @Inject
    public BrandList(ModelFunctionFactory factory) {
        this.factory = factory;
        loadBrands();
    }

    /**
     * @param brandService brand service
     */
    @EJB
    public void setService(BrandService brandService) {
        this.service = brandService;
    }

    /**
     * Load brands into the brands list.
     */
    public void loadBrands() {
        // Convert the list of Brand entities to BrandModels
        brands = factory.brandsToModel().apply(service.findAll());
    }

    public List<BrandModel> getBrands() {
        return brands;
    }

    /**
     * Deletes a brand by its ID.
     *
     * @param brand the brand to be deleted
     * @return navigation outcome
     */
    public String deleteAction(BrandModel brand) {
        service.delete(brand.getId());
        loadBrands();
        return "brand_list?faces-redirect=true";
    }
}