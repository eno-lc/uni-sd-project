package com.uni.sd.views.EntityViews;

import com.uni.sd.data.dto.GradeDto;
import com.uni.sd.data.service.GradeService;
import com.uni.sd.views.GeneralViews.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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

@PageTitle("Grades")
@Route(value = "grade/:GradeID?/:action?(edit)", layout = MainLayout.class)
@RouteAlias(value = "grades", layout = MainLayout.class)
@Uses(Icon.class)
@PermitAll
public class GradeView extends Div implements BeforeEnterObserver {

    private final String Grade_ID = "GradeID";
    private final String Grade_EDIT_ROUTE_TEMPLATE = "grade/%s/edit";

    private final Grid<GradeDto> grid = new Grid<>(GradeDto.class, false);

    TextField filterText = new TextField();
    private TextField course;
    private TextField student;
    private TextField grade;
    private TextField professor;
    private DatePicker dateAssigned;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");

    private final BeanValidationBinder<GradeDto> binder;
    private final GradeService gradeService;
    private GradeDto gradeInstance;

    public GradeView( GradeService gradeService) {
        this.gradeService = gradeService;

        addClassNames("master-detail-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(getToolbar(),splitLayout);

        // Configure Grid
        grid.addColumn("course").setAutoWidth(true);
        grid.addColumn("student").setAutoWidth(true);
        grid.addColumn("grade").setAutoWidth(true);
        grid.addColumn("professor").setAutoWidth(true);
        grid.addColumn("dateAssigned").setAutoWidth(true);



        grid.setItems(query -> gradeService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(Grade_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(GradeView.class);
            }
        });


        binder = new BeanValidationBinder<>(GradeDto.class);
        binder.bindInstanceFields(this);


        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setConfirmButtonTheme("error primary");
        confirmDialog.setHeader("Confirmation");
        confirmDialog.setText("Are you sure you want to delete this grade?");

        confirmDialog.setConfirmButton("Delete", e -> {
            if (this.gradeInstance != null && this.gradeInstance.getId() != null) {
                gradeService.delete(this.gradeInstance.getId());
                clearForm();
                refreshGrid();
                Notification.show("Data deleted");
                UI.getCurrent().navigate(GradeView.class);
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
                if (this.gradeInstance == null) {
                    this.gradeInstance = new GradeDto();
                }

                binder.writeBean(this.gradeInstance);
                gradeService.update(this.gradeInstance);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(GradeView.class);
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
        Optional<Long> GradeId = event.getRouteParameters().get(Grade_ID).map(Long::parseLong);
        if (GradeId.isPresent()) {
            Optional<GradeDto> gradeFromBackend = Optional.ofNullable(gradeService.get(GradeId.get()));
            if (gradeFromBackend.isPresent()) {
                populateForm(gradeFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested grade was not found, ID = %s", GradeId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(GradeView.class);
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
        course = new TextField("Course");
        student = new TextField("Student");
        grade = new TextField("Grade");
        professor = new TextField("Professor");
        dateAssigned = new DatePicker("Date Assigned");
        formLayout.add(course, student, grade, professor, dateAssigned);

        editorDiv.add(formLayout);

        var name = SecurityContextHolder.getContext().getAuthentication();
        if (name.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER"))) {
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
        if (name.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER"))) {
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

    private void populateForm(GradeDto value) {
        this.gradeInstance = value;
        binder.readBean(this.gradeInstance);

    }

    private void updateList() {
        grid.setItems(gradeService.findAllGrades(filterText.getValue()));
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
