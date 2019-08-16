package square.events.types;

import square.events.Event;
import square.window.layers.classes.Food;

public class FoodEatenEvent extends Event {
    private Food food;

    public FoodEatenEvent(Food food) {
        super(Type.FOOD_EATEN);
        this.food = food;
    }

    public Food getFood() {
        return food;
    }

    public int getScoreValue() {
        return food.getScoreValue();
    }
}
