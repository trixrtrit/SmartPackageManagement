package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class StockDTO {

    private long id;

    Product product;

    List<Package> primaryPackageList;

    List<Package> secondaryPackageList;

    public StockDTO() {
        this.primaryPackageList = new ArrayList<Package>();
        this.secondaryPackageList = new ArrayList<Package>();
    }
    public StockDTO(Product product) {
        this.product = product;
        this.primaryPackageList = new ArrayList<Package>();
        this.secondaryPackageList = new ArrayList<Package>();
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
