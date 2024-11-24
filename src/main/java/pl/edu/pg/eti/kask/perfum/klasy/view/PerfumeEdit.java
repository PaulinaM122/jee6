package pl.edu.pg.eti.kask.perfum.klasy.view;

import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.perfum.klasy.entity.Perfume;
import pl.edu.pg.eti.kask.perfum.klasy.model.PerfumeEditModel;
import pl.edu.pg.eti.kask.perfum.klasy.service.PerfumeService;
import pl.edu.pg.eti.kask.perfum.component.ModelFunctionFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

/**
 * View bean for rendering single perfume edit form.
 */
@ViewScoped
@Named
public class PerfumeEdit implements Serializable {

    private PerfumeService service;
    private final ModelFunctionFactory factory;

    @Setter
    @Getter
    private UUID id;

    @Getter
    private PerfumeEditModel perfume;

    @Inject
    public PerfumeEdit(ModelFunctionFactory factory) {
        this.factory = factory;
    }

    public void init() throws IOException {
        Optional<Perfume> perfume = service.findForCallerPrincipal(id);
        if (perfume.isPresent()) {
            this.perfume = factory.perfumeToEditModel().apply(perfume.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "Perfume not found");
        }
    }

    /**
     * @param service service for managing perfumes
     */
    @EJB
    public void setService(PerfumeService service) {
        this.service = service;
    }

    public String saveAction() {
        service.update(factory.updatePerfume().apply(service.find(id).orElseThrow(), perfume));
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return viewId + "?faces-redirect=true&includeViewParams=true";
    }
}
