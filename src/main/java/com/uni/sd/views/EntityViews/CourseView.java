package com.uni.sd.views.EntityViews;

import com.uni.sd.data.dto.CourseDto;
import com.uni.sd.data.service.CourseService;
import com.uni.sd.views.GeneralViews.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.security.PermitAll;
import java.util.Optional;

@PageTitle("Courses")
@Route(value = "course/:CourseID?/:action?(edit)", layout = MainLayout.class)
@RouteAlias(value = "courses", layout = MainLayout.class)
@Uses(Icon.class)
@PermitAll
public class CourseView extends Div implements BeforeEnterObserver {

    private final String Course_ID = "CourseID";
    private final String Course_EDIT_ROUTE_TEMPLATE = "course/%s/edit";

    private final Grid<CourseDto> grid = new Grid<>(CourseDto.class, false);

    TextField filterText = new TextField();
    private TextField name;
    private TextField professor;
    private TextField ects;
    private TextField enrollmentCode;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");

    private final BeanValidationBinder<CourseDto> binder;
    private final CourseService courseService;
    private CourseDto courseInstance;
    public CourseView( CourseService courseService) {
        this.courseService = courseService;

        addClassNames("master-detail-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(getToolbar(),splitLayout);

        // Configure Grid
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("professor").setAutoWidth(true);
        grid.addColumn("ects").setAutoWidth(true);
        grid.addColumn("enrollmentCode").setAutoWidth(true);



        grid.setItems(query -> courseService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(Course_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(CourseView.class);
            }
        });


        binder = new BeanValidationBinder<>(CourseDto.class);
        binder.bindInstanceFields(this);


        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setConfirmButtonTheme("error primary");
        confirmDialog.setHeader("Confirmation");
        confirmDialog.setText("Are you sure you want to delete this course?");

        confirmDialog.setConfirmButton("Delete", e -> {
            if (this.courseInstance != null && this.courseInstance.getId() != null) {
                courseService.delete(this.courseInstance.getId());
                clearForm();
                refreshGrid();
                Notification.show("Data deleted");
                UI.getCurrent().navigate(CourseView.class);
            }
        });

        confirmDialog.setCancelButton("Cancel", e -> confirmDialog.close());

        delete.addClickListener(e -> confirmDialog.open());

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });



        save.addClickListener(e -> {
            try {
                if (this.courseInstance == null) {
                    this.courseInstance = new CourseDto();
                }

                binder.writeBean(this.courseInstance);
                courseService.update(this.courseInstance);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(CourseView.class);
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
        Optional<Long> CourseId = event.getRouteParameters().get(Course_ID).map(Long::parseLong);
        if (CourseId.isPresent()) {
            Optional<CourseDto> courseFromBackend = Optional.ofNullable(courseService.get(CourseId.get()));
            if (courseFromBackend.isPresent()) {
                populateForm(courseFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested course was not found, ID = %s", CourseId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(CourseView.class);
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
        name = new TextField("Name");
        professor = new TextField("Professor");
        ects = new TextField("Ects");
        enrollmentCode = new TextField("Enrollment Code");

        formLayout.add(name,professor, ects, enrollmentCode);

        editorDiv.add(formLayout);

        var name = SecurityContextHolder.getContext().getAuthentication();
        if (name.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER"))) {
            editorLayoutDiv.setVisible(false);
            editorDiv.setVisible(false);

        } else {
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
        if (name.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER"))) {
            buttonLayout.setVisible(false);
            editorLayoutDiv.setVisible(false);
        } else {
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

    private void populateForm(CourseDto value) {
        this.courseInstance = value;
        binder.readBean(this.courseInstance);

    }

    private void updateList() {
        grid.setItems(courseService.findAllCourses(filterText.getValue()));
    }

    private Component getToolbar() {

        filterText.setPlaceholder("Filter by course name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        filterText.setWidth("250px");

        HorizontalLayout toolbarHorizontalLayout = new HorizontalLayout(filterText);
        toolbarHorizontalLayout.addClassName("toolbar");

        return toolbarHorizontalLayout;

    }

}
