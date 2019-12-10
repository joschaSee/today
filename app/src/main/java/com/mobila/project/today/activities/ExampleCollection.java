package com.mobila.project.today.activities;

import com.mobila.project.today.model.Course;
import com.mobila.project.today.model.Section;
import com.mobila.project.today.model.Semester;
import com.mobila.project.today.model.Task;
import com.mobila.project.today.model.Lecture;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExampleCollection {
    public static List<Task> getExampleTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(8, "some Content", new java.util.Date()));
        tasks.add(new Task(9, "some Random", new java.util.Date()));
        tasks.add(new Task(10, "some mor Random", new Date()));
        return tasks;
    }

    public static List<Section> getExampleSections() {
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(123, "Vorlesung", "Prof. Schwotzer"));
        sections.add(new Section(321, "Übung", "Prof. Schwotzer"));
        return sections;
    }

    public static List<com.mobila.project.today.model.Lecture> getExampleLectures() {
        List<com.mobila.project.today.model.Lecture> lectures = new ArrayList<>();
        lectures.add(new Lecture(456, 1, new Date(), "WH C666"));
        lectures.add(new Lecture(4536, 2, new Date(), "WH C445"));
        lectures.add(new Lecture(4526, 3, new Date(), "WH C624"));
        return lectures;
    }

    public static List<Course> getExampleCourse() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(1, "Mobile Anwendungen"));
        courses.add(new Course(2, "Software Engeneering"));
        courses.add(new Course(3, "Mathematik 1"));
        courses.add(new Course(4, "Netzwerke"));
        courses.add(new Course(5, "Theoretische Grundlagen der Informatik"));
        courses.add(new Course(6, "Soziale Netzwerke"));
        return courses;
    }

    public static List<Semester> getExampleSemesters() {
        List<Semester> semesters = new ArrayList<>();
        semesters.add(new Semester(11, 1));
        semesters.add(new Semester(11, 2));
        semesters.add(new Semester(11, 3));
        return semesters;
    }
}
