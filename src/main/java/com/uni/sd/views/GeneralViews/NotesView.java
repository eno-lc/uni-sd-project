package com.uni.sd.views.GeneralViews;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.richtexteditor.RichTextEditorVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Border;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderColor;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.Height;

import javax.annotation.security.PermitAll;

@PageTitle("Notes")
@Route(value = "notes", layout = MainLayout.class)
@PermitAll
public class NotesView extends Main {

    public NotesView() {
        addClassNames(Display.FLEX, Flex.GROW, Height.FULL);

        RichTextEditor editor = new RichTextEditor();
        editor.addClassNames(Border.RIGHT, BorderColor.CONTRAST_10, Flex.GROW);
        editor.addThemeVariants(RichTextEditorVariant.LUMO_NO_BORDER);
        add(editor);
    }
}