package CompositePattern.v2;



public class Main {
    public static void main(String[] args) {
        Directory root = Directory.root();
        Directory document = new Directory("document");
        Directory notes = new Directory("notes");
        root.addItem(document);
        document.addItem(notes);
        root.addItem(new File("me.txt", "Hello, I'm Waterball."));
        notes.addItem(new File("meeting-0825.txt", "Lauren: To be or not to be."));
        notes.addItem(new File("meeting-0824.txt", "Waterball: Composition over Inheritance."));

        CLI cli = new CLI(root);
        cli.start();
    }
}
