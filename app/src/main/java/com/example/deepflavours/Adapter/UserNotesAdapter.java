package com.example.deepflavours.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.deepflavours.Model.Note;
import com.example.deepflavours.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
