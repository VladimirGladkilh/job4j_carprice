package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @ManyToOne
    @JoinColumn(name = "marka_id")
    private Marka marka;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;

    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Photo mainPhoto;

    @Enumerated(EnumType.STRING)
    @Column(name = "body")
    private Body body;

    @Enumerated(EnumType.STRING)
    @Column(name = "gear")
    private Gear gear;

    @Enumerated(EnumType.STRING)
    @Column(name = "engineType")
    private EngineType engineType;

    @Enumerated(EnumType.STRING)
    @Column(name = "privod")
    private Privod privod;

    private int year;
    private int probeg;
    private double price;
    private boolean saled = false;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Photo getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(Photo mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public void addPhotos(Photo photo) {
        this.photos.add(photo);
    }

    public static Car of(Marka marka, Model model, Body body, Gear gear,
                         EngineType engineType, Privod privod, int year, int probeg, double price) {
        Car car = new Car();
        car.created =  new Date(System.currentTimeMillis());;
        car.marka = marka;
        car.model = model;
        car.body = body;
        car.gear = gear;
        car.engineType = engineType;
        car.privod = privod;
        car.year = year;
        car.probeg = probeg;
        car.price = price;
        return car;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Marka getMarka() {
        return marka;
    }

    public void setMarka(Marka marka) {
        this.marka = marka;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Gear getGear() {
        return gear;
    }

    public void setGear(Gear gear) {
        this.gear = gear;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    public Privod getPrivod() {
        return privod;
    }

    public void setPrivod(Privod privod) {
        this.privod = privod;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getProbeg() {
        return probeg;
    }

    public void setProbeg(int probeg) {
        this.probeg = probeg;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSaled() {
        return saled;
    }

    public void setSaled(boolean saled) {
        this.saled = saled;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Car car = (Car) obj;
        return id == car.id;
    }

    @Override
    public String toString() {
        return marka +
                " " + model +
                " " + year;
    }
}
