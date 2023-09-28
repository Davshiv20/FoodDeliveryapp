package com.example.fooddeliveryapk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class menu extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<FoodItem> foodItemList;

    private List<FoodItem> selectedFoodItems;
    private List<FoodItem> cartItemList;
    String name;
    String address;
    String phoneNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);

        // Initialize RecyclerView and the data list
        recyclerView = findViewById(R.id.recyclerView);
        foodItemList = new ArrayList<>();
        cartItemList = new ArrayList<>();
        selectedFoodItems = new ArrayList<>();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
        phoneNo = intent.getStringExtra("phoneNo");
        TextView totalPriceTextView = findViewById(R.id.totalPriceTextView);
        double totalCartPrice = calculateTotalCartPrice(cartItemList);
        totalPriceTextView.setText("Total Price: Rs." + totalCartPrice);

        // Add sample food items (you can replace these with your actual data)
        foodItemList.add(new FoodItem(1, "Fruit Bowl", "Rs.70", "A delicious bowl of fresh fruits offers a delightful medley of nature's finest flavors and colors, a true feast for the senses. Each bite is a burst of vibrant sweetness, a symphony of textures, and a source of pure nourishment. This inviting bowl is a celebration of health and well-being, combining the lusciousness of ripe fruits with the crispness of freshly cut produce.", 4.5, R.drawable.food_img_1));
        foodItemList.add(new FoodItem(2, "Sprouts Bowl", "Rs.40", "A healthy bowl of sprouts is a wholesome delight that embodies vitality and nutrition. Packed with essential nutrients and a refreshing crunch, it's a nutritious choice that keeps you energized throughout the day. These sprouted gems offer a blend of earthy flavors and a satisfying bite, making them a perfect addition to your balanced diet.", 4.2 , R.drawable.food_img_2));
        foodItemList.add(new FoodItem(3, "Mango Shake", "Rs.50", "A mango shake is a delicious and refreshing beverage made by blending ripe mangoes with milk or yogurt and sweeteners like sugar or honey. It results in a creamy, fruity concoction that's both sweet and tangy, perfect for quenching your thirst and satisfying your sweet tooth on a hot day.", 4.4 , R.drawable.shake_img_1));
        foodItemList.add(new FoodItem(4, "Chocolate Shake", "Rs.50", "A chocolate shake is a delicious and refreshing beverage made by blending ripe mangoes with milk or yogurt and sweeteners like sugar or honey. It results in a creamy, fruity concoction that's both sweet and tangy, perfect for quenching your thirst and satisfying your sweet tooth on a hot day.", 4.3 , R.drawable.chocolatemilkshake));
        foodItemList.add(new FoodItem(5, "Strawberry Shake", "Rs.50", "A strawberry shake is a delicious and refreshing beverage made by blending ripe mangoes with milk or yogurt and sweeteners like sugar or honey. It results in a creamy, fruity concoction that's both sweet and tangy, perfect for quenching your thirst and satisfying your sweet tooth on a hot day.", 4.4 , R.drawable.strawbberry));


        Button goToCartButton = findViewById(R.id.add_to_cart_button);

        // Set an OnClickListener for the "Go to Cart" button
        goToCartButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  ArrayList<Integer> selectedFoodItemIds = new ArrayList<>();
                                                  for (FoodItem item : cartItemList) {
                                                      selectedFoodItemIds.add(item.getId());
                                                  }
                                                  double totalCartPrice = calculateTotalCartPrice(cartItemList);
                                                  showFoodPreview(selectedFoodItemIds, totalCartPrice);
                                              }
                                          });

        // Create a FoodAdapter and set it to the RecyclerView
        foodAdapter = new FoodAdapter(foodItemList);
        recyclerView.setAdapter(foodAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private double calculateTotalCartPrice(List<FoodItem> cartItemList) {
        double total = 0.0;
        for (FoodItem item : this.cartItemList) {
            total += item.getTotalPrice();
        }
        return total;
    }
    private void showFoodPreview(ArrayList<Integer> selectedFoodItemIds, double totalCartPrice) {
        Gson gson = new Gson();
        String selectedFoodItemsJson = gson.toJson(selectedFoodItems);

        Intent intent = new Intent(menu.this, FoodPreviewActivity.class);
        intent.putExtra("selectedFoodItemsJson", selectedFoodItemsJson);
        intent.putIntegerArrayListExtra("selectedFoodItemIds", selectedFoodItemIds);
        intent.putExtra("totalCartPrice", totalCartPrice);
        intent.putExtra("name", name);
        intent.putExtra("address", address);
        intent.putExtra("phoneNo", phoneNo);
        startActivity(intent);
    }

    // Adapter class for the RecyclerView
    private class FoodAdapter extends RecyclerView.Adapter<FoodViewHolder> {

        private List<FoodItem> dataList;

        public FoodAdapter(List<FoodItem> dataList) {
            this.dataList = dataList;
        }

        @Override
        public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
            return new FoodViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final FoodViewHolder holder, final int position) {
            final FoodItem foodItem = dataList.get(position);

            // Bind data to the ViewHolder
            holder.foodName.setText(foodItem.getName());
            holder.foodPrice.setText(foodItem.getPrice());
            holder.foodImage.setImageResource(foodItem.getImageResource());
            holder.quantityTextView.setText(String.valueOf(foodItem.getQuantity()));


            // Handle "Add to Cart" button click
            holder.decreaseQuantityButton.setOnClickListener(view -> {
                int quantity = foodItem.getQuantity();
                if (quantity >= 1) {
                    quantity--;
                    foodItem.setQuantity(quantity);
                    holder.quantityTextView.setText(String.valueOf(quantity));
                    updateCartItemList(cartItemList, foodItem, -1);
                    updateSelectedFoodItems();
                }
            });

            // Handle "Increase Quantity" button click
            holder.increaseQuantityButton.setOnClickListener(view -> {
                int quantity = foodItem.getQuantity();
                quantity++;
                foodItem.setQuantity(quantity);
                holder.quantityTextView.setText(String.valueOf(quantity));
                updateCartItemList(cartItemList, foodItem, 1);
                updateSelectedFoodItems();
            });

        }
        private void updateSelectedFoodItems() {
            selectedFoodItems.clear();
            for (FoodItem item : dataList) {
                if (item.getQuantity() > 0) {
                    selectedFoodItems.add(item);
                }
            }
        }
        private void updateCartItemList(List<FoodItem> cartItemList, FoodItem foodItem, int quantityChange) {
            boolean itemFound = false;
            for (FoodItem item : cartItemList) {
                if (item.getId() == foodItem.getId()) {
                    item.setQuantity(item.getQuantity() + quantityChange);
                    itemFound = true;
                    break;
                }
            }
            if (!itemFound && quantityChange > 0) {
                // If the item is not in the cart, add it
                FoodItem cartItem = new FoodItem(foodItem.getId(), foodItem.getName(), foodItem.getPrice(), foodItem.getDescription(), foodItem.getRating(), foodItem.getImageResource());
                cartItem.setQuantity(quantityChange);
                cartItemList.add(cartItem);
            } else if (itemFound && quantityChange < 0 && foodItem.getQuantity() == 0) {
                // If the item is removed from the cart completely, remove it from cartItemList
                cartItemList.remove(foodItem);
            }
            // Update the total price TextView
            TextView totalPriceTextView = findViewById(R.id.totalPriceTextView);
            double totalCartPrice = calculateTotalCartPrice(cartItemList);
            totalPriceTextView.setText("Total Price: Rs." + totalCartPrice);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }
//    private void showFoodPreview(FoodItem foodItem) {
//        Intent intent = new Intent(menu.this, FoodPreviewActivity.class);
//        double totalCartPrice = calculateTotalCartPrice();
//        intent.putExtra("foodName", foodItem.getName());
//        intent.putExtra("foodDescription", foodItem.getDescription());
//        intent.putExtra("foodRating", foodItem.getRating());
//        Intent.putExtra("totalCartPrice", String.valueOf(totalCartPrice));
//        intent.putExtra("pricePerItem", Double.parseDouble(foodItem.getPrice().replace("Rs.", "")));
//        intent.putExtra("name", name);
//        intent.putExtra("address", address);
//        intent.putExtra("phoneNo", phoneNo);
//        startActivity(intent);
//    }

    // ViewHolder class for each item in the RecyclerView
    private static class FoodViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;

        Button decreaseQuantityButton;
        TextView quantityTextView;
        Button increaseQuantityButton;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            decreaseQuantityButton = itemView.findViewById(R.id.decrease_quantity_button);
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
            increaseQuantityButton = itemView.findViewById(R.id.increase_quantity_button);
        }
    }

    // Data model class for a food item
    public class FoodItem {
        private int id;
        private String name;
        private String price;
        private String description;
        private double rating;
        private int imageResource;
        private int quantity;

        public FoodItem(int id, String name, String price, String description, double rating, int imageResource) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.description = description;
            this.rating = rating;
            this.imageResource = imageResource;
            this.quantity = 0; // Initialize quantity to 1 by default
        }

        // Getters and setters for all attributes
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }

        public String getDescription() {
            return description;
        }

        public double getRating() {
            return rating;
        }

        public int getImageResource() {
            return imageResource;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

            public double getTotalPrice() {
            // Calculate the total price based on quantity
            return quantity * Double.parseDouble(price.replace("Rs.", ""));
        }
    }

}