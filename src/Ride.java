public class Ride {
    private String name;
    private int seats;

    public Ride(String name, int seats){
        this.name = name;
        this.seats = seats;
    }

    public String getName(){
        return name;
    }

    public int getSeats(){
        return seats;
    }

    public void setSeats(int seats){
        this.seats = seats;
    }

}
