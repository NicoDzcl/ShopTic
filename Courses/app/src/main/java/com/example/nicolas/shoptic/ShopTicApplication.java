package com.example.nicolas.shoptic;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.NotificationCompat;
import android.util.TypedValue;

import com.example.nicolas.shoptic.core.Category;
import com.example.nicolas.shoptic.core.Frequency;
import com.example.nicolas.shoptic.core.List;
import com.example.nicolas.shoptic.core.ListItem;
import com.example.nicolas.shoptic.core.Product;

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
                products.add(new Product("Orange", 0., new Category("Alimentaire", false), false));
                products.add(new Product("Citron", 0., new Category("Alimentaire", false), false));
                products.add(new Product("Bière", 0., new Category("Alcool", false), false));
                products.add(new Product("Vin Rouge", 0., new Category("Alcool", false), false));
                products.add(new Product("Stylo", 0., new Category("Fourniture", false), false));
                products.add(new Product("Gomme", 0., new Category("Fourniture", false), false));
                products.add(new Product("Feutre", 0., new Category("Fourniture", false), false));
                products.add(new Product("Assiette", 0., new Category("Vaisselle", false), false));
                products.add(new Product("Frites", 0., new Category("Surgelé", false), false));
            }
        }
        Collections.sort(products);
        return products;
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
     * DOES NOT CHECK IF p IS ALREADY PRESENT IN l
     * @param p product to add
     * @param l the list in which the product has to be added
     */
    public void addProductToList(Product p, List l){
        listItems.add(new ListItem(1, ListItem.ItemUnit.PIECE, p, l));
        saveListItems();
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
                        .setSmallIcon(R.drawable.ic_menu_share)
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

}
