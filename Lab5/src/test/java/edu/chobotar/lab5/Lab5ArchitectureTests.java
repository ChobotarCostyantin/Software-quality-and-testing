package edu.chobotar.lab5;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.Configuration.consideringAllDependencies;

/*
  @author Harsteel
  @project Lab5
  @class Lab5ArchitectureTests
  @version 1.0.0
  @since 20.04.2025 - 00.17
*/
public class Lab5ArchitectureTests {
    private JavaClasses applicationClasses;
    private String projectName = "lab5";
    @BeforeEach
    void initialize() {
        applicationClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("edu.chobotar." + projectName);
    }

    @Test
    void shouldFollowLayerArchitecture()  {
        layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controller").definedBy("..controller..")
                .layer("Service").definedBy("..service..")
                .layer("Repository").definedBy("..repository..")

                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Service")
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")

                .check(applicationClasses);
    }

    @Test
    void projectPackagesShouldBeFreeOfCycles() {
        slices()
                .matching(".." + projectName +".(*)..")
                .should().beFreeOfCycles()
                .check(applicationClasses);
    }

    @Test
    void controllersShouldNotDependOnOtherControllers() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..controller..")
                .because("Controllers should not depend on other controllers")
                .check(applicationClasses);
    }

    @Test
    void repositoriesShouldNotDependOnServices() {
        noClasses()
                .that().resideInAPackage("..repository..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..service..")
                .because("Repositories should not depend on services")
                .check(applicationClasses);
    }

    @Test
    void repositoriesShouldNotDependOnControllers() {
        noClasses()
                .that().resideInAPackage("..repository..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..controller..")
                .because("Repositories should not depend on services")
                .check(applicationClasses);
    }


    @Test
    void anyControllerFieldsShouldNotBeAnnotatedAutowired() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should()
                .beAnnotatedWith(Autowired.class)
                .check(applicationClasses);
    }
    @Test
    void controllersShouldResideInControllerPackage() {
        classes()
                .that().haveSimpleNameEndingWith("Controller")
                .should().resideInAPackage("..controller..")
                .check(applicationClasses);
    }

    @Test
    void classesInControllerPackageShouldHaveControllerSuffix() {
        classes()
                .that().resideInAPackage("..controller..")
                .should().haveSimpleNameEndingWith("Controller")
                .check(applicationClasses);
    }

    @Test
    void restControllersShouldResideInControllerPackage() {
        classes()
                .that().areAnnotatedWith(RestController.class)
                .should().resideInAPackage("..controller..")
                .check(applicationClasses);
    }

    @Test
    void controllersAnnotatedWithControllerShouldResideInControllerPackage() {
        classes()
                .that().areAnnotatedWith(Controller.class)
                .should().resideInAPackage("..controller..")
                .allowEmptyShould(true)
                .check(applicationClasses);
    }

    @Test
    void servicesShouldResideInServicePackage() {
        classes()
                .that().haveSimpleNameEndingWith("Service")
                .should().resideInAPackage("..service..")
                .check(applicationClasses);
    }

    @Test
    void serviceMethodsShouldNotDirectlyAccessRepositories() {
        noFields()
                .that().areDeclaredInClassesThat()
                .resideInAPackage("..service..")
                .should()
                .bePublic()
                .because("Service classes should not have public fields with repositories, to encapsulate them")
                .check(applicationClasses);
    }

    @Test
    void classesInServicePackageShouldHaveServiceSuffix() {
        classes()
                .that().resideInAPackage("..service..")
                .should().haveSimpleNameEndingWith("Service")
                .check(applicationClasses);
    }

    @Test
    void classesInServicePackageShouldBeAnnotatedWithService() {
        classes()
                .that().resideInAPackage("..service..")
                .should().beAnnotatedWith(Service.class)
                .check(applicationClasses);
    }

    @Test
    void repositoriesShouldResideInRepositoryPackage() {
        classes()
                .that().haveSimpleNameEndingWith("Repository")
                .should().resideInAPackage("..repository..")
                .check(applicationClasses);
    }

    @Test
    void classesInRepositoryPackageShouldHaveRepositorySuffix() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().haveSimpleNameEndingWith("Repository")
                .check(applicationClasses);
    }

    @Test
    void classesInRepositoryPackageShouldBeAnnotatedWithRepository() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().beAnnotatedWith(Repository.class)
                .check(applicationClasses);
    }

    @Test
    void modelShouldNotDependOnOtherLayers() {
        noClasses()
                .that().resideInAPackage("..model..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("..controller..", "..service..", "..repository..")
                .because("Models should be independent of other layers")
                .check(applicationClasses);
    }

    @Test
    void modelFieldsShouldBePrivate() {
        fields()
                .that().areDeclaredInClassesThat()
                .resideInAPackage("..model..")
                .should().notBePublic()
                .because("Models must encapsulate its state.")
                .check(applicationClasses);

    }

    @Test
    void noClassesShouldDependOnApplicationClass() {
        noClasses()
                .should()
                .dependOnClassesThat()
                .areAnnotatedWith(SpringBootApplication.class)
                .because("Classes should not depend on the main application class")
                .check(applicationClasses);
    }
}
