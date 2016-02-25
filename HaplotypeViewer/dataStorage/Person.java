package dataStorage;

public class Person {
    private final int index;
    private String name;

    public Person(int id, String name) {
        this.index = id;
        this.name = name;
    }
    
    public String getName() {
    	return this.name;
    }

    public void setName(String name) {
    	this.name = name;
    }

    public int getIndex() {
        return index;
    }
}
