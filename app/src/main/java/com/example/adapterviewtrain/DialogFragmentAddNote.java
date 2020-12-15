package com.example.adapterviewtrain;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.adapterviewtrain.Model.Note;

/**@
 *
 * Programmed by Ahmed Salem
 *
 */
public class DialogFragmentAddNote extends DialogFragment {

    public static final String NOTE_OBJ = "NOTE_OBJ";
    private OnSaveListener saveListener;
    private OnEditListener editListener;
    private Button btn_add, btn_cancel;
    private EditText et_note;
    private CheckBox checkBox;
    private Note note;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(NOTE_OBJ);
        }
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_dialog_note, (ViewGroup) getView());
        btn_add = view.findViewById(R.id.item_dialog_btn_add);
        btn_cancel = view.findViewById(R.id.item_dialog_btn_cancel);
        et_note = view.findViewById(R.id.item_dialog_et_note);
        checkBox = view.findViewById(R.id.item_dialog_checkBox);
        if (note == null) {
            builder.setTitle(getString(R.string.add));
        } else {
            builder.setTitle(getString(R.string.edit));
            et_note.setText(note.getText());
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == btn_add) {
                    String text = et_note.getText().toString();
                    if (TextUtils.isEmpty(text)) {
                        Toast.makeText(getContext(), getString(R.string.toast_enter_note), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (note == null) {
                        // new note .
                        if (saveListener != null) {
                            note = new Note((int) System.currentTimeMillis(), text,checkBox.isChecked());
                            saveListener.onSave(note);
                        }
                    } else {
                        // edit note .
                        if (editListener != null) {
                            String textNote = et_note.getText().toString();
                            editListener.onEdit(note, textNote,note.isImportant());
                        }
                    }
                }
                DialogFragmentAddNote.this.dismiss();
            }
        };
        btn_add.setOnClickListener(listener);
        btn_cancel.setOnClickListener(listener);
        builder.setView(view);
        return builder.create();
    }

    public void setSaveListener(OnSaveListener listener) {
        saveListener = listener;
    }

    public void setEditListener(OnEditListener listener) {
        editListener = listener;
    }

    public interface OnSaveListener {
        void onSave(Note note);
    }

    public interface OnEditListener {
        void onEdit(Note note, String textNote,boolean isChecked);
    }

}
