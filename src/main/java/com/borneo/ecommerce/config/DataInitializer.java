package com.borneo.ecommerce.config;

import com.borneo.ecommerce.model.Category;
import com.borneo.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            // Top-level categories
            Category electronics = new Category();
            electronics.setName("Electronics");
            categoryRepository.save(electronics);

            Category toysGames = new Category();
            toysGames.setName("Toys & Games");
            categoryRepository.save(toysGames);

            // Subcategories for Electronics
            Category mobilePhones = new Category();
            mobilePhones.setName("Mobile Phones");
            mobilePhones.setParent(electronics);
            categoryRepository.save(mobilePhones);

            Category computers = new Category();
            computers.setName("Computers & Accessories");
            computers.setParent(electronics);
            categoryRepository.save(computers);

            Category appliances = new Category();
            appliances.setName("Home Appliances");
            appliances.setParent(electronics);
            categoryRepository.save(appliances);

            // Subcategories for Toys & Games
            Category rcToys = new Category();
            rcToys.setName("RC Toys");
            rcToys.setParent(toysGames);
            categoryRepository.save(rcToys);

            Category actionFigures = new Category();
            actionFigures.setName("Action Figures");
            actionFigures.setParent(toysGames);
            categoryRepository.save(actionFigures);

            // You can add more subcategories as needed
        }
    }
}