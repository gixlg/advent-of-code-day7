import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ProgramLoader {

    private Map<String, Tree<Program>> map = new HashMap<String, Tree<Program>>();
    private Tree<Program> tree;

    public void loadProgramsList(String pathToInputFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(pathToInputFile))) {


            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                Program program = generateProgram(sCurrentLine);
                map.put(program.getName(), new Tree(program));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Program generateProgram(String input) {
        int programNameIndex = input.indexOf(" ");
        String programName = input.substring(0, programNameIndex);

        int programWeightIndexStart = input.indexOf("(");
        int programWeightIndexEnd = input.indexOf(")");
        String programWeight = input.substring(programWeightIndexStart + 1, programWeightIndexEnd);

        int subProgramsIndex = input.indexOf("->");
        String[] subPrograms;
        if (subProgramsIndex != -1) {
            subPrograms = input.substring(subProgramsIndex + 3).split(", ");
            return new Program(programName, programWeight, Arrays.asList(subPrograms));
        }

        return new Program(programName, programWeight);
    }

    public void createTree() throws Exception {

        do {
            agregateCycle();
        } while (map.size() > 1);

        Iterator<Map.Entry<String, Tree<Program>>> it = map.entrySet().iterator();
        Map.Entry<String, Tree<Program>> entry = it.next();

        System.out.println(entry.getValue().toString());

    }

    private void agregateCycle() throws Exception {
        toRemove = new ArrayList<String>();
        for (Map.Entry<String, Tree<Program>> entry : map.entrySet()) {
            Tree<Program> selectedTree = entry.getValue();
            if (selectedTree.getHead().getSubProgram() != null) {
                appendSubProgram(selectedTree);
            }

        }

        for (String s : toRemove) {
            map.remove(s);
        }

    }

    private List<String> toRemove;

    private Tree<Program> appendSubProgram(Tree<Program> node) throws Exception {

        Program program = node.getHead();
        for (String programName : program.getSubProgram()) {
            Tree<Program> subProgramTree = map.get(programName);
            Program subProgram = subProgramTree.getHead();
            toRemove.add(subProgram.getName());
            node.addLeaf(subProgramTree);
        }

        return node;

    }
}
