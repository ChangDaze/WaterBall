package MatchmakingSystem.v2;

public class Individual {
    private final int id;
    private final Gender gender;
    private final int age;
    private final String intro;
    private final String habits;
    private final int[] coord;

    public Individual(int id, Gender gender, int age, String intro, String habits, int[] coord){
        this.id = id;
        this.gender = gender;
        this.age = age;
        this.intro = intro;
        this.habits = habits;
        this.coord = coord;
    }

    public int getId() {
        return id;
    }

    public Gender getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getIntro() {
        return intro;
    }

    public String getHabits() {
        return habits;
    }

    public int[] getCoord() {
        return coord;
    }
}
