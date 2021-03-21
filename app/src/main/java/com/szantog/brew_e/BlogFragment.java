package com.szantog.brew_e;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BlogFragment extends Fragment {

    private List<BlogItem> blogItems = new ArrayList<>();

    private TextView blogTitleText;
    private TextView blogAuthorText;
    private TextView blogDateText;
    private TextView blogMainText;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy. MM. dd. HH:mm");

    public BlogFragment(List<BlogItem> blogItems) {
        this.blogItems = blogItems;
    }

    private void showBlog(int index) {
        blogTitleText.setText(blogItems.get(index).getTitle());
        blogAuthorText.setText(blogItems.get(index).getShop_name());
        blogDateText.setText(simpleDateFormat.format(new Date(blogItems.get(index).getUpload_date() * 1000)));
        blogMainText.setText(Html.fromHtml(blogItems.get(index).getText()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blog_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        blogTitleText = view.findViewById(R.id.blog_title);
        blogAuthorText = view.findViewById(R.id.blog_shop_name);
        blogDateText = view.findViewById(R.id.blog_date);
        blogMainText = view.findViewById(R.id.blog_main);
        blogMainText.setMovementMethod(new ScrollingMovementMethod());
        if (blogItems.size() > 0) {
            showBlog(0);
        } else {
            blogTitleText.setText("Nincs még blog feltöltve");
        }

        TextView shopNameTextView = view.findViewById(R.id.blog_shop_name);
        shopNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String items[] = new String[blogItems.size()];
                for (int i = 0; i < blogItems.size(); i++) {
                    items[i] = blogItems.get(i).getTitle();
                }
                new AlertDialog.Builder(getActivity())
                        .setTitle("További cikkek - " + blogItems.get(0).getShop_name())
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                showBlog(which);
                            }
                        })
                        .show();
            }
        });
    }
}
