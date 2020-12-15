package com.example.adapterviewtrain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.app.SearchManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapterviewtrain.Model.Note;

import java.util.ArrayList;

/**@
 *
 * Programmed by Ahmed Salem
 *
 */
public class NoteActivity extends AppCompatActivity {
    private ListView listView;
    private SearchView searchView;
    private Toolbar toolbar;
    private NoteAdapter adapter;
    private String searchQuery ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        toolbar = findViewById(R.id.note_toolbar);
        setSupportActionBar(toolbar);
        listView = findViewById(R.id.note_lv);


        ArrayList<Note> notes = new ArrayList<>();
        notes.add(new Note(1, "Example 1", true));
        notes.add(new Note(2, "Example 2", false));
        notes.add(new Note(3, "Example 3", true));
        notes.add(new Note(4, "Example 4", false));
        notes.add(new Note(5, "Example 5", true));
        adapter = new NoteAdapter(notes);
        listView.setAdapter(adapter);
        adapter.setOnEditDeleteListener(new NoteAdapter.OnEditDeleteListener() {
            @Override
            public void onEdit(Note note) {
                DialogFragmentAddNote dialogFragmentAddNote = new DialogFragmentAddNote();
                Bundle bundle = new Bundle();
                bundle.putParcelable(DialogFragmentAddNote.NOTE_OBJ, note);
                dialogFragmentAddNote.setArguments(bundle);
                dialogFragmentAddNote.setEditListener(new DialogFragmentAddNote.OnEditListener() {
                    @Override
                    public void onEdit(Note note, String textNote, boolean isChecked) {
                        note.setText(textNote);
                        note.setImportant(isChecked);
                        adapter.set(note);
                    }
                });
                dialogFragmentAddNote.show(getSupportFragmentManager(), dialogFragmentAddNote.getTag());
            }

            @Override
            public void onDelete(Note note) {
                adapter.delete(note);
            }
        });
    }

    MenuItem searchMenuItem, addMenuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        searchMenuItem = menu.findItem(R.id.menu_search);
        addMenuItem = menu.findItem(R.id.menu_note_add);
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
//                addMenuItem.setVisible(false);
                addMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
//                addMenuItem.setVisible(false);

                addMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                adapter.setNativeArray();

                return true;
            }
        });
        searchView = (SearchView) searchMenuItem.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchQuery = s;
                adapter.getFilter().filter(searchQuery);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    searchQuery = null;
                    adapter.setNativeArray();
                }
                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchQuery = null;
                adapter.setNativeArray();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_note_add) {
            DialogFragmentAddNote dialogFragmentAddNote = new DialogFragmentAddNote();
            dialogFragmentAddNote.setSaveListener(new DialogFragmentAddNote.OnSaveListener() {
                @Override
                public void onSave(Note note) {
                    adapter.add(searchQuery, note);
                }
            });
            dialogFragmentAddNote.show(getSupportFragmentManager(), dialogFragmentAddNote.getTag());
            return true;
        }
        return false;
    }
}