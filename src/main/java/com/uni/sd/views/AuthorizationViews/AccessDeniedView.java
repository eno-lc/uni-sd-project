package com.uni.sd.views.AuthorizationViews;

import com.uni.sd.views.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@Route(value = "not_allowed", layout = MainLayout.class)
@PageTitle("Not allowed")
@PermitAll
public class AccessDeniedView extends VerticalLayout {

    public AccessDeniedView() {

        H1 h1 = new H1("Oops! You are not allowed to access this page!");
        h1.addClassName("h1-not-allowed");
        add(h1);
    }
}
