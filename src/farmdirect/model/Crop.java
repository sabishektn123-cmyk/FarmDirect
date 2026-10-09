package farmdirect.model;

public class Crop {

    private int cropId;
    private String cropName;
    private String category;

    public Crop(int cropId, String cropName, String category) {
        this.cropId = cropId;
        this.cropName = cropName;
        this.category = category;
    }

    public int getCropId() {
        return cropId;
    }

    public String getCropName() {
        return cropName;
    }

    public String getCategory() {
        return category;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return cropName + " (" + category + ")";
    }
}