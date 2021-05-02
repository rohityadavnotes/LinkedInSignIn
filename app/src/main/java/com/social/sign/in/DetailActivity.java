package com.social.sign.in;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.social.sign.in.customimageview.CircleImageView;
import com.social.sign.in.data.local.SharedPrefs;
import com.social.sign.in.data.remote.RetrofitService;
import com.social.sign.in.data.remote.ServiceGenerator;
import org.json.JSONArray;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    public static final String LINKED_IN_ACCOUNT_DETAILS = "linked_in_account_details";

    private CircleImageView profileCircleImageView;
    private TextView nameTextView;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView emailTextView;
    private TextView linkedInIdTextView;

    private MaterialButton signOutMaterialButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initializeView();
        initializeObject();
        initializeEvent();
    }

    protected void initializeView() {
        profileCircleImageView      = findViewById(R.id.pictureCircleImageView);
        nameTextView                = findViewById(R.id.nameTextView);
        firstNameTextView           = findViewById(R.id.firstNameTextView);
        lastNameTextView            = findViewById(R.id.lastNameTextView);
        emailTextView               = findViewById(R.id.emailTextView);
        linkedInIdTextView          = findViewById(R.id.linkedInIdTextView);
        signOutMaterialButton       = findViewById(R.id.signOutMaterialButton);
    }

    protected void initializeObject() {
        String token = SharedPrefs.getInstance(getApplicationContext()).get("token", String.class);

        if(!token.equals(""))
        {
            String profileUrl = "https://api.linkedin.com/v2/me?projection=(id,firstName,lastName,emailAddress,profilePicture(displayImage~:playableStreams))";
            callApi(profileUrl, token);
        }
    }

    protected void initializeEvent() {
        signOutMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    /*
     ***********************************************************************************************
     ******************************************* Helper methods ************************************
     ***********************************************************************************************
     */
    private void signOut() {
    }

    private void callApi(String profileUrl, String token) {

        RetrofitService retrofitService = ServiceGenerator.createService(getApplicationContext(), RetrofitService.class);
        Call<JsonObject> call = retrofitService.getUser(profileUrl, "Bearer "+token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful())
                {
                    if (response.code() == 200)
                    {
                        Logger.addLogAdapter(new AndroidLogAdapter());
                        Logger.json(response.body().toString());

                        JSONObject jsonObject;
                        try
                        {
                            jsonObject = new JSONObject(response.body().toString());

                            /**
                             * First Name
                             */
                            JSONObject firstName = jsonObject.getJSONObject("firstName");
                            JSONObject localizedFirstName = firstName.getJSONObject("localized");
                            String firstNameString = localizedFirstName.getString("en_US");
                            firstNameTextView.setText(firstNameString);

                            /**
                             * Last Name
                             */
                            JSONObject lastName = jsonObject.getJSONObject("lastName");
                            JSONObject localizedLastName = lastName.getJSONObject("localized");
                            String lastNameString = localizedLastName.getString("en_US");
                            lastNameTextView.setText(lastNameString);

                            nameTextView.setText(firstNameString+" "+lastNameString);

                            /**
                             * Profile image
                             */
                            JSONObject profilePicture = jsonObject.getJSONObject("profilePicture");
                            JSONObject displayImage = profilePicture.getJSONObject("displayImage~");

                            JSONArray elementsArray = displayImage.getJSONArray("elements");
                            JSONArray identifiersArray = elementsArray.getJSONObject(0).getJSONArray("identifiers");

                            JSONObject identifier = identifiersArray.getJSONObject(0);
                            String profilePictureURLArrayString = identifier.getString("identifier");

                            Glide.with(getApplicationContext())
                                    .load(profilePictureURLArrayString)
                                    .error(R.drawable.placeholder)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            return false;
                                        }
                                    })
                                    .into(profileCircleImageView);

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        System.out.println("Something wrong");
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                System.out.println("*****"+throwable.getMessage());
            }
        });
    }
}