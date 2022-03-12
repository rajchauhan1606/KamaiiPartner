package com.kamaii.partner.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kamaii.partner.DTO.TrackingData;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

import static android.content.Context.MODE_PRIVATE;

public class TrackingDetailDialog extends BottomSheetDialogFragment
{
    private int BookingFlag = 0;
    private int PaymentType = 0;

    private int CancelFlag = 1;
    private int CallFlag = 2;

    boolean isStartBooking = false;

    LinearLayout llSt;

    LinearLayout laycallcancel;


    LinearLayout laycall;

    LinearLayout laycancel;

    LinearLayout layshare;

    LinearLayout btnshare;

    LinearLayout llTime;

    ImageView imgone;

    ImageView imgtwo;

    ImageView imgvendonrimage;


    CustomTextView etAddressSelectsource;

    CustomTextView etAddressSelectdesti;

    CustomTextViewBold txtservicename;

    CustomTextViewBold txttotalamount;

    CustomTextViewBold txtptype;

    CustomTextViewBold txtptypemsg;

    CustomTextViewBold tvStatus;

    CustomTextView txtvechile;

    CustomTextView txtvendorname;


    DialogListener dialogListener;

    TrackingData liveTrackingData;

    PulsatorLayout pulsator;
    RelativeLayout relanimation;
    private UserDTO userDTO;
    private BottomSheetBehavior mBottomSheetBehavior;
    public static TrackingDetailDialog newInstance() {
        return new TrackingDetailDialog();
    }
    private SharedPrefrence prefrence;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.layout_tracking_detail_dialog, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        setCancelable(false);

        setViewData();
        // get the views and attach the listener
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);

    }
     @NonNull
    @Override
     public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return  dialog;
    }
    private void setupFullHeight(BottomSheetDialog bottomSheetDialog)
    {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        behavior.setHideable(false);
        behavior.setPeekHeight(300);
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
    public void setData(TrackingData trackingData, RelativeLayout relativeLayout, PulsatorLayout pulsatorLayout) {
        relanimation = relativeLayout;
        pulsator = pulsatorLayout;
        liveTrackingData = trackingData;


    }

    public void setViewData() {
        txtservicename.setText(liveTrackingData.getProductName());
        txtvendorname.setText(liveTrackingData.getVendorName());
        txtvechile.setText(liveTrackingData.getVehicleNumber());

        Glide.with(getActivity()).
                load(liveTrackingData.getVendorImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgvendonrimage);

        etAddressSelectsource.setText(liveTrackingData.getSourceAddress());

        txttotalamount.setText("Total Fare" + " " + liveTrackingData.getTotalAmount() + " " + "Rs");

        if (!liveTrackingData.getDestinationAddress().trim().isEmpty()) {
            etAddressSelectdesti.setText(liveTrackingData.getDestinationAddress());

        } else {
            etAddressSelectdesti.setText(liveTrackingData.getSourceAddress());
        }

        if (liveTrackingData.getPaymentType() == 0)
        {
            txtptype.setText("Online Payment");
            txtptypemsg.setText("Relax when the ride ends");
        }
        else if (liveTrackingData.getPaymentType() == 1)
        {
            txtptype.setText("Cash");
            txtptypemsg.setText("Pay when the ride ends");
        }
        else if (liveTrackingData.getPaymentType() == 2) {
            txtptype.setText("Wallet Payment");
            txtptypemsg.setText("Relax when the ride ends");
        } else {
            txtptype.setText("");
            txtptypemsg.setText("");
        }
        if (liveTrackingData.getBookingFlag() == 0) {

            relanimation.setVisibility(View.VISIBLE);
            tvStatus.setText("waiting for rider");
            laycallcancel.setVisibility(View.VISIBLE);
            layshare.setVisibility(View.GONE);
            laycall.setVisibility(View.GONE);

           /* if (liveTrackingData.getScreenFlag().equalsIgnoreCase(GlobalUtils.CONFIRM_BOOKING_SCREEN))
            {
                Intent intent = new Intent(getActivity(), MyService.class);
                getActivity().startService(intent);
            }*/

        } else if (liveTrackingData.getBookingFlag() == 1) {

            laycallcancel.setVisibility(View.VISIBLE);
            laycall.setVisibility(View.VISIBLE);
            relanimation.setVisibility(View.GONE);
            layshare.setVisibility(View.GONE);
            tvStatus.setText("accept");
            //ivStatus.setBackground(mContext.getResources().getDrawable(R.drawable.ic_accept));
        } else if (liveTrackingData.getBookingFlag() == 2) {
            // mBackgroundHandler.obtainMessage(FINISH_COUNTER, true).sendToTarget();

            laycallcancel.setVisibility(View.GONE);
            laycall.setVisibility(View.GONE);
            tvStatus.setText("");
            layshare.setVisibility(View.GONE);
            // ivStatus.setBackground(mContext.getResources().getDrawable(R.drawable.ic_inprogress));

        } else if (liveTrackingData.getBookingFlag() == 3) {
            isStartBooking = true;
            relanimation.setVisibility(View.GONE);
            laycallcancel.setVisibility(View.GONE);
            laycall.setVisibility(View.GONE);
            tvStatus.setText("Your booking in progress");
            layshare.setVisibility(View.VISIBLE);
            // ivStatus.setBackground(mContext.getResources().getDrawable(R.drawable.ic_inprogress));

        }

        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogListener.onClickView(CallFlag, liveTrackingData);
                //  dialogListener.onFinishEditDialog(liveTrackingData);
            }
        });
        laycall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!liveTrackingData.getMobileNumber().trim().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + liveTrackingData.getMobileNumber()));
                    getActivity().startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Contact number is not available", Toast.LENGTH_LONG).show();
                }

            }
        });


        laycancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListener.onClickView(CancelFlag, liveTrackingData);
                // completeDialog(getActivity().getResources().getString(R.string.cancel), getActivity().getResources().getString(R.string.booking_cancel_msg) + " " + liveTrackingData.getVendorName() + "?");
            }
        });
    }

    public void setvisiblityView() {
        laycallcancel.setVisibility(View.GONE);
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public interface DialogListener {

        void onClickView(int flag, TrackingData track);
    }
}
