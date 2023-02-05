package com.uni.sd.views.GeneralViews;

import com.uni.sd.views.CoursePosts.AlgebraView;
import com.uni.sd.views.CoursePosts.DSAView;
import com.uni.sd.views.CoursePosts.InfrastructureView;
import com.uni.sd.views.CoursePosts.JavaView;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.ListStyleType;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

import javax.annotation.security.PermitAll;

@PageTitle("Home")
@Route(value = "home", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class HomeView extends Main implements HasComponents, HasStyle {

    private OrderedList imageContainer;

    public HomeView() {
        constructUI();

        imageContainer.add(new HomeListViewCard("Data Structures and Algorithms",
                "https://i.ibb.co/FDS0NGZ/dsa.png", "Edmond Jahjaga", DSAView.class));
        imageContainer.add(new HomeListViewCard("Algebra II",
                "https://i.ibb.co/TPZK0vp/alg.png", "Rexhep Gjergji", AlgebraView.class));
        imageContainer.add(new HomeListViewCard("Infrastructure of Servers",
                "https://i.ibb.co/8MPRjqm/ios.png", "Osman Osmani", InfrastructureView.class));
        imageContainer.add(new HomeListViewCard("Advanced Java II",
                "https://i.postimg.cc/QMh4j1PW/java.png", "Fatos Maxhuni", JavaView.class));
    }

    private void constructUI() {
        addClassNames("image-list-view");
        addClassNames(MaxWidth.SCREEN_LARGE, Margin.Horizontal.AUTO, Padding.Bottom.LARGE, Padding.Horizontal.LARGE);

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames(AlignItems.CENTER, JustifyContent.BETWEEN);

        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("Courses and other materials");
        header.addClassNames(Margin.Bottom.NONE, Margin.Top.XLARGE, FontSize.XXXLARGE);
        Paragraph description = new Paragraph("Here you can find all the courses and other materials that you need to study.");
        description.addClassNames(Margin.Bottom.XLARGE, Margin.Top.NONE, TextColor.SECONDARY);
        headerContainer.add(header, description);


        imageContainer = new OrderedList();
        imageContainer.addClassNames(Gap.MEDIUM, Display.GRID, ListStyleType.NONE, Margin.NONE, Padding.NONE);

        container.add(headerContainer);
        add(container, imageContainer);

    }
}