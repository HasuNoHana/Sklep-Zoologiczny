package controller;

import com.github.javafaker.Faker;
import model.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DataGeneratorController {
        final double MAXIMUMTRANSACTIONCOST = 1500;
        final double EMAILONLY = 0.6;
        final double PHONENUMBERONLY = 0.3;
        final double NOTPAYED = 0.9;
        final double MINIMUMDELIVERYCOST = 1500;
        final double MAXIMUMDELIVERYCOST = 20000;
        final double ORDERCOMPLETION = 0.8;
        final double MINIMUMPRODUCTCOST = 10;
        final double MAXIMUMPRODUCTCOST = 500;
        final int MAXIMUMQUANTITY = 10;

    public void generateDaneKlienta(int clientNum){
        DatabaseController db = new DatabaseController();
        Faker faker = new Faker(new Locale("pl"));
        for (int i = 0; i < clientNum; i++){
            db.insertIntoDaneKlienta( faker.name().firstName(), faker.name().lastName(), Integer.parseInt((faker.phoneNumber().subscriberNumber(9))));
        }
    }

    public void generateStanowisko(){
        DatabaseController db = new DatabaseController();
        List<String> positions = Arrays.asList("Menadżer","Pracownik", "Pracownik Magazynier");
        for(String positon : positions){
            db.insertIntoStanowisko(positon);
        }
    }

    public void generatePracownik(int employeesNum) throws Exception {
        DatabaseController db = new DatabaseController();
        Faker faker = new Faker(new Locale("pl"));
        List<Stanowisko> positions = db.selectAllFromStanowisko();
        if(!positions.isEmpty()){
            for(int i = 0; i< employeesNum; i++){
                Stanowisko nextPosition = positions.get(ThreadLocalRandom.current().nextInt(0, positions.size()));
                while(nextPosition.getId() == 1)
                    nextPosition = positions.get(ThreadLocalRandom.current().nextInt(0, positions.size()));
                db.insertIntoPracownik(faker.name().firstName(), faker.name().lastName(), nextPosition.getId());
            }
            db.insertIntoPracownik(faker.name().firstName(), faker.name().lastName(),1);
        } else {
            throw new Exception("No positions in database!");
        }

    }

    public void generateTransport(int transportsNum){
        DatabaseController db = new DatabaseController();
        List<Pracownik> employees = db.selectAllFromPracownik();
        Faker faker = new Faker(new Locale("pl"));
        for(int i = 0; i < transportsNum; i++){
            Pracownik employee = employees.get(ThreadLocalRandom.current().nextInt(0, employees.size()));
            db.insertIntoTransport(faker.date().past(1000, TimeUnit.DAYS), employee.getId());
        }
    }

    public void generateTransakcja(int transactionNum){
        DatabaseController db = new DatabaseController();
        List<Pracownik> employees = db.selectAllFromPracownik();
        Faker faker = new Faker(new Locale("pl"));
        for(int i = 0; i < transactionNum; i++){
            Pracownik employee = employees.get(ThreadLocalRandom.current().nextInt(0, employees.size()));
            double cost = Double.parseDouble(faker.commerce().price(0, MAXIMUMTRANSACTIONCOST).replaceAll(",","."));
            db.insertIntoTransakcja(faker.date().past(1000, TimeUnit.DAYS), cost,
                    employee.getId(), Transakcja.randomTransactionType());
        }
    }

    public void generateHurtownia(int warehouseNum){
        DatabaseController db = new DatabaseController();
        Faker faker = new Faker(new Locale("pl"));
        String contact;
        for (int i=0; i< warehouseNum; i++){
            double random = ThreadLocalRandom.current().nextDouble(0, 1);
            if(random < PHONENUMBERONLY)
                contact = faker.phoneNumber().subscriberNumber(9);
            else if(random > EMAILONLY)
                contact = faker.internet().safeEmailAddress();
            else
                contact = faker.phoneNumber().subscriberNumber(9) + " " + faker.internet().safeEmailAddress();
            db.insertIntoHurtownia(faker.company().name(), contact);
        }
    }

    public void generateFaktura(){
        DatabaseController db = new DatabaseController();
        Faker faker = new Faker(new Locale("pl"));
        List<Transakcja> transactions = db.selectFakturaOnlyFromTransakcja();
        Calendar calendar = Calendar.getInstance();
        for (Transakcja transaction : transactions) {
            calendar.setTime(transaction.getDate());
            double random = ThreadLocalRandom.current().nextDouble(0, 1);
            boolean ifPayed;
            if(random >= NOTPAYED)
                ifPayed = false;
            else
                ifPayed = true;
            String invoiceNr = transaction.getId() + "/" + (calendar.get(Calendar.MONTH)+1) + calendar.get(Calendar.YEAR);
            db.insertIntoFaktura(invoiceNr, faker.date().between(transaction.getDate(), new Date()), ifPayed,
                    faker.company().name(), faker.address().fullAddress(), Faktura.generateNIP(), transaction.getId());
        }
    }

    public void generatorDostawa(int deliveriesNum){
        DatabaseController db = new DatabaseController();
        Faker faker = new Faker(new Locale("pl"));
        List<Pracownik> employees= db.selectAllFromPracownik();
        List<Hurtownia> warehouses = db.selectAllFromHurtownia();
        for(int i=0; i< deliveriesNum; i++){
            db.insertIntoDostawa(faker.date().past(1000, TimeUnit.DAYS),
                    ThreadLocalRandom.current().nextDouble(MINIMUMDELIVERYCOST,MAXIMUMDELIVERYCOST),
                    warehouses.get(ThreadLocalRandom.current().nextInt(0, warehouses.size())).getId(),
                    employees.get(ThreadLocalRandom.current().nextInt(0, employees.size())).getId());
        }
    }
    public void generatorZamowienie(int ordersNum){
        DatabaseController db = new DatabaseController();
        Faker faker = new Faker(new Locale("pl"));
        List<DaneKlienta> clients = db.selectAllFromDaneKlienta();
        List<Transakcja> transactions = db.selectAllFromTransakcja();
        for (int i =0; i< ordersNum; i++){
            double random = ThreadLocalRandom.current().nextDouble(0, 1);
            int transactionRandom = ThreadLocalRandom.current().nextInt(0, transactions.size());
            boolean ifCompleted;
            if(random < ORDERCOMPLETION)
                ifCompleted = true;
            else
                ifCompleted = false;
            db.insertIntoZamowienie(ifCompleted, transactions.get(transactionRandom).getDate(),
                    faker.date().between(transactions.get(transactionRandom).getDate(), new Date()),
                    clients.get( ThreadLocalRandom.current().nextInt(0, clients.size())).getId());
        }
    }

    public void generatorKategoria(){
        DatabaseController db = new DatabaseController();
        List<Kategoria.category> categories = Kategoria.getCategory();
        for (Kategoria.category category : categories) {
            db.insertIntoKategoria(category.toString());
        }
    }

    public void generatorProdukt(){
        DatabaseController db = new DatabaseController();
        List<Produkt.animal> animals = Produkt.getAnimals();
        List<Produkt.productType> products = Produkt.getProductTypes();
        Map<Produkt.productType, Kategoria.category> mappedProducts = Produkt.getMappedProductTypes();
        List<Kategoria> categories = db.selectAllFromKategoria();
        for (Produkt.productType product : products) {
            Kategoria.category cat = mappedProducts.get(product);
            int categoryId = categories.stream()
                    .filter(item -> item.getName().toString().equals(cat.toString()))
                    .findAny()
                    .orElse(null).getId();
            for(Produkt.animal animal : animals){
                int quantity = ThreadLocalRandom.current().nextInt(3, MAXIMUMQUANTITY);
                for(int i =1; i < quantity+1; i++){
                    String name = String.valueOf(product).replaceAll("_", " ") + " "
                            + String.valueOf(animal).replaceAll("_", " ") + " " + i;
                    db.insertIntoProdukt(name,
                            ThreadLocalRandom.current().nextDouble(MINIMUMPRODUCTCOST,MAXIMUMPRODUCTCOST),
                            categoryId);
                }
            }
        }
    }

}

