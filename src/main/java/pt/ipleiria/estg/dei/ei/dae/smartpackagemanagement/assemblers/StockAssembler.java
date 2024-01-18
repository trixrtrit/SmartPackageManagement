package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.PackageDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.StockDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Package;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.Stock;

import java.util.List;
import java.util.stream.Collectors;

public class StockAssembler {

    public static StockDTO from(Stock stock) {
        return new StockDTO(
                stock.getId(),
                stock.getProduct()//,
                //PackageAssembler.from(stock.getPrimaryPackageList()),
                //PackageAssembler.from(stock.getSecondaryPackageList())
        );
    }

    public static List<StockDTO> from(List<Stock> stocks) {
        return stocks.stream().map(StockAssembler::from).collect(Collectors.toList());
    }

    public static StockDTO fromWithPackages(Stock stock) {
        return new StockDTO(
                stock.getId(),
                stock.getProduct(),
                PackageAssembler.from(stock.getPrimaryPackageList()),
                PackageAssembler.from(stock.getSecondaryPackageList())
        );
    }

    public static List<StockDTO> fromWithPackages(List<Stock> stocks) {
        return stocks.stream().map(StockAssembler::fromWithPackages).collect(Collectors.toList());
    }


}
