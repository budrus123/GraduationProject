public class Room implements Comparable<Room> {
    private int id,capacity;
    private String label ;

    public int getId() {
        return id;
    }

    public Room(int id, int capacity, String label) {
        this.id = id;
        this.capacity = capacity;
        this.label = label;
    }

    public void setId(int id) {
        this.id = id;

    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public int compareTo(Room o)
    {
        return(capacity - o.capacity);
    }
}
