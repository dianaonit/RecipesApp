package com.example.deepflavours.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.deepflavours.Fragment.ProfileFragment;
import com.example.deepflavours.Model.User;
import com.example.deepflavours.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends  RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private String sourceFragment;
    private String previousUser;


    private FirebaseUser firebaseUser;

    public UserAdapter(Context mContext, List<User> mUsers){
        this.mContext = mContext;
        this.mUsers = mUsers;

    }

    public UserAdapter(Context mContext, List<User> mUsers, String sourceFragment){
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.sourceFragment = sourceFragment;
    }

    public UserAdapter(Context mContext, List<User> mUsers, String sourceFragment, String previousUser){
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.sourceFragment = sourceFragment;
        this.previousUser = previousUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.user_item,viewGroup,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            final User user = mUsers.get(i);

            viewHolder.btn_follow.setVisibility(View.VISIBLE);

            viewHolder.userfullname.setText(user.getUsername());
            Glide.with(mContext).load(user.getImageurl()).into(viewHolder.image_profile);

            isFollowing(user.getId(),viewHolder.btn_follow);


            if(user.getId().equals(firebaseUser.getUid())){
                viewHolder.btn_follow.setVisibility(View.GONE);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                            ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    new ProfileFragment(user.getId(),sourceFragment, previousUser)).commit();

                }
            });

            viewHolder.btn_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(viewHolder.btn_follow.getText().toString().equals("follow") || viewHolder.btn_follow.getText().toString().equals("folgen") || viewHolder.btn_follow.getText().toString().equals("suivre") || viewHolder.btn_follow.getText().toString().equals("urmărește") ){
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                                .child("following").child(user.getId()).setValue(true);
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                                .child("followers").child(firebaseUser.getUid()).setValue(true);

                        addNotifications(user.getId());

                    }else{
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                                .child("following").child(user.getId()).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                                .child("followers").child(firebaseUser.getUid()).removeValue();
                    }
                }
            });

    }


    private void addNotifications(String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text" , "started following you");
        hashMap.put("postid", "" );
        hashMap.put("ispost", false);

        reference.push().setValue(hashMap);

    }



    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userfullname;
        public CircleImageView image_profile;
        public Button btn_follow;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userfullname = itemView.findViewById(R.id.fullname);
            image_profile = itemView.findViewById(R.id.image_profile);
            btn_follow = itemView.findViewById(R.id.btn_follow);
        }


    }

    private void isFollowing(String userid, Button button){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(userid).exists()){
                        button.setText(mContext.getResources().getString(R.string.btn_following));
                    }else{
                        button.setText(mContext.getResources().getString(R.string.btn_follow));
                    }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
