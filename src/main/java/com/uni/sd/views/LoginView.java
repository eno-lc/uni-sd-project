package com.uni.sd.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.cookieconsent.CookieConsent;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;


@Route(value = "login")
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterListener {

   private final LoginForm loginForm = new LoginForm();
    public static boolean isDark = false;


    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        loginForm.setAction("login");
        loginForm.getStyle().set("margin-bottom", "210px");
        loginForm.addClassName("login-form");
        loginForm.setForgotPasswordButtonVisible(false);


        HorizontalLayout header = new HorizontalLayout(loginForm);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.addClassName("login-header");


        CookieConsent cookieConsent = new CookieConsent();
        cookieConsent.setPosition(CookieConsent.Position.BOTTOM);
        cookieConsent.addClassName("cookie-consent");
        cookieConsent.setCookieName("cookieConsent");
        add(
                header,
                loginForm,
                cookieConsent
        );
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
