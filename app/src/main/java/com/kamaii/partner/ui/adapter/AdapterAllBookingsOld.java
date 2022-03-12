package com.kamaii.partner.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kamaii.partner.DTO.ProductDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.AdapterAllOldBookingsBinding;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.ui.fragment.NewBookings;
import com.kamaii.partner.ui.models.ArtistBookingOld;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.MultiTouchListener;
import com.kamaii.partner.utils.OnTouch;
import com.kamaii.partner.utils.ProjectUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class AdapterAllBookingsOld extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = AdapterAllBookingsOld.class.getSimpleName();
    private NewBookings newBookings;
    private ArrayList<ArtistBookingOld> artistBookingsList;
    private ArrayList<ArtistBookingOld> searchBookingsList;
    private UserDTO userDTO;
    private LayoutInflater myInflater;
    private Context context;
    private final int VIEW_ITEM = 1;
    private final int VIEW_SECTION = 0;
    private GridLayoutManager gridLayoutManager;
    AdapterCart adapterCart;
    private ArrayList<ProductDTO> productDTOArrayList;
    private HashMap<String, String> parms = new HashMap<>();
    private Dialog dialogpriview;
    String locationstatus = "";
    ImageView img_priview;
    Boolean isCheck = true;
    String s_charge = "";

    public static ImageView img_upload;

    public AdapterAllBookingsOld(NewBookings newBookings, ArrayList<ArtistBookingOld> artistBookingsList, UserDTO userDTO, LayoutInflater myInflater) {
        this.newBookings = newBookings;
        this.artistBookingsList = artistBookingsList;
        this.searchBookingsList = new ArrayList<ArtistBookingOld>();
        this.searchBookingsList.addAll(artistBookingsList);
        this.userDTO = userDTO;
        this.myInflater = myInflater;
        context = newBookings.getActivity();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int viewType) {
        RecyclerView.ViewHolder vh;
        if (myInflater == null) {
            myInflater = LayoutInflater.from(viewGroup.getContext());
        }
        if (viewType == VIEW_ITEM) {
            AdapterAllOldBookingsBinding binding =
                    DataBindingUtil.inflate(myInflater, R.layout.adapter_all_old_bookings, viewGroup, false);
            vh = new AdapterAllBookingsOld.MyViewHolder(binding);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_section, viewGroup, false);
            vh = new AdapterAllBookingsOld.MyViewHolderSection(v);
        }
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderMain, int pos) {

        dialogpriview = new Dialog(context, R.style.Theme_Dialog);
        dialogpriview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogpriview.setContentView(R.layout.dailog_priview);
        dialogpriview.setCancelable(true);



        final int position = pos;


        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        if (holderMain instanceof AdapterAllBookingsOld.MyViewHolder) {
            final AdapterAllBookingsOld.MyViewHolder holder = (AdapterAllBookingsOld.MyViewHolder) holderMain;

            gridLayoutManager = new GridLayoutManager(context, 1);
            productDTOArrayList = new ArrayList<>();


            productDTOArrayList = artistBookingsList.get(position).getProduct();
            locationstatus = artistBookingsList.get(position).getLocation_status();
            adapterCart = new AdapterCart(newBookings, productDTOArrayList, locationstatus);
            holder.binding.rvCart.setLayoutManager(gridLayoutManager);
            holder.binding.rvCart.setAdapter(adapterCart);
            //holder.binding.llLocationdesti.setVisibility(View.VISIBLE);

            SimpleDateFormat sdf = new SimpleDateFormat("mm.ss");

            if (artistBookingsList.get(position).getIsmap().equalsIgnoreCase("1")){

                holder.binding.llLocation.setVisibility(View.VISIBLE);
                holder.binding.llLocationdesti.setVisibility(View.VISIBLE);
            }
            else {
                holder.binding.llLocation.setVisibility(View.GONE);
                holder.binding.llLocationdesti.setVisibility(View.GONE);
            }


            try {

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!artistBookingsList.get(position).getDestinationaddress().equalsIgnoreCase("")) {

                holder.binding.tvdestiLocation.setText(artistBookingsList.get(position).getDestinationaddress());
            } else {
                holder.binding.tvdestiLocation.setText(artistBookingsList.get(position).getAddress());

            }


            holder.binding.tvName.setText(artistBookingsList.get(position).getUserName());
            holder.binding.tvtotal.setText(artistBookingsList.get(position).getNetpay());
            holder.binding.tvSubtotal.setText(artistBookingsList.get(position).getItemtotal());
            holder.binding.tvLocation.setText(artistBookingsList.get(position).getAddress());

            holder.binding.tvreason.setText(artistBookingsList.get(position).getDecline_reason());
            holder.binding.tvDate.setText(ProjectUtils.changeDateFormate1(artistBookingsList.get(position).getBooking_date()) + " " + artistBookingsList.get(position).getBooking_time());

            s_charge = artistBookingsList.get(position).getServicecharge();
            if (!artistBookingsList.get(position).getApproxdatetime().equalsIgnoreCase("")) {
                holder.binding.img.setVisibility(View.VISIBLE);
                holder.binding.tvapproxtime.setText("Approx Time" + " : " + artistBookingsList.get(position).getApproxdatetime());
            } else {
                holder.binding.img.setVisibility(View.GONE);
            }

//            Log.e("SUB_CATEGORY_ID","ID:--- "+productDTOArrayList.get(0).getSub_category_id());
            if (productDTOArrayList.get(0).getSub_category_id().equals("242") || productDTOArrayList.get(0).getSub_category_id().equals("434")) {
                holder.binding.serviceTxt.setText("Driver Allowance");
                holder.binding.serviceDigitTxt1.setText(Html.fromHtml("&#x20B9;" + artistBookingsList.get(position).getServicecharge()));
            } else if (productDTOArrayList.get(0).getSub_category_id().equals("453")) {
                holder.binding.serviceTxt.setVisibility(View.GONE);
                holder.binding.serviceDigitTxt1.setVisibility(View.GONE);
            } else {

                if (artistBookingsList.get(position).getRider_order().equalsIgnoreCase("1")){
                    holder.binding.serviceChargeRelative.setVisibility(View.GONE);
                }
                else {
                    if (s_charge.equals("0")) {
                        holder.binding.serviceDigitTxt1.setText("Free");
                    } else {
                        holder.binding.serviceDigitTxt1.setText(Html.fromHtml("&#x20B9;" + artistBookingsList.get(position).getServicecharge()));
                    }
                }

            }



            holder.binding.txtmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCheck) {

                        // holder.binding.tmpView3.setVisibility(View.VISIBLE);
                        //  holder.binding.tmpView2.setVisibility(View.VISIBLE);
                        holder.binding.laymoreee.setVisibility(View.VISIBLE);
                        isCheck = false;
                    } else {
                        // holder.binding.tmpView3.setVisibility(View.GONE);
                        holder.binding.tmpView2.setVisibility(View.GONE);
                        holder.binding.laymoreee.setVisibility(View.GONE);
                        isCheck = true;
                    }
                }
            });


            holder.binding.iviamge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img_priview = dialogpriview.findViewById(R.id.img_priview);

                    if (!artistBookingsList.get(position).getImage().equalsIgnoreCase("")) {
                        Glide.with(context).
                                load(Consts.IMAGE_URL + artistBookingsList.get(position).getImage())
                                .placeholder(R.drawable.dummyuser_image)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(img_priview);
                    }

                    img_priview.setOnTouchListener(new MultiTouchListener(onTouch));
                    dialogpriview.show();
                }
            });
            if (!artistBookingsList.get(position).getUserMobile().equalsIgnoreCase("")) {
                holder.binding.imgphone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobileno = artistBookingsList.get(position).getUserMobile();

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + mobileno));
                        context.startActivity(intent);
                    }
                });
            }

            Glide.with(context).
                    load(artistBookingsList.get(position).getUserImage())
                    .placeholder(R.drawable.dummyuser_image)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.binding.ivArtist);


            //holder.binding.tvDescription.setText(artistBookingsList.get(position).getDescription());
            holder.binding.tvcategory.setText(artistBookingsList.get(position).getCategory_name());


            if (artistBookingsList.get(position).getLocation_status().equalsIgnoreCase("1")) {
                holder.binding.cardData.setBackgroundColor(context.getResources().getColor(R.color.lightgreen));
                //holder.binding.imgLocationCustomer.setVisibility(View.VISIBLE);
            }


            if (artistBookingsList.get(position).getBooking_type().equalsIgnoreCase("0") || artistBookingsList.get(position).getBooking_type().equalsIgnoreCase("3")) {

                if (artistBookingsList.get(position).getBooking_flag().equalsIgnoreCase("2")) {
                    holder.binding.llSt.setVisibility(View.GONE);
                    holder.binding.llACDE.setVisibility(View.GONE);
                    holder.binding.tvCompleted.setVisibility(View.GONE);
                    holder.binding.iviamge.setVisibility(View.GONE);
                    // holder.binding.tvRejected.setVisibility(View.VISIBLE);
                    holder.binding.llreason.setVisibility(View.VISIBLE);
                    //                    holder.binding.imgLocationCustomer.setVisibility(View.GONE);


                    holder.binding.cardData.setBackgroundColor(context.getResources().getColor(R.color.chat_in));


                } else if (artistBookingsList.get(position).getBooking_flag().equalsIgnoreCase("4")) {
                    holder.binding.llSt.setVisibility(View.GONE);
                    holder.binding.llACDE.setVisibility(View.GONE);
                    // holder.binding.tvCompleted.setVisibility(View.VISIBLE);


                    holder.binding.cardData.setBackgroundColor(context.getResources().getColor(R.color.white));


                    if (!artistBookingsList.get(position).getImage().equalsIgnoreCase("")) {
                        holder.binding.iviamge.setVisibility(View.VISIBLE);
                    }


                    holder.binding.tvRejected.setVisibility(View.GONE);
                    holder.binding.llreason.setVisibility(View.GONE);
                    //                    holder.binding.imgLocationCustomer.setVisibility(View.GONE);
                }


            } else if (artistBookingsList.get(position).getBooking_type().equalsIgnoreCase("2")) {

                if (artistBookingsList.get(position).getBooking_flag().equalsIgnoreCase("2")) {
                    holder.binding.llSt.setVisibility(View.GONE);
                    holder.binding.llACDE.setVisibility(View.GONE);
                    holder.binding.tvCompleted.setVisibility(View.GONE);
                    holder.binding.iviamge.setVisibility(View.GONE);
                    //    holder.binding.tvRejected.setVisibility(View.VISIBLE);
                    holder.binding.llreason.setVisibility(View.VISIBLE);
                    //                    holder.binding.imgLocationCustomer.setVisibility(View.GONE);

                    holder.binding.cardData.setBackgroundColor(context.getResources().getColor(R.color.lightred));

                } else if (artistBookingsList.get(position).getBooking_flag().equalsIgnoreCase("4")) {
                    holder.binding.llSt.setVisibility(View.GONE);
                    holder.binding.llACDE.setVisibility(View.GONE);
                    //   holder.binding.tvCompleted.setVisibility(View.VISIBLE);
                    if (!artistBookingsList.get(position).getImage().equalsIgnoreCase("")) {
                        holder.binding.iviamge.setVisibility(View.VISIBLE);
                    }


                    holder.binding.cardData.setBackgroundColor(context.getResources().getColor(R.color.white));

                    holder.binding.tvRejected.setVisibility(View.GONE);
                    holder.binding.llreason.setVisibility(View.GONE);
                    //      holder.binding.imgLocationCustomer.setVisibility(View.GONE);
                }


            }

        } else {
            AdapterAllBookingsOld.MyViewHolderSection view = (AdapterAllBookingsOld.MyViewHolderSection) holderMain;
            view.tvSection.setText(artistBookingsList.get(position).getSection_name());
        }
    }


    OnTouch onTouch = new OnTouch() {
        @Override
        public void removeBorder() {

        }
    };


    @Override
    public int getItemViewType(int position) {
        return artistBookingsList.get(position).isSection() ? VIEW_SECTION : VIEW_ITEM;
    }

    @Override
    public int getItemCount() {
        return artistBookingsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterAllOldBookingsBinding binding;

        public MyViewHolder(AdapterAllOldBookingsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }


    public static class MyViewHolderSection extends RecyclerView.ViewHolder {
        public CustomTextView tvSection;

        public MyViewHolderSection(View view) {
            super(view);
            tvSection = view.findViewById(R.id.tvSection);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        artistBookingsList.clear();
        if (charText.length() == 0) {
            artistBookingsList.addAll(searchBookingsList);
        } else {
            for (ArtistBookingOld historyDTO : searchBookingsList) {
                if (historyDTO.getUserName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    artistBookingsList.add(historyDTO);
                }
            }
        }
        notifyDataSetChanged();
    }
}
