package com.example.deepflavours.Adapter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.deepflavours.Fragment.HomeFragment;
import com.example.deepflavours.Fragment.RecipeDetailFragment;
import com.example.deepflavours.Login;
import com.example.deepflavours.MainActivity;
import com.example.deepflavours.Model.Client;
import com.example.deepflavours.Model.Data;
import com.example.deepflavours.Model.MyResponse;
import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.Model.Sender;
import com.example.deepflavours.Model.Token;
import com.example.deepflavours.Model.User;
import com.example.deepflavours.R;
import com.example.deepflavours.SignUp;
import com.example.deepflavours.notification.APIService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public Context mContext;
    public List<Recipe> mPost;

    public static final String CHANNEL_LIKE = "channelLike";

    private NotificationManagerCompat notificationManager;
    APIService apiService;
    boolean notify= false;



    private FirebaseUser firebaseUser;
    List<String> cookedRecipesIds = new ArrayList<>();

    public PostAdapter(Context mContext, List<Recipe> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item_home_fragment, viewGroup, false);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        notificationManager = NotificationManagerCompat.from(mContext);

        return new PostAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Recipe post = mPost.get(i);


        viewHolder.titlePostItem.setText(post.getTitle());
        Glide.with(mContext).load(post.getRecipeimage()).into(viewHolder.imagePost);


        isLiked(post.getRecipeid(), viewHolder.like);

        nrLikes(viewHolder.likes, post.getRecipeid());

        nrCooked(viewHolder.cooknr, post.getRecipeid());

        isSaved(post.getRecipeid(), viewHolder.save);


        viewHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CookedRecipes").child(post.getRecipeid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean isCookedByCurrentUser = false;


                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            if (userSnapshot.getKey().equals(currentUserId)) {
                                isCookedByCurrentUser = true;

                            }
                        }

                        if (!isCookedByCurrentUser && viewHolder.save.getTag().equals("save")) {
                            FirebaseDatabase.getInstance().getReference().child("Saves").child(post.getRecipeid())
                                    .child(firebaseUser.getUid()).setValue(true);
                        } else if (viewHolder.save.getTag().equals("saved")) {
                            FirebaseDatabase.getInstance().getReference().child("Saves").child(post.getRecipeid())
                                    .child(firebaseUser.getUid()).removeValue();
                        } else if (isCookedByCurrentUser && viewHolder.save.getTag().equals("save")) {
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.already_cooking_this), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        viewHolder.imagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid", post.getRecipeid());
                editor.apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RecipeDetailFragment("HomeFragment", post.getRecipeid())).commit();
            }
        });


        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getRecipeid())
                            .child(firebaseUser.getUid()).setValue(true);

                    addNotifications(post.getUserid(), post.getRecipeid(),post.getUserid());

//                    sendOnChannelLike(v);

                    if(notify) {
                        sendNotification(post.getUserid(),firebaseUser.getUid());
                    }
                    notify=true;

                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getRecipeid())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView postItem_CardView;
        public TextView titlePostItem, cooknr, textView_CookThis, likes;
        public ImageView imagePost, like, save,dot_notify;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postItem_CardView = itemView.findViewById(R.id.post_CardView);
            titlePostItem = itemView.findViewById(R.id.post_titleCardView);
            cooknr = itemView.findViewById(R.id.cooktimes_nr);
            textView_CookThis = itemView.findViewById(R.id.tv_timesCook);
            imagePost = itemView.findViewById(R.id.image_Post_CardView);
            likes = itemView.findViewById(R.id.nr_like_post);
            like = itemView.findViewById(R.id.like_post);
            save = itemView.findViewById(R.id.save_post);
            dot_notify = itemView.findViewById(R.id.notifications_dot);
        }
    }

    private void isLiked(String postid, ImageView imageView) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_baseline_favorite_24);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void addNotifications(String userid, String postid, String receiver){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text" , "liked your post");
        hashMap.put("postid", postid );
        hashMap.put("receiver", receiver);
        hashMap.put("ispost", true);

        reference.push().setValue(hashMap);

//        createNotificationChannel();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 User user = dataSnapshot.getValue(User.class);
                 sendNotification(receiver,user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       // updateToken(FirebaseInstanceId.getInstance().getToken());
    }

//    private void createNotificationChannel(){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_LIKE,
//                    "channelLike",
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//
//            NotificationManager manager = mContext.getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }
//    }

//    public void sendOnChannelLike(View v){
//
//        Notification notification = new NotificationCompat.Builder(mContext,CHANNEL_LIKE)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("You've got a new like")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .build();
//
//        notificationManager.notify(1, notification);
//
//
//    }

    private void sendNotification(String receiver, final String username){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Token token = dataSnapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid());

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        if(response.body().success !=1){
                                                Toast.makeText(mContext, "Failed!",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void updateToken(String token){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        databaseReference.child(firebaseUser.getUid()).setValue(token1);
    }



    private void nrLikes(TextView likes, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void nrCooked(TextView cook, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("CookedRecipes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cook.setText(dataSnapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void isSaved(String postid, ImageView imageView) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Saves")
                .child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_baseline_turned_in_24_white);
                    imageView.setTag("saved");
                } else {
                    imageView.setImageResource(R.drawable.ic_baseline_turned_in_not_24);
                    imageView.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
