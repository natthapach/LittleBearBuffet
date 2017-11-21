package controllers;

import models.*;
import models.Package;

import java.util.List;

/**
 * Created by PC301 on 25/9/2560.
 */
public class MainController implements CoreController, FirebaseObserver{

    private DatabaseManager dbManager;
    private RestaurantManager restaurantManager;
    private CustomerManager customerManager;


    public void start() {
        List<Package> packages = null;
        while(packages == null){
            System.out.println("loading packages ...");
            packages = dbManager.loadPackages();
        }
        System.out.println("start");
        System.out.println("packages = " + packages);
        restaurantManager.setPackages(packages);

        List<Category> categories = null;
        while (categories == null){
            System.out.println("loading categories ...");
            categories = dbManager.loadCategories();
        }
        restaurantManager.setCategories(categories);
    }

    public boolean addOrder(Order order) {
//        if(order.getTable() != customerManager.getTable()) {
//            order = new Order(order.getId(), order.getAmount(), order.getItem(), customerManager.getTable());
//        }
        dbManager.addOrder(order);

//        if (newOrder != null) {
//            System.out.println("add order complete");
//            customerMana22ger.addOrder(order);
//            return true;
//        }
//        System.out.println("add order fail");
        return false;
    }

    public void selectPackage(Package pk, int amount) {
        dbManager.selectPackage(pk, amount);
        customerManager.setPackageObj(pk);
        customerManager.setAmount(amount);

        List<Item> items = dbManager.loadItems(pk);
        System.out.println("load item complete");

        restaurantManager.addItemsToCategory(items);
    }

    public boolean checkBill() {
        Package pk = customerManager.getPackageObj();
        int amount = customerManager.getAmount();
        boolean isSuccess = dbManager.checkBill(pk, amount);
        if (isSuccess){
            customerManager.clearOrder();
            restaurantManager.clearCategories();
        }
        return isSuccess;
    }

    public int getTable() {
        return customerManager.getTable();
    }

    public List<Package> getPackages() {
        return restaurantManager.getPackages();
    }

    public int getAmount() {
        return customerManager.getAmount();
    }

    public void setTable(int table) {
        customerManager.setTable(table);
    }

    public void setDatabaseManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void setCustomerManager(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    public void setRestaurantManager(RestaurantManager restaurantManager) {
        this.restaurantManager = restaurantManager;
    }

    public List<Category> getCategories() {
        return restaurantManager.getCategories();
    }

    public List<Order> getOrders() {
        return customerManager.getOrders();
    }

    public double getTotalPrice() {
        return customerManager.getTotalPrice();
    }

    public Package getCurrentPackage() {
        return customerManager.getPackageObj();
    }

    @Override
    public int getCurrentPackageId() {
        return customerManager.getPackageObj().getId();
    }

    @Override
    public void onItemAdd(Item item) {
        restaurantManager.addItem(item);
    }

    @Override
    public void onItemChange(Item item) {
        restaurantManager.changeItem(item);
    }

    @Override
    public void onItemDelete(Item item) {
        restaurantManager.removeItem(item);
    }

    @Override
    public void onPackageAdd(Package packageObj) {
        restaurantManager.addPackage(packageObj);
    }

    @Override
    public void onPackageChange(Package packageObj) {
        restaurantManager.changePacakage(packageObj);
    }

    @Override
    public void onPackageDelete(Package packageObj) {
        restaurantManager.removePackage(packageObj);
    }

    @Override
    public void onCategoryAdd(Category category) {
        restaurantManager.addCategory(category);
    }

    @Override
    public void onCategoryChange(Category category) {
        restaurantManager.changeCategory(category);
    }

    @Override
    public void onCategoryDelete(Category category) {
        restaurantManager.removeCategory(category);
    }

    @Override
    public void onOrderAdd(Order order) {
        customerManager.addOrder(order);
        System.err.println("onOrder Add");
    }

    @Override
    public void onOrderChange(Order order) {
        customerManager.changeOrder(order);
    }

    @Override
    public void onOrderDelete(Order order) {
        customerManager.deleteOrder(order);
    }
}
