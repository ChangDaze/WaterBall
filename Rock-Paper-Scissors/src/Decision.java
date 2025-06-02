public enum Decision {
    ROCK("石頭"), PAPER("布"), SCISSORS("剪刀");

    private final String name;

    Decision(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        //應該是在用替換字元時會自動呼叫toString
        return name;
    }
}
