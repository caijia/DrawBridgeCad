package com.cj.drawbridge.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cj.drawbridge.R;
import com.cj.drawbridge.helper.GlideImageLoader;
import com.cj.drawbridge.helper.GridSpacingItemDecoration;
import com.cj.drawbridge.helper.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BridgePictureListDialog extends BottomSheetDialogFragment {

    private static final int[] IMAGE_RES_ARRAY = {
            R.drawable.icon_bridge_1, R.drawable.icon_bridge_2, R.drawable.icon_bridge_3,
            R.drawable.icon_bridge_4, R.drawable.icon_bridge_5, R.drawable.icon_bridge_6,
            R.drawable.icon_bridge_7, R.drawable.icon_bridge_8, R.drawable.icon_bridge_9,
            R.drawable.icon_bridge_10, R.drawable.icon_bridge_11, R.drawable.icon_bridge_12,
            R.drawable.icon_bridge_13, R.drawable.icon_bridge_14, R.drawable.icon_bridge_15,
            R.drawable.icon_bridge_16, R.drawable.icon_bridge_17, R.drawable.icon_bridge_18
    };

    private static final int[] IMAGE_ORDER_ARRAY = {
            18, 11, 9, 10, 12, 16, 15, 8, 7, 14, 6, 4, 13, 2, 17, 1, 3, 5
    };

    private static final int SPAN_COUNT = 3;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bridge_picture_list, container, false);
        return view;
    }

    private OnClickBridgeListener onClickBridgeListener;

    private List<BridgeImage> getData() {
        List<BridgeImage> list = new ArrayList<>();
        int length = IMAGE_RES_ARRAY.length;
        for (int i = 0; i < length; i++) {
            BridgeImage image = new BridgeImage(IMAGE_RES_ARRAY[i], IMAGE_ORDER_ARRAY[i],
                    i + 1);
            list.add(image);
        }
        return list;
    }

    static class BridgeImage {
        public int imageRes;
        public int order;
        public int type;

        public BridgeImage(int imageRes, int order, int type) {
            this.imageRes = imageRes;
            this.order = order;
            this.type = type;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler_view);

        List<BridgeImage> bridgeImageList = getData();
        BridgeImageAdapter adapter = new BridgeImageAdapter(bridgeImageList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        int space = (int) Util.dpToPx(getContext(), 1);
        int color = ContextCompat.getColor(getContext(), R.color.color_efefef);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(space, false,
                color));
        recyclerView.setAdapter(adapter);
    }

    public void setOnClickBridgeListener(OnClickBridgeListener onClickBridgeListener) {
        this.onClickBridgeListener = onClickBridgeListener;
    }

    public interface OnClickBridgeListener {
        void onClickBridge(int type);
    }

    class BridgeImageAdapter extends RecyclerView.Adapter<BridgeImageAdapter.BridgeImageVH> {

        private List<BridgeImage> bridgeImages;

        public BridgeImageAdapter(List<BridgeImage> bridgeImages) {
            this.bridgeImages = bridgeImages;
            Collections.sort(this.bridgeImages, (o1, o2) -> o1.order - o2.order);
        }

        @NonNull
        @Override
        public BridgeImageVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_bridge_picture, viewGroup, false);
            return new BridgeImageVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BridgeImageVH holder, int position) {
            ViewGroup.LayoutParams params = holder.flImageContainer.getLayoutParams();
            params.width = params.height = Util.getScreenWidth(getContext()) / SPAN_COUNT;

            BridgeImage bridgeImage = bridgeImages.get(position);
            GlideImageLoader.getInstance().loadImage(holder.ivBridge, bridgeImage.imageRes,
                    R.drawable.shape_black);

            holder.setExtra(bridgeImage);
            holder.vClick.setOnClickListener(holder);
        }

        @Override
        public int getItemCount() {
            return bridgeImages.size();
        }

        class BridgeImageVH extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView ivBridge;
            private BridgeImage bridgeImage;
            private FrameLayout flImageContainer;
            private View vClick;

            public BridgeImageVH(@NonNull View itemView) {
                super(itemView);
                ivBridge = itemView.findViewById(R.id.iv_bridge);
                flImageContainer = itemView.findViewById(R.id.fl_image_container);
                vClick = itemView.findViewById(R.id.v_click);
            }

            @Override
            public void onClick(View v) {
                int type = bridgeImage.type;
                if (onClickBridgeListener != null) {
                    onClickBridgeListener.onClickBridge(type);
                }
            }

            public void setExtra(BridgeImage bridgeImage) {
                this.bridgeImage = bridgeImage;
            }
        }
    }
}
