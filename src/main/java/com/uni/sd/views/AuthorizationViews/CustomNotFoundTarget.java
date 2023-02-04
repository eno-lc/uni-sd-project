package com.uni.sd.views.AuthorizationViews;


import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.RouteNotFoundError;

import javax.servlet.http.HttpServletResponse;

public class CustomNotFoundTarget extends RouteNotFoundError {

    private static final long serialVersionUID = 3337229943239284836L;

    @Override
    public int setErrorParameter(final BeforeEnterEvent event, final ErrorParameter<NotFoundException> parameter) {
        event.forwardTo(AccessDeniedView.class);
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
