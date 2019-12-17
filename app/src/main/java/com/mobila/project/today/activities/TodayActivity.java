package com.mobila.project.today.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mobila.project.today.R;
import com.mobila.project.today.UncheckedTodayException;
import com.mobila.project.today.activities.adapters.TaskAdapter;
import com.mobila.project.today.control.utils.DateUtils;
import com.mobila.project.today.model.Course;
import com.mobila.project.today.model.Semester;
import com.mobila.project.today.model.Task;
import com.mobila.project.today.activities.adapters.CourseAdapter;
import com.mobila.project.today.model.dataProviding.SampleDataProvider;
import com.mobila.project.today.model.dataProviding.dataAccess.databank.SemesterDataSource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TodayActivity extends AppCompatActivity {

    private List<Task> tasks;
    private List<Course> courses;
    private List<Semester> semesters;

    TextView semesterView;
    int currentSemester;

    private SemesterDataSource semesterDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);
        this.semesterView = findViewById(R.id.semester_view);

        this.courses = SampleDataProvider.getExampleCourses();
        this.tasks = SampleDataProvider.getExampleTasks();
        this.semesters = SampleDataProvider.getExampleSemesters();


        this.semesterDataSource = new SemesterDataSource(this);
        this.semesters = semesterDataSource.getAllSemesters();


        initCourseView();
        initTaskView();
        initSemsterView();
        setTimeDisplayed();
    }

    @Override
    protected void onPause(){
        super.onPause();
        this.semesterDataSource.saveSemesters(this.semesters);
        this.semesterDataSource.close();
    }

    @Override
    protected void onResume(){
        super.onResume();
        this.semesterDataSource.open();
        this.semesters = semesterDataSource.getAllSemesters();
    }

    private void setTimeDisplayed() {
        TextView dateDisplay = findViewById(R.id.day_display);
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat day =
                new SimpleDateFormat(DateUtils.DAY_WEEKDAY_FORMAT, Locale.getDefault());
        SimpleDateFormat date =
                new SimpleDateFormat(DateUtils.DAY_DATE_FORMAT, Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        builder.append(day.format(calender.getTime()));
        builder.append(System.getProperty("line.separator"));
        builder.append(date.format(calender.getTime()));
        dateDisplay.setText(builder);
    }

    private void initSemsterView() {
        if (semesters.size()!= 0) {
            currentSemester = semesters.size() - 1;
            this.setSemester();
        }
        showAppropiateSemesterButtons();
    }

    private void initTaskView() {
        RecyclerView recyclerView = findViewById(R.id.rv_course_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TaskAdapter(this, this.tasks));
    }

    private void initCourseView() {
        RecyclerView courseRecyclerView = findViewById(R.id.recycler_view_courses);
        courseRecyclerView.setAdapter(new CourseAdapter(this, this.courses));
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void goBackSemester(View view) {
        if (currentSemester > 0) {
            currentSemester--;
        }
        this.setSemester();
        showAppropiateSemesterButtons();
    }

    public void goForwardSemester(View view) {
        if (this.semesters.isEmpty()){
            semesters = new ArrayList<>();
            this.currentSemester=0;
            semesters.add(new Semester(
                    String.valueOf(currentSemester+1), currentSemester+1));
        }
        else if (currentSemester == semesters.size() - 1) {
            semesters.add(new Semester(
                    String.valueOf(semesters.size()+1), semesters.size()+1));
            currentSemester++;
        }
        if (currentSemester < semesters.size() - 1) {
            currentSemester++;
        }
        this.setSemester();
        showAppropiateSemesterButtons();
    }

    private void showAppropiateSemesterButtons() {
        ImageButton goForewardButton = findViewById(R.id.go_foreward_semester);
        ImageButton goBackwardsButton = findViewById(R.id.go_back_semester);
        if (currentSemester == semesters.size() - 1) {
            goForewardButton.setImageResource(R.drawable.baseline_add_24);
        } else {
            goForewardButton.setImageResource(R.drawable.baseline_arrow_back_ios_24);
        }
        if (currentSemester == 0) {
            goBackwardsButton.setImageResource(R.drawable.transparent_placeholder);
        } else {
            goBackwardsButton.setImageResource(R.drawable.baseline_arrow_back_ios_24);
        }
        if (currentSemester==0&&semesters.size()==0){
            goBackwardsButton.setImageResource(R.drawable.transparent_placeholder);
            goForewardButton.setImageResource(R.drawable.baseline_add_24);
        }
    }

    private void setSemester() {
        try {
            semesterView.setText(String.format(Locale.getDefault(),
                    "Semester %d", semesters.get(currentSemester).getSemesterNr()));
        } catch (UncheckedTodayException ignored) {
        }
    }
}
