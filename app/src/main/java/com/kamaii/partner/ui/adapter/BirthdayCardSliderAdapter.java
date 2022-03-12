package com.kamaii.partner.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.kamaii.partner.DTO.CategoryDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.ui.models.BusinessCardSliderModel;
import com.kamaii.partner.utils.CustomTextViewBold;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BirthdayCardSliderAdapter extends PagerAdapter {

    private ArrayList<BusinessCardSliderModel> images;
    private LayoutInflater inflater;
    private Context context;
    ArrayList<CategoryDTO> categoryDTOS;
    UserDTO userDTO;

    ImageView myImage;
    ListView imagelistview;
    CustomTextViewBold b_name;
    CustomTextViewBold partner_name;
    CustomTextViewBold business_services_name;
    String str = "";
    String pathOfImage = "";

    public BirthdayCardSliderAdapter(Context context, ArrayList<BusinessCardSliderModel> images, UserDTO userDTO, ArrayList<CategoryDTO> categoryDTOS, String str,String pathOfImage) {
        this.context = context;
        this.images = images;
        this.userDTO = userDTO;
        this.pathOfImage = pathOfImage;
        this.categoryDTOS = categoryDTOS;
        this.str = str;
        inflater = LayoutInflater.from(context);

        Log.e("Sliderimagesize1234", "" + images.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public @NotNull Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.birthday_slide3, view, false);
        ImageView myImage = myImageLayout.findViewById(R.id.image2);
        ImageView myImage2 = myImageLayout.findViewById(R.id.birthday_image);

        CustomTextViewBold b_name = myImageLayout.findViewById(R.id.birthday_card_customer_name);

        CustomTextViewBold partner_name = myImageLayout.findViewById(R.id.business_card_partner_name);
        CustomTextViewBold business_services_name = myImageLayout.findViewById(R.id.business_services_name);


        b_name.setText(str);

        Log.e("LOSULALIT", "LOSULALIT1");
        Log.e("Sliderimagesize1234", "" + images.get(position).getImg_path());


        Glide.with(context)
                .load(images.get(position).getImg_path())
                .placeholder(R.drawable.dafault_product)
                .into(myImage);


        Glide.with(context)
                .load(pathOfImage)
                .into(myImage2);
        // myImage.setImageResource(Integer.parseInt(images.get(position).getImage()));


        view.addView(myImageLayout, 0);

        return myImageLayout;
    }
}
