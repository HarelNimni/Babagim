package harel.nimni.babagim;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class Helpers
{
    public interface CallBackUser
    {
        void onSuccess(ArrayList<User> users);
        void onFailure(String errorMessage);
    }
    public static void insertUsersData(ArrayList<User> usersData, CallBackUser callBack) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersData.clear();
                for (DataSnapshot snapshotUser : snapshot.getChildren()) {

                    User user = snapshotUser.getValue(User.class);
                    usersData.add(user);
                }
                callBack.onSuccess(usersData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callBack.onFailure("error");
            }
        });
    }

    public static ArrayList<ArrayList<Integer>> menuImages = new ArrayList<>(Arrays.asList(
            new ArrayList<Integer>(Arrays.asList(R.drawable.pita_shawarma, R.drawable.lapha_shawarma, R.drawable.baget_shawarma, R.drawable.pita_shawarma, R.drawable.lapha_shawarma,
                    R.drawable.fried_pita_shawarma, R.drawable.fried_lapha_shawarma, R.drawable.baba_logo, R.drawable.plate_shawarma, R.drawable.baba_logo)),

            new ArrayList<Integer>(Arrays.asList(R.drawable.pita_falafel, R.drawable.lapha_falafel, R.drawable.baba_logo, R.drawable.pita_falafel, R.drawable.lapha_falafel, R.drawable.plate_falafel)),

            new ArrayList<Integer>(Arrays.asList(R.drawable.pita_chicken_breast, R.drawable.lapha_chicken_breast, R.drawable.baget_chicken_breast, R.drawable.plate_chicken_breast,
                    R.drawable.pita_kabab, R.drawable.lapha_kabab, R.drawable.baget_kabab, R.drawable.plate_kabab,
                    R.drawable.baba_logo, R.drawable.lapha_chicken, R.drawable.baget_chicken, R.drawable.plate_chicken,
                    R.drawable.pita_steak, R.drawable.lapha_steak, R.drawable.baget_steak, R.drawable.plate_steak,
                    R.drawable.pita_jerusalem_mix, R.drawable.lapha_jerusalem_mix, R.drawable.baba_logo, R.drawable.plate_jerusalem_mix,
                    R.drawable.pita_schnitzel, R.drawable.lapha_schnitzel, R.drawable.baget_schnitzel, R.drawable.plate_schnitzel, R.drawable.hallah_schnitzel)),

            new ArrayList<Integer>(Arrays.asList(R.drawable.shawarma_deal, R.drawable.falafel_deal, R.drawable.baba_logo, R.drawable.baba_logo, R.drawable.kabab_deal, R.drawable.chicken_breast_deal, R.drawable.schnitzel_deal, R.drawable.baba_logo, R.drawable.baba_logo)),

            new ArrayList<Integer>(Arrays.asList(R.drawable.chips, R.drawable.rice, R.drawable.hummus, R.drawable.salad, R.drawable.baba_logo, R.drawable.mini_salads, R.drawable.falafel_box,
                    R.drawable.pita, R.drawable.lapha, R.drawable.baget, R.drawable.fried_onion, R.drawable.baba_spice)),

            new ArrayList<Integer>(Arrays.asList(R.drawable.apple_juice, R.drawable.grapes_juice, R.drawable.eshkolit_juice, R.drawable.orange_juice, R.drawable.cola, R.drawable.cola_zero, R.drawable.sprite, R.drawable.fanta,
                    R.drawable.soda, R.drawable.cola_glass, R.drawable.cola_zero_glass, R.drawable.sprite_glass, R.drawable.big_cola, R.drawable.big_cola_zero, R.drawable.water))));

    public static final int  SHAWARMA_KEY = 0;
    public static final int  FALAFEL_KEY = 1;
    public static final int  GRILL_KEY = 2;
    public static final int  DEALS_KEY = 3;
    public static final int  SIDES_KEY = 4;
    public static final int  DRINKS_KEY = 5;

}
