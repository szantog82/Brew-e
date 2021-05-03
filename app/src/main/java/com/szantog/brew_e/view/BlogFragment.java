package com.szantog.brew_e.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szantog.brew_e.R;
import com.szantog.brew_e.model.BlogItem;
import com.szantog.brew_e.viewmodel.RetrofitListViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class BlogFragment extends Fragment implements View.OnClickListener {

    private RetrofitListViewModel retrofitListViewModel;

    private List<BlogItem> blogItems = new ArrayList<>();

    private TextView blogTitleText;
    private TextView blogAuthorText;
    private TextView blogDateText;
    private TextView blogMainText;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy. MM. dd. HH:mm");

    private Html.ImageGetter imageGetter = new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            LevelListDrawable drawable = new LevelListDrawable();
            new LoadImage().execute(source, drawable);
            return drawable;
        }
    };

    private void showBlog(int index) {
        blogTitleText.setText(blogItems.get(index).getTitle());
        blogAuthorText.setText(blogItems.get(index).getShop_name());
        blogDateText.setText(simpleDateFormat.format(new Date(blogItems.get(index).getUpload_date() * 1000)));

        Spanned spanned = Html.fromHtml(blogItems.get(index).getText(), imageGetter, null);
        blogMainText.setText(spanned);
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

        retrofitListViewModel = new ViewModelProvider(requireActivity()).get(RetrofitListViewModel.class);
        retrofitListViewModel.getBlogs().observe(getViewLifecycleOwner(), new Observer<List<BlogItem>>() {
            @Override
            public void onChanged(List<BlogItem> blogItems) {
                updateUI(blogItems);
            }
        });

        retrofitListViewModel.getSelectedShopId().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                setUI(integer);
            }
        });

        blogMainText.setMovementMethod(new ScrollingMovementMethod());
        if (blogItems.size() > 0) {
            showBlog(0);
        } else {
            blogTitleText.setText("Nincs még blog feltöltve");
        }

        TextView shopNameTextView = view.findViewById(R.id.blog_shop_name);
        shopNameTextView.setOnClickListener(this);
    }

    private void setUI(int shop_id) {
        if (shop_id == -1) {
            blogTitleText.setBackgroundColor(getActivity().getColor(R.color.gray1));
            blogAuthorText.setBackgroundColor(getActivity().getColor(R.color.white));
            blogAuthorText.setOnClickListener(null);
            blogTitleText.setOnClickListener(this);
        } else {
            blogTitleText.setBackgroundColor(getActivity().getColor(R.color.white));
            blogAuthorText.setBackgroundColor(getActivity().getColor(R.color.gray1));
            blogAuthorText.setOnClickListener(this);
            blogTitleText.setOnClickListener(null);
        }
    }

    private void updateUI(List<BlogItem> items) {
        if (items.size() > 0) {
            blogItems = items;
            showBlog(0);
        } else {
            blogTitleText.setText("Nincs még blog feltöltve");
            blogAuthorText.setText("");
            blogDateText.setText("");
            blogMainText.setText("");
        }
    }

    @Override
    public void onClick(View view) {
        String message = "További cikkek";
        if (view.getId() == R.id.blog_shop_name) {
            message += " - " + blogItems.get(0).getShop_name();
        }
        String items[] = new String[blogItems.size()];
        for (int i = 0; i < blogItems.size(); i++) {
            items[i] = blogItems.get(i).getTitle();
        }
        new AlertDialog.Builder(getActivity())
                .setTitle(message)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        showBlog(which);
                    }
                })
                .show();
    }

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable levelListDrawable;

        @Override
        protected Bitmap doInBackground(Object[] objects) {
            String source = (String) objects[0];
            levelListDrawable = (LevelListDrawable) objects[1];
            try {
                InputStream inputStream = new URL(source).openStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = (bitmap.getWidth() < displayMetrics.widthPixels / 2) ? bitmap.getWidth() : displayMetrics.widthPixels / 2;
                int height = (bitmap.getHeight() < displayMetrics.heightPixels / 2) ? bitmap.getHeight() : displayMetrics.heightPixels / 2;
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getActivity().getResources(), bitmap);
                levelListDrawable.addLevel(1, 1, bitmapDrawable);
                levelListDrawable.setBounds(0, 0, width, height);
                levelListDrawable.setLevel(1);
                CharSequence c = blogMainText.getText();
                blogMainText.setText(c);
            }
        }
    }
}
