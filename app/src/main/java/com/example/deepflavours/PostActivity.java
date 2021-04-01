package com.example.deepflavours;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deepflavours.Fragment.AllMyPostFragment;
import com.example.deepflavours.Fragment.HomeFragment;
import com.example.deepflavours.Fragment.ProfileFragment;
import com.example.deepflavours.Fragment.SaveFragment;
import com.example.deepflavours.Fragment.SearchFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    Uri imageUri;
    String myUrl = "";
    StorageTask uploadTask;
    StorageReference storageReference;

    ImageView image_added, close, post;
    TextInputEditText title, description, servings, prepTime, cookTime, ingredients, directions;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        close = findViewById(R.id.close);
        image_added = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        prepTime = findViewById(R.id.prepTime);
        cookTime = findViewById(R.id.cookTime);
        ingredients = findViewById(R.id.ingredients);
        directions = findViewById(R.id.directions);
        servings = findViewById(R.id.servings);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("recipes");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                messageValidationError();

            }
        });

        CropImage.activity()
                .setAspectRatio(1, 1)
                .start(PostActivity.this);
    }


    private void messageValidationError() {

        final String rTitle = title.getText().toString().trim();
        final String rDescription = description.getText().toString().trim();
        final String rSevings = servings.getText().toString().trim();
        final String rPrepTime = prepTime.getText().toString().trim();
        final String rCookTime = cookTime.getText().toString().trim();
        final String rIngredients = ingredients.getText().toString().trim();
        final String rDirections = directions.getText().toString().trim();

        boolean error = false;


        if (rTitle.length() > 25) {
            title.setError("Max length: 25 char!");
            title.requestFocus();
            error = true;
        } else if (rTitle.isEmpty()) {
            title.setError("Can't be empty!");
            title.requestFocus();
            error = true;
        } else {
            title.setError(null);
            title.requestFocus();
        }


        if (rDescription.length() > 40) {
            description.setError("Max length: 40 char!");
            description.requestFocus();
            error = true;
        } else if (rDescription.isEmpty()) {
            description.setError("Can't be empty!");
            description.requestFocus();
            error = true;
        } else {
            description.setError(null);
            description.requestFocus();
        }


        if (rSevings.length() > 2) {
            servings.setError("Max length: 2 char!");
            servings.requestFocus();
            error = true;
        } else if (rSevings.isEmpty()) {
            servings.setError("Can't be empty!");
            servings.requestFocus();
            error = true;
        } else {
            servings.setError(null);
            servings.requestFocus();
        }


        if (rPrepTime.length() > 3) {
            prepTime.setError("Max length: 3 char!");
            prepTime.requestFocus();
            error = true;
        } else if (rPrepTime.isEmpty()) {
            prepTime.setError("Can't be empty!");
            prepTime.requestFocus();
            error = true;
        } else {
            prepTime.setError(null);
            prepTime.requestFocus();
        }


        if (rCookTime.length() > 3) {
            cookTime.setError("Max length: 3 char!");
            cookTime.requestFocus();
            error = true;
        } else if (rCookTime.isEmpty()) {
            cookTime.setError("Can't be empty!");
            cookTime.requestFocus();
            error = true;
        } else {
            cookTime.setError(null);
            cookTime.requestFocus();
        }

        if (rIngredients.isEmpty()) {
            ingredients.setError("Can't be empty!");
            ingredients.requestFocus();
            error = true;
        } else {
            ingredients.setError(null);
            ingredients.requestFocus();
        }

        if (rDirections.isEmpty()) {
            directions.setError("Can't be empty!");
            directions.requestFocus();
            error = true;
        } else {
            directions.setError(null);
            directions.requestFocus();
        }

        if (!error) {
            uploadImage();
        }

    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void uploadImage() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting..");
        progressDialog.show();

        if (imageUri != null) {

            StorageReference filereference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            uploadTask = filereference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        myUrl = downloadUri.toString();



                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
                        String recipeid = reference.push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("recipeid", recipeid);
                        hashMap.put("recipeimage", myUrl);
                        hashMap.put("title", title.getText().toString());
                        hashMap.put("description", description.getText().toString());
                        hashMap.put("servings", servings.getText().toString());
                        hashMap.put("preparationtime", prepTime.getText().toString());
                        hashMap.put("cooktime", cookTime.getText().toString());
                        hashMap.put("ingredients", ingredients.getText().toString());
                        hashMap.put("directions", directions.getText().toString());
                        hashMap.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        reference.child(recipeid).setValue(hashMap);



                        progressDialog.dismiss();

                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                        finish();

                    } else {
                        Toast.makeText(PostActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "No Image Selected!", Toast.LENGTH_SHORT).show();

        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            image_added.setImageURI(imageUri);

        } else {
            startActivity(new Intent(PostActivity.this, MainActivity.class));
            finish();
        }

    }


}