package com.uni.sd.views.EntityViews;

import com.uni.sd.data.dto.StaffDto;
import com.uni.sd.data.dto.UserDto;
import com.uni.sd.data.entity.Staff;
import com.uni.sd.data.entity.User;
import com.uni.sd.data.service.StaffService;
import com.uni.sd.data.service.UserService;
import com.uni.sd.views.GeneralViews.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.util.Optional;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

@PageTitle("Staff")
@Route(value = "staff/:StaffID?/:action?(edit)", layout = MainLayout.class)
@RouteAlias(value = "staff", layout = MainLayout.class)
@Uses(Icon.class)
@PermitAll
@RolesAllowed("ADMIN")
public class StaffView extends Div implements BeforeEnterObserver {

    private final String Staff_ID = "StaffID";
    private final Grid<UserDto> grid = new Grid<>(UserDto.class, false);
    TextField filterText = new TextField();
    private ComboBox<String> userType;
    private ComboBox<String> roles;


    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<StaffDto> binder;

    private StaffDto staff;

    private final StaffService staffService;
    private final UserService userService;

    public StaffView(StaffService staffService, UserService userService) {
        this.staffService = staffService;
        this.userService = userService;
        addClassNames("master-detail-view");

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

        grid.setItems(query -> this.userService.findAllUsers().stream()
                .filter(user -> user.getUserType().equals("Staff"))
                .skip(query.getPage() * query.getPageSize())
                .limit(query.getPageSize()));


        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);



        binder = new BeanValidationBinder<>(StaffDto.class);
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.staff == null) {
                    this.staff = new StaffDto();
                }
                binder.writeBean(this.staff);
                staffService.update(this.staff);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(StaffView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> StaffId = event.getRouteParameters().get(Staff_ID).map(Long::parseLong);
        if (StaffId.isPresent()) {
            Optional<StaffDto> staffFromBackend = Optional.ofNullable(staffService.get(StaffId.get()));
            if (staffFromBackend.isPresent()) {
                populateForm(staffFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested Staff memeber was not found, ID = %s", StaffId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(StaffView.class);
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

    private void populateForm(StaffDto value) {
        this.staff = value;
        binder.readBean(this.staff);

    }

    private void updateList() {
        grid.setItems(query -> this.userService.findAllUsers(filterText.getValue()).stream()
                .filter(user -> user.getUserType().equals("Staff"))
                .skip(query.getPage() * query.getPageSize())
                .limit(query.getPageSize()));
    }

    private Component getToolbar() {

        filterText.setPlaceholder("Filter by staff name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        filterText.setWidth("250px");

        HorizontalLayout toolbarHorizontalLayout = new HorizontalLayout(filterText);
        toolbarHorizontalLayout.addClassName("toolbar");

        return toolbarHorizontalLayout;

    }
}
