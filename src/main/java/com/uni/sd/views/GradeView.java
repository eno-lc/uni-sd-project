//package com.uni.sd.views;
//
//import com.uni.sd.data.entity.Grade;
//import com.uni.sd.data.entity.Student;
//import com.uni.sd.data.service.GradeService;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.datepicker.DatePicker;
//import com.vaadin.flow.component.dependency.Uses;
//import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.component.html.Div;
//import com.vaadin.flow.component.icon.Icon;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.data.binder.BeanValidationBinder;
//import com.vaadin.flow.router.PageTitle;
//import com.vaadin.flow.router.Route;
//import com.vaadin.flow.router.RouteAlias;
//
//import javax.annotation.security.RolesAllowed;
//
//@PageTitle("Grades")
//@Route(value = "grade/:GradeID?/:action?(edit)", layout = MainLayout.class)
//@RouteAlias(value = "grades", layout = MainLayout.class)
//@Uses(Icon.class)
//@RolesAllowed({"ADMIN", "MANAGER"})
//public class GradeView extends Div {
//
//    private final String Student_ID = "GradeID";
//    private final String Student_EDIT_ROUTE_TEMPLATE = "grade/%s/edit";
//
//    private final Grid<Grade> grid = new Grid<>(Grade.class, false);
//
//    private TextField course;
//    private TextField student;
//    private TextField grade;
//    private TextField professor;
//    private DatePicker dateAssigned;
//
//    private final Button cancel = new Button("Cancel");
//    private final Button save = new Button("Save");
//
//    private final BeanValidationBinder<Grade> binder;
//    private final GradeService gradeService;
//
//    public GradeView( GradeService gradeService) {
//        this.gradeService = gradeService;
//        binder = new BeanValidationBinder<>(Grade.class);
//    }
//}
