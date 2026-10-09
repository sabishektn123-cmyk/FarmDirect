package farmdirect.model;

public class Farmer extends User {

    private String farmLocation;

    public Farmer(int userId,
                  String name,
                  String email,
                  String password,
                  String farmLocation) {

        super(userId, name, email, password);

        this.farmLocation = farmLocation;
    }

    public String getFarmLocation() {
        return farmLocation;
    }

    public void setFarmLocation(String farmLocation) {
        this.farmLocation = farmLocation;
    }

    @Override
    public String getRole() {
        return "FARMER";
    }
}