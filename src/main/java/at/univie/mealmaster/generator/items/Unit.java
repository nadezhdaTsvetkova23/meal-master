package at.univie.mealmaster.generator.items;

public class Unit {
    private String name;
    private String abreviation;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbreviation() {
        return abreviation;
    }

    public void setAbkuerzung(String abreviation) {
        this.abreviation = abreviation;
    }
}
