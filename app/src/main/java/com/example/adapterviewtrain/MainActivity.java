package com.example.adapterviewtrain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapterviewtrain.Model.ImageModel;
import com.example.adapterviewtrain.interfaces.OnItemListClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<ImageModel> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.main_list);

        List<String> texts = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            texts.add("item : " + i);
        }
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_multichoice,texts);
//        listView.setAdapter(arrayAdapter);
//        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        images = new ArrayList<>();
        images.add(new ImageModel("image 0", R.drawable.image_1));
        images.add(new ImageModel("image 1", R.drawable.image_3));
        images.add(new ImageModel("image 2", R.drawable.image_1));
        images.add(new ImageModel("image 3", R.drawable.image_3));
        images.add(new ImageModel("image 4", R.drawable.image_1));
        images.add(new ImageModel("image 5", R.drawable.image_3));
        images.add(new ImageModel("image 6", R.drawable.image_1));

        MyArrayAdapter arrayAdapter = new MyArrayAdapter(images);
        arrayAdapter.setListener(new OnItemListClickListener() {
            @Override
            public void onImageClick(View view, int position) {
                Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextClick(View view, int position, String text) {
                Toast.makeText(MainActivity.this, text + " " + position, Toast.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(arrayAdapter);
    }

    public class MyArrayAdapter extends BaseAdapter {
        private ArrayList<ImageModel> data;
        private ImageModel imageModel;
        private OnItemListClickListener listener;

        public MyArrayAdapter(@NonNull ArrayList<ImageModel> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            imageModel = data.get(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_list, parent, false);
                viewHolder = new ViewHolder();
                final ImageView imageView;
                final TextView textView;
                imageView = viewHolder.imageView = convertView.findViewById(R.id.item_list_imageView);
                textView = viewHolder.textView = convertView.findViewById(R.id.item_list_textView);

                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //int pos = (int) view.getTag();
                        View itemView = (View) view.getParent();
                        ListView lv = (ListView) itemView.getParent();
                        int pos = lv.getPositionForView(itemView);
                        if (listener != null) {
                            if (view == imageView) {
                                listener.onImageClick(view, pos);
                                return;
                            }
                            if (view == textView) {
                                listener.onTextClick(view, pos, data.get(pos).getText());
                            }
                        }

                    }
                };
                imageView.setOnClickListener(clickListener);
                textView.setOnClickListener(clickListener);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.imageView.setImageResource(imageModel.getImageId());
            viewHolder.textView.setText(imageModel.getText());

            viewHolder.imageView.setTag(position);
            viewHolder.textView.setTag(position);

            return convertView;
        }

        public void setListener(OnItemListClickListener listener) {
            this.listener = listener;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public ArrayList<ImageModel> getData() {
            return data;
        }

        public void setData(ArrayList<ImageModel> data) {
            this.data = data;
        }

        class ViewHolder {
            ImageView imageView;
            TextView textView;
        }

    }
}