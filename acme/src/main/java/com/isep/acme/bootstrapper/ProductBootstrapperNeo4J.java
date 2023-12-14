package com.isep.acme.bootstrapper;

import com.isep.acme.model.Product;
import com.isep.acme.repositories.neo4j.ProductRepositoryNeo4J;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.stereotype.Component;

@Component
@EnableNeo4jRepositories("com.isep.acme.repositories.neo4j")
public class ProductBootstrapperNeo4J implements CommandLineRunner{
    @Autowired
    private ProductRepositoryNeo4J productRepositoryNeo4J;

    @Override
    public void run(String... args) throws Exception {
        if (productRepositoryNeo4J.findBySku("asd578fgh267").isEmpty()) {
            Product p1 = new Product("asd578fgh267", "Pen", "very good nice product");
            productRepositoryNeo4J.saveProduct(p1);
        }
        if (productRepositoryNeo4J.findBySku("c1d4e7r8d5f2").isEmpty()) {
            Product p2 = new Product("c1d4e7r8d5f2", "Pencil", " writes ");
            productRepositoryNeo4J.saveProduct(p2);
        }
        if (productRepositoryNeo4J.findBySku("c4d4f1v2f5v3").isEmpty()) {
            Product p3 = new Product("c4d4f1v2f5v3", "Rubber", "erases");
            productRepositoryNeo4J.saveProduct(p3);
        }
        if (productRepositoryNeo4J.findBySku("v145dc2365sa").isEmpty()) {
            Product p4 = new Product("v145dc2365sa", "Wallet", "stores money");
            productRepositoryNeo4J.saveProduct(p4);
        }
        if (productRepositoryNeo4J.findBySku("fg54vc14tr78").isEmpty()) {
            Product p5 = new Product("fg54vc14tr78", "pencil case", " stores pencils");
            productRepositoryNeo4J.saveProduct(p5);
        }
        if (productRepositoryNeo4J.findBySku("12563dcfvg41").isEmpty()) {
            Product p6 = new Product("12563dcfvg41", "Glasses case", " stores glasses");
            productRepositoryNeo4J.saveProduct(p6);
        }
        if (productRepositoryNeo4J.findBySku("vcg46578vf32").isEmpty()) {
            Product p7 = new Product("vcg46578vf32", "tissues", " nose clearing material");
            productRepositoryNeo4J.saveProduct(p7);
        }
        if (productRepositoryNeo4J.findBySku("vgb576hgb675").isEmpty()) {
            Product p8 = new Product("vgb576hgb675", "mouse pad", " mouse adapted surface");
            productRepositoryNeo4J.saveProduct(p8);
        }
        if (productRepositoryNeo4J.findBySku("unbjh875ujg7").isEmpty()) {
            Product p9 = new Product("unbjh875ujg7", " mug ", " drink something from it");
            productRepositoryNeo4J.saveProduct(p9);
        }
        if (productRepositoryNeo4J.findBySku("u1f4f5e2d2xw").isEmpty()) {
            Product p10 = new Product("u1f4f5e2d2xw", " Lamp ", " it lights");
            productRepositoryNeo4J.saveProduct(p10);
        }
        if (productRepositoryNeo4J.findBySku("j85jg76jh845").isEmpty()) {
            Product p11 = new Product("j85jg76jh845", " Pillow ", " cold both sides");
            productRepositoryNeo4J.saveProduct(p11);
        }
        if (productRepositoryNeo4J.findBySku("g4f7e85f4g54").isEmpty()) {
            Product p12 = new Product("g4f7e85f4g54", " chair ", " comfortable ");
            productRepositoryNeo4J.saveProduct(p12);
        }
    }

}
