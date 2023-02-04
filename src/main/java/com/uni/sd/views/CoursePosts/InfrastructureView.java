package com.uni.sd.views.CoursePosts;

import com.uni.sd.data.entity.Professor;
import com.uni.sd.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

import java.util.List;

import static com.uni.sd.views.CoursePosts.AlgebraView.getCard;

@PageTitle("Infrastructure of Servers")
@Route(value = "ios", layout = MainLayout.class)
@PermitAll
public class InfrastructureView extends Div implements AfterNavigationObserver {

    Grid<Professor> grid = new Grid<>();

    public InfrastructureView(){
        addClassName("card-list-view");;
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::createCard);
        add(grid);
    }

    private HorizontalLayout createCard(Professor professor) {

        return (HorizontalLayout) getCard(professor);
    }


    private static Professor createProfessor(String firstName, String lastName, String post, String image) {
        Professor professor = new Professor();
        professor.setFirstName(firstName);
        professor.setLastName(lastName);
        professor.setPost(post);
        professor.setImage(image);
        return professor;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        List<Professor> professors = List.of(
                createProfessor("Osman", "Osmani", "Lecture 1 - Introduction to Servers", "https://imgs.search.brave.com/k01hc-j_4vEv8Syhy4uYJDSUjmJiiakiCWrjDgY7a5I/rs:fit:640:640:1/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vNzM2/eC84Yi8xNi83YS84/YjE2N2FmNjUzYzIz/OTlkZDkzYjk1MmE0/ODc0MDYyMC5qcGc"),
                createProfessor("Osman", "Osmani", "Lecture 2 - Introduction to OSI Model", "https://imgs.search.brave.com/k01hc-j_4vEv8Syhy4uYJDSUjmJiiakiCWrjDgY7a5I/rs:fit:640:640:1/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vNzM2/eC84Yi8xNi83YS84/YjE2N2FmNjUzYzIz/OTlkZDkzYjk1MmE0/ODc0MDYyMC5qcGc"),
                createProfessor("Osman", "Osmani", "Lecture 3 - Introduction to IaC", "https://imgs.search.brave.com/k01hc-j_4vEv8Syhy4uYJDSUjmJiiakiCWrjDgY7a5I/rs:fit:640:640:1/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vNzM2/eC84Yi8xNi83YS84/YjE2N2FmNjUzYzIz/OTlkZDkzYjk1MmE0/ODc0MDYyMC5qcGc"),
                createProfessor("Osman", "Osmani", "Lecture 4 - Introduction to Cloud", "https://imgs.search.brave.com/k01hc-j_4vEv8Syhy4uYJDSUjmJiiakiCWrjDgY7a5I/rs:fit:640:640:1/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vNzM2/eC84Yi8xNi83YS84/YjE2N2FmNjUzYzIz/OTlkZDkzYjk1MmE0/ODc0MDYyMC5qcGc")
        );
        grid.setItems(professors);
    }
}
