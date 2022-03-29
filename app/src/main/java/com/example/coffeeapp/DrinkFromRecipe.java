package com.example.coffeeapp;

public class DrinkFromRecipe extends Drink {
    private Recipe recipeUsed;
    private String recipeName;
    private String beansUsed;
    private float amountOfCoffeeUsed;
    private String prepMethodUsed;
    private String extrasUsed;
    private String drinkNotes;

    public Recipe getRecipeUsed() {
        return recipeUsed;
    }

    public void setRecipeUsed(Recipe recipeUsed) {
        this.recipeUsed = recipeUsed;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getBeansUsed() {
        return beansUsed;
    }

    public void setBeansUsed(String beansUsed) {
        this.beansUsed = beansUsed;
    }

    public float getAmountOfCoffeeUsed() {
        return amountOfCoffeeUsed;
    }

    public void setAmountOfCoffeeUsed(float amountOfCoffeeUsed) {
        this.amountOfCoffeeUsed = amountOfCoffeeUsed;
    }

    public String getPrepMethodUsed() {
        return prepMethodUsed;
    }

    public void setPrepMethodUsed(String prepMethodUsed) {
        this.prepMethodUsed = prepMethodUsed;
    }

    public String getExtrasUsed() {
        return extrasUsed;
    }

    public void setExtrasUsed(String extrasUsed) {
        this.extrasUsed = extrasUsed;
    }

    public String getDrinkNotes() {
        return drinkNotes;
    }

    public void setDrinkNotes(String drinkNotes) {
        this.drinkNotes = drinkNotes;
    }
}
