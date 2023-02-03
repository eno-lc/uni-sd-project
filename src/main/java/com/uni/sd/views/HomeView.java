package com.uni.sd.views;

import com.uni.sd.views.MainLayout;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
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
                "https://imgs.search.brave.com/VknzcO1pJ65_0j-KSzneebBXTUsz5PLKupZR5UrMiBQ/rs:fit:1200:720:1/g:ce/aHR0cHM6Ly9taXJv/Lm1lZGl1bS5jb20v/bWF4LzI1NjAvMCpW/aXBWWUFlQ0JRU3Fz/M05DLmpwZw", "Edmond Jahjaga", StudentView.class));
        imageContainer.add(new HomeListViewCard("Algebra II",
                "https://imgs.search.brave.com/uCMeeoEWM96PwbPZxQ7If9OTHXE6Tk6JoUkLv4Eic70/rs:fit:946:316:1/g:ce/aHR0cHM6Ly9tYXRo/Yml0c25vdGVib29r/LmNvbS9BbGdlYnJh/Mi9JbWFnZXMvQWxn/ZWJyYTJMb2dvMWEu/anBn", "Rexhep Gjergji", StudentView.class));
        imageContainer.add(new HomeListViewCard("Advanced Java II",
                "https://imgs.search.brave.com/3z63nswKi-RR6ao6jK04HzzBkok3WF8Y1o_Y2kl7jJI/rs:fit:1200:440:1/g:ce/aHR0cHM6Ly93d3cu/ZmlsZXBpY2tlci5p/by9hcGkvZmlsZS8w/b1dUR0N0VFJPS2tO/ejhiRndEMA", "Fatos Maxhuni", StudentView.class));
        imageContainer.add(new HomeListViewCard("Infrastructure of Servers",
                "https://imgs.search.brave.com/jvCPbB6yp7LftVPGirvLyV3347Z_UMXWaWvytfKmCmU/rs:fit:1088:437:1/g:ce/aHR0cHM6Ly9kZWFs/cy5oYXJsZXlveGZv/cmQuY29tL3dwLWNv/bnRlbnQvdXBsb2Fk/cy8yMDIwLzA3L21j/c2Utd2luZG93cy1z/ZXJ2ZXItMjAxMi1p/bXBsZW1lbnRpbmct/YW4tYWR2YW5jZWQt/c2VydmVyLWluZnJh/c3RydWN0dXJlLWV4/YW0tNzAtNDE0Lmpw/Zw", "Osman Osmani", StudentView.class));

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