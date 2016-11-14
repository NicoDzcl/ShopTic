package com.example.nicolas.shoptic;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.util.TypedValue;

import com.example.nicolas.shoptic.core.Category;
import com.example.nicolas.shoptic.core.Frequency;
import com.example.nicolas.shoptic.core.List;
import com.example.nicolas.shoptic.core.ListItem;
import com.example.nicolas.shoptic.core.Product;
import com.google.android.gms.maps.model.LatLng;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.TreeMap;

/**
 * The ShopTicApplication. Used as a controller (Modèle d'Interfaçage de l'Application [Seeheim])
 * Est le singleton, est accessible de (presque) partout dans l'application
 */

public class ShopTicApplication extends Application {

    private static final String LISTS_SAVE_FILE = "lists_file";
    private static final String PRODUCT_SAVE_FILE = "product_file";
    private static final String LISTITEM_SAVE_FILE = "listitem_file";
    /**
     * Extra data code for Intent launched when a list is selected
     */
    public static final String INTENT_MESSAGE_LIST = "com.example.nicolas.shoptic.LIST";

    private ArrayList<List> lists = null;
    private ArrayList<Product> products = null;
    private ArrayList<ListItem> listItems = null;

    /**
     * @return All the lists stored (if any)
     */
    public ArrayList<List> getLists() {
        if (lists == null){
            try {
                FileInputStream fis = getApplicationContext().openFileInput(LISTS_SAVE_FILE);
                ObjectInputStream is = new ObjectInputStream(fis);
                lists = (ArrayList<List>) is.readObject();
                is.close();
                fis.close();
            } catch (IOException|ClassNotFoundException e) {
                lists = new ArrayList<>();
            }
        }
        return lists;
    }

    /**
     * Adds a list
     * @param list list to add
     * @return true if the list has been added, false if the same list is already present
     */
    public boolean addList(List list){
        for (List l: getLists()){
            if (l.equals(list)){
                return false;
            }
        }
        lists.add(list);
        saveLists();
        return true;
    }

    /**
     * Delete the list from the app at the position "position"
     * @param position the position of the list to remove in the list : {@link #getLists getLists}
     */
    public void deleteList(int position){
        ArrayList<Integer> positionToRemove = new ArrayList<>();
        for (int i = 0; i < getListItems().size(); i++){
            if (listItems.get(i).getList().getName().equals(lists.get(position).getName())){
                positionToRemove.add(i);
            }
        }
        for (Integer i: positionToRemove){
            listItems.remove((int)i);
        }
        lists.remove(position);
        saveListItems();
        saveLists();
    }

    /**
     * Save the list into a file (by Serialization)
     */
    private void saveLists(){
        FileOutputStream fos;
        try {
            fos = getApplicationContext().openFileOutput(LISTS_SAVE_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream os;
            os = new ObjectOutputStream(fos);
            os.writeObject(lists);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all the products stored (if any), returns the default ones if none is saved
     * @return The products of the app
     */
    public ArrayList<Product> getProducts() {
        if (products == null){
            try {
                FileInputStream fis = getApplicationContext().openFileInput(PRODUCT_SAVE_FILE);
                ObjectInputStream is = new ObjectInputStream(fis);
                products = (ArrayList<Product>) is.readObject();
                is.close();
                fis.close();
            } catch (IOException|ClassNotFoundException e) {
                products = new ArrayList<>();
                products.add(new Product("Orange", null, 0., new Category("Alimentaire", false), false));
                products.add(new Product("Citron", null, 0., new Category("Alimentaire", false), false));
                products.add(new Product("Bière", null, 0., new Category("Alcool", false), false));
                products.add(new Product("Vin Rouge", null, 0., new Category("Alcool", false), false));
                products.add(new Product("Stylo", null, 0., new Category("Fourniture", false), false));
                products.add(new Product("Gomme", null, 0., new Category("Fourniture", false), false));
                products.add(new Product("Feutre", null, 0., new Category("Fourniture", false), false));
                products.add(new Product("Assiette", null, 0., new Category("Vaisselle", false), false));
                products.add(new Product("Frites", null, 0., new Category("Surgelé", false), false));
            }
        }
        Collections.sort(products);
        return products;
    }

    public boolean addProduct(String name, String imageUri, String categoryName){
        String categoryNameTrimmed = categoryName.trim();
        String productsNameTrimmed = name.trim();
        productsNameTrimmed = productsNameTrimmed.substring(0, 1).toUpperCase() + productsNameTrimmed.substring(1).toLowerCase();
        Product p = new Product(productsNameTrimmed, imageUri, 0., new Category(categoryName.substring(0, 1).toUpperCase() + categoryNameTrimmed.substring(1), false), true);
        if (isProductExist(p)){
            return false;
        }else{
            products.add(p);
            saveProducts();
            return true;
        }
    }

    private void saveProducts(){
        FileOutputStream fos;
        try {
            fos = getApplicationContext().openFileOutput(PRODUCT_SAVE_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream os;
            os = new ObjectOutputStream(fos);
            os.writeObject(products);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProductPrice(Product p, double price){
        p.setPrice(price);
        saveProducts();
    }

    public void setProductImage(Product p, String uri){
        p.setImageUri(uri);
        saveProducts();
    }

    public void deleteProduct(Product p){
        ArrayList<Integer> positionToRemove = new ArrayList<>();
        for (int i = 0; i < getListItems().size(); i++){
            if (listItems.get(i).getProduct().equals(p)){
                positionToRemove.add(i);
            }
        }
        for (Integer i: positionToRemove){
            listItems.remove((int)i);
        }
        products.remove(p);
        saveListItems();
        saveProducts();
    }

    public boolean isProductExist(Product product){
        return products.contains(product);
    }




    /**
     * @return An list containing the names of all the products registered
     */
    public ArrayList<String> getAllProductsName(){
        ArrayList<String> toReturn = new ArrayList<>();
        for (Product p: getProducts()){
            toReturn.add(p.getName());
        }
        return toReturn;
    }

    public double getProductPrice(Product product){
        return product.getPrice();
    }

    /**
     * Get all the associations between lists and products
     * @return All the association betwwen lists and products
     */
    public ArrayList<ListItem> getListItems() {
        if (listItems == null){
            try {
                FileInputStream fis = getApplicationContext().openFileInput(LISTITEM_SAVE_FILE);
                ObjectInputStream is = new ObjectInputStream(fis);
                listItems = (ArrayList<ListItem>) is.readObject();
                is.close();
                fis.close();
            } catch (IOException|ClassNotFoundException e) {
                listItems = new ArrayList<>();
            }
        }
        return listItems;

    }

    /**
     * Returns the associations from the list @param l
     * @param l The list in which retrieve associations
     * @return Associations linked to the list l
     */
    public ArrayList<ListItem> getItemsInList(List l){

        ArrayList<ListItem> toReturn = new ArrayList<>();
        for (ListItem item: getListItems()){
            if (Objects.equals(item.getList().getName(), l.getName())){
                toReturn.add(item);
            }
        }
        return toReturn;
    }

    /**
     * @param l The list in which retrieve products
     * @return The products contained in list l
     */
    public ArrayList<Product> getProductsInList(List l){
        ArrayList<Product> toReturn = new ArrayList<>();
        for (ListItem i: getItemsInList(l)){
            toReturn.add(i.getProduct());
        }
        Collections.sort(toReturn);
        return toReturn;
    }

    /**
     * Checks if a product is in a list
     * @param p product to check
     * @param l list to search in for the product
     * @return true if the product is in the list, else false
     */
    public boolean isProductInList(Product p, List l){
        for (ListItem item: getItemsInList(l)){
            if (item.getProduct().equals(p)){
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a product to a list
     * @param p product to add
     * @param l the list in which the product has to be added
     */
    public void addProductToList(Product p, List l){
        if (!isProductInList(p, l)){
            listItems.add(new ListItem(1, ListItem.ItemUnit.PIECE, p, l));
            saveListItems();
        }

    }

    /**
     * Adds a product to a list by its name. If the product is not found, does nothing
     * @param productName The name of the product to add
     * @param l the list
     */
    public void addProductToList(String productName, List l){
        Product p = getProductFromName(productName);
        if (p != null){
            addProductToList(p, l);
        }
    }

    /**
     *
     * @param name
     * @return
     */
    private Product getProductFromName(String name){
        for(Product p: getProducts()){
            if (name.toLowerCase().equals(p.getName().toLowerCase())){
                return p;
            }
        }
        return null;
    }

    public ArrayList<String> getAllCategoriesName(){
        ArrayList<String> categoriesNames = new ArrayList<>();
        for (Category c: getAllCategories()){
            categoriesNames.add(c.getName());
        }
        return categoriesNames;
    }

    private ArrayList<Category> getAllCategories(){
        ArrayList<Category> cat = new ArrayList<>();
        cat.addAll(getCategoriesFromItems(getProducts()).keySet());
        Collections.sort(cat);
        return cat;
    }

    /**
     * Removes the product from a list
     * @param p the product to remove
     * @param l the list in which the product has to be removed
     */
    public void removeProductFromList(Product p, List l){
        int positionToRemove = -1;
        for (int i = 0; i < getListItems().size(); i++){
            ListItem li = getListItems().get(i);
            if (p.equals(li.getProduct()) && l.equals(li.getList())){
                positionToRemove = i;
                break;
            }
        }
        if (positionToRemove != -1) {
            listItems = getListItems();
            listItems.remove(positionToRemove);
            saveLists();
        }
    }

    /**
     * Save all the associations product/list
     */
    private void saveListItems(){
        FileOutputStream fos;
        try {
            fos = getApplicationContext().openFileOutput(LISTITEM_SAVE_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream os;
            os = new ObjectOutputStream(fos);
            os.writeObject(listItems);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compute the size in pixels from the dps wanted
     * @param dps size wanted in dps
     * @return size in pixel
     */
    public int getPixelsFromDPs(int dps){
        Resources r = this.getResources();
        return (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
    }

    /**
     * Returns all the categories found into the products array mapped with the number of products
     * in each categories
     * @param products Products to found categories
     * @return All the different categories found [category : number of item in it]
     */
    public TreeMap<Category, Integer> getCategoriesFromItems(ArrayList<Product> products){
        TreeMap<Category, Integer> ret = new TreeMap<>();

        for (Product p: products){
            if (ret.containsKey(p.getCategory())){
                ret.put(p.getCategory(), ret.get(p.getCategory()) + 1);
            }else{
                ret.put(p.getCategory(), 1);
            }
        }
        return ret;
    }

    /**
     * Sets a products as checked into a list
     * @param p the product to mark as "bought"
     * @param l the list in which checks the product
     */
    public void toggleItemInList(Product p, List l){
        for (ListItem item: getItemsInList(l)){
            if (item.getProduct().equals(p)){
                item.toggleChecked();
                saveListItems();
                break;
            }
        }
    }

    /**
     * Checks if a product is checked in a list
     * @param p the product to check if it is checked
     * @param l the list
     * @return true if the item is checked in the list. False if it is not OR IF IT IS NOT PRESENT IN THE LIST
     */
    public boolean isItemChecked(Product p, List l){
        for (ListItem item: getItemsInList(l)){
            if (item.getProduct().equals(p)){
                return item.isChecked();
            }
        }
        return false;
    }

    /**
     * Returns a string that indicates the quantity of the product in a list
     * @param p product to get the quantity
     * @param l list in which the quantity has to be checked
     * @return The string : "quantity units"
     */
    public String getQuantityProductInList(Product p, List l){
        for (ListItem item: getItemsInList(l)){
            if (item.getProduct().equals(p)){
                return item.getQuantity() + " " + item.getQuantityUnits();
            }
        }

        return "";
    }

    /**
     * Returns only the quantity of a product in a list (without the unit)
     * @param p the product
     * @param l the list
     * @return the quantity (without units)
     */
    public int getIntQuantityProductInList(Product p, List l) {
        for (ListItem item: getItemsInList(l)){
            if (item.getProduct().equals(p)) {
                return item.getQuantity();
            }
        }
        return 1;
    }

    /**
     * Returns the position of the units set for a product in a list. (the position in {@link #getAllQuantityUnits()})
     * @param p the product to get the unit
     * @param l the list
     * @return the position of the quantity set in the list {@link #getAllQuantityUnits()}
     */
    public int getPositionUnitsProductInList(Product p, List l){
        for (ListItem item: getItemsInList(l)){
            if (item.getProduct().equals(p)) {
                return getAllQuantityUnits().indexOf(item.getQuantityUnits());
            }
        }
        return 0;
    }

    /**
     * Set the quantity (and its unit) for a product in a list
     * @param p the product to set the quantity
     * @param l the list
     * @param quantity the quantity
     * @param unit must be in [PIECE, DIZAINE, LITRE, GRAMME, KILOGRAMME, PACK, PAQUET, DOUZAINE, BOUTEILLE]
     */
    public void setQuantityProductInList(Product p, List l, int quantity, String unit){
        for (ListItem item: getItemsInList(l)){
            if (item.getProduct().equals(p)){
                item.setQuantity(quantity);
                item.setUnit(getUnitFromString(unit));
                saveListItems();
                break;
            }
        }
    }

    /**
     *
     * @param s the units, must be in [PIECE, DIZAINE, LITRE, GRAMME, KILOGRAMME, PACK, PAQUET, DOUZAINE, BOUTEILLE]
     * @return the enum object from {@link com.example.nicolas.shoptic.core.ListItem.ItemUnit}
     */
    private ListItem.ItemUnit getUnitFromString(String s){
        return ListItem.ItemUnit.values()[getAllQuantityUnits().indexOf(s.toLowerCase())];
    }


    /**
     * @return Returns all the quantity units available as a string array
     */
    public ArrayList<String> getAllQuantityUnits(){
        ArrayList<String> toReturn = new ArrayList<>();
        for (ListItem.ItemUnit u: ListItem.ItemUnit.values()){
            toReturn.add(u.toString().toLowerCase());
        }
        return toReturn;
    }

    public void createNotification(String strContent){
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.shoptic)
                        .setContentTitle("ShopTic")
                        .setContentText(strContent);

        Intent resultIntent = new Intent(this, MainActivity.class);

        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        // Sets an ID for the notification
        int mNotificationId = 1;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public String getTextFromFrequency(Frequency fr){
        switch (fr){
            case ONCE:
                return "Une seule fois";
            case HEBDO:
                return "Toutes les semaines";
            case MONTHLY:
                return "Tous les mois";
            case ANNUALLY:
                return "Tous les ans";
            default:
                return "";
        }
    }

    public Frequency getFrequencyFromText(String str){
        for (Frequency f: Frequency.values()) {
            getTextFromFrequency(f);
            if(str.equals(getTextFromFrequency(f))){
                return f;
            }
        }
        return Frequency.ONCE;
    }

    public int getTimeInMilliForRepeatition(Frequency fr){
        switch (fr){
            case ONCE:
                return 0;
            case HEBDO:
                return 1000*60*60*24*7;
            case MONTHLY:
                return 1000*60*60*24*30;
            case ANNUALLY:
                return 1000*60*60*24*365;
            default:
                return 0;
        }
    }

    public void changeAlertState(List list, Boolean b) {
        for (List lst : getLists()) {
            if (lst.getName().equals(list.getName())) {
                lst.setAlarm(b);
                saveListItems();
                break;
            }
        }
        saveLists();
    }

    public void setLocation(List list, LatLng latlng){
        for (List lst: getLists()){
            if (lst.getName().equals(list.getName())){
                lst.setLat(latlng.latitude);
                lst.setLng(latlng.longitude);
                saveListItems();
                break;
            }
        }
        saveLists();
    }

    public void onLocationChange(double lat, double lon){
        for (List list: getLists()){
            System.out.println("lat :" + list.getLat() + " lon :" + list.getLng());
            double dist = distance(lat, list.getLat(), lon, list.getLng());
            if (dist<500){
                createNotification("Vous etes prêt de la localisation pour allez faire la liste " + list.getName());
                long time = 2000;
                Vibrator vibrator =(Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(time);

            }

        }

    }

    public static double distance(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters


        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }


}
