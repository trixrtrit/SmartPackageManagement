package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.assemblers;

import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.dtos.StandardPackageProductDTO;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.StandardPackageProduct;

import java.util.List;
import java.util.stream.Collectors;

public class StandardPackageProductAssembler {
    public static StandardPackageProductDTO from(StandardPackageProduct standardPackageProduct) {
        return new StandardPackageProductDTO(
                standardPackageProduct.getId(),
                ProductAssembler.fromWithProductParameters(standardPackageProduct.getProduct()),
                standardPackageProduct.getAddedAt(),
                standardPackageProduct.getRemovedAt()
        );
    }

    public static List<StandardPackageProductDTO> from(List<StandardPackageProduct> standardPackageProducts) {
        return standardPackageProducts.stream().map(StandardPackageProductAssembler::from).collect(Collectors.toList());
    }

    public static StandardPackageProductDTO fromWithPackages(StandardPackageProduct standardPackageProduct) {
        return new StandardPackageProductDTO(
                standardPackageProduct.getId(),
                StandardPackageAssembler.from(standardPackageProduct.getStandardPackage()),
                standardPackageProduct.getAddedAt(),
                standardPackageProduct.getRemovedAt()
        );
    }

    public static List<StandardPackageProductDTO> fromWithPackages(
            List<StandardPackageProduct> standardPackageProducts
    ) {
        return standardPackageProducts.stream().
                map(StandardPackageProductAssembler::fromWithPackages).collect(Collectors.toList());
    }

    public static StandardPackageProductDTO fromWithPackageAndProduct(StandardPackageProduct standardPackageProduct) {
        return new StandardPackageProductDTO(
                standardPackageProduct.getId(),
                StandardPackageAssembler.from(standardPackageProduct.getStandardPackage()),
                ProductAssembler.fromWithProductParameters(standardPackageProduct.getProduct()),
                standardPackageProduct.getAddedAt(),
                standardPackageProduct.getRemovedAt()
        );
    }

    public static List<StandardPackageProductDTO> fromWithPackageAndProduct(
            List<StandardPackageProduct> standardPackageProducts
    ) {
        return standardPackageProducts.stream().
                map(StandardPackageProductAssembler::fromWithPackageAndProduct).collect(Collectors.toList());
    }
}
