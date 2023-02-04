package com.uni.sd.views;

import com.uni.sd.data.entity.User;
import com.uni.sd.data.service.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;

@PageTitle("User Management")
@Route(value = "users/:UserID?/:action?(edit)", layout = MainLayout.class)
@RouteAlias(value = "users", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("ADMIN")
public class UserView extends Div implements BeforeEnterObserver {

    private final String User_ID = "UserID";
    private final String User_EDIT_ROUTE_TEMPLATE = "users/%s/edit";

    private final Grid<User> grid = new Grid<>(User.class, false);

    private TextField username;
    private ComboBox<String> userType;
    private TextField email;
    private ComboBox<String> roles;
    private TextField firstName;
    private TextField lastName;
    private PasswordField password;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final Button delete = new Button("Delete");
    private final BeanValidationBinder<User> binder;

    private User user;
    TextField filterText = new TextField();
    private final UserService userService;

    public UserView(UserService userService) {
        this.userService = userService;
        addClassNames("master-detail-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(getToolbar(),splitLayout);

        // Configure Grid
        grid.addColumn("username").setAutoWidth(true);
        grid.addColumn("firstName").setAutoWidth(true);
        grid.addColumn("lastName").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("userType").setAutoWidth(true);
        grid.addColumn("roles").setAutoWidth(true);

        userType.setItems("Student", "Professor", "Staff");
        roles.setItems("ROLE_ADMIN", "ROLE_USER", "ROLE_MANAGER");
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setConfirmButtonTheme("error primary");
        confirmDialog.setHeader("Confirmation");
        confirmDialog.setText("Are you sure you want to delete this user?");
        confirmDialog.setConfirmButton("Delete", e -> {
            if (this.user != null && this.user.getId() != null) {
                userService.deleteUser(this.user.getId());
                clearForm();
                refreshGrid();
                Notification.show("Data deleted");
                UI.getCurrent().navigate(UserView.class);
            }
        });
        confirmDialog.setCancelButton("Cancel", e -> confirmDialog.close());

        delete.addClickListener(e -> confirmDialog.open());

        grid.setItems(query -> userService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(User_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(UserView.class);
            }
        });

        binder = new BeanValidationBinder<>(User.class);
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.user == null) {
                    this.user = new User();
                }
                binder.writeBean(this.user);
                userService.updateUser(this.user);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(UserView.class);
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
        Optional<Long> UserId = event.getRouteParameters().get(User_ID).map(Long::parseLong);
        if (UserId.isPresent()) {
            Optional<User> userFromBackend = userService.get(UserId.get());
            if (userFromBackend.isPresent()) {
                populateForm(userFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested user was not found, ID = %s", UserId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(UserView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        username = new TextField("Username");
        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        email = new TextField("Email");
        userType = new ComboBox<>("User Type");
        roles = new ComboBox<>("Roles");
        password = new PasswordField("Password");
        formLayout.add(username, firstName, lastName, email, userType, roles, password);

        editorDiv.add(formLayout);
        var name = SecurityContextHolder.getContext().getAuthentication();
        if (name.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_MANAGER"))) {
            editorLayoutDiv.setVisible(false);
            editorDiv.setVisible(false);

        }else{
            editorLayoutDiv.setVisible(true);
            editorDiv.setVisible(true);
            createButtonLayout(editorLayoutDiv);
            splitLayout.addToSecondary(editorLayoutDiv);
        }
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        var name = SecurityContextHolder.getContext().getAuthentication();
        if (name.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_MANAGER"))) {
            buttonLayout.setVisible(false);
            editorLayoutDiv.setVisible(false);
        }else{
            editorLayoutDiv.setVisible(true);
            buttonLayout.setVisible(true);
            buttonLayout.add(save, cancel, delete);
            editorLayoutDiv.add(buttonLayout);
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

    private void populateForm(User value) {
        this.user = value;
        binder.readBean(this.user);

    }

    private void updateList() {
        grid.setItems(userService.findAllUsers(filterText.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        filterText.setWidth("250px");

        HorizontalLayout toolbarHorizontalLayout = new HorizontalLayout(filterText);
        toolbarHorizontalLayout.addClassName("toolbar");

        return toolbarHorizontalLayout;
    }
}
