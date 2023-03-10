package com.uni.sd.views.EntityViews;


import com.uni.sd.data.dto.ProfessorDto;
import com.uni.sd.data.dto.UserDto;
import com.uni.sd.data.entity.Professor;
import com.uni.sd.data.entity.User;
import com.uni.sd.data.service.ProfessorService;
import com.uni.sd.data.service.UserService;
import com.uni.sd.views.GeneralViews.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.util.Optional;

@PageTitle("Professors")
@Route(value = "professors/:ProfessorID?/:action?(edit)", layout = MainLayout.class)
@RouteAlias(value = "professors", layout = MainLayout.class)
@Uses(Icon.class)
@PermitAll
@RolesAllowed({"ADMIN", "MANAGER"})
public class ProfessorView extends Div implements BeforeEnterObserver{

    private final String Professor_ID = "ProfessorID";
    private final String Professor_EDIT_ROUTE_TEMPLATE = "professors/%s/edit";

    private final Grid<UserDto> grid = new Grid<>(UserDto.class, false);

    private TextField username;
    private ComboBox<String> userType;
    private TextField email;
    private ComboBox<String> roles;
    private TextField firstName;
    private TextField lastName;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");

    private final BeanValidationBinder<ProfessorDto> binder;
    TextField filterText = new TextField();
    private ProfessorDto professor;

    private final ProfessorService professorService;
    private final UserService userService;

    public ProfessorView(ProfessorService professorService, UserService userService) {
        this.professorService = professorService;
        this.userService = userService;
        addClassNames("master-detail-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);

        add(getToolbar(),splitLayout);

        grid.addColumn("username").setAutoWidth(true);
        grid.addColumn("firstName").setAutoWidth(true);
        grid.addColumn("lastName").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("userType").setAutoWidth(true);
        grid.addColumn("roles").setAutoWidth(true);


        userType = new ComboBox<>("User Type");
        roles = new ComboBox<>("Roles");
        userType.setItems("Student", "Professor", "Staff");
        roles.setItems("ROLE_ADMIN", "ROLE_USER", "ROLE_MANAGER");


        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setConfirmButtonTheme("error primary");
        confirmDialog.setHeader("Confirmation");
        confirmDialog.setText("Are you sure you want to delete this professor?");
        confirmDialog.setConfirmButton("Delete", e -> {
            if (this.professor != null && this.professor.getId() != null) {
                professorService.delete(this.professor.getId());
                clearForm();
                refreshGrid();
                Notification.show("Data deleted");
                UI.getCurrent().navigate(ProfessorView.class);
            }
        });
        confirmDialog.setCancelButton("Cancel", e -> confirmDialog.close());

        delete.addClickListener(e -> confirmDialog.open());


        grid.setItems(query -> this.userService.findAllUsers().stream()
                .filter(user -> user.getUserType().equals("Professor"))
                .skip(query.getPage() * query.getPageSize())
                .limit(query.getPageSize()));

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(Professor_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ProfessorView.class);
            }
        });

        binder = new BeanValidationBinder<>(ProfessorDto.class);
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.professor == null) {
                    this.professor = new ProfessorDto();
                }
                binder.writeBean(this.professor);
                professorService.update(this.professor);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(ProfessorView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Notification.Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> ProfessorId = event.getRouteParameters().get(Professor_ID).map(Long::parseLong);
        if (ProfessorId.isPresent()) {
            Optional<ProfessorDto> professorFromBackEnd = Optional.ofNullable(professorService.get(ProfessorId.get()));
            if (professorFromBackEnd.isPresent()) {
                populateForm(professorFromBackEnd.get());
            } else {
                Notification.show(
                        String.format("The requested Professor was not found, ID = %s", ProfessorId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                refreshGrid();
                event.forwardTo(ProfessorView.class);
            }
        }
    }




    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(ProfessorDto value) {
        this.professor = value;
        binder.readBean(this.professor);

    }

    private void updateList() {
        grid.setItems(query -> this.userService.findAllUsers(filterText.getValue()).stream()
                .filter(user -> user.getUserType().equals("Professor"))
                .skip(query.getPage() * query.getPageSize())
                .limit(query.getPageSize()));
    }

    private Component getToolbar() {

        filterText.setPlaceholder("Filter by professor name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        filterText.setWidth("250px");
        HorizontalLayout toolbarHorizontalLayout = new HorizontalLayout(filterText);
        toolbarHorizontalLayout.addClassName("toolbar");

        return toolbarHorizontalLayout;

    }
}
