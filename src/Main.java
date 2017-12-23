import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        ProgramLoader programLoader = new ProgramLoader();
        programLoader.loadProgramsList("input.txt");
        Tree<Program> tree = programLoader.createTree();

        programLoader.getTowerWeight(tree);

        System.out.println(tree.toString());

        System.out.print("\n\n");
        System.out.printf("The name of the bottom program is: "+tree.getHead().getName());
        System.out.print("\n\n");

        programLoader.checkTowerWeight(tree);
    }


}
