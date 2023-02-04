package com.uni.sd.views;

import com.uni.sd.data.entity.User;
import com.uni.sd.data.service.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PageTitle("Person Form")
@Route(value = "profile", layout = MainLayout.class)
@PermitAll
public class ProfileView extends VerticalLayout {


    private final TextField username = new TextField("Username");
    private final PasswordField password = new PasswordField("Password");
    private final ComboBox<String> roles = new ComboBox<>("Role");
    private final EmailField email = new EmailField("Email");
    private final TextField firstName = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private final ComboBox<String> userType = new ComboBox<>("User Type");

    private final Binder<User> binder = new BeanValidationBinder<>(User.class);
    private final Button save = new Button("Save");
    private final UserService userService;
    Notification notification = new Notification("Profile updated successfully", 3000);
    public ProfileView(UserService userService, UserService userService1) {
        this.userService = userService1;
        roles.setEnabled(false);
        username.setEnabled(false);
        userType.setEnabled(false);
        firstName.setEnabled(false);
        lastName.setEnabled(false);
        email.setEnabled(false);
        addClassName("person-form-view");

        binder.bindInstanceFields(this);


        userType.setItems("Student", "Professor", "Staff");
        roles.setItems("ROLE_ADMIN", "ROLE_USER", "ROLE_MANAGER");


        binder.readBean(userService.getCurrentUser());
        save.addClickListener(click -> {
            validateAndSave();
            UI.getCurrent().getPage().reload();
        });

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());
    }
    //jom tu e rujt current user which means no changes
    private void validateAndSave() {
        var user = userService.getCurrentUser();
        try{
            binder.writeBean(user);
            userService.saveUser(user);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }



    private Component createTitle() {
        return new H3("Personal information");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(username, roles, password, email, firstName, lastName, userType);
        return formLayout;
    }


    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        return buttonLayout;
    }


}
