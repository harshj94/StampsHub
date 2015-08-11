package stampshub.app.stampshub;

public class Item {

    private String title;
    private String description;
    private String objectId;

    public Item(String title, String description,String objectId) {
        super();
        this.title = title;
        this.description = description;
        this.objectId=objectId;
    }
    public String getTitle()
    {
        return title;
    }
    public String getDescription()
    {
        return description;
    }
    public String getObjectId()
    {
        return objectId;
    }
}
