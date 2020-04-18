package org.hitam.epics.biswajeet.anewbeginning;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.hitam.epics.biswajeet.anewbeginning.support.CheckoutItem;
import org.hitam.epics.biswajeet.anewbeginning.support.CheckoutItemAdapter;
import org.hitam.epics.biswajeet.anewbeginning.support.RazorOrderRequest;
import org.hitam.epics.biswajeet.anewbeginning.support.RazorOrderResponse;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CheckoutActivity extends Activity implements PaymentResultListener  {
    public static ArrayList<CheckoutItem> CheckOutList = new ArrayList<>();
    private CheckoutItemAdapter checkoutItemAdapter;
    ListView CheckOutListView;
    TextView TotalPrice;
    Button pay;
    private static final String TAG = "Payment";
    DatabaseReference reference;
    String keyId="";
    String keySecret="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Checkout.preload(getApplicationContext());
        checkoutItemAdapter = new CheckoutItemAdapter(this, CheckOutList);
        CheckOutListView = (ListView) findViewById(R.id.Checkout);
        CheckOutListView.setAdapter(checkoutItemAdapter);
        CheckOutListView.setEmptyView(findViewById(R.id.checkout_empty));
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TotalPrice = (TextView) findViewById(R.id.checkout_total);
        TotalPrice.setText("" + calculateTotal());
        pay = findViewById(R.id.button2);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calculateTotal() > 0) {
                    startPayment();
                } else {
                    Toast.makeText(CheckoutActivity.this, "Please add items to cart to checkout", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void startPayment() {

//        checkout.setKeyID("<YOUR_KEY_ID>");
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_qMP7MlmYKPK5DC");

        /**
         * Set your logo here
         */
//        checkout.setImage(R.drawable.abnf_logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();
            Log.d(TAG, getOrderId());
            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "ANBFH");

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Test Order");
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            Log.d(TAG, "message8937 "+getOrderId());
            options.put("order_id", getOrderId());
            options.put("currency", "INR");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", calculateTotal() * 100);

            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }


    RestTemplate restTemplate = new RestTemplate();
    public String getOrderId()  {
            reference=FirebaseDatabase.getInstance().getReference().child("razorpay");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String key_id=dataSnapshot.child("key_id").getValue().toString();
                    String key_secret=dataSnapshot.child("key_secret").getValue().toString();
                    keyId=key_id;
                    keySecret=key_secret;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            RazorOrderRequest order = new RazorOrderRequest(calculateTotal() * 100);
            HttpHeaders headers = new HttpHeaders();
//            String auth = "rzp_test_qMP7MlmYKPK5DC:StsObG7Geqjmo0Qt4f9uhHDu";
        String auth =String.format("%s:%s",keyId,keySecret);
            auth = new String(Base64.encode(auth.getBytes(), Base64.NO_WRAP), StandardCharsets.UTF_8);
            headers.add("Authorization", String.format("Basic %s", auth));
            HttpEntity<RazorOrderRequest> requestEntity = new HttpEntity<>(order, headers);
            String razorOrderURL = "https://api.razorpay.com/v1/orders";
            ResponseEntity<RazorOrderResponse> responseEntity = restTemplate.exchange(razorOrderURL, HttpMethod.POST, requestEntity, RazorOrderResponse.class);
            RazorOrderResponse response = responseEntity.getBody();
            return response.getId();
    }

//


    public static double calculateTotal() {
        double total = 0;
        for (CheckoutItem checkoutItem : CheckOutList) {
            total += checkoutItem.getQuantity() * checkoutItem.getPrice();
        }
        return total;
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Error:" + s, Toast.LENGTH_SHORT).show();
    }


//    public void checkout(View view) {
//        if (calculateTotal() > 0) {
//            makePaymentBill();
//            sendPaymentIntialisingMail();
//            QWParams qwParams = new QWParams();//object creation
//
//            qwParams.setMobile("8762070029");
//            qwParams.setSignature("55c657c645cfe150f1dad8e891366789211f0397e6b5626cb3b2c8c608390bff");
//            qwParams.setPartnerid("10");
//            qwParams.setAmount(String.valueOf(calculateTotal()));
//            //For test environment
//            qwParams.setEnv(QWConstants.ENV_UAT); //Use QWConstants.ENV_UAT for testing.
//            QWSDKLibInit.init(this, qwParams, QWConstants.PAYMENT);
//        } else {
//            Toast.makeText(this, "Please add items to cart to checkout", Toast.LENGTH_SHORT).show();
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }


}
