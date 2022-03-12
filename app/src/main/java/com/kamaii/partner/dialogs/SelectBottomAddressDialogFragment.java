package com.kamaii.partner.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kamaii.partner.R;

public class SelectBottomAddressDialogFragment extends BottomSheetDialogFragment {
    RadioButton rdt_home,rdt_work;
    ImageView img_close;
    DialogListener dialogListener;
    TextView txt_header_dialog;
    String header;
    public static SelectBottomAddressDialogFragment newInstance() {
        return new SelectBottomAddressDialogFragment();
    }
    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_address_filter, container, false);
        initView(view);
        setCancelable(false);
        return view;
    }
    public void setHeader(String header)
    {
        this.header=header;
    }
    public interface DialogListener {

        void onClickView(int flag);
    }
    private void initView(View view)
    {
        RadioGroup rg = (RadioGroup) view.findViewById(R.id.rdt_group);
        rdt_home = (RadioButton) view.findViewById(R.id.rdt_home);
        rdt_work = (RadioButton) view.findViewById(R.id.rdt_work);
        img_close = (ImageView) view.findViewById(R.id.img_close);
        txt_header_dialog = (TextView) view.findViewById(R.id.txt_header_dialog);
        txt_header_dialog.setText(header);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rdt_home:
                        dialogListener.onClickView(1);
                        dismiss();
                        break;
                    case R.id.rdt_work:
                        dialogListener.onClickView(2);
                        dismiss();
                        break;
                }
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
