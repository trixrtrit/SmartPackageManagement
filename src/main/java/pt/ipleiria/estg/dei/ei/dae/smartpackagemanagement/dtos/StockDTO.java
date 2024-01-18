package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class StockDTO {

    private long id;

    Product product;

    List<PackageDTO> primaryPackageList;

    List<PackageDTO> secondaryPackageList;

    public StockDTO() {
        this.primaryPackageList = new ArrayList<PackageDTO>();
        this.secondaryPackageList = new ArrayList<PackageDTO>();
    }
    public StockDTO(long id, Product product) {
        this.id = id;
        this.product = product;
        this.primaryPackageList = new ArrayList<PackageDTO>();
        this.secondaryPackageList = new ArrayList<PackageDTO>();
    }
    public StockDTO(long id, Product product, List<PackageDTO> primaryPackageList, List<PackageDTO> secondaryPackageList) {
        this.id = id;
        this.product = product;
        this.primaryPackageList = primaryPackageList;
        this.secondaryPackageList = secondaryPackageList;
    }
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<PackageDTO> getPrimaryPackageList() {
        return primaryPackageList;
    }

//    public void setPrimaryPackageList(List<Package> primaryPackageList) {
//        this.primaryPackageList = primaryPackageList;
//    }

    public List<PackageDTO> getSecondaryPackageList() {
        return secondaryPackageList;
    }

    //    public void setSecondaryPackageList(List<Package> secondaryPackageList) {
//        this.secondaryPackageList = secondaryPackageList;
//    }
    public void addPrimaryPackage(PackageDTO aPackage){
        if(!primaryPackageList.contains(aPackage)){
            primaryPackageList.add(aPackage);
        }
    }
    public void addSecondaryPackage(PackageDTO aPackage){
        if(!secondaryPackageList.contains(aPackage)){
            secondaryPackageList.add(aPackage);
        }
    }
    public void removePrimaryPackage(PackageDTO aPackage){
        if(primaryPackageList.contains(aPackage)){
            primaryPackageList.remove(aPackage);
        }
    }
    public void removeSecondaryPackage(PackageDTO aPackage){
        if(secondaryPackageList.contains(aPackage)){
            secondaryPackageList.remove(aPackage);
        }
    }

}
