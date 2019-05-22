package com.eugeneskachko.notes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eugeneskachko.notes.dataBase.Note;

import java.text.SimpleDateFormat;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private Context context;
    private List<Note> noteList;

    public NotesAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_notes, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder notesViewHolder, int position) {
        Note note = noteList.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm");

        if (note.getNoteTitle().length() > 20)
            notesViewHolder.textViewTitle.setText(note.getNoteTitle().substring(0, 20) + "...");
        else
            notesViewHolder.textViewTitle.setText(note.getNoteTitle());
        if (note.getNoteDescription().length() > 99)
            notesViewHolder.textViewDescription.setText(note.getNoteDescription().substring(0, 99) + "...");
        else
            notesViewHolder.textViewDescription.setText(note.getNoteDescription());
        notesViewHolder.textViewDate.setText(dateFormat.format(note.getUnixTimestamp()));
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewTitle, textViewDescription, textViewDate;

        public NotesViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.tvNoteTitle);
            textViewDescription = itemView.findViewById(R.id.tvNoteDescription);
            textViewDate = itemView.findViewById(R.id.tvNoteDate);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Note note = noteList.get(getAdapterPosition());

            Intent intent = new Intent(context, NoteDetailsActivity.class);
            intent.putExtra("note", note);

            context.startActivity(intent);
        }
    }
}
