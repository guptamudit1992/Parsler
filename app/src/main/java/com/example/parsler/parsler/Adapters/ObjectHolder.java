package com.example.parsler.parsler.Adapters;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.parsler.parsler.Commons.StringConstants;
import com.example.parsler.parsler.Models.PickupSummaryObject;
import com.example.parsler.parsler.databinding.MessageCardBinding;
import com.example.parsler.parsler.databinding.PickupCardBinding;

public class ObjectHolder<X,Y> extends RecyclerView.ViewHolder {

    public ObjectHolder(ViewDataBinding itemView) {
        super(itemView.getRoot());
    }

    public void bindConnection(X object, Y _binding, String case_constant) {
        switch (case_constant) {

            case StringConstants.PICKUP_SUMMARY_ADAPTER:
                    PickupCardBinding pickupCardBinding =
                            (PickupCardBinding) _binding;
                    pickupCardBinding
                            .setPickupObject((PickupSummaryObject) object);
                    break;

            case StringConstants.MESSAGES_ADAPTER:
                    MessageCardBinding messageCardBinding =
                            (MessageCardBinding) _binding;
                    messageCardBinding
                            .setMessageObject((PickupSummaryObject) object);
                    break;

            default:
                Log.e(StringConstants.RESPONSE_ERROR,
                        StringConstants.OBJECT_HANDLER_NOT_FOUND);
                break;
        }
    }
}
