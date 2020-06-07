package com.example.links_orgenizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class TagsActivity extends AppCompatActivity implements TagAdapter.ItemClicked {

    public static ArrayList<Tag> tags;
    Button submit_b, cancel_b, update_b, delete_b;
    EditText tag_et, description_et;
    FragmentManager fragmentManager;
    Fragment list_f;

    private String serialize_tags() {
        String ser_tags = "";

        for (int i = 0; i < tags.size(); i++) {
            if (i > 0)
                ser_tags += ",";
            ser_tags += tags.get(i).getName() + "," + tags.get(i).getDescription();
        }

        return ser_tags;
    }

    static public int get_tag_index(ArrayList<Tag> tags, String tag) {
        for (int i = 0; i < tags.size(); i++) {
            if (tags.get(i).getName().equals(tag))
                return i;
        }
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        int i;

        submit_b = findViewById(R.id.submit_b);
        cancel_b = findViewById(R.id.cancel_b);
        update_b = findViewById(R.id.update_b);
        delete_b = findViewById(R.id.delete_b);
        tag_et = findViewById(R.id.tag_et);
        description_et = findViewById(R.id.description_et);

        fragmentManager = getSupportFragmentManager();
        list_f = fragmentManager.findFragmentById(R.id.tags_list1_frag);

        update_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i;
                if (tag_et.getText().toString().isEmpty() ||
                        description_et.getText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                i = get_tag_index(tags, tag_et.getText().toString());
                if (i == -1)
                    tags.add(new Tag(tag_et.getText().toString(), description_et.getText().toString()));
                else
                    tags.get(i).setDescription(description_et.getText().toString());
                ((tags_list_f)list_f).notify_change();
                Toast.makeText(v.getContext(), "Update successful", Toast.LENGTH_SHORT).show();
                tag_et.setText(null);
                description_et.setText(null);
            }
        });

        submit_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("tags_list", serialize_tags());
                setResult(RESULT_OK, intent);
                TagsActivity.this.finish();
            }
        });

        delete_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tag_et.getText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(), "Please enter tag", Toast.LENGTH_SHORT).show();
                    return;
                }
                int i = get_tag_index(tags, tag_et.getText().toString());
                if (i == -1) {
                    Toast.makeText(v.getContext(), "Tag does not exist", Toast.LENGTH_SHORT).show();
                    return;
                }
                tags.remove(i);
                ((tags_list_f)list_f).notify_change();
                Toast.makeText(v.getContext(), "Delete successful", Toast.LENGTH_SHORT).show();
                tag_et.setText(null);
                description_et.setText(null);
            }
        });

        cancel_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                TagsActivity.this.finish();
            }
        });

        String tags_list = getIntent().getStringExtra("tags");
        String tag, desc;

        tags = new ArrayList<Tag>();

        StringTokenizer tokens = new StringTokenizer(tags_list, ",");

        while (true) {
            tag = tokens.nextToken();
            if (tag.equals("#"))
                break;
            desc = tokens.nextToken();

            tags.add(new Tag(tag, desc));
        }

        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
    }

    @Override
    public void onClick(int i) {
        tag_et.setText(tags.get(i).getName());
        description_et.setText(tags.get(i).getDescription());
    }
}
