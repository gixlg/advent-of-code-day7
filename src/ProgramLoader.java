import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

    public Tree<Program> createTree() throws Exception {

        do {
            agregateCycle();
        } while (map.size() > 1);

        Iterator<Map.Entry<String, Tree<Program>>> it = map.entrySet().iterator();
        Map.Entry<String, Tree<Program>> entry = it.next();

        return entry.getValue();

    }

    private void agregateCycle() throws Exception {
        toRemove = new ArrayList<String>();
        for (Map.Entry<String, Tree<Program>> entry : map.entrySet()) {
            Tree<Program> selectedTree = entry.getValue();
            if (selectedTree.getHead().getSubPrograms() != null) {
                appendSubPrograms(selectedTree);
            }

        }

        toRemove.stream().forEach(elementToRemove -> map.remove(elementToRemove));

    }

    private List<String> toRemove;

    private Tree<Program> appendSubPrograms(Tree<Program> node) throws Exception {
        Program program = node.getHead();
        program.getSubPrograms().forEach(programName -> appendSubProgram(programName, node));
        return node;
    }

    private void appendSubProgram(String programName, Tree<Program> node) {
        Tree<Program> subProgramTree = map.get(programName);
        Program subProgram = subProgramTree.getHead();
        toRemove.add(subProgram.getName());
        node.addLeaf(subProgramTree);
    }


    public void checkTowerWeight(Tree<Program> node) {
        Tree<Program> unbalancedTower = this.findUnbalancedTower(node);
        System.out.println();
        System.out.println();
        System.out.println(unbalancedTower.toString(2));
    }


    public Tree<Program> findUnbalancedTower(Tree<Program> node) {


        if (node.getLeafs() == null || node.getLeafs().size() == 0) {
            return null;
        }


        for (Tree<Program> subTree : node.getLeafs()) {
            Tree<Program> unbalance = this.findUnbalancedTower(subTree);
            if (unbalance != null) return unbalance;
        }


        List<Integer> weights = node.getLeafs().stream().map(tree -> getTowerWeight(tree)).collect(Collectors.toList());

        Optional<Integer> unbalanceWeight = weights.stream()
                .filter(i -> Collections.frequency(weights, i) == 1).findFirst();

        if (!unbalanceWeight.isPresent()) return null;

        calculateCorrection(node, unbalanceWeight.get(), weights);

        return node;
    }


    private void calculateCorrection(Tree<Program> node, Integer unbalanceWeight, List<Integer> weights) {
        Integer balanceWeight = weights.stream().filter(i -> Collections.frequency(weights, i) > 1).distinct().findFirst().get();
        System.out.println("UNbalanceWeight: " + unbalanceWeight);
        System.out.println("balanceWeight: " + balanceWeight);

        Tree<Program> unbalance = node.getLeafs().stream().filter(tree -> tree.getHead().getTowerWeight() == unbalanceWeight).findFirst().get();

        Integer diff = balanceWeight - unbalanceWeight;
        System.out.println("out: " + (unbalance.getHead().getWeight() + diff));
    }

    public Integer getTowerWeight(Tree<Program> node) {
        Integer weight = node.getHead().getWeight();

        List<Integer> subTowerWeights = new ArrayList<Integer>();
        for (Tree<Program> subTree : node.getLeafs()) {
            Integer subWeight = getTowerWeight(subTree);

            subTowerWeights.add(subWeight);
            weight += subWeight;
        }

        node.getHead().setTowerWeight(weight);
        return weight;

    }
}
