package controllers;

import models.Order;

/**
 * Created by PC301 on 25/9/2560.
 */
public interface CoreController {

    public void start();

    public boolean addOrder(Order order);

    public boolean checkBill();

    // เอ๋ยังไม่ได้เขียนลง classdiagrams
    public int getTable();

    public void setDatabaseManager(DatabaseManager dbManager);
}