package com.example.katching.util;

import com.example.katching.R;

import java.util.HashMap;
import java.util.Map;

public class ImageMap {
    public static final Map<String, Integer> budgetImageMap = new HashMap<>();
    static {
        budgetImageMap.put("Food / Beverages", R.drawable.food);
        budgetImageMap.put("Housing / Utilities", R.drawable.housing);
        budgetImageMap.put("Transportation", R.drawable.transportation);
        budgetImageMap.put("Shopping", R.drawable.shopping);
        budgetImageMap.put("Health / Wellness", R.drawable.medicine);
        budgetImageMap.put("Entertainment / Leisure", R.drawable.entertainment);
        budgetImageMap.put("Education", R.drawable.education);
        budgetImageMap.put("Communication", R.drawable.contact);
        budgetImageMap.put("Pets", R.drawable.pet);
        budgetImageMap.put("Savings / Investments", R.drawable.investment);
        budgetImageMap.put("Gifts / Donations", R.drawable.gift);
        budgetImageMap.put("Personal / Miscellaneous", R.drawable.other);
    }

    public static int getImageResource(String type) {
        Integer res = budgetImageMap.get(type);
        return res != null ? res : R.drawable.other;
    }
}
