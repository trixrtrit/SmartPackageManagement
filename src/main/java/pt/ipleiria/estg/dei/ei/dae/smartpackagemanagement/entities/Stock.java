package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stocks")
public class Stock {
    @Id
    private long id;
    @OneToOne
    @JoinColumn(name = "product_id")
    Product product;
    @OneToMany(mappedBy = "stock")
    List<Package> primaryPackageList;
    @OneToMany(mappedBy = "stock")
    List<Package> secondaryPackageList;

    public Stock() {
        this.primaryPackageList = new ArrayList<Package>();
        this.secondaryPackageList = new ArrayList<Package>();
    }
    public Stock(Product product) {
        this.product = product;
        this.primaryPackageList = new ArrayList<Package>();
        this.secondaryPackageList = new ArrayList<Package>();
    }

    public long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Package> getPrimaryPackageList() {
        return primaryPackageList;
    }

//    public void setPrimaryPackageList(List<Package> primaryPackageList) {
//        this.primaryPackageList = primaryPackageList;
//    }

    public List<Package> getSecondaryPackageList() {
        return secondaryPackageList;
    }

//    public void setSecondaryPackageList(List<Package> secondaryPackageList) {
//        this.secondaryPackageList = secondaryPackageList;
//    }
    public void addPrimaryPackage(Package aPackage){
        if(!primaryPackageList.contains(aPackage)){
            primaryPackageList.add(aPackage);
        }
    }
    public void addSecondaryPackage(Package aPackage){
        if(!secondaryPackageList.contains(aPackage)){
            secondaryPackageList.add(aPackage);
        }
    }
    public void removePrimaryPackage(Package aPackage){
        if(primaryPackageList.contains(aPackage)){
            primaryPackageList.remove(aPackage);
        }
    }
    public void removeSecondaryPackage(Package aPackage){
        if(secondaryPackageList.contains(aPackage)){
            secondaryPackageList.remove(aPackage);
        }
    }

}
