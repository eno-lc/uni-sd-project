package com.uni.sd.views.CoursePosts;

import com.uni.sd.data.entity.Professor;
import com.uni.sd.views.GeneralViews.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import javax.validation.constraints.NotNull;
import java.util.List;


@PageTitle("Algebra")
@Route(value = "algebra", layout = MainLayout.class)
@PermitAll
public class AlgebraView extends Div implements AfterNavigationObserver {

    Grid<Professor> grid = new Grid<>();

    public AlgebraView(){
        addClassName("card-list-view");;
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(professor -> createCard(professor));
        add(grid);
    }

    private Component createCard(Professor professor) {
        return getCard(professor);
    }

    @NotNull
    public static Component getCard(Professor professor) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        Image image = new Image();
        image.setSrc(professor.getImage());
        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        Span name = new Span(professor.getFirstName()+" "+professor.getLastName());
        name.addClassName("name");
        header.add(name);

        Span post = new Span(professor.getPost());
        post.addClassName("post");
        Span lecture = new Span(professor.getLectureContent());
        lecture.addClassName("post");

        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");


        description.add(header, post, actions, lecture);
        card.add(image, description);
        return card;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        List<Professor> professors = List.of(
                createProfessor("Rexhep", "Gjergji", "Lecture 1 - Introduction to Functions", "https://imgs.search.brave.com/k01hc-j_4vEv8Syhy4uYJDSUjmJiiakiCWrjDgY7a5I/rs:fit:640:640:1/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vNzM2/eC84Yi8xNi83YS84/YjE2N2FmNjUzYzIz/OTlkZDkzYjk1MmE0/ODc0MDYyMC5qcGc", "\n A function in algebra is a rule that assigns a unique output (or \"value\") to each input (or \"argument\") in a set. It is often represented graphically as a curve on a coordinate plane, with the input values on the x-axis and the corresponding output values on the y-axis. The defining property of a function is that it assigns exactly one output value to each input value. In mathematical notation, a function is usually denoted by f(x), where x is the input variable and f(x) is the output value. To evaluate a function for a particular input, we simply substitute the input value into the function rule and simplify.\n"),
                createProfessor("Rexhep", "Gjergji", "Lecture 2 - Introduction to Limits", "https://imgs.search.brave.com/k01hc-j_4vEv8Syhy4uYJDSUjmJiiakiCWrjDgY7a5I/rs:fit:640:640:1/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vNzM2/eC84Yi8xNi83YS84/YjE2N2FmNjUzYzIz/OTlkZDkzYjk1MmE0/ODc0MDYyMC5qcGc","Limits in algebra are a fundamental concept in calculus that describe the behavior of a function as the input (or \"independent variable\") approaches a particular value. The limit of a function at a point is the value that the function approaches as the input approaches that point, regardless of whether the function actually reaches that value at that point. Limits can be used to determine the behavior of a function near a point, such as its continuity, whether it has a vertical asymptote, and more. Limits can be calculated using algebraic methods or by using graph-based approaches. They are often written symbolically as \"lim(x -> a) f(x) = L\", where \"f(x)\" is the function, \"a\" is the value that the independent variable approaches, and \"L\" is the limit of the function as x approaches a.\n"),
                createProfessor("Rexhep", "Gjergji", "Lecture 3 - Introduction to Graphs", "https://imgs.search.brave.com/k01hc-j_4vEv8Syhy4uYJDSUjmJiiakiCWrjDgY7a5I/rs:fit:640:640:1/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vNzM2/eC84Yi8xNi83YS84/YjE2N2FmNjUzYzIz/OTlkZDkzYjk1MmE0/ODc0MDYyMC5qcGc", "Graphs in algebra are visual representations of mathematical relationships between variables. They are used to display and analyze functions, data sets, and other mathematical objects. In a graph, the horizontal axis (x-axis) represents the independent variable and the vertical axis (y-axis) represents the dependent variable. Points on the graph represent specific pairs of (x, y) values, where x is the input and y is the output. A graph can be used to visualize the behavior of a function, to identify patterns in data, and to estimate values of the dependent variable based on values of the independent variable. Common types of graphs include line graphs, scatter plots, bar graphs, and histograms. In algebra, graphing is often used to understand the properties of a function, such as its intercepts, symmetry, increasing or decreasing intervals, and more.\n"),
                createProfessor("Rexhep", "Gjergji", "Lecture 4 - Introduction to Equations", "https://imgs.search.brave.com/k01hc-j_4vEv8Syhy4uYJDSUjmJiiakiCWrjDgY7a5I/rs:fit:640:640:1/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vNzM2/eC84Yi8xNi83YS84/YjE2N2FmNjUzYzIz/OTlkZDkzYjk1MmE0/ODc0MDYyMC5qcGc", "An equation in algebra is a mathematical statement that says that two expressions are equal. Equations can be used to describe a wide variety of mathematical relationships, from simple linear relationships to more complex nonlinear relationships. An equation typically consists of two parts: the left-hand side (LHS) and the right-hand side (RHS), separated by an equal sign (=). To solve an equation, one must find the value of the variable that makes the LHS equal to the RHS. This process involves isolating the variable on one side of the equation and applying algebraic operations to both sides to find the solution. In algebra, equations are used to model real-world situations, to make predictions based on given information, and to analyze the relationships between different variables.\n")
        );
        grid.setItems(professors);
    }

    private static Professor createProfessor(String firstName, String lastName, String post, String image, String lectureContent) {
        Professor professor = new Professor();
        professor.setFirstName(firstName);
        professor.setLastName(lastName);
        professor.setPost(post);
        professor.setImage(image);
        professor.setLectureContent(lectureContent);
        return professor;
    }
}
