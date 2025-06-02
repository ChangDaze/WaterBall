package MatchmakingSystem.v1;

public class Main {
    public static void main(String[] args){
        Individual id1 = new Individual(1, Gender.MALE, 20, "我是男大學生", "打籃球,打羽球", new int[]{10, 10});
        Individual id2 = new Individual(2, Gender.FEMALE, 22, "我是女大學生", "打籃球,看影片,爬山", new int[]{-10, 10});
        Individual id3 = new Individual(3, Gender.MALE, 30, "我是男工程師", "打籃球,寫程式", new int[]{30, 15});
        Individual id4 = new Individual(4, Gender.FEMALE, 30, "我是女工程師", "打籃球,打羽球,看影片,爬山,跑馬拉松", new int[]{-100, 90});
        Individual id5 = new Individual(5, Gender.MALE, 40, "我是家庭主夫", "打籃球,逛街,慢跑", new int[]{10, 9});
        Individual[] members = {id1, id2, id3, id4, id5};
        MatchmakingSystem system = new MatchmakingSystem(members, "Distance-Based");
        Individual matched = system.Match(id1);
        System.out.printf("【%s】 id %d 配對到 id %d \r\n", "Distance-Based", id1.getId(), matched.getId());
        MatchmakingSystem system2 = new MatchmakingSystem(members, "Habit-Based");
        Individual matched2 = system2.Match(id1);
        System.out.printf("【%s】 id %d 配對到 id %d \r\n", "Habit-Based", id1.getId(), matched2.getId());
    }
}
