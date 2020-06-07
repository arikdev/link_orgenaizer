package com.example.links_orgenizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements LinkAdapter.ItemClicked, TagsListForAdd_adapter.ItemClicked {

    public static final String APP_FILE_NAME = "Data.txt";
    public static final String APP_TAG_FILE_NAME = "Tags.txt";
    static final int TAGS_ADD_STATE = 0;
    static final int TAGS_FILTER_STATE = 1;
    final int TAGS_ACTIVITY = 3;
    int tags_state;

    public static ArrayList<Link> links;
    public static ArrayList<Tag> tags;
    public static ArrayList<Tag> filter;
    TextView name_tv, tag_tv, description_tv, link_tv, link_add_tags_tv;
    ImageView type_iv, delete_iv;
    Button add_b;
    EditText name_et, type_et, description_et, link_et;
    FragmentManager fragmentManager;
    Fragment list_f, link_add_f, link_info_f, tags_for_add_f, filter_f;
    Switch filter_s;
    private Boolean is_landscape;

    Boolean with_filter;

    static public int get_link_index(ArrayList<Link> links, String link) {
        for (int i = 0; i < links.size(); i++) {
            if (links.get(i).getName().equals(link))
                return i;
        }
        return -1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    String get_tags() {
        String tags_list = "";

        for (int i = 0; i < tags.size(); i++) {
            if (i > 0)
                tags_list += ",";
            tags_list += tags.get(i).getName() + "," + tags.get(i).getDescription();
        }
        return tags_list;
    }

    void handle_tags_update(String tags_list) {
        tags.clear();
        String tag, desc;

        StringTokenizer tokens = new StringTokenizer(tags_list, ",");

        // Load tag to array.
        while (tokens.hasMoreTokens()) {
            tag = tokens.nextToken();
            if (!tokens.hasMoreTokens())
                break;
            desc = tokens.nextToken();
            tags.add(new Tag(tag, desc));
        }

        // Write tags to file.
        try {
            FileOutputStream file = openFileOutput(APP_TAG_FILE_NAME, MODE_PRIVATE);
            OutputStreamWriter outputFile = new OutputStreamWriter(file);

            for (int i = 0; i< tags.size(); i++) {
                outputFile.write(tags.get(i).getName().toString() + "," +
                        tags.get(i).getDescription() + "\n");
            }
            outputFile.flush();
            outputFile.close();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        ((tags_for_add_f)tags_for_add_f).change_notify();
    }

    void tags_load() {
        File file = getApplicationContext().getFileStreamPath(APP_TAG_FILE_NAME);
        String line, tag ,desc;

        if (!file.exists())
            return;

        tags.clear();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput(APP_TAG_FILE_NAME)));

            while ((line = reader.readLine()) != null) {
                StringTokenizer tokens = new StringTokenizer(line, ",'");
                tag = tokens.nextToken();
                desc = tokens.nextToken();
                Log.i("XXX", "tag line:" + tag + ":" + desc);
                tags.add(new Tag(tag, desc));
            }
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TAGS_ACTIVITY:
                switch (resultCode) {
                    case RESULT_OK:
                        String tags_list = data.getStringExtra("tags_list");
                        handle_tags_update(tags_list);
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(MainActivity.this, "CANCELED !!", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view:
                fragmentManager.beginTransaction()
                        .show(list_f)
                        .show(link_info_f)
                        .hide(link_add_f)
                        .show(tags_for_add_f)
                        .show(filter_f)
                        .commit();
                tags_state = TAGS_FILTER_STATE;
                break;
            case R.id.add:
                fragmentManager.beginTransaction()
                        .show(list_f)
                        .hide(link_info_f)
                        .show(link_add_f)
                        .show(tags_for_add_f)
                        .hide(filter_f)
                        .commit();
                tags_state = TAGS_ADD_STATE;
                break;
            case R.id.save:
                data_save();
                Toast.makeText(this, "Data saved successfully",Toast.LENGTH_SHORT).show();
                break;

            case R.id.tags:
                String tags_list = get_tags();
                tags_list += ",#";
                Intent intent = new Intent(MainActivity.this, com.example.links_orgenizer.TagsActivity.class);
                intent.putExtra("tags", tags_list);
                startActivityForResult(intent, TAGS_ACTIVITY);
                break;

            case R.id.list:
                if (!is_landscape) {
                    fragmentManager.beginTransaction()
                            .show(list_f)
                            .hide(link_info_f)
                            .hide(link_add_f)
                            .show(tags_for_add_f)
                            .show(filter_f)
                            .commit();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Toast.makeText(this, "Configuration changrd !!!!!", Toast.LENGTH_SHORT).show();
        Log.i("XXX", "CONF changed RRR!!!");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name_tv = findViewById(R.id.info_name_tv);
        description_tv = findViewById(R.id.info_description_tv);
        tag_tv = findViewById(R.id.info_tags_tv);
        link_tv = findViewById(R.id.info_link_tv);
        fragmentManager = getSupportFragmentManager();
        list_f = fragmentManager.findFragmentById(R.id.list_frag);
        link_add_f = fragmentManager.findFragmentById(R.id.link_add_frag);
        link_info_f = fragmentManager.findFragmentById(R.id.link_info_frag);
        tags_for_add_f = fragmentManager.findFragmentById(R.id.tags_list_for_add_frag);
        filter_f = fragmentManager.findFragmentById(R.id.filter_frag);
        name_et = findViewById(R.id.name_et);
        type_et = findViewById(R.id.type_et);
        link_add_tags_tv = findViewById(R.id.link_add_tags_tv);
        link_et = findViewById(R.id.link_et);
        description_et = findViewById(R.id.description_et);
        add_b = findViewById(R.id.add_b);
        filter_s = findViewById(R.id.filter_s);
        filter_s.setChecked(false);
        with_filter = false;
        delete_iv = findViewById(R.id.delete_iv);
        int orientation = getResources().getConfiguration().orientation;

        is_landscape = findViewById(R.id.layout_land) != null;

        link_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + link_tv.getText().toString().trim()));
                startActivity(intent);
            }
        });

        filter_s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                with_filter = isChecked;
                ((list_f)list_f).set_filter(with_filter, filter);
                ((list_f)list_f).notify_change();
            }
        });

        delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = get_link_index(links, name_tv.getText().toString());
                if (i < 0) {
                    Toast.makeText(v.getContext(), "Failed getting index for link:" + name_tv.getText().toString(), Toast.LENGTH_SHORT).show();
                    return;
                }
                links.remove(i);
                name_tv.setText(null);
                description_tv.setText(null);
                tag_tv.setText(null);
                link_tv.setText(null);
                ((list_f)list_f).notify_change();
            }
        });

        add_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name_et.getText().toString().isEmpty() ||
                type_et.getText().toString().isEmpty() ||
                description_et.getText().toString().isEmpty() ||
                link_et.getText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                StringTokenizer tokens = new StringTokenizer(link_add_tags_tv.getText().toString());
                while (tokens.hasMoreTokens()) {
                    String cur_tag = tokens.nextToken();
                    if (TagsActivity.get_tag_index(tags, cur_tag) == -1) {
                        Toast.makeText(v.getContext(), "Tag " + cur_tag + " is invalid", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                links.add(new Link(type_et.getText().toString(),
                                name_et.getText().toString(),
                                description_et.getText().toString(),
                                link_add_tags_tv.getText().toString(),
                                link_et.getText().toString()));

                ((list_f)list_f).notify_change();

                name_et.setText(null);
                type_et.setText(null);
                description_et.setText(null);
                link_add_tags_tv.setText(null);
                link_et.setText(null);

                Toast.makeText(v.getContext(), "Update successfully", Toast.LENGTH_SHORT).show();
            }
        });

        links = new ArrayList<Link>();
        tags = new ArrayList<Tag>();
        filter = new ArrayList<Tag>();

        data_load();
        tags_load();

        if (links.size() > 0)
            onClick(0);

        tags_state = TAGS_FILTER_STATE;

        if (is_landscape) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            fragmentManager.beginTransaction()
                    .show(list_f)
                    .show(link_info_f)
                    .hide(link_add_f)
                    .show(tags_for_add_f)
                    .show(filter_f)
                    .commit();
        } else {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            fragmentManager.beginTransaction()
                    .show(list_f)
                    .hide(link_info_f)
                    .hide(link_add_f)
                    .show(tags_for_add_f)
                    .show(filter_f)
                    .commit();
        }
    }

    void data_load() {
        File file = getApplicationContext().getFileStreamPath(APP_FILE_NAME);
        String line;

        if (!file.exists())
            return;

        links.clear();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput(APP_FILE_NAME)));

            while ((line = reader.readLine()) != null) {
                StringTokenizer tokens = new StringTokenizer(line, ",'");
                links.add(new Link(tokens.nextToken(), tokens.nextToken(), tokens.nextToken(),
                        tokens.nextToken(), tokens.nextToken()));
            }
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    void data_save() {
        try {
            FileOutputStream file = openFileOutput(APP_FILE_NAME, MODE_PRIVATE);
            OutputStreamWriter outputFile = new OutputStreamWriter(file);

            for (int i = 0; i< links.size(); i++) {
                outputFile.write(links.get(i).getType().toString() + "," +
                        links.get(i).getName() + "," +
                        links.get(i).getDescription() +"," +
                        links.get(i).getTags() + "," +
                        links.get(i).getLink() + "\n") ;
            }
            outputFile.flush();
            outputFile.close();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onClick(int i) {
        int x;
        name_tv.setText(links.get(i).getName());
        description_tv.setText(links.get(i).getDescription());
        tag_tv.setText(links.get(i).getTags());
        link_tv.setText(links.get(i).getLink());
    }

    @Override
    public void tag_onClick(int i) {
        switch (tags_state) {
            case TAGS_ADD_STATE:
                String new_tags = new String("");
                String token;

                Boolean found = false;
                // Check if tags already contains this tag
                StringTokenizer tokens = new StringTokenizer(link_add_tags_tv.getText().toString());
                Log.i("XXX", ">>>>>> ADD STATE tags:" + link_add_tags_tv.getText().toString());
                while (tokens.hasMoreTokens()) {
                    token = tokens.nextToken();
                    Log.i("XXX", ">>>>>> ADD STATE loop token:" + token);
                    if (token.equals(tags.get(i).getName())) {
                        Log.i("XXX", ">>>>>>> equls");
                        found = true;
                        continue;
                    }
                    new_tags += token + " ";
                    Log.i("XXX", "new_tags:" + new_tags  + "  token:" + token);
                }
                if (found) {
                    Log.i("XXX", ">>>>>>> Found !! new_tags:" + new_tags + ":");
                    //Delete the tag
                    link_add_tags_tv.setText(new_tags);
                    return;
                }
                if (link_add_tags_tv.getText().toString() == null) {
                    link_add_tags_tv.setText(tags.get(i).getName());
                    return;
                }
                link_add_tags_tv.setText(link_add_tags_tv.getText().toString() + " " + tags.get(i).getName());
                break;
            case TAGS_FILTER_STATE:
                int index = TagsActivity.get_tag_index(filter, tags.get(i).getName());
                // If the tags exists we will delete
                if (index == -1)
                    filter.add(new Tag(tags.get(i).getName(), tags.get(i).getDescription()));
                else
                    filter.remove(index);
                ((filter_f)filter_f).notify_update();
                ((list_f)list_f).set_filter(with_filter, filter);
                break;
            default:
                break;
        }
    }
}
