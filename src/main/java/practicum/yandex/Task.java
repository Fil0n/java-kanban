package main.java.practicum.yandex;

public class Task {
    private String name;
    private String desription;
    private int id;
    private Status status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if(id == 0 || this.id != 0) {
           return;
        }

        this.id =  id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesription() {
        return desription;
    }

    public void setDesription(String desription) {
        this.desription = desription;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return String.format("id: %d \n name: %s \n description: %s \n status: %s \n", id, name != null ? name : "", desription != null ? desription : "", status != null ? status : "");
    }
}
