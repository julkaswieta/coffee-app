package com.example.coffeeapp;

import android.graphics.Bitmap;

/**
 * Represents a drink made from a recipe
 * extends from a Drink
 */
public class DrinkFromRecipe extends Drink {
    public static int idCounterR = 0;

    private Recipe recipeUsed;
    private String recipeName;
    private String beansUsed;
    private float amountOfCoffeeUsed;
    private String prepMethodUsed;
    private String extrasUsed;
    private String drinkNotes;

    /**
     * returns the next available id
     * @return  id
     */
    public static int nextId() {
        return ++idCounterR;
    }

    /**
     * returns the Recipe object used to create the drink
     * @return  Recipe
     */
    public Recipe getRecipeUsed() {
        return recipeUsed;
    }

    /**
     * sets the Recipe object used to create the drink
     * @param recipeUsed    Recipe
     */
    public void setRecipeUsed(Recipe recipeUsed) {
        this.recipeUsed = recipeUsed;
    }

    /**
     * returns the recipe name
     * @return  recipe name
     */
    public String getRecipeName() {
        return recipeName;
    }

    /**
     * sets the recipe name
     * @param recipeName
     */
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    /**
     * returns the beans used by the Recipe
     * @return  Bean name + roaster
     */
    public String getBeansUsed() {
        return beansUsed;
    }

    /**
     * Sets the beans to a new string
     * @param beansUsed
     */
    public void setBeansUsed(String beansUsed) {
        this.beansUsed = beansUsed;
    }

    /**
     * returns the amount of coffeee used in th recipe
     * @return
     */
    public float getAmountOfCoffeeUsed() {
        return amountOfCoffeeUsed;
    }

    /**
     * sets the amount of coffeee used in th recipe
     * @param amountOfCoffeeUsed
     */
    public void setAmountOfCoffeeUsed(float amountOfCoffeeUsed) {
        this.amountOfCoffeeUsed = amountOfCoffeeUsed;
    }

    /**
     * returns the prep method of the reicpe
     */
    public String getPrepMethodUsed() {
        return prepMethodUsed;
    }

    /**
     * sets the prep method of the recipe
     * @param prepMethodUsed
     */
    public void setPrepMethodUsed(String prepMethodUsed) {
        this.prepMethodUsed = prepMethodUsed;
    }

    /**
     * returns the extras used by the recipe
     * @return
     */
    public String getExtrasUsed() {
        return extrasUsed;
    }

    /**
     * sets ther extras used by the recipe
     * @param extrasUsed
     */
    public void setExtrasUsed(String extrasUsed) {
        this.extrasUsed = extrasUsed;
    }

    /**
     * returns the notes on the drink
     * @return  notes
     */
    public String getDrinkNotes() {
        return drinkNotes;
    }

    /**
     * setss the notes on the drink
     * @param drinkNotes
     */
    public void setDrinkNotes(String drinkNotes) {
        this.drinkNotes = drinkNotes;
    }
}
