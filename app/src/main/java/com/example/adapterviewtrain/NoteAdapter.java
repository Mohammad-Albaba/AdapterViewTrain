package com.example.adapterviewtrain;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapterviewtrain.Model.Note;

import java.util.ArrayList;
import java.util.List;

/**@
 *
 * Programmed by Ahmed Salem
 *
 */

public class NoteAdapter extends BaseAdapter {

    private ArrayList<Note> originalNotes;
    private ArrayList<Note> filteringNotes;
    private OnEditDeleteListener listener;
    private NoteFilter noteFilter;
    private final static int ITEM_TYPE_NORMAL = 0;
    private final static int ITEM_TYPE_IMPORTANT = 1;

    public NoteAdapter(ArrayList<Note> originalNotes) {
        this.originalNotes = originalNotes;
        filteringNotes = new ArrayList<>();
        filteringNotes.addAll(this.originalNotes);
        noteFilter = new NoteFilter();
    }

    @Override
    public int getCount() {
        return filteringNotes.size();
    }

    @Override
    public Note getItem(int i) {
        return filteringNotes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        NormalViewHolder normalViewHolder;
        ImportantViewHolder importantViewHolder;
        if (view == null) {
            normalViewHolder = new NormalViewHolder();
            importantViewHolder = new ImportantViewHolder();

            if (getItemViewType(i) == ITEM_TYPE_NORMAL) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_note_list, null, false);
                normalViewHolder.textView = view.findViewById(R.id.item_note_tv);
                normalViewHolder.btnEdit = view.findViewById(R.id.item_note_btn_edit);
                normalViewHolder.btnDelete = view.findViewById(R.id.item_note_btn_delete);
                normalViewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onEdit((Note) view.getTag());
                        }
                    }
                });
                normalViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onDelete((Note) view.getTag());
                        }
                    }
                });
                view.setTag(R.layout.item_note_list, normalViewHolder);
            } else {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_note_list_2, null, false);
                importantViewHolder.textView = view.findViewById(R.id.item_note_tv2);
                view.setTag(R.layout.item_note_list_2, importantViewHolder);
            }

        } else {
            normalViewHolder = (NormalViewHolder) view.getTag(R.layout.item_note_list);
            importantViewHolder = (ImportantViewHolder) view.getTag(R.layout.item_note_list_2);
        }

        Note note = getItem(i);
        if (getItemViewType(i) == ITEM_TYPE_NORMAL) {
            if (normalViewHolder != null) {
                normalViewHolder.textView.setText(note.getText());
                normalViewHolder.btnDelete.setTag(note);
                normalViewHolder.btnEdit.setTag(note);
                view.setTag(R.layout.item_note_list, normalViewHolder);
            }

        } else {
            if (importantViewHolder != null) {
                importantViewHolder.textView.setText(note.getText());
                view.setTag(R.layout.item_note_list_2, importantViewHolder);
            }
        }

        return view;
    }
//
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isImportant()) {
            return ITEM_TYPE_IMPORTANT;
        } else {
            return ITEM_TYPE_NORMAL;
        }
    }

    public void delete(Note note) {
        originalNotes.remove(note);
        filteringNotes.remove(note);
        notifyDataSetChanged();
    }

    public void setNativeArray() {
        if (!filteringNotes.equals(originalNotes)) {
            filteringNotes.clear();
            filteringNotes.addAll(this.originalNotes);
            notifyDataSetChanged();
        }
    }

    class NormalViewHolder {
        TextView textView;
        ImageButton btnEdit, btnDelete;
    }

    class ImportantViewHolder {
        TextView textView;
    }

    public ArrayList<Note> getNotes() {
        return originalNotes;
    }

    public void setNotes(ArrayList<Note> data) {
        this.filteringNotes = data;
        notifyDataSetChanged();
    }

    public Filter getFilter() {
        return noteFilter;
    }

    public void setOnEditDeleteListener(OnEditDeleteListener listener) {
        this.listener = listener;
    }

    public void add(String searchQuery, Note note) {
        if (searchQuery == null || note.getText().toLowerCase().contains(searchQuery)) {
            filteringNotes.add(0, note);
        }
        originalNotes.add(0, note);
        notifyDataSetChanged();
    }

    public void set(Note note) {
        originalNotes.set(originalNotes.indexOf(note), note);
        filteringNotes.set(originalNotes.indexOf(note), note);
        notifyDataSetChanged();
    }

    public interface OnEditDeleteListener {
        void onEdit(Note note);

        void onDelete(Note note);
    }

    public class NoteFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResult = new FilterResults();
            String searchQuery = (String) charSequence;
            List<Note> searchNotes = new ArrayList<>();
            for (Note note : originalNotes) {
                if (note.getText().toLowerCase().contains(searchQuery.toLowerCase())) {
                    searchNotes.add(note);
                }
            }
            filterResult.count = searchNotes.size();
            filterResult.values = searchNotes;
            return filterResult;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            setNotes((ArrayList<Note>) filterResults.values);
        }
    }
}
