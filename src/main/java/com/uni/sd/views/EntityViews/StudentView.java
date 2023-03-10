package com.uni.sd.views.EntityViews;

import com.uni.sd.data.dto.StudentDto;
import com.uni.sd.data.dto.UserDto;
import com.uni.sd.data.entity.Student;
import com.uni.sd.data.entity.User;
import com.uni.sd.data.service.StudentService;
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

import javax.annotation.security.RolesAllowed;

@PageTitle("Students")
@Route(value = "students/:StudentID?/:action?(edit)", layout = MainLayout.class)
@RouteAlias(value = "students", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed({"ADMIN", "MANAGER"})
public class StudentView extends Div implements BeforeEnterObserver {

    private final String Student_ID = "StudentID";
    private final String Student_EDIT_ROUTE_TEMPLATE = "students/%s/edit";

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
    private final BeanValidationBinder<StudentDto> binder;

    private StudentDto studentDto;
    TextField filterText = new TextField();
    private final StudentService StudentService;
    private final UserService userService;

    public StudentView(StudentService StudentService, UserService userService) {
        this.StudentService = StudentService;
        this.userService = userService;
        addClassNames("master-detail-view");

        userType = new ComboBox<>("User Type");
        roles = new ComboBox<>("Roles");

        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        add(getToolbar(),splitLayout);

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
        confirmDialog.setText("Are you sure you want to delete this student?");
        confirmDialog.setConfirmButton("Delete", e -> {
            if (this.studentDto != null && this.studentDto.getId() != null) {
                StudentService.delete(this.studentDto.getId());
                clearForm();
                refreshGrid();
                Notification.show("Data deleted");
                UI.getCurrent().navigate(StudentView.class);
            }
        });
        confirmDialog.setCancelButton("Cancel", e -> confirmDialog.close());

        delete.addClickListener(e -> confirmDialog.open());

        grid.setItems(query -> userService.findAllUsers().stream()
                .filter(user -> user.getUserType().equals("Student"))
                .skip(query.getPage() * query.getPageSize())
                .limit(query.getPageSize()));


        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(Student_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(StudentView.class);
            }
        });

        binder = new BeanValidationBinder<>(StudentDto.class);
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.studentDto == null) {
                    this.studentDto = new StudentDto();
                }
                binder.writeBean(this.studentDto);
                StudentService.update(this.studentDto);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(StudentView.class);
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
        Optional<Long> StudentId = event.getRouteParameters().get(Student_ID).map(Long::parseLong);
        if (StudentId.isPresent()) {
            Optional<StudentDto> StudentFromBackend = Optional.ofNullable(StudentService.get(StudentId.get()));
            if (StudentFromBackend.isPresent()) {
                populateForm(StudentFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested Student was not found, ID = %s", StudentId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                refreshGrid();
                event.forwardTo(StudentView.class);
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

    private void populateForm(StudentDto value) {
        this.studentDto = value;
        binder.readBean(this.studentDto);

    }


    private void updateList() {
        grid.setItems(query -> this.userService.findAllUsers(filterText.getValue()).stream()
                .filter(user -> user.getUserType().equals("Student"))
                .skip(query.getPage() * query.getPageSize())
                .limit(query.getPageSize()));
    }

    private Component getToolbar() {

        filterText.setPlaceholder("Filter by student name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        filterText.setWidth("250px");

        HorizontalLayout toolbarHorizontalLayout = new HorizontalLayout(filterText);
        toolbarHorizontalLayout.addClassName("toolbar");

        return toolbarHorizontalLayout;

    }
}
