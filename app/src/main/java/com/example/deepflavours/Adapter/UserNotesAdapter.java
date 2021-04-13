package com.example.deepflavours.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepflavours.Fragment.RecipeDetailFragment;
import com.example.deepflavours.Model.Note;
import com.example.deepflavours.NoteDetailActivity;
import com.example.deepflavours.R;
import com.example.deepflavours.UserNotesActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class UserNotesAdapter extends RecyclerView.Adapter<UserNotesAdapter.ViewHolder> {

    public Context mContext;
    public List<Note> mNote;

    private FirebaseUser firebaseUser;


    public UserNotesAdapter(Context mContext, List<Note> mNote) {
        this.mContext = mContext;
        this.mNote = mNote;
    }


    @NonNull
    @Override
    public UserNotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_notes_item, viewGroup, false);
        return new UserNotesAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UserNotesAdapter.ViewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Note note = mNote.get(i);

        viewHolder.noteTitle.setText(note.getTitle());
        viewHolder.noteDirections.setText(note.getDirections());

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (note.getUserId().equals(firebaseUser.getUid())) {

                    AlertDialog alertDialog = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();

                    alertDialog.setTitle(mContext.getResources().getString(R.string.delete_question));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, mContext.getResources().getString(R.string.delete_post_no),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, mContext.getResources().getString(R.string.delete_post_yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    FirebaseDatabase.getInstance().getReference("Notes")
                                            .child(note.getRecipeId()).child(note.getNoteId())
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(mContext, mContext.getResources().getString(R.string.delete_note), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    dialogInterface.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                return true;
            }
        });


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NoteDetailActivity.class);
                intent.putExtra("noteId", note.getNoteId());
                intent.putExtra("recipeId", note.getRecipeId());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mNote.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView noteTitle, noteDirections;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.note_title);
            noteDirections = itemView.findViewById(R.id.note_directions);

        }
    }


}
