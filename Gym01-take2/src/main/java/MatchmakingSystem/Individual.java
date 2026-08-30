package MatchmakingSystem;

import java.util.Arrays;

public class Individual {
    private Integer id;
    private Gender gender;
    private Integer age;
    private String intro;
    private String[] habits;
    private Integer coordX;
    private Integer coordY;

    public Individual(Integer id, Gender gender, Integer age, String intro, String[] habits, Integer coordX, Integer coordY) {
        this.id = id;
        this.gender = gender;
        this.age = age;
        this.intro = intro;
        this.habits = habits;
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public Integer getId() {
        return id;
    }

    public Gender getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public String getIntro() {
        return intro;
    }

    public String[] getHabits() {
        return habits;
    }

    public Integer getCoordX() {
        return coordX;
    }

    public Integer getCoordY() {
        return coordY;
    }

    @Override
    public String toString() {
        return "Individual{" +
                "id=" + id +
                ", gender=" + gender +
                ", age=" + age +
                ", intro='" + intro + '\'' +
                ", habits=" + Arrays.toString(habits) +
                ", coord=(" + coordX + ", " + coordY + ")" +
                '}';
    }
}
