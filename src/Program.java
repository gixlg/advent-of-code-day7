import java.util.List;

public class Program {

    private String name;
    private Integer weight;
    private Integer towerWeight = 0;
    private List<String> subProgram;

    public Program(String name, String weight, List<String> subProgram) {
        this.name = name;
        this.weight = Integer.parseInt(weight);
        this.subProgram = subProgram;
    }

    public Program(String name, String weight) {
        this.name = name;
        this.weight = Integer.parseInt(weight);
        this.towerWeight = Integer.parseInt(weight);
    }

    public List<String> getSubProgram() {
        return subProgram;
    }

    public String getName() {
        return name;
    }

    public Integer getTowerWeight() {
        return towerWeight;
    }

    public void setTowerWeight(Integer towerWeight) {
        this.towerWeight = towerWeight;
    }

    public Integer getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        if (weight!=null){
            return String.format("%s (%s - %s)", name, towerWeight, weight);
        }else{
            return String.format("%s", name);
        }
    }
}
