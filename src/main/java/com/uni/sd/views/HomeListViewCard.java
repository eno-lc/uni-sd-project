package com.uni.sd.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.theme.lumo.LumoUtility.*;

public class HomeListViewCard extends ListItem {

    public HomeListViewCard(String text, String url, String professor) {
        addClassNames(Background.CONTRAST_5, Display.FLEX, FlexDirection.COLUMN, AlignItems.START, Padding.MEDIUM,
                BorderRadius.LARGE);

        Div div = new Div();
        div.addClassNames(Background.CONTRAST, Display.FLEX, AlignItems.CENTER, JustifyContent.CENTER,
                Margin.Bottom.MEDIUM, Overflow.HIDDEN, BorderRadius.MEDIUM, Width.FULL);
        div.setHeight("160px");

        Image image = new Image();
        image.setWidth("100%");
        image.setSrc(url);
        image.setAlt(text);

        div.add(image);

        Span header = new Span();
        header.addClassNames(FontSize.XLARGE, FontWeight.SEMIBOLD);
        header.setText(text);

        Span subtitle = new Span();
        subtitle.addClassNames(FontSize.SMALL, TextColor.SECONDARY);
        subtitle.setText(professor);


        Button badge = new Button();
        badge.getElement().setAttribute("theme", "badge");
        badge.setText("Visit");

        add(div, header, subtitle, badge);

    }
}
